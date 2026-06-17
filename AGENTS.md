# AGENTS.md

## 项目定位

项目名称：住房公积金管理系统——筹集子系统。

当前目录从零新建传统 Maven SSM Web 项目，不是在已有代码上改造。只实现课程设计所需代码、SQL、JSP 页面、README 和必要测试说明。

## 技术栈

- Java
- Maven
- Spring MVC
- Spring IoC
- MyBatis
- MySQL
- JSP / HTML / CSS / JavaScript
- Tomcat

禁止使用 Spring Boot，除非用户后续明确改变项目要求。

## 基础包名与分层

基础包名：`com.housingfund.collection`

必须使用分层结构：

- `controller`：接收请求、参数绑定、页面跳转和提示信息，不直接访问数据库。
- `service`：业务接口。
- `service.impl`：业务规则、事务控制和后端二次校验。
- `mapper`：MyBatis 数据访问，只负责 SQL。
- `entity`：数据库表实体。
- `vo`：页面展示和表单对象。
- `util`：账号、日期、金额、身份证等工具类。
- `exception`：业务异常。

## 功能范围

只实现以下核心功能：

- 系统参数维护
- 单位开户
- 个人开户
- 单位资料修改
- 个人资料修改
- 单位信息查询
- 个人信息查询

不要扩展贷款、提取、汇缴、补缴、封存、启封、单位注销、个人注销等复杂业务，除非用户后续明确要求。

## 数据库规则

数据库使用 MySQL。核心表固定为：

- `tb001`：系统参数表
- `tb002`：单位基本资料表
- `tb003`：个人基本资料表

账号生成规则：

- 单位账号从 `tb001` 中 `SEQNAME='UNITACCNUM'` 的当前序号生成，不足 12 位前面补 `0`，单位开户成功后序号加 1。
- 个人账号从 `tb001` 中 `SEQNAME='PERACCNUM'` 的当前序号生成，不足 12 位前面补 `0`，个人开户成功后序号加 1。

金额字段使用 `DECIMAL`，不要使用 `double` 保存金额。

## 校验与事务

- 前端页面需要使用 HTML5 和 JavaScript 做基础校验。
- 后端 `service.impl` 必须再次校验关键业务规则，不能只依赖前端。
- 新增、修改、账号生成、开户和汇总字段更新必须在 Service 层使用事务控制。
- 所有新增、修改业务需要返回清晰错误信息。
- 不要吞异常；业务错误使用自定义业务异常表达。

## 页面与资源约定

- JSP 页面优先放在 `src/main/webapp/WEB-INF/jsp`。
- 静态资源优先放在 `src/main/webapp/static/css` 和 `src/main/webapp/static/js`。
- 页面风格保持简洁，不引入复杂前端框架。

## 运行与验证

常用命令：

```powershell
mvn clean package
```

如果当前环境无法运行 Maven，必须明确说明原因，不要伪造编译或测试结果。

Tomcat 部署方式：

- 使用 Maven 打包生成 `war`。
- 将 `target` 下生成的 `war` 部署到 Tomcat。
- 根据实际 MySQL 环境修改数据库连接配置后再启动。

## 修改输出要求

每次修改后必须说明：

- 修改了哪些文件
- 实现了哪些功能
- 如何验证
- 当前未完成项

## 禁止事项

- 不写课程设计报告。
- 不生成 Word 文档。
- 不写论文内容。
- 不删除已有可用代码。
- 不伪造编译、运行或测试通过结果。
- 不在 Controller 中写 SQL。
- 不把业务规则下沉到 Mapper。
