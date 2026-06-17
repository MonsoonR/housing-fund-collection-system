# 住房公积金管理系统——筹集子系统

本项目是传统 Maven SSM Web 项目，用于课程设计。当前已包含基础配置、数据库脚本、首页入口、基础 Java 类和系统参数维护模块。

## 技术栈

- JDK 25 作为本机开发和 Maven 构建环境
- Maven `war` 项目
- Java 17 兼容字节码：`<maven.compiler.release>17</maven.compiler.release>`
- Spring Framework 5.3.x
- Spring MVC
- MyBatis / MyBatis-Spring
- MySQL Connector/J
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
src/main/webapp/
  static/js/validate.js
  WEB-INF/web.xml
  WEB-INF/jsp/index.jsp
  WEB-INF/jsp/param/form.jsp
  WEB-INF/jsp/param/list.jsp
src/test/java/com/housingfund/collection/
  service/impl/ParamServiceImplTest.java
```

## 数据库初始化

1. 登录 MySQL。
2. 执行 `db/schema.sql` 创建数据库和三张核心表。
3. 执行 `db/data.sql` 初始化账号序号参数。
4. 根据本机 MySQL 修改 `src/main/resources/jdbc.properties`。

默认数据库名：`housingfund_collection`。

## 构建

```powershell
mvn clean package
```

构建成功后，WAR 文件位于：

```text
target/housingfund-collection.war
```

当前环境验证记录：

- 2026-06-17：已尝试执行 `mvn clean package`。
- 结果：当前 shell 提示 `mvn` 不是可识别的命令，说明本机 Maven 命令未配置到 PATH 或未安装，因此本次未完成 Maven 编译验证。

## 部署

1. 使用 Tomcat 9。
2. 将 `target/housingfund-collection.war` 放入 Tomcat 的 `webapps` 目录。
3. 确认 MySQL 已执行数据库脚本，并修改过 `jdbc.properties`。
4. 启动 Tomcat，访问应用上下文路径。

## 当前阶段已包含

- Maven `war` 项目配置
- Spring 根容器配置
- Spring MVC 配置
- MyBatis 基础配置
- `web.xml`
- 首页 `index.jsp`
- 系统参数维护 JSP：`param/list.jsp`、`param/form.jsp`
- 前端基础校验脚本：`static/js/validate.js`
- `tb001`、`tb002`、`tb003` 数据库脚本
- 基础 controller、entity、util、exception
- 系统参数维护模块：新增、删除、修改、查询 `tb001`
- 系统参数 Service 单元测试类：`ParamServiceImplTest`

## 系统参数维护模块

访问入口：

```text
/params
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

## 当前未完成

- 单位开户
- 个人开户
- 单位资料修改
- 个人资料修改
- 单位信息查询
- 个人信息查询
- 其他业务模块的 Service、Mapper、VO 和业务 JSP 页面
