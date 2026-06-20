# 数据库设计说明

数据库使用 MySQL，核心表固定为 `TB001`、`TB002`、`TB003`。建表脚本位于 `db/schema.sql`，基础序号数据位于 `db/data.sql`，演示数据位于 `db/demo-data.sql`。

## 1. TB001 系统参数表

`TB001` 用于保存系统参数和账号生成序号。

| 字段 | 类型 | 约束 | 说明 |
| --- | --- | --- | --- |
| `SEQNAME` | `CHAR(20)` | 主键 | 键值信息，已按指导书对齐为 `CHAR(20)`。 |
| `SEQ` | `BIGINT` | 非空，`SEQ > 0` | 当前序号。 |
| `MAXSEQ` | `BIGINT` | 非空，`MAXSEQ > 0` | 最大序号。 |
| `DESC` | `VARCHAR(200)` | 非空 | 描述，MySQL 中使用反引号。 |
| `FREEUSE1` | `VARCHAR(200)` | 可空 | 备用字段。 |

基础参数：

| SEQNAME | 用途 | 保护规则 |
| --- | --- | --- |
| `UNITACCNUM` | 单位账号生成序号 | 禁止删除。 |
| `PERACCNUM` | 个人账号生成序号 | 禁止删除。 |

## 2. TB002 单位账户表

`TB002` 保存缴存单位基本资料、账户状态和缴存汇总信息。

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `UNITACCNUM` | `CHAR(12)` | 单位公积金账号，主键。 |
| `UNITACCNAME` | `VARCHAR(50)` | 单位名称。 |
| `UNITADDR` | `VARCHAR(200)` | 单位地址。 |
| `ORGCODE` | `CHAR(20)` | 组织机构代码。数据库按指导书表结构采用 `CHAR(20)`，业务按 9 位校验。 |
| `UNITCHAR` | `CHAR(1)` | 单位类别。 |
| `UNITKIND` | `CHAR(3)` | 企业类型。 |
| `SALARYDATE` | `CHAR(2)` | 发薪日期。 |
| `UNITPHONE` | `VARCHAR(30)` | 联系电话。 |
| `UNITLINKMAN` | `VARCHAR(50)` | 单位经办人。 |
| `UNITAGENTPAPNO` | `CHAR(18)` | 经办人身份证号码。 |
| `ACCSTATE` | `CHAR(1)` | 单位账户状态，`0` 正常，`9` 销户演示数据。 |
| `BALANCE` | `DECIMAL(16,2)` | 公积金余额。 |
| `BASENUMBER` | `DECIMAL(16,2)` | 单位缴存基数汇总。 |
| `UNITPROP` | `DECIMAL(5,3)` | 单位比例。 |
| `PERPROP` | `DECIMAL(5,3)` | 个人比例。 |
| `UNITPAYSUM` | `DECIMAL(16,2)` | 单位月应缴额汇总。 |
| `PERPAYSUM` | `DECIMAL(16,2)` | 个人月应缴额汇总。 |
| `PERSNUM` | `INT` | 单位人数。 |
| `LASTPAYDATE` | `DATE` | 最后汇缴月。 |
| `INSTCODE` | `CHAR(8)` | 公积金中心机构代码，已对齐为 `CHAR(8)`。 |
| `OP` | `CHAR(6)` | 柜员。 |
| `CREATDATE` | `DATE` | 建立日期。 |
| `REMARK` | `VARCHAR(200)` | 备注。 |

约束与索引：

- 主键：`UNITACCNUM`。
- 唯一约束：`uk_tb002_orgcode_accstate (ORGCODE, ACCSTATE)`，用于控制正常状态组织机构代码重复。
- 普通索引：`idx_tb002_unitaccname (UNITACCNAME)`，用于单位名称模糊查询。

## 3. TB003 个人账户表

`TB003` 保存个人账户资料和缴存信息。

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `ACCNUM` | `CHAR(12)` | 个人公积金账号，主键。 |
| `UNITACCNUM` | `CHAR(12)` | 所属单位公积金账号，外键关联 `TB002.UNITACCNUM`。 |
| `PERNAME` | `VARCHAR(12)` | 姓名，业务必要扩展字段。 |
| `IDTYPE` | `VARCHAR(20)` | 证件类型，业务必要扩展字段。 |
| `IDCARD` | `VARCHAR(30)` | 证件号码，业务必要扩展字段，唯一。 |
| `OPENDATE` | `DATE` | 开户日期。 |
| `BALANCE` | `DECIMAL(16,2)` | 公积金余额。 |
| `PERACCSTATE` | `CHAR(1)` | 个人账户状态，`0` 正常，`9` 销户。 |
| `BASENUMBER` | `DECIMAL(16,2)` | 缴存基数。 |
| `UNITPROP` | `DECIMAL(5,3)` | 单位比例。 |
| `INDIPROP` | `DECIMAL(5,3)` | 个人比例。 |
| `LASTPAYDATE` | `DATE` | 最后汇缴月。 |
| `UNITMONPAYSUM` | `DECIMAL(16,2)` | 单位月应缴额。 |
| `PERMONPAYSUM` | `DECIMAL(16,2)` | 个人月应缴额。 |
| `YPAYAMT` | `DECIMAL(16,2)` | 本年汇补缴额。 |
| `YDRAWAMT` | `DECIMAL(16,2)` | 年提取额。 |
| `YINTERESTBAL` | `DECIMAL(16,2)` | 年度结息。 |
| `INSTCODE` | `CHAR(8)` | 公积金中心机构代码，已对齐为 `CHAR(8)`。 |
| `OP` | `CHAR(6)` | 柜员。 |
| `REMARK` | `VARCHAR(200)` | 备注。 |

约束与索引：

- 主键：`ACCNUM`。
- 唯一约束：`uk_tb003_idcard (IDCARD)`。
- 外键：`fk_tb003_unitaccnum`，`TB003.UNITACCNUM` 引用 `TB002.UNITACCNUM`。
- 普通索引：`idx_tb003_unitaccnum (UNITACCNUM)`。

## 4. 账号生成规则

单位账号：

1. 锁定 `TB001` 中 `SEQNAME='UNITACCNUM'` 的记录。
2. 读取当前 `SEQ`。
3. 将序号左侧补 `0` 到 12 位，生成单位账号。
4. 插入 `TB002` 成功后将 `SEQ` 加 1。

个人账号：

1. 锁定 `TB001` 中 `SEQNAME='PERACCNUM'` 的记录。
2. 读取当前 `SEQ`。
3. 将序号左侧补 `0` 到 12 位，生成个人账号。
4. 新开个人账户或强制变更新建错误账户成功后将 `SEQ` 加 1。
5. 销户账户重启时复用原个人账号，不递增 `PERACCNUM`。

## 5. 单位账户与个人账户关系

一个单位账户可以对应多个个人账户。`TB002.UNITACCNUM` 是单位主键，`TB003.UNITACCNUM` 是个人账户所属单位字段。个人开户成功后，系统会同步更新单位表中的 `PERSNUM`、`BASENUMBER`、`UNITPAYSUM` 和 `PERPAYSUM`。

## 6. 指导书内部冲突处理说明

1. `TB001.SEQNAME`：指导书要求 `CHARACTER(20)`，项目已对齐为 `CHAR(20)`。
2. 金额字段：指导书多处金额字段要求 `DECIMAL(16,2)`，项目已将余额、缴存基数、月缴额和年度金额字段对齐为 `DECIMAL(16,2)`。
3. `TB002.INSTCODE`、`TB003.INSTCODE`：指导书要求 `CHARACTER(8)`，项目已对齐为 `CHAR(8)`。
4. `TB002.ORGCODE`：指导书表结构写作 `CHARACTER(20)`，交易要素要求 9 位。项目采用数据库 `CHAR(20)` 对齐表结构，Service/JSP 校验按 9 位组织机构代码执行。
5. `TB003.PERNAME/IDTYPE/IDCARD`：指导书 2.3.1 表结构未完整列出这些字段，但个人开户、个人资料修改和个人信息查询交易要素必须保存姓名、证件类型和证件号码，因此项目保留为业务必要扩展字段。
