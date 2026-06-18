# 住房公积金管理系统——筹集子系统

本项目是传统 Maven SSM Web 项目，用于课程设计。当前已包含基础配置、数据库脚本、首页入口、基础 Java 类、系统参数维护模块和单位开户模块。

## 技术栈

- JDK 25 作为本机开发和 Maven 构建环境
- Maven `war` 项目
- Java 17 兼容字节码：`<maven.compiler.release>17</maven.compiler.release>`
- Spring Framework 5.3.x
- Spring MVC
- MyBatis / MyBatis-Spring
- MySQL 9.5
- MySQL Connector/J 9.5.0
- JSP / JSTL 1.2
- Java EE `javax.*` 依赖
- Tomcat 9

本项目不使用 Spring Boot，不使用 `jakarta.*` 依赖，不按 Tomcat 10/11 路线配置。

## 目录结构

```text
db/
  schema.sql
  data.sql
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
  mapper/ParamMapper.xml
  mapper/UnitMapper.xml
src/main/webapp/
  static/js/validate.js
  WEB-INF/web.xml
  WEB-INF/jsp/index.jsp
  WEB-INF/jsp/param/form.jsp
  WEB-INF/jsp/param/list.jsp
  WEB-INF/jsp/unit/open.jsp
  WEB-INF/jsp/unit/receipt.jsp
src/test/java/com/housingfund/collection/
  service/impl/ParamServiceImplTest.java
  service/impl/UnitServiceImplTest.java
  web/JspSyntaxTest.java
```

## 数据库初始化

本项目后续开发和验证以 MySQL 9.5 为数据库基准，Maven 依赖使用 MySQL Connector/J 9.5.0。

导入顺序：

1. 执行 `db/schema.sql` 创建数据库和三张核心表。
2. 执行 `db/data.sql` 初始化账号序号参数。

PowerShell 示例命令：

```powershell
mysql -u root -p < db/schema.sql
mysql -u root -p housingfund_collection < db/data.sql
```

数据库连接配置位于 `src/main/resources/jdbc.properties`：

- 数据库名在 `jdbc.url` 中配置，默认是 `housingfund_collection`
- 默认连接本机 MySQL：`127.0.0.1:3306`
- 用户名在 `jdbc.username` 中配置
- 密码在 `jdbc.password` 中配置

仓库默认 `jdbc.password` 仅用于本地连接示例，部署前请按自己的 MySQL 环境修改。不要把真实数据库密码提交到说明文档中。

默认数据库名：`housingfund_collection`。

## 构建

```powershell
D:\dev\apache-maven-3.9.16\bin\mvn.cmd clean package
```

构建成功后，WAR 文件位于：

```text
target/housingfund-collection.war
```

当前环境验证记录：

- 2026-06-17：使用系统 Maven `D:\dev\apache-maven-3.9.16\bin\mvn.cmd clean package`。
- 结果：构建成功，12 个测试通过，生成 `target/housingfund-collection.war`。
- 2026-06-18：MySQL 基准为 9.5，使用 `mvn clean package`。
- 结果：构建成功，12 个测试通过，生成 `target/housingfund-collection.war`。

## 部署

1. 使用 Tomcat 9。
2. 将 `target/housingfund-collection.war` 放入 Tomcat 的 `webapps` 目录。
3. 确认 MySQL 9.5 已执行数据库脚本，并修改过 `jdbc.properties`。
4. 启动 Tomcat。

默认 WAR 名为 `housingfund-collection.war`，Tomcat 部署后的访问路径通常是：

```text
http://localhost:8080/housingfund-collection/
http://localhost:8080/housingfund-collection/params
http://localhost:8080/housingfund-collection/units/open
```

## 当前阶段已包含

- Maven `war` 项目配置
- Spring 根容器配置
- Spring MVC 配置
- MyBatis 基础配置
- `web.xml`
- 首页 `index.jsp`
- 系统参数维护 JSP：`param/list.jsp`、`param/form.jsp`
- 单位开户 JSP：`unit/open.jsp`、`unit/receipt.jsp`
- 前端基础校验脚本：`static/js/validate.js`
- `tb001`、`tb002`、`tb003` 数据库脚本
- 基础 controller、entity、util、exception
- 系统参数维护模块：新增、删除、修改、查询 `tb001`
- 单位开户模块：录入单位资料、生成单位账号、写入 `tb002`、更新 `UNITACCNUM.seq`
- 系统参数 Service 单元测试类：`ParamServiceImplTest`
- 单位开户 Service 单元测试类：`UnitServiceImplTest`
- JSP 基础语法回归测试：`JspSyntaxTest`

## 系统参数维护模块

访问入口：

```text
http://localhost:8080/housingfund-collection/params
```

支持功能：

- 查询全部系统参数
- 按 `seqname` 模糊查询
- 新增系统参数
- 修改系统参数，主键 `seqname` 只读且后端不会按表单值修改
- 删除普通系统参数
- 禁止删除 `UNITACCNUM` 和 `PERACCNUM`

核心校验：

- `seqname`、`seq`、`maxseq`、`seq_desc` 必填
- `seq`、`maxseq` 必须为正整数
- `seq` 不能大于 `maxseq`
- `seqname` 不能重复
- Service 层进行后端二次校验，Controller 不写 SQL，Mapper 只负责数据库访问

手动测试步骤：

1. 访问 `/params`，确认能看到 `UNITACCNUM` 和 `PERACCNUM` 两条初始化参数。
2. 使用查询框输入 `ACC`，确认可以按 `seqname` 模糊查询。
3. 点击“新增参数”，新增普通参数，例如 `TESTSEQ`，`seq=1`，`maxseq=100`，描述非空。
4. 再次新增同名 `TESTSEQ`，确认页面提示“键值信息已存在”。
5. 新增或修改时输入 `seq=101`、`maxseq=100`，确认页面或后端提示“当前序号不能大于最大序号”。
6. 修改 `TESTSEQ`，确认 `seqname` 只读，提交后主键不被表单篡改。
7. 删除 `TESTSEQ`，确认普通参数可删除。
8. 尝试删除 `UNITACCNUM` 或 `PERACCNUM`，确认提示禁止删除。

## 单位开户模块

访问入口：

```text
http://localhost:8080/housingfund-collection/units/open
```

支持功能：

- 填写单位开户表单并提交。
- 根据 `tb001` 中 `SEQNAME='UNITACCNUM'` 的当前序号生成 12 位单位账号。
- 同一事务内完成锁定序号、生成账号、插入 `tb002`、更新 `UNITACCNUM.seq=seq+1`。
- 开户成功后显示单位账号、单位名称、组织机构代码、开户日期、单位比例、个人比例。
- 业务失败返回开户表单并显示错误信息，保留已填写内容。

核心校验：

- 单位名称、地址、组织机构代码、单位类别、企业类型、发薪日期、联系电话、单位经办人、经办人身份证号码、单位比例、个人比例必填。
- 组织机构代码长度必须为 9 位；正常状态单位已存在时提示“该单位已建户”。
- 单位类别取值为 `1` 到 `5`；企业类型取值为 `110`、`120`、`130`、`140`、`150`、`160`、`170`、`190`、`200`、`300`、`900`。
- 发薪日期必须是 `01` 到 `31`。
- 经办人身份证号码按 18 位居民身份证规则校验。
- 单位比例、个人比例必须在 `0.050` 到 `0.120` 之间。
- `UNITACCNUM.seq` 大于 `maxseq` 时开户失败。

手动测试步骤：

1. 访问 `/units/open`，确认首页“单位开户”入口可跳转。
2. 使用合法数据提交，例如组织机构代码 `A12345678`、发薪日期 `15`、单位比例 `0.080`、个人比例 `0.080`、身份证号 `11010519491231002X`。
3. 确认成功回执显示 12 位单位账号；当 `UNITACCNUM.seq=1` 时账号应为 `000000000001`。
4. 回到数据库检查 `tb002` 新增单位资料，`tb001.UNITACCNUM.seq` 加 1。
5. 再次使用相同组织机构代码开户，确认提示“该单位已建户”并保留表单内容。
6. 输入单位比例 `0.049` 或个人比例 `0.121`，确认页面或后端提示比例范围错误。
7. 将 `UNITACCNUM.seq` 调整为大于 `maxseq` 后提交，确认提示“单位账号序号已超过最大值”。

## 当前未完成

- 个人开户
- 单位资料修改
- 个人资料修改
- 单位信息查询
- 个人信息查询
- 其他业务模块的 Service、Mapper、VO 和业务 JSP 页面
