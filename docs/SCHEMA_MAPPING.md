# 数据库表字段映射说明

本项目 MySQL 表结构尽量直接使用《网络程序设计课程设计指导书》2.3.1 的表名和字段名。Java 实体类保留驼峰属性名，MyBatis `resultMap` 负责映射到指导书字段。

## TB001 系统参数表

| 指导书字段名 | 项目实际字段名 | 是否一致 | 说明 |
| --- | --- | --- | --- |
| SEQNAME | SEQNAME | 是 | 键值信息 |
| SEQ | SEQ | 是 | 当前序号 |
| MAXSEQ | MAXSEQ | 是 | 最大序号 |
| DESC | DESC | 是 | MySQL 中使用反引号 `` `DESC` `` |
| FREEUSE1 | FREEUSE1 | 是 | 备用1 |

初始化数据：

| SEQNAME | SEQ | MAXSEQ | DESC |
| --- | ---: | ---: | --- |
| UNITACCNUM | 1 | 999999999 | 公积金单位账号序号 |
| PERACCNUM | 1 | 999999999 | 公积金个人账号序号 |

## TB002 单位基本资料表

| 指导书字段名 | 项目实际字段名 | 是否一致 | Java 属性 |
| --- | --- | --- | --- |
| UNITACCNUM | UNITACCNUM | 是 | unitAccNum |
| UNITACCNAME | UNITACCNAME | 是 | unitName |
| UNITADDR | UNITADDR | 是 | unitAddr |
| ORGCODE | ORGCODE | 是 | orgCode |
| UNITCHAR | UNITCHAR | 是 | unitKind |
| UNITKIND | UNITKIND | 是 | unitType |
| SALARYDATE | SALARYDATE | 是 | salaryDate |
| UNITPHONE | UNITPHONE | 是 | phone |
| UNITLINKMAN | UNITLINKMAN | 是 | agentName |
| UNITAGENTPAPNO | UNITAGENTPAPNO | 是 | agentIdCard |
| ACCSTATE | ACCSTATE | 是 | accState |
| BALANCE | BALANCE | 是 | balance |
| BASENUMBER | BASENUMBER | 是 | baseNumber |
| UNITPROP | UNITPROP | 是 | unitRatio |
| PERPROP | PERPROP | 是 | perRatio |
| UNITPAYSUM | UNITPAYSUM | 是 | unitPaySum |
| PERPAYSUM | PERPAYSUM | 是 | perPaySum |
| PERSNUM | PERSNUM | 是 | persNum |
| LASTPAYDATE | LASTPAYDATE | 是 | lastPayDate |
| INSTCODE | INSTCODE | 是 | instCode |
| OP | OP | 是 | op |
| CREATDATE | CREATDATE | 是 | createDate |
| REMARK | REMARK | 是 | remark |

旧字段迁移结果：

| 原项目字段 | 指导书字段 |
| --- | --- |
| UNITNAME | UNITACCNAME |
| UNITTYPE | UNITKIND |
| UNITKIND | UNITCHAR |
| PHONE | UNITPHONE |
| AGENTNAME | UNITLINKMAN |
| AGENTIDCARD | UNITAGENTPAPNO |
| UNITRATIO | UNITPROP |
| PERRATIO | PERPROP |

## TB003 个人基本资料表

| 指导书字段名 | 项目实际字段名 | 是否一致 | Java 属性 |
| --- | --- | --- | --- |
| ACCNUM | ACCNUM | 是 | perAccNum |
| UNITACCNUM | UNITACCNUM | 是 | unitAccNum |
| OPENDATE | OPENDATE | 是 | createTime |
| BALANCE | BALANCE | 是 | perBalance |
| PERACCSTATE | PERACCSTATE | 是 | status |
| BASENUMBER | BASENUMBER | 是 | baseNum |
| UNITPROP | UNITPROP | 是 | unitRatio |
| INDIPROP | INDIPROP | 是 | perRatio |
| LASTPAYDATE | LASTPAYDATE | 是 | lastPayDate |
| UNITMONPAYSUM | UNITMONPAYSUM | 是 | unitMonthPay |
| PERMONPAYSUM | PERMONPAYSUM | 是 | perMonthPay |
| YPAYAMT | YPAYAMT | 是 | ypayAmt |
| YDRAWAMT | YDRAWAMT | 是 | ydrawAmt |
| YINTERESTBAL | YINTERESTBAL | 是 | yinterestBal |
| INSTCODE | INSTCODE | 是 | instCode |
| OP | OP | 是 | op |
| REMARK | REMARK | 是 | remark |

必要补充字段：

| 补充字段 | 项目实际字段名 | 是否一致 | 原因 |
| --- | --- | --- | --- |
| 姓名 | PERNAME | 是 | 指导书个人开户、个人资料修改交易要素要求，但 2.3.1 表结构未完整列出 |
| 证件类型 | IDTYPE | 是 | 指导书个人开户、个人资料修改交易要素要求 |
| 证件号码 | IDCARD | 是 | 指导书个人开户、个人资料修改交易要素要求 |

旧字段迁移结果：

| 原项目字段 | 指导书字段 |
| --- | --- |
| PERACCNUM | ACCNUM |
| CREATE_TIME | OPENDATE |
| PERBALANCE | BALANCE |
| STATUS | PERACCSTATE |
| BASENUM | BASENUMBER |
| UNITRATIO | UNITPROP |
| PERRATIO | INDIPROP |
| UNITMONTHPAY | UNITMONPAYSUM |
| PERMONTHPAY | PERMONPAYSUM |

已删除的非指导书个人字段：

| 原项目字段 | 处理 |
| --- | --- |
| PHONE | 已删除，不出现在页面、VO、实体、Mapper 更新 SQL 或测试中 |
| ADDRESS | 已删除，不出现在页面、VO、实体、Mapper 更新 SQL 或测试中 |
| UPDATE_TIME | 已删除，个人资料修改不以该字段作为指导书输出要素 |
