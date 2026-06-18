# 住房公积金管理系统——筹集子系统

传统 Maven SSM Web 项目，按《网络程序设计课程设计指导书》2.2.1—2.2.7 实现筹集子系统核心模块。本文只写运行、部署、模块、数据库和验证说明，不包含课程设计报告正文。

## 技术栈

- JDK 25
- Maven 3.9.16
- `maven.compiler.release=17`
- Spring Framework 5.3.x
- Spring MVC / Spring IoC
- MyBatis / MyBatis-Spring
- MySQL
- JSP / JSTL / HTML / CSS / JavaScript
- Tomcat 9
- Java EE `javax.*`

不使用 Spring Boot，不使用 Jakarta。

## 指导书模块对应

| 指导书章节 | 模块 | 访问路径 |
| --- | --- | --- |
| 2.2.1 | 系统参数维护 | `/params` |
| 2.2.2 | 单位开户 | `/units/open` |
| 2.2.3 | 个人开户 | `/persons/open` |
| 2.2.4 | 单位资料修改 | `/units/edit` |
| 2.2.5 | 个人资料修改 | `/persons/edit` |
| 2.2.6 | 单位信息查询 | `/units/query` |
| 2.2.7 | 个人信息查询 | `/persons/query` |

首页路径：`/` 或 `/index`。

未实现且不在本课程设计范围内：汇缴、补缴、提取、封存、启封、比例变更、基数变更、单位注销、个人注销、贷款、复杂登录权限系统。

## 数据库初始化

本轮已将核心表和字段向指导书 2.3.1 对齐。课程设计测试库可以重新导入，不提供复杂生产迁移脚本。

需要重新导入数据库时按顺序执行：

```bat
mysql -u root -p < db/schema.sql
mysql -u root -p housingfund_collection < db/data.sql
mysql -u root -p housingfund_collection < db/demo-data.sql
```

`db/schema.sql` 会创建并重建：

- `TB001`：系统参数表
- `TB002`：单位基本资料表
- `TB003`：个人基本资料表

`db/data.sql` 初始化账号序号：

- `UNITACCNUM, 1, 999999999, 公积金单位账号序号`
- `PERACCNUM, 1, 999999999, 公积金个人账号序号`

`db/demo-data.sql` 提供答辩演示数据，包含正常单位、资料修改单位、销户单位、正常个人、身份证冲突占用账户和销户个人重新启用数据。

## 数据库字段说明

字段映射详见 `docs/SCHEMA_MAPPING.md`。

关键对齐结果：

- `TB001` 使用指导书字段 `SEQNAME`、`SEQ`、`MAXSEQ`、`DESC`、`FREEUSE1`。`DESC` 在 MySQL 中用反引号包裹。
- `TB002` 使用 `UNITACCNAME`、`UNITCHAR`、`UNITKIND`、`UNITPHONE`、`UNITLINKMAN`、`UNITAGENTPAPNO`、`UNITPROP`、`PERPROP` 等指导书字段。
- `TB003` 使用 `ACCNUM`、`OPENDATE`、`BALANCE`、`PERACCSTATE`、`BASENUMBER`、`UNITPROP`、`INDIPROP`、`UNITMONPAYSUM`、`PERMONPAYSUM` 等指导书字段。
- `TB003` 额外保留 `PERNAME`、`IDTYPE`、`IDCARD`，原因是指导书个人开户和个人资料修改交易要素要求姓名、证件类型、证件号码，但 2.3.1 表结构未完整列出。

## 数据库连接配置

Spring 当前仍读取：

```text
src/main/resources/jdbc.properties
```

仓库中的 `jdbc.password` 是占位值：

```properties
jdbc.password=change_me
```

部署前按本机 MySQL 修改 `jdbc.properties`。示例配置见：

```text
src/main/resources/jdbc.example.properties
```

不要提交真实数据库密码。需要本地私有配置时可使用 `*.local.properties` 或 `src/main/resources/jdbc-local.properties`，这些路径已加入 `.gitignore`。

## Maven 构建

```bat
D:\dev\apache-maven-3.9.16\bin\mvn.cmd clean package
```

期望结果：

```text
BUILD SUCCESS
Tests run: 全部通过
Failures: 0
Errors: 0
```

WAR 输出路径：

```text
target/housingfund-collection.war
```

## Tomcat 9 部署

1. 启动 MySQL。
2. 按需重新导入 `db/schema.sql`、`db/data.sql`、`db/demo-data.sql`。
3. 根据本机 MySQL 修改 `src/main/resources/jdbc.properties`。
4. 执行 Maven 构建。
5. 将 `target/housingfund-collection.war` 放入 Tomcat 9 的 `webapps` 目录。
6. 启动 Tomcat。
7. 访问 `http://localhost:8080/housingfund-collection/`。

## 七个访问路径

- `http://localhost:8080/housingfund-collection/params`
- `http://localhost:8080/housingfund-collection/units/open`
- `http://localhost:8080/housingfund-collection/persons/open`
- `http://localhost:8080/housingfund-collection/units/edit`
- `http://localhost:8080/housingfund-collection/persons/edit`
- `http://localhost:8080/housingfund-collection/units/query`
- `http://localhost:8080/housingfund-collection/persons/query`

## 验收资料

- `docs/ACCEPTANCE_CHECKLIST.md`：按指导书 2.2.1—2.2.7 排列的手动验收清单。
- `docs/DEMO_FLOW.md`：答辩演示流程。
- `docs/SCHEMA_MAPPING.md`：指导书表名和字段名与项目实际字段名的映射。

## 个人资料强制变更说明

个人资料修改已实现证件号码冲突回显、强制变更提示、占用账户证件号码首位改 `9`、当前账户更新为正确信息。指导书中“新建一个人账户存储占用的个人账户信息（错误的）”当前未额外建账，原因是课程设计固定 `TB003.IDCARD` 唯一且没有独立错误账户迁移目标表；该处理方式也写入 `docs/ACCEPTANCE_CHECKLIST.md`。
