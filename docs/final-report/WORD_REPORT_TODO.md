# Word 报告整理清单

本清单按课程设计报告模板整理，目标是把已有 Markdown 材料、真实截图、代码和 SQL 合并到 Word 文档中。人工填写项必须按个人和班级真实信息补齐。

## 封面和基本信息

- 应该放哪些文字：题目“住房公积金管理系统——筹集子系统”，课程名称，院系，专业，班级，成员信息，指导教师，完成日期。
- 应该插入哪些图：无，除非模板要求校徽。
- 应该插入哪些截图：无。
- 应该引用哪些代码或 SQL：无。
- 需要人工填写：姓名、学号、班级、专业、指导教师、日期、小组成员排序。

## 第1章 需求分析

### 1.1 项目简介

- 应该放哪些文字：说明项目是传统 Maven SSM Web 项目，实现住房公积金管理系统中的筹集子系统；列出 7 个核心功能；说明不包含贷款、提取、汇缴、补缴、封存、启封、注销等扩展业务。
- 应该插入哪些图：可插入系统功能范围示意图。
- 应该插入哪些截图：不建议放页面截图，页面截图放第3章或第4章。
- 应该引用哪些代码或 SQL：可引用 `README.md` 的功能范围说明。
- 需要人工填写：课程设计任务来源、个人承担角色描述。

### 1.2 用例建模

- 应该放哪些文字：说明参与者主要是柜员或业务操作员，围绕参数维护、开户、资料修改、查询进行操作。
- 应该插入哪些图：用例图。
- 应该插入哪些截图：无。
- 应该引用哪些代码或 SQL：无。
- 需要人工填写：如果模板要求，用自己的话补充用例建模说明。

### 1.2.1 用例图

- 应该放哪些文字：图前简短说明系统边界和参与者。
- 应该插入哪些图：从 `docs/final-report/DIAGRAMS.md` 整理出的系统用例图。
- 应该插入哪些截图：无。
- 应该引用哪些代码或 SQL：无。
- 需要人工填写：图号、图名和 Word 交叉引用。

### 1.2.2 用例描述

- 应该放哪些文字：按系统参数维护、单位开户、个人开户、单位资料修改、个人资料修改、单位信息查询、个人信息查询分别写用例名称、参与者、前置条件、基本流程、异常流程和后置条件。
- 应该插入哪些图：无。
- 应该插入哪些截图：无。
- 应该引用哪些代码或 SQL：可引用 `docs/ACCEPTANCE_CHECKLIST.md` 中的验收点。
- 需要人工填写：用例编号和表格格式。

## 第2章 系统设计

### 2.1 概要设计

- 应该放哪些文字：说明系统采用 Spring MVC + Spring IoC + MyBatis + JSP 的 SSM 架构，部署为 WAR 包运行在 Tomcat 上。
- 应该插入哪些图：系统架构图、分层结构图。
- 应该插入哪些截图：无。
- 应该引用哪些代码或 SQL：引用 `src/main/webapp/WEB-INF/web.xml`、`src/main/resources/applicationContext.xml`、`src/main/resources/spring-mvc.xml` 的关键配置片段。
- 需要人工填写：图号、图名和文字衔接。

### 2.2 功能设计

- 应该放哪些文字：按模块说明每个功能的输入、处理、输出和异常提示；重点写单位开户账号生成、个人开户汇总更新、5 列 Excel 批量导入、失败批次整体回滚、个人资料强制变更。
- 应该插入哪些图：功能层次图、关键业务流程图。
- 应该插入哪些截图：可少量引用首页导航页截图。
- 应该引用哪些代码或 SQL：引用 `UnitServiceImpl`、`PersonServiceImpl`、`ParamServiceImpl` 的关键方法名。
- 需要人工填写：流程图编号和模块说明文字。

### 2.3 数据设计

- 应该放哪些文字：说明数据库使用 MySQL，核心表为 `TB001`、`TB002`、`TB003`，金额字段用 `DECIMAL`。
- 应该插入哪些图：ER 图。
- 应该插入哪些截图：数据库表查询结果可放第4章，本节主要放结构图。
- 应该引用哪些代码或 SQL：引用 `db/schema.sql` 或对应建表 SQL。
- 需要人工填写：数据库名称和本机 MySQL 版本，如模板要求。

### 2.3.1 数据库概念结构设计

- 应该放哪些文字：描述系统参数、单位、个人三个实体及关系；单位和个人是一对多关系，系统参数为账号生成提供序列。
- 应该插入哪些图：ER 图或概念模型图。
- 应该插入哪些截图：无。
- 应该引用哪些代码或 SQL：无，概念设计以图和文字为主。
- 需要人工填写：图号、图名。

### 2.3.2 数据库逻辑结构设计

- 应该放哪些文字：列出 `TB001`、`TB002`、`TB003` 字段、类型、主键、唯一约束和业务含义。
- 应该插入哪些图：可插入表结构截图或 Word 表格。
- 应该插入哪些截图：可放数据库工具中的表结构截图，但优先用表格整理。
- 应该引用哪些代码或 SQL：引用 `db/schema.sql` 的建表语句；引用 `src/main/resources/mapper/*.xml` 中和三张表有关的 SQL。
- 需要人工填写：字段说明表的格式和编号。

## 第3章 系统实现

- 应该放哪些文字：按功能模块说明实现过程，突出 Controller、Service、Mapper 的职责划分。重点写账号生成、开户事务、Excel 解析、强制变更和查询展示。
- 应该插入哪些图：首页导航页、系统参数维护列表、单位开户表单、个人开户页、单位资料修改入口、个人资料修改入口、查询入口截图。
- 应该插入哪些截图：`01-home-navigation.png`、`02-system-params-list.png`、`03-unit-open-form.png`、`05-person-open-manual-and-excel.png`。
- 应该引用哪些代码或 SQL：引用 `UnitController`、`PersonController`、`UnitServiceImpl`、`PersonServiceImpl`、`ParamServiceImpl`、`UnitMapper.xml`、`PersonMapper.xml` 的关键片段。
- 需要人工填写：每个截图的图号、图名、页面说明。

## 第4章 系统测试

- 应该放哪些文字：说明测试环境、测试方法、自动化测试和人工验收结合；列出主要测试用例和结果。不能写未执行的测试为通过。
- 应该插入哪些图：测试用例表、Maven BUILD SUCCESS 截图、Tomcat 启动或首页访问截图、数据库查询截图。
- 应该插入哪些截图：`04-unit-open-receipt.png`、`06-person-open-receipt.png`、`07-excel-import-success.png`、`08-excel-import-rollback.png`、`09-unit-edit-receipt.png`、`10-person-edit-normal-receipt.png`、`11-person-force-conflict.png`、`12-person-force-receipt.png`、`13-unit-query-result.png`、`14-person-query-result.png`、`15-maven-build-success.png`、`16-database-tb001-tb002-tb003.png`、`17-tomcat-home-success.png`。
- 应该引用哪些代码或 SQL：引用 `src/test` 下关键测试类；引用验证数据库的 `SELECT * FROM TB001/TB002/TB003` SQL。
- 需要人工填写：真实测试日期、测试人员、截图对应数据、失败问题和处理情况。

## 第5章 课程设计总结

- 应该放哪些文字：总结完成的功能、技术收获、遇到的问题和解决方式；可以写账号序列生成、事务回滚、Excel 导入、强制变更和 SSM 配置调试。
- 应该插入哪些图：一般不需要。
- 应该插入哪些截图：一般不需要。
- 应该引用哪些代码或 SQL：一般不引用，除非模板要求附证。
- 需要人工填写：个人真实心得、分工体会、仍可改进的地方。

## 附录 代码清单

- 应该放哪些文字：说明只选核心代码，不全文粘贴所有文件。
- 应该插入哪些图：无。
- 应该插入哪些截图：可插入关键代码截图，但更建议粘贴带格式的代码片段。
- 应该引用哪些代码或 SQL：`UnitServiceImpl#openUnit`、`PersonServiceImpl#openPerson`、`PersonServiceImpl#importPersons`、`PersonServiceImpl#forceUpdatePerson`、`ParamServiceImpl`、`UnitMapper.xml`、`PersonMapper.xml`、`db/schema.sql`。
- 需要人工填写：代码清单编号、页码、与正文交叉引用。
