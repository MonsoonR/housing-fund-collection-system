# 报告图表源码

本文件提供可复制到 Mermaid 渲染工具或 Markdown 编辑器中的图源码。图名与当前项目模块、包结构和数据库表保持一致。

## 1. 系统用例图

```mermaid
flowchart LR
    actor["综合柜员"]
    admin["系统管理员/运维人员"]

    actor --> UC1["系统参数维护"]
    actor --> UC2["单位开户"]
    actor --> UC3["个人开户"]
    actor --> UC4["个人 Excel 批量开户"]
    actor --> UC5["单位资料修改"]
    actor --> UC6["个人资料修改"]
    actor --> UC7["单位信息查询"]
    actor --> UC8["个人信息查询"]

    admin --> UC9["数据库初始化"]
    admin --> UC10["配置本地数据库连接"]
    admin --> UC11["Tomcat 部署 WAR"]

    UC3 --> UC4
    UC6 --> UC12["身份证冲突强制变更"]
```

## 2. 功能层次图

```mermaid
flowchart TB
    root["住房公积金归集业务系统"]
    root --> p["系统参数维护"]
    root --> uo["单位开户"]
    root --> po["个人开户"]
    po --> po1["手工开户"]
    po --> po2["Excel 批量开户"]
    root --> ue["单位资料修改"]
    root --> pe["个人资料修改"]
    pe --> pe1["普通修改"]
    pe --> pe2["冲突强制变更"]
    root --> uq["单位信息查询"]
    root --> pq["个人信息查询"]
```

## 3. 系统架构图

```mermaid
flowchart TB
    browser["浏览器"]
    jsp["JSP / HTML / CSS / JavaScript"]
    controller["Spring MVC Controller"]
    service["Service / ServiceImpl\n业务规则与事务"]
    mapper["MyBatis Mapper 接口与 XML"]
    mysql["MySQL\nTB001 / TB002 / TB003"]

    browser --> jsp
    jsp --> controller
    controller --> service
    service --> mapper
    mapper --> mysql

    subgraph config["配置"]
        jdbc["jdbc.properties\njdbc-local.properties"]
        spring["applicationContext.xml\nspring-mvc.xml"]
    end

    config --> controller
    config --> service
    config --> mapper
```

## 4. 数据库 ER 图

```mermaid
erDiagram
    TB001 {
        CHAR20 SEQNAME PK
        BIGINT SEQ
        BIGINT MAXSEQ
        VARCHAR DESC
        VARCHAR FREEUSE1
    }

    TB002 {
        CHAR12 UNITACCNUM PK
        VARCHAR UNITACCNAME
        CHAR20 ORGCODE
        CHAR1 ACCSTATE
        DECIMAL BALANCE
        DECIMAL BASENUMBER
        DECIMAL UNITPROP
        DECIMAL PERPROP
        DECIMAL UNITPAYSUM
        DECIMAL PERPAYSUM
        INT PERSNUM
        CHAR8 INSTCODE
    }

    TB003 {
        CHAR12 ACCNUM PK
        CHAR12 UNITACCNUM FK
        VARCHAR PERNAME
        VARCHAR IDTYPE
        VARCHAR IDCARD UK
        DECIMAL BALANCE
        CHAR1 PERACCSTATE
        DECIMAL BASENUMBER
        DECIMAL UNITPROP
        DECIMAL INDIPROP
        DECIMAL UNITMONPAYSUM
        DECIMAL PERMONPAYSUM
        CHAR8 INSTCODE
    }

    TB002 ||--o{ TB003 : "单位拥有个人账户"
    TB001 ||--o{ TB002 : "UNITACCNUM 序号生成"
    TB001 ||--o{ TB003 : "PERACCNUM 序号生成"
```

## 5. 单位开户流程图

```mermaid
flowchart TD
    A["进入 /units/open"] --> B["录入单位开户交易要素"]
    B --> C["Controller 绑定 UnitOpenForm"]
    C --> D["UnitServiceImpl.validate"]
    D --> E{"校验通过？"}
    E -- 否 --> F["返回页面错误提示"]
    E -- 是 --> G["检查正常状态组织机构代码是否已开户"]
    G --> H{"已开户？"}
    H -- 是 --> F
    H -- 否 --> I["锁定 TB001.UNITACCNUM"]
    I --> J["生成 12 位单位账号"]
    J --> K["插入 TB002"]
    K --> L["递增 TB001.UNITACCNUM.SEQ"]
    L --> M["显示单位开户回单"]
```

## 6. 个人开户流程图

```mermaid
flowchart TD
    A["进入 /persons/open"] --> B["输入单位公积金账号"]
    B --> C["查询正常单位并反显单位名称、比例"]
    C --> D["录入个人开户资料"]
    D --> E["PersonServiceImpl.validateOpenCandidate"]
    E --> F{"单位正常且资料合法？"}
    F -- 否 --> G["返回错误提示"]
    F -- 是 --> H{"证件号存在销户账户？"}
    H -- 是 --> I["重新启用原个人账户"]
    H -- 否 --> J["锁定 TB001.PERACCNUM"]
    J --> K["生成 12 位个人账号"]
    K --> L["插入 TB003"]
    I --> M["更新 TB002 汇总人数和金额"]
    L --> M
    M --> N["显示个人开户回单"]
```

## 7. 个人资料强制变更时序图

```mermaid
sequenceDiagram
    participant U as 综合柜员
    participant C as PersonController
    participant S as PersonServiceImpl
    participant M as PersonMapper
    participant P as ParamMapper
    participant DB as MySQL

    U->>C: 提交个人资料修改
    C->>S: updatePerson(form)
    S->>M: selectIdCardConflict(newIdCard, currentAcc)
    M->>DB: 查询占用账户 B
    DB-->>M: 返回 B
    M-->>S: conflict
    S-->>C: 返回冲突结果
    C-->>U: 显示冲突确认页

    U->>C: 确认强制变更
    C->>S: forceUpdatePerson(form)
    S->>P: selectBySeqnameForUpdate(PERACCNUM)
    P->>DB: 锁定个人账号序号
    S->>M: insertWrongAccountCopy(B, 9开头证件号)
    M->>DB: 新建错误账户 C
    S->>M: updateIdCard(B, 8开头证件号)
    M->>DB: 释放 B 原证件号
    S->>M: updateEditableFields(A)
    M->>DB: A 使用正确证件号
    S->>P: updateSeq(PERACCNUM)
    S-->>C: 返回强制变更回单
    C-->>U: 显示新建错误账户账号
```

## 8. Excel 批量开户流程图

```mermaid
flowchart TD
    A["进入 /persons/open"] --> B["选择 Excel 文件"]
    B --> C["POST /persons/open/import"]
    C --> D["Apache POI 读取工作簿"]
    D --> E["跳过空行并按固定列取值"]
    E --> F["逐行复用 validateOpenCandidate"]
    F --> G{"存在失败行？"}
    G -- 是 --> H["返回成功 0 条、失败数量和逐行原因"]
    H --> I["事务回滚：不新增 TB003、不更新 TB002、不递增 PERACCNUM"]
    G -- 否 --> J["逐条调用 openPerson"]
    J --> K["插入或重启个人账户"]
    K --> L["更新单位汇总和 PERACCNUM"]
    L --> M["返回成功数量、失败 0 条"]
```

## 9. 核心类与分层结构图

```mermaid
classDiagram
    class ParamController
    class UnitController
    class PersonController
    class ParamService
    class UnitService
    class PersonService
    class ParamServiceImpl
    class UnitServiceImpl
    class PersonServiceImpl
    class ParamMapper
    class UnitMapper
    class PersonMapper
    class SystemParam
    class UnitBasicInfo
    class PersonBasicInfo

    ParamController --> ParamService
    UnitController --> UnitService
    PersonController --> PersonService
    ParamService <|.. ParamServiceImpl
    UnitService <|.. UnitServiceImpl
    PersonService <|.. PersonServiceImpl
    ParamServiceImpl --> ParamMapper
    UnitServiceImpl --> UnitMapper
    UnitServiceImpl --> ParamMapper
    PersonServiceImpl --> PersonMapper
    PersonServiceImpl --> UnitMapper
    PersonServiceImpl --> ParamMapper
    ParamMapper --> SystemParam
    UnitMapper --> UnitBasicInfo
    PersonMapper --> PersonBasicInfo
```
