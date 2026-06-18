# 指导书对齐演示流程

## 准备

1. 复制 `src/main/resources/jdbc.example.properties` 为 `src/main/resources/jdbc-local.properties`，填写本机 MySQL 用户名和密码。不要修改并提交仓库占位密码。
2. 重新导入数据库：

```powershell
mysql -u root -p < db/schema.sql
mysql -u root -p housingfund_collection < db/data.sql
mysql -u root -p housingfund_collection < db/demo-data.sql
```

3. 构建并部署到 Tomcat 9：

```powershell
mvn clean package
```

4. 访问 `http://localhost:8080/housingfund-collection/`。

## 演示顺序

1. 首页展示 7 个指导书模块入口。
2. 系统参数维护：查询 `UNITACCNUM`、`PERACCNUM`，展示当前序号、最大序号、描述、备注1。
3. 单位开户：录入指导书交易要素，生成 12 位单位公积金账号，展示《公积金开户回单》。
4. 个人开户：输入刚开户单位的单位公积金账号，反显单位名称、单位比例、个人比例，录入姓名、证件类型、证件号码、缴存基数，展示《个人住房公积金开户回单》。
5. 个人开户 Excel 批量导入：在 `/persons/open` 上传 Excel，列顺序为单位账号、姓名、证件类型、证件号码、缴存基数、单位比例、个人比例；展示成功数、失败数和逐行失败原因。
6. 单位信息查询：按单位公积金账号精确查询；再按单位名称模糊查询，点击或双击单位名称打开该单位情况。
7. 个人信息查询：按个人公积金账号查询；再按证件号码查询，展示合计比例和合计月缴额。
8. 单位资料修改：按单位公积金账号反显资料，只修改指导书允许字段，展示《住房公积金账户资料变更回单》。
9. 个人资料修改：按个人公积金账号反显资料，只修改姓名、证件类型、证件号码，展示个人资料变更回单。
10. 个人资料强制变更：使用 demo 冲突证件号码触发占用账户回显，确认强制变更后展示被占用账号、新建错误账户账号、原证件号码和变更后的错误证件号码。
11. 错误校验演示：重复组织机构代码、重复证件号码、比例越界、发薪日期越界、证件号码非法、不修改任何字段、Excel 重复行或单位不存在。
12. 最后说明项目分层：Controller 接收请求，Service 负责业务规则和事务，Mapper 负责 MyBatis SQL，JSP + JavaScript 负责页面与基础校验。
