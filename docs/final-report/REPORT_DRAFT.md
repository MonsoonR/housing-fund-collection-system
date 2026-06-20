# 住房公积金归集业务系统课程设计报告草稿

> 说明：本文是用于迁移到 Word 报告模板的 Markdown 草稿。姓名、学号、班级、指导教师等个人信息应在 Word 模板中人工补齐。

## 课程设计题目

住房公积金归集业务系统。

## 第1章 需求分析

### 1.1 项目简介

本课程设计实现住房公积金管理系统中的归集业务部分。系统围绕缴存单位和缴存职工的账户管理展开，主要完成系统参数维护、单位开户、个人开户、资料修改和信息查询等业务。项目采用传统 Maven SSM Web 技术路线，后端使用 Spring、Spring MVC、MyBatis，前端使用 JSP、HTML、CSS 和 JavaScript，数据库使用 MySQL，最终以 WAR 包部署到 Tomcat。

系统不扩展贷款、提取、汇缴、补缴、封存、启封、单位注销、个人注销等复杂业务，功能范围聚焦于课程设计指导书规定的筹集子系统核心模块。

### 1.2 功能性需求

系统需要实现以下功能：

1. 系统参数维护：维护 `TB001` 中的系统序号和普通参数，支持参数查询、新增、修改和删除；账号生成序列 `UNITACCNUM`、`PERACCNUM` 必须受保护。
2. 单位开户：录入单位名称、地址、组织机构代码、单位类别、企业类型、发薪日期、联系电话、经办人、缴存比例等信息，生成 12 位单位公积金账号，写入 `TB002`。
3. 个人开户：根据正常状态单位账号开立个人账户，保存姓名、证件类型、证件号码、缴存基数等信息，生成 12 位个人公积金账号，写入 `TB003` 并同步更新单位汇总字段。
4. 个人 Excel 批量开户：在个人开户模块中上传 Excel 文件，按“单位账号、姓名、证件类型、证件号码、缴存基数、单位比例、个人比例”列顺序批量开户；任一数据行失败时整体不提交，并显示逐行失败原因。
5. 单位资料修改：按单位公积金账号查询并反显单位资料，只允许修改指导书规定的单位基础资料字段，销户单位不可修改。
6. 个人资料修改：按个人公积金账号查询并反显个人资料，只允许修改姓名、证件类型、证件号码；证件号码冲突时支持强制变更，并新建错误账户保存占用账户原信息。
7. 单位信息查询：支持按单位公积金账号精确查询和按单位名称模糊查询，显示单位账户、缴存比例、月缴额、人数等核心信息。
8. 个人信息查询：支持按个人公积金账号和证件号码查询，显示单位信息、姓名、证件号码、缴存基数、比例、月缴额、余额和状态等信息。

### 1.3 非功能性需求

系统应满足以下非功能性要求：

1. 采用 MVC 分层架构，Controller 不直接访问数据库，业务规则集中在 Service 层，SQL 访问集中在 Mapper 层。
2. 开户、序号生成、单位汇总更新、Excel 批量导入和强制变更应具备事务一致性。
3. 金额、缴存基数、月缴额等字段使用 `BigDecimal` 和 `DECIMAL(16,2)`，避免浮点数误差。
4. 前端页面使用 HTML5 和 JavaScript 做基础校验，后端 Service 层必须再次校验关键业务规则。
5. 本地数据库密码不写入仓库，仓库内 `jdbc.properties` 使用占位值，本地通过 `jdbc-local.properties` 覆盖。
6. 系统能够通过 Maven 构建、Tomcat 部署、MySQL 初始化和浏览器页面操作进行验收。

### 1.4 角色与业务流程

系统主要面向综合柜员使用。综合柜员根据单位经办人或缴存职工提交的资料完成账户开户、资料变更和信息查询。系统管理员或运维人员负责数据库初始化、配置数据库连接和部署 WAR 包。

核心业务流程包括单位开户、个人开户、Excel 批量开户、资料修改和信息查询。其中单位开户先建立缴存单位账户，个人开户必须基于正常状态的单位账户。个人资料修改中，如果新证件号码被其他个人账户占用，系统先提示冲突信息，用户确认强制变更后再进行事务性处理。

### 1.5 用例建模

用例图可使用 `DIAGRAMS.md` 中的“系统用例图”。主要用例包括系统参数维护、单位开户、个人开户、Excel 批量开户、单位资料修改、个人资料修改、单位信息查询和个人信息查询。

### 1.6 验收要求对应关系

课程设计指导书要求完成系统分析、概要设计、详细设计、数据库设计、模块编码、前后端联调测试和报告编写。本项目已形成对应材料：功能与流程说明见本报告第1章和第2章，数据库设计见 `DATABASE_DESIGN.md`，关键代码说明见 `CORE_CODE_EXPLANATION.md`，测试用例见 `TEST_CASES.md`，验收总结见 `ACCEPTANCE_SUMMARY.md`。

## 第2章 系统设计

### 2.1 概要设计

系统采用传统 SSM 分层结构。浏览器访问 JSP 页面，提交请求到 Spring MVC Controller；Controller 进行参数绑定和页面跳转；Service 层负责业务校验、事务控制和账号生成；Mapper 层通过 MyBatis XML 执行 SQL；MySQL 保存系统参数、单位资料和个人资料。

系统核心包结构如下：

- `controller`：处理请求、绑定参数、返回 JSP。
- `service`：定义业务接口。
- `service.impl`：实现业务规则和事务控制。
- `mapper`：MyBatis 数据访问接口。
- `entity`：数据库表实体。
- `vo`：页面表单、查询结果和回单对象。
- `util`：账号、日期、身份证和金额相关工具。
- `exception`：业务异常。

### 2.2 功能设计

系统功能分为八个验收入口，其中个人 Excel 批量开户归属于个人开户模块：

1. 系统参数维护。
2. 单位开户。
3. 个人开户。
4. 个人 Excel 批量开户。
5. 单位资料修改。
6. 个人资料修改。
7. 单位信息查询。
8. 个人信息查询。

功能层次图、活动图和时序图源码见 `DIAGRAMS.md`。

### 2.3 数据设计

系统核心表为：

- `TB001`：系统参数表，保存 `UNITACCNUM`、`PERACCNUM` 等序号。
- `TB002`：单位基本资料表，保存单位开户资料、缴存比例、余额、汇总基数、人数等信息。
- `TB003`：个人基本资料表，保存个人账号、所属单位、姓名、证件信息、缴存基数、比例、月缴额、余额和账户状态。

`TB003.UNITACCNUM` 与 `TB002.UNITACCNUM` 建立外键关系。`TB001` 不直接建立外键，但为单位账号和个人账号提供逻辑序号来源。

### 2.4 关键业务规则设计

单位账号和个人账号均从 `TB001` 中取当前序号生成，不足 12 位前补 `0`。单位开户成功后递增 `UNITACCNUM`，个人新开户成功后递增 `PERACCNUM`。销户个人账户重新启用时复用原账号，不生成新账号。

Excel 批量开户采用“全部成功才提交”的事务策略。系统先解析 Excel 并逐行复用个人开户校验逻辑，若存在任一失败行，则本批次不新增任何个人账户，不更新单位汇总，也不递增个人账号序号。

个人资料强制变更中，若 A 账户要改为被 B 账户占用的证件号码，系统使用 `TB001.PERACCNUM` 生成新个人账号 C，C 复制 B 原信息并使用 `9` 开头的错误证件号保存；B 使用 `8` 开头证件号释放原号码；A 更新为正确证件号码。该过程在事务内完成。

## 第3章 系统实现

### 3.1 开发环境与技术栈

项目采用 Java、Maven、Spring、Spring MVC、MyBatis、JSP、MySQL、Tomcat。构建命令为 `mvn clean package`，输出 WAR 包为 `target/housingfund-collection.war`。

### 3.2 系统参数维护实现

系统参数维护入口为 `/params`，对应 `ParamController`、`ParamServiceImpl`、`ParamMapper` 和 `param/list.jsp`、`param/form.jsp`。Service 层负责检查参数名、序号范围和账号序列保护规则，Mapper 层操作 `TB001`。

### 3.3 单位开户实现

单位开户入口为 `/units/open`，对应 `UnitController#open` 和 `UnitServiceImpl#openUnit`。业务层校验组织机构代码、发薪日期、身份证号码、缴存比例等字段，锁定 `TB001.UNITACCNUM` 后生成 12 位单位账号，插入 `TB002` 并递增序号。

### 3.4 个人开户实现

个人开户入口为 `/persons/open`，对应 `PersonController#open` 和 `PersonServiceImpl#openPerson`。系统先校验单位账号必须存在且为正常状态，再校验姓名、证件类型、证件号码、缴存基数和比例。新开户写入 `TB003`，并更新 `TB002` 的人数、基数、单位月缴额和个人月缴额。

### 3.5 Excel 批量开户实现

Excel 批量入口为 `/persons/open/import`，对应 `PersonController#importPersons` 和 `PersonServiceImpl#importPersons`。系统使用 Apache POI 解析 `.xls/.xlsx`，跳过空行，检查 Excel 内重复证件号、单位状态、证件号占用、缴存基数和比例格式。导入执行时复用 `validateOpenCandidate` 和 `openPerson`，保证批量导入与手工开户校验一致。

### 3.6 单位资料修改实现

单位资料修改入口为 `/units/edit`，对应 `UnitController#update` 和 `UnitServiceImpl#updateUnit`。系统按单位账号反显资料，提交时只更新单位名称、地址、组织机构代码、类别、类型、发薪日期、联系电话、经办人、经办人身份证号和备注，不允许通过请求参数修改余额、汇总金额、人数、状态和账号。

### 3.7 个人资料修改实现

个人资料修改入口为 `/persons/edit`，对应 `PersonController#update`、`PersonController#forceUpdate`、`PersonServiceImpl#updatePerson` 和 `PersonServiceImpl#forceUpdatePerson`。普通修改只更新姓名、证件类型和证件号码。证件号冲突时返回冲突确认页，强制变更时调用 `insertWrongAccountCopy` 新建错误账户保存占用账户原信息。

### 3.8 信息查询实现

单位信息查询入口为 `/units/query`，对应 `UnitServiceImpl#queryUnits` 和 `UnitMapper` 中的账号精确查询、名称模糊查询 SQL。个人信息查询入口为 `/persons/query`，对应 `PersonServiceImpl#queryPerson` 和 `PersonMapper` 中按个人账号、按证件号查询 SQL。

## 第4章 系统测试

### 4.1 测试环境

测试使用 MySQL 测试库 `housingfund_collection_acceptance`，按顺序导入 `db/schema.sql`、`db/data.sql`、`db/demo-data.sql`。本地 Tomcat 部署访问路径为 `http://127.0.0.1:18090/housingfund-collection/`，项目标准 WAR 路径为 `/housingfund-collection/`。

### 4.2 自动化构建测试

执行 `mvn clean package`，结果为：

```text
Tests run: 78, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
WAR: target/housingfund-collection.war
```

测试覆盖 Schema 字段对齐、系统参数、单位业务、个人业务、VO 字段和 JSP 语法。

### 4.3 实机验收测试

实机验证覆盖首页、系统参数维护、单位开户、个人手工开户、Excel 批量开户、单位资料修改、个人资料修改、单位信息查询和个人信息查询。Excel 批量开户验证了失败批次整体回滚和成功批次写入 2 条数据。个人资料强制变更验证了目标账户获得正确证件号、原占用账户释放证件号、新建错误账户保存原占用账户信息以及 `TB001.PERACCNUM` 正确递增。

详细测试用例见 `TEST_CASES.md`，截图插入位置见 `SCREENSHOT_INSERTION_GUIDE.md`。

## 第5章 课程设计总结

本次课程设计完成了住房公积金归集业务系统的核心功能，实现了从数据库设计、后端业务处理、前端 JSP 页面、MyBatis SQL 到 Tomcat 部署的完整流程。开发过程中重点解决了账号序号生成、事务一致性、Excel 批量导入、个人资料强制变更、数据库字段与指导书对齐、本地数据库密码隔离等问题。

通过本项目，加深了对 Spring MVC 请求处理、Spring IoC 和事务管理、MyBatis 映射、JSP 页面展示以及 MySQL 表结构设计的理解。系统仍可继续改进的方向包括引入登录权限控制、完善操作日志、增加更多浏览器端自动化测试和进一步优化页面交互，但这些内容不属于本次课程设计核心范围。

## 参考资料或附录

1. 《2025-2026学年第二学期网络程序设计课程设计指导书》。
2. 《2025-2026学年第二学期网络程序设计课程设计报告模版》。
3. 《课程设计说明书及成绩考核表》。
4. Spring Framework、Spring MVC、MyBatis、MySQL、Apache POI、Tomcat 官方文档。
5. 项目源码、`README.md`、`docs/ACCEPTANCE_CHECKLIST.md`、`docs/DEMO_FLOW.md`、`docs/SCHEMA_MAPPING.md`。
