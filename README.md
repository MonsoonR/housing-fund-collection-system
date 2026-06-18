# 住房公积金管理系统——筹集子系统

本项目是传统 Maven SSM Web 项目，用于课程设计。当前已包含基础配置、数据库脚本、首页入口、基础 Java 类、系统参数维护模块、单位开户模块、个人开户模块、单位资料修改模块、单位信息查询模块和个人信息查询模块。

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
  mapper/PersonMapper.xml
src/main/webapp/
  static/js/validate.js
  WEB-INF/web.xml
  WEB-INF/jsp/index.jsp
  WEB-INF/jsp/param/form.jsp
  WEB-INF/jsp/param/list.jsp
  WEB-INF/jsp/unit/open.jsp
  WEB-INF/jsp/unit/receipt.jsp
  WEB-INF/jsp/unit/edit.jsp
  WEB-INF/jsp/unit/edit-receipt.jsp
  WEB-INF/jsp/unit/query.jsp
  WEB-INF/jsp/person/open.jsp
  WEB-INF/jsp/person/receipt.jsp
  WEB-INF/jsp/person/query.jsp
src/test/java/com/housingfund/collection/
  service/impl/ParamServiceImplTest.java
  service/impl/UnitServiceImplTest.java
  service/impl/PersonServiceImplTest.java
  web/JspSyntaxTest.java
```

## 数据库初始化

本项目后续开发和验证以 MySQL 9.5 为数据库基准，Maven 依赖使用 MySQL Connector/J 9.5.0。

导入顺序：

1. 执行 `db/schema.sql` 创建数据库和三张核心表。
2. 执行 `db/data.sql` 初始化账号序号参数。

如果已经导入过旧版脚本，本轮个人开户为 `tb003` 增加了 `UNITMONTHPAY`、`PERMONTHPAY` 字段，并将 `STATUS` 语义统一为 `0` 正常、`9` 销户。测试库建议重新执行 `db/schema.sql` 和 `db/data.sql`；已有业务数据的环境需要先备份，再手工 `ALTER TABLE` 补齐字段和状态含义。

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
- 2026-06-18：完成个人开户闭环后，使用 `mvn clean package`。
- 结果：构建成功，20 个测试通过，生成 `target/housingfund-collection.war`。
- 2026-06-18：完成单位信息查询和个人信息查询后，使用 `mvn clean package`。
- 结果：构建成功，30 个测试通过，生成 `target/housingfund-collection.war`。
- 2026-06-18：完成单位资料修改闭环后，使用 `mvn clean package`。
- 结果：构建成功，41 个测试通过，生成 `target/housingfund-collection.war`。

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
http://localhost:8080/housingfund-collection/persons/open
http://localhost:8080/housingfund-collection/units/edit
http://localhost:8080/housingfund-collection/units/query
http://localhost:8080/housingfund-collection/persons/query
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
- 单位资料修改 JSP：`unit/edit.jsp`、`unit/edit-receipt.jsp`
- 个人开户 JSP：`person/open.jsp`、`person/receipt.jsp`
- 查询 JSP：`unit/query.jsp`、`person/query.jsp`
- 前端基础校验脚本：`static/js/validate.js`
- `tb001`、`tb002`、`tb003` 数据库脚本
- 基础 controller、entity、util、exception
- 系统参数维护模块：新增、删除、修改、查询 `tb001`
- 单位开户模块：录入单位资料、生成单位账号、写入 `tb002`、更新 `UNITACCNUM.seq`
- 个人开户模块：录入个人资料、生成或重新启用个人账号、写入 `tb003`、更新 `PERACCNUM.seq` 和单位汇总字段
- 单位资料修改模块：按单位账号查询并反显单位资料，只允许修改单位名称、地址、组织机构代码、单位类别、企业类型、发薪日期、联系电话、单位经办人、经办人身份证号码和备注
- 单位信息查询模块：按单位账号精确查询，或按单位名称模糊查询，展示单位汇总和缴存信息
- 个人信息查询模块：按个人账号或身份证号精确查询，关联显示缴存单位和个人缴存信息
- 系统参数 Service 单元测试类：`ParamServiceImplTest`
- 单位开户 Service 单元测试类：`UnitServiceImplTest`
- 个人开户 Service 单元测试类：`PersonServiceImplTest`
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

## 单位资料修改模块

访问入口：

```text
http://localhost:8080/housingfund-collection/units/edit
```

支持功能：

- 输入 12 位单位账号，查询 `tb002` 并反显可修改字段。
- 只允许修改单位名称、单位地址、组织机构代码、单位类别、企业类型、发薪日期、联系电话、单位经办人、经办人身份证号码和备注。
- 不修改单位账号、单位比例、个人比例、余额、缴存基数、单位人数、单位月缴额、个人月缴额、账户状态、最后汇缴月、机构代码、柜员和开户日期。
- 修改成功后显示单位账号、单位名称、组织机构代码、联系电话、单位经办人、修改结果和修改时间。
- 业务失败返回修改表单并保留已输入内容。

核心校验：

- 单位账号必填且长度必须为 12 位。
- 单位不存在时提示“单位账号不存在”；`ACCSTATE='9'` 时提示“已销户单位不能修改”。
- 单位名称必填且最多 50 个字符；单位地址、组织机构代码、联系电话、单位经办人、经办人身份证号码必填。
- 组织机构代码长度必须为 9 位。
- 单位类别取值为 `1` 到 `5`；企业类型取值为 `110`、`120`、`130`、`140`、`150`、`160`、`170`、`190`、`200`、`300`、`900`。
- 发薪日期必须是 `01` 到 `31`。
- 经办人身份证号码按 18 位居民身份证规则校验。
- 至少修改一项；修改后的组织机构代码和单位名称不能同时与其他单位重复。

手动测试步骤：

1. 先通过 `/units/open` 新增一个正常单位，或确认数据库中已有 `ACCSTATE='0'` 的单位，例如 `000000000001`。
2. 访问 `/units/edit`，输入单位账号 `000000000001` 查询，确认页面反显单位名称、单位地址、组织机构代码、单位类别、企业类型、发薪日期、联系电话、单位经办人、经办人身份证号码和备注。
3. 修改单位名称为 `测试单位修改后`，或修改联系电话为 `0551-87654321` 后提交，确认回执显示“修改成功”和修改时间。
4. 回到数据库检查 `tb002`：允许修改字段已变化，`UNITACCNUM`、`UNITRATIO`、`PERRATIO`、`BALANCE`、`BASENUMBER`、`PERSNUM`、`UNITPAYSUM`、`PERPAYSUM`、`ACCSTATE`、`LASTPAYDATE`、`INSTCODE`、`OP`、`CREATDATE` 未变化。
5. 输入不存在的单位账号，例如 `000000009999`，确认提示“单位账号不存在”。
6. 将测试单位 `ACCSTATE` 手工改为 `9` 后再查询或提交修改，确认提示“已销户单位不能修改”；测试完成后按需要改回 `0`。
7. 查询成功后不改任何字段直接提交，确认提示“请至少修改一项单位资料”。
8. 准备另一个单位后，将当前单位的组织机构代码和单位名称同时改成另一个单位的值，确认提示“修改后的组织机构代码和单位名称已被其他单位占用”。
9. 输入组织机构代码 `ABC`、发薪日期 `32` 或身份证号 `123456`，确认页面或后端提示对应校验错误。

## 个人开户模块

访问入口：

```text
http://localhost:8080/housingfund-collection/persons/open
```

支持功能：

- 填写个人开户表单并提交。
- 根据 `tb001` 中 `SEQNAME='PERACCNUM'` 的当前序号生成 12 位个人账号。
- 同一事务内完成单位状态校验、身份证重复校验、锁定序号、插入 `tb003`、更新 `PERACCNUM.seq=seq+1`、更新 `tb002` 单位汇总字段。
- 身份证号存在且个人账户状态为 `9` 销户时，重新启用原个人账号，不消耗新的 `PERACCNUM` 序号。
- 开户成功后显示个人账号、姓名、身份证号、单位账号、单位名称、缴存基数、单位比例、个人比例、单位月缴额、个人月缴额和开户时间。
- 业务失败返回开户表单并显示错误信息，保留已填写内容。

核心校验：

- 单位账号必填，长度必须为 12 位，且单位必须存在并处于 `ACCSTATE='0'` 正常状态。
- 个人姓名必填；纯中文姓名最多 12 个汉字，其他输入最多 50 个字符。
- 证件类型目前统一使用 `居民身份证`。
- 身份证号按 18 位居民身份证规则校验。
- 缴存基数必填且必须大于 0，后端使用 `BigDecimal` 计算并保留 2 位小数。
- 身份证号对应正常账户已存在时提示“该人员已开户”。
- `tb003.STATUS` 统一为 `0` 正常、`9` 销户。

手动测试步骤：

1. 先通过 `/units/open` 新增一个正常单位，或确认数据库中已有 `ACCSTATE='0'` 的单位。
2. 访问 `/persons/open`，输入该单位账号，例如 `000000000001`。
3. 输入个人数据：姓名 `李四`，证件类型 `居民身份证`，身份证号 `11010519491231002X`，缴存基数 `5000.00`，联系电话和地址可留空或填写。
4. 提交后确认回执显示 12 位个人账号；当 `PERACCNUM.seq=1` 时账号应为 `000000000001`。
5. 回到数据库检查 `tb003` 新增个人资料，`tb001.PERACCNUM.seq` 加 1。
6. 检查对应 `tb002`：`BASENUMBER` 增加个人缴存基数，`UNITPAYSUM` 增加单位月缴额，`PERPAYSUM` 增加个人月缴额，`PERSNUM` 加 1。
7. 再次使用同一身份证号开户，确认提示“该人员已开户”并保留表单内容。
8. 输入不存在或非正常状态的单位账号，确认提示“单位账号不存在或状态非正常”。
9. 输入缴存基数 `0` 或负数，确认页面或后端提示“缴存基数必须大于0”。
10. 将已有个人账户 `STATUS` 手工改为 `9` 后，用相同身份证号再次开户，确认原个人账号被重新启用且 `PERACCNUM.seq` 不增加。

## 单位信息查询模块

访问入口：

```text
http://localhost:8080/housingfund-collection/units/query
```

支持功能：

- 输入单位账号时按 12 位单位账号精确查询。
- 未输入单位账号但输入单位名称时，按单位名称模糊查询。
- 两个条件都为空时提示“请输入单位账号或单位名称”。
- 查询无结果时页面显示“未查询到单位信息”。
- 查询结果展示单位名称、单位账号、单位地址、经办人姓名、联系电话、公积金余额、单位比例、个人比例、合计比例、最后汇缴月、单位月缴额、个人月缴额、合计月缴额、单位人数和账户状态。

手动测试步骤：

1. 访问 `/units/query`。
2. 输入已有单位账号，例如 `000000000001`，确认结果表格显示该单位的完整信息。
3. 清空单位账号，输入单位名称关键字，例如 `测试`，确认可返回多个匹配单位。
4. 两个查询条件都留空提交，确认提示“请输入单位账号或单位名称”。
5. 输入不存在的单位账号，例如 `000000009999`，确认显示“未查询到单位信息”。
6. 输入非 12 位单位账号，确认前端或后端提示“单位账号长度必须为12位”。

## 个人信息查询模块

访问入口：

```text
http://localhost:8080/housingfund-collection/persons/query
```

支持功能：

- 输入个人账号时按 12 位个人账号精确查询。
- 未输入个人账号但输入身份证号时，按身份证号精确查询。
- 两个条件都为空时提示“请输入个人账号或身份证号”。
- 查询无结果时页面显示“未查询到个人信息”。
- 查询结果关联 `tb003` 和 `tb002`，展示缴存单位全称、缴存单位账号、姓名、个人账号、身份证号、余额、开户时间、最后汇缴月、单位比例、个人比例、合计比例、单位月缴额、个人月缴额、合计月缴额和个人账户状态。

手动测试步骤：

1. 先确认已有个人开户数据，可用 `/persons/open` 创建个人账户。
2. 访问 `/persons/query`。
3. 输入已有个人账号，例如 `000000000001`，确认显示该个人及其缴存单位信息。
4. 清空个人账号，输入身份证号，例如 `11010519491231002X`，确认可按身份证号查询。
5. 两个查询条件都留空提交，确认提示“请输入个人账号或身份证号”。
6. 输入不存在的个人账号，例如 `000000009999`，确认显示“未查询到个人信息”。
7. 输入错误身份证号，例如 `123456`，确认前端或后端提示“身份证号不正确”。

## 当前未完成

- 个人资料修改
- 其他业务模块的 Service、Mapper、VO 和业务 JSP 页面
