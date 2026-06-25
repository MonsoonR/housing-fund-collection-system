# 任务书对齐修复记录

本文记录本轮按《网络程序设计课程设计指导书》和报告模板进行的最小必要修复。审查对象包括 `db/schema.sql`、实体类、Mapper XML、JSP 页面、README、验收清单和 `docs/final-report` Markdown 辅助材料。

## 1. 已完全一致的字段

### TB001 系统参数表

| 任务书字段 | 当前数据库字段 | Java/Mapper 映射 | 状态 |
| --- | --- | --- | --- |
| `SEQNAME` | `SEQNAME CHAR(20)` | `SystemParam.seqname` / `ParamMapper.xml` | 一致 |
| `SEQ` | `SEQ BIGINT` | `SystemParam.seq` | 一致 |
| `MAXSEQ` | `MAXSEQ BIGINT` | `SystemParam.maxseq` | 一致 |
| `DESC` | `` `DESC` VARCHAR(200) `` | `SystemParam.seqDesc`，Mapper 使用反引号转义 | 一致 |
| `FREEUSE1` | `FREEUSE1 VARCHAR(200)` | `SystemParam.freeuse1` | 一致 |

### TB002 单位基本资料表

| 任务书字段 | 当前数据库字段 | Java/Mapper 映射 | 状态 |
| --- | --- | --- | --- |
| `UNITACCNUM` | `UNITACCNUM CHAR(12)` | `UnitBasicInfo.unitAccNum` | 一致 |
| `UNITACCNAME` | `UNITACCNAME VARCHAR(50)` | `unitName` | 一致 |
| `UNITADDR` | `UNITADDR VARCHAR(200)` | `unitAddr` | 一致 |
| `ORGCODE` | `ORGCODE CHAR(20)` | `orgCode` | 表结构一致，校验按交易要素 9 位 |
| `UNITCHAR` | `UNITCHAR CHAR(1)` | `unitKind` | 一致 |
| `UNITKIND` | `UNITKIND CHAR(3)` | `unitType` | 一致 |
| `SALARYDATE` | `SALARYDATE CHAR(2)` | `salaryDate` | 一致 |
| `UNITPHONE` | `UNITPHONE VARCHAR(30)` | `phone` | 一致 |
| `UNITLINKMAN` | `UNITLINKMAN VARCHAR(50)` | `agentName` | 一致 |
| `UNITAGENTPAPNO` | `UNITAGENTPAPNO CHAR(18)` | `agentIdCard` | 一致 |
| `ACCSTATE` | `ACCSTATE CHAR(1)` | `accState` | 一致 |
| `BALANCE` | `BALANCE DECIMAL(16,2)` | `balance` | 一致 |
| `BASENUMBER` | `BASENUMBER DECIMAL(16,2)` | `baseNumber` | 一致 |
| `UNITPROP` | `UNITPROP DECIMAL(5,3)` | `unitRatio` | 一致 |
| `PERPROP` | `PERPROP DECIMAL(5,3)` | `perRatio` | 一致 |
| `UNITPAYSUM` | `UNITPAYSUM DECIMAL(16,2)` | `unitPaySum` | 一致 |
| `PERPAYSUM` | `PERPAYSUM DECIMAL(16,2)` | `perPaySum` | 一致 |
| `PERSNUM` | `PERSNUM INT` | `persNum` | 一致 |
| `LASTPAYDATE` | `LASTPAYDATE DATE` | `lastPayDate` | 一致 |
| `INSTCODE` | `INSTCODE CHAR(8)` | `instCode` | 一致 |
| `OP` | `OP CHAR(6)` | `op` | 一致 |
| `CREATDATE` | `CREATDATE DATE` | `createDate` | 一致 |
| `REMARK` | `REMARK VARCHAR(200)` | `remark` | 一致 |

### TB003 个人基本资料表

| 任务书字段 | 当前数据库字段 | Java/Mapper 映射 | 状态 |
| --- | --- | --- | --- |
| `ACCNUM` | `ACCNUM CHAR(12)` | `PersonBasicInfo.perAccNum` | 一致 |
| `UNITACCNUM` | `UNITACCNUM CHAR(12)` | `unitAccNum` | 一致 |
| `OPENDATE` | `OPENDATE DATE` | `createTime` | 一致 |
| `BALANCE` | `BALANCE DECIMAL(16,2)` | `perBalance` | 一致 |
| `PERACCSTATE` | `PERACCSTATE CHAR(1)` | `status` | 一致 |
| `BASENUMBER` | `BASENUMBER DECIMAL(16,2)` | `baseNum` | 一致 |
| `UNITPROP` | `UNITPROP DECIMAL(5,3)` | `unitRatio` | 一致 |
| `INDIPROP` | `INDIPROP DECIMAL(5,3)` | `perRatio` | 一致 |
| `LASTPAYDATE` | `LASTPAYDATE DATE` | `lastPayDate` | 一致 |
| `UNITMONPAYSUM` | `UNITMONPAYSUM DECIMAL(16,2)` | `unitMonthPay` | 一致 |
| `PERMONPAYSUM` | `PERMONPAYSUM DECIMAL(16,2)` | `perMonthPay` | 一致 |
| `YPAYAMT` | `YPAYAMT DECIMAL(16,2)` | `ypayAmt` | 一致 |
| `YDRAWAMT` | `YDRAWAMT DECIMAL(16,2)` | `ydrawAmt` | 一致 |
| `YINTERESTBAL` | `YINTERESTBAL DECIMAL(16,2)` | `yinterestBal` | 一致 |
| `INSTCODE` | `INSTCODE CHAR(8)` | `instCode` | 一致 |
| `OP` | `OP CHAR(6)` | `op` | 一致 |
| `REMARK` | `REMARK VARCHAR(200)` | `remark` | 一致 |

## 2. 与任务书不同但已修复的字段或说明

| 项目 | 修复前 | 本轮修复 |
| --- | --- | --- |
| 个人开户 Excel 批量导入 | Excel 解析 7 列：单位账号、姓名、证件类型、证件号码、缴存基数、单位比例、个人比例 | 页面批量区域单独提交单位公积金账号；Excel 只解析 5 列：序号、个人姓名、证件类型、证件号码、缴存基数 |
| 批量导入比例来源 | 单位比例、个人比例要求从 Excel 读取并与单位资料比对 | 单位比例、个人比例统一从 `TB002` 正常单位资料读取，并按 `0.050-0.120` 校验 |
| 个人资料强制变更文案 | 页面和文档把原占用账户 `8` 前缀释放写成业务规则 | 页面、README、验收清单、报告辅助材料只强调新建错误账户证件号码为 `9` + 原身份证号后 17 位；内部释放处理仅作为唯一约束实现说明 |
| 回单标题 | 成功页使用“单位开户办理成功”“个人开户办理成功”“单位资料修改办理成功”“个人资料修改办理成功” | 改为《公积金开户回单》《个人住房公积金开户回单》《住房公积金账户资料变更回单》《住房公积金个人变更回单》 |
| 查询字段术语 | 查询页面使用“合计比例”“单位月缴额”等普通标签 | 调整为“缴存比例（单位/个人/合计）”“月汇缴金额（单位/个人/合计）”等任务书术语 |
| 报告题目 | 部分 Markdown 标题混用“住房公积金归集业务系统” | 标题统一为“住房公积金管理系统——筹集子系统”，正文可说明筹集/归集业务含义 |

## 3. 任务书内部矛盾或为了业务必须保留的字段

| 字段或实现 | 保留原因 |
| --- | --- |
| `TB003.PERNAME`、`TB003.IDTYPE`、`TB003.IDCARD` | 任务书个人开户、个人资料修改、批量导入交易要素要求姓名、证件类型、证件号码，必须保存。即使部分表结构摘要未完整列出，也不能删除。 |
| `TB002.ORGCODE CHAR(20)` 与业务校验 9 位 | 表结构字段长度按指导书表结构采用 `CHAR(20)`，但交易要素和页面校验按组织机构代码 9 位执行。强行改为 `CHAR(9)` 会破坏表结构对齐。 |
| `TB003.IDCARD VARCHAR(30)` | 当前业务支持 18 位身份证，同时强制变更会生成 `9` + 后 17 位错误证件号。保留 `VARCHAR(30)` 给错误账户和后续证件类型留余量，不影响当前 18 位身份证校验。 |
| `TB001.DESC` 映射为 `seqDesc` | `DESC` 是 SQL 关键字，数据库字段按任务书保留并使用反引号；Java 属性使用 `seqDesc` 避免保留字和语义不清。 |
| `TB002` / `TB003` 中余额、年度金额、汇缴月等字段 | 本项目不扩展贷款、提取、汇缴、补缴等功能，但任务书表结构列出这些字段，查询和回单也需要部分汇总信息，故按默认值维护。 |
| 强制变更内部释放占用证件号码 | `TB003.IDCARD` 有唯一约束。为了让目标账户获得正确证件号码，同时新建错误账户保存 `9` 前缀证件号码，代码内部需要给原占用账户设置一个不冲突的释放值。该值只作为技术实现，不作为任务书业务规则展示。 |

## 4. 本轮验证重点

- 单元测试覆盖 5 列 Excel 成功导入、Excel 内重复证件号失败、单位账号不存在失败、缴存基数为空或格式错误失败、失败批次整体回滚、销户账户重新启用。
- JSP 测试覆盖个人开户批量入口、任务书回单标题、强制变更页面不把 `8` 前缀作为业务规则展示。
- 最终验证以 `mvn clean package` 输出为准。
