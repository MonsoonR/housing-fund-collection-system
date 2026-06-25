# 住房公积金管理系统——筹集子系统

传统 Maven SSM Web 项目，按《网络程序设计课程设计指导书》实现筹集子系统核心业务。本文只说明运行、部署、模块、数据库和验收，不包含课程设计报告正文。

## 技术栈

- Java / Maven
- Spring MVC / Spring IoC
- MyBatis / MyBatis-Spring
- MySQL
- JSP / JSTL / HTML / CSS / JavaScript
- Tomcat 9
- Java EE `javax.*`

不使用 Spring Boot，不使用 Jakarta。

## 指导书模块对应

部署到 Tomcat 后，默认上下文路径统一为 `/housingfund-collection/`，首页为：

```text
http://localhost:8080/housingfund-collection/
```

| 指导书模块 | 业务模块 | 页面路径 |
| --- | --- | --- |
| 系统参数维护 | 维护 `TB001` 序号和系统参数 | `/params` |
| 单位开户 | 生成单位账号，写入 `TB002`，递增 `TB001.UNITACCNUM` | `/units/open` |
| 个人开户 | 手工开户、销户账户重启、Excel 批量开户 | `/persons/open` |
| 单位资料修改 | 修改单位基础资料，不修改汇总和状态字段 | `/units/edit` |
| 个人资料修改 | 修改姓名、证件类型、证件号码，支持冲突强制变更 | `/persons/edit` |
| 单位信息查询 | 按账号精确查询、按名称模糊查询 | `/units/query` |
| 个人信息查询 | 按个人账号或证件号码查询 | `/persons/query` |

未实现且不在本课程设计范围内：贷款、提取、汇缴、补缴、封存、启封、单位注销、个人注销、复杂登录权限系统。

## 数据库初始化

按顺序执行：

```powershell
mysql -u root -p < db/schema.sql
mysql -u root -p housingfund_collection < db/data.sql
mysql -u root -p housingfund_collection < db/demo-data.sql
```

- `db/schema.sql` 会创建数据库并先 `DROP TABLE IF EXISTS TB003/TB002/TB001`，会清空重建课程设计测试表。
- `db/data.sql` 不删表，只初始化 `TB001` 中 `UNITACCNUM` 和 `PERACCNUM` 两条基础序号数据。
- `db/demo-data.sql` 不删全库，只清理 `000000900xxx` 演示账号段和脚本内列出的演示证件号码，再导入 demo 单位、正常个人、冲突占用个人和销户个人。

核心表：

- `TB001`：系统参数表。
- `TB002`：单位基本资料表。
- `TB003`：个人基本资料表。

字段映射和指导书冲突处理详见 `docs/SCHEMA_MAPPING.md`。

## 数据库连接配置

仓库内配置只允许放占位值。`src/main/resources/jdbc.properties` 中密码必须保持：

```properties
jdbc.password=change_me
```

本地运行时复制示例配置并填写自己的 MySQL 用户名和密码：

```powershell
Copy-Item src/main/resources/jdbc.example.properties src/main/resources/jdbc-local.properties
```

然后编辑 `src/main/resources/jdbc-local.properties`。Spring 会先读取 `jdbc.properties`，再读取可选的 `jdbc-local.properties` 覆盖本地配置。

不要提交真实数据库密码。`.gitignore` 已忽略：

```text
*.local.properties
src/main/resources/jdbc-local.properties
```

## Excel 批量个人开户

入口在个人开户页面：

```text
http://localhost:8080/housingfund-collection/persons/open
```

批量区域先填写“单位公积金账号”，上传文件支持 `.xls` 和 `.xlsx`。系统使用页面提交的单位账号查询正常单位，并从单位资料读取单位名称、单位比例和个人比例。Excel 首行是表头，后续列顺序固定：

```text
序号、个人姓名、证件类型、证件号码、缴存基数
```

导入策略为“全部成功才提交”：任一非空数据行失败时，本批次成功数为 0，不生成个人账号，不更新单位汇总和 `TB001.PERACCNUM`。页面显示成功数量、失败数量和逐行失败原因。

## 个人资料强制变更说明

当 A 账户要改成的证件号码已被 B 账户占用，并选择强制变更时：

- 使用 `TB001.PERACCNUM` 生成新个人账号 C。
- C 复制 B 原有姓名、单位账号、证件类型、状态、余额、缴存基数、缴存比例、月缴额、开户日期、最后汇缴月、年度金额、机构、柜员和备注。
- C 的证件号码使用 `9` + B 原证件号码后 17 位，作为错误账户保存记录。
- A 更新为正确姓名、证件类型和证件号码。
- 全过程在 Service 层事务内完成。

该方案保留指导书要求的“新建一个人账户存储占用的个人账户信息（错误的）”。实现内部为了满足 `TB003.IDCARD` 唯一约束，会临时处理原占用账户证件号码，但该处理不作为任务书业务规则展示。

## 构建

```powershell
mvn clean package
```

构建成功后 WAR 输出：

```text
target/housingfund-collection.war
```

## Tomcat 9 部署

1. 启动 MySQL。
2. 按需导入 `db/schema.sql`、`db/data.sql`、`db/demo-data.sql`。
3. 配置本机 `src/main/resources/jdbc-local.properties`。
4. 执行 `mvn clean package`。
5. 将 `target/housingfund-collection.war` 放入 Tomcat 9 的 `webapps` 目录。
6. 启动 Tomcat。
7. 访问 `http://localhost:8080/housingfund-collection/`。

`.idea` 中的本机 Tomcat 配置不作为验收依据，验收以 WAR 名称 `housingfund-collection.war` 和上述访问路径为准。

## 验收路径

- `http://localhost:8080/housingfund-collection/params`
- `http://localhost:8080/housingfund-collection/units/open`
- `http://localhost:8080/housingfund-collection/persons/open`
- `http://localhost:8080/housingfund-collection/persons/open` 页面内 Excel 批量导入
- `http://localhost:8080/housingfund-collection/units/edit`
- `http://localhost:8080/housingfund-collection/persons/edit`
- `http://localhost:8080/housingfund-collection/units/query`
- `http://localhost:8080/housingfund-collection/persons/query`

## 验收资料

- `docs/ACCEPTANCE_CHECKLIST.md`：手动验收清单。
- `docs/DEMO_FLOW.md`：答辩演示流程。
- `docs/SCHEMA_MAPPING.md`：指导书字段、项目字段和冲突处理说明。
