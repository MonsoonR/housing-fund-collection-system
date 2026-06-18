# 住房公积金管理系统——筹集子系统

传统 Maven SSM Web 项目，用于完成住房公积金管理系统筹集子系统的 7 个核心模块。本文只保留运行、部署、模块和测试说明，不包含课程设计报告正文。

## 技术栈

- JDK 25
- Maven 3.9.16
- `maven.compiler.release=17`
- Spring Framework 5.3.x / Spring MVC
- MyBatis / MyBatis-Spring
- MySQL
- JSP / JSTL
- Tomcat 9
- Java EE `javax.*`

本项目不使用 Spring Boot，不使用 Jakarta，不按 Tomcat 10/11 路线配置。

## 目录结构

```text
db/
  schema.sql
  data.sql
  demo-data.sql
docs/
  ACCEPTANCE_CHECKLIST.md
  DEMO_FLOW.md
src/main/java/com/housingfund/collection/
  controller/
  entity/
  exception/
  mapper/
  service/
  service/impl/
  util/
  vo/
src/main/resources/
  applicationContext.xml
  jdbc.properties
  mybatis-config.xml
  spring-mvc.xml
  mapper/
src/main/webapp/
  static/js/validate.js
  WEB-INF/web.xml
  WEB-INF/jsp/
src/test/java/com/housingfund/collection/
```

## 数据库初始化

导入顺序：

1. 执行 `db/schema.sql` 创建数据库和 `tb001`、`tb002`、`tb003` 三张核心表。
2. 执行 `db/data.sql` 初始化 `UNITACCNUM` 和 `PERACCNUM` 两条账号序号参数。
3. 答辩或手动验收前可选执行 `db/demo-data.sql`，快速准备演示单位、演示个人、身份证冲突和销户重启用数据。

PowerShell 示例：

```powershell
mysql -u root -p < db/schema.sql
mysql -u root -p housingfund_collection < db/data.sql
mysql -u root -p housingfund_collection < db/demo-data.sql
```

数据库连接配置位于 `src/main/resources/jdbc.properties`。部署前按实际 MySQL 地址、用户名和密码修改，不要提交真实生产密码。

## 构建

```powershell
D:\dev\apache-maven-3.9.16\bin\mvn.cmd clean package
```

构建成功后生成：

```text
target/housingfund-collection.war
```

最近回归验证记录：

- 2026-06-18：执行 `D:\dev\apache-maven-3.9.16\bin\mvn.cmd clean package`，结果 `BUILD SUCCESS`，62 个测试通过，生成 `target/housingfund-collection.war`。

## Tomcat 9 部署

1. 确认 MySQL 已启动，并按需导入 `schema.sql`、`data.sql`、`demo-data.sql`。
2. 修改 `src/main/resources/jdbc.properties` 中的数据库连接配置。
3. 执行 Maven 构建。
4. 将 `target/housingfund-collection.war` 放入 Tomcat 9 的 `webapps` 目录。
5. 启动 Tomcat。
6. 访问 `http://localhost:8080/housingfund-collection/`。

## 最终验收入口

- `/params`：系统参数维护
- `/units/open`：单位开户
- `/persons/open`：个人开户
- `/units/edit`：单位资料修改
- `/persons/edit`：个人资料修改
- `/units/query`：单位信息查询
- `/persons/query`：个人信息查询

首页 `index.jsp` 已提供以上 7 个核心模块入口。

## 模块范围

已实现：

- 系统参数维护：查询、新增、修改、删除普通参数，禁止删除 `UNITACCNUM` 和 `PERACCNUM`。
- 单位开户：生成 12 位单位账号，写入 `tb002`，更新 `UNITACCNUM` 序号。
- 个人开户：查询单位信息，生成 12 位个人账号，支持销户个人重新启用，更新单位汇总字段。
- 单位资料修改：按单位账号反显并修改指导书要求的单位资料字段。
- 个人资料修改：按个人账号反显并修改姓名、证件类型、证件号码，支持身份证占用确认和强制变更。
- 单位信息查询：按单位账号精确查询或单位名称模糊查询。
- 个人信息查询：按个人账号或身份证号查询，并关联展示单位信息。

范围外且未实现：

- 汇缴、补缴、提取、注销、封存、启封、比例变更、基数变更、贷款、权限系统、复杂登录系统。

## 验收资料

- `docs/ACCEPTANCE_CHECKLIST.md`：Tomcat 9 部署和 7 个核心模块手动验收清单。
- `docs/DEMO_FLOW.md`：答辩演示顺序。
- `db/demo-data.sql`：答辩前可选导入的演示数据脚本。

## 回归测试

执行：

```powershell
D:\dev\apache-maven-3.9.16\bin\mvn.cmd clean package
```

期望：

- Maven 输出 `BUILD SUCCESS`。
- Surefire 测试全部通过。
- WAR 文件生成到 `target/housingfund-collection.war`。
