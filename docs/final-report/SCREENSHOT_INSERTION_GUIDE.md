# 截图插入指南

截图目录：`docs/acceptance-screenshots/`。

当前已收集的截图以入口页和表单页为主，成功回单、查询结果、构建日志和数据库查询结果仍建议手工补齐。不能将未存在截图写成已存在。

| 序号 | 建议文件名 | 对应报告章节 | 截图内容 | 验收意义 | 当前已有文件 | 手工截图步骤 |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | `01-home-navigation.png` | 第3章 系统实现 | 首页或导航页，显示 7 个核心入口 | 证明系统入口完整 | 是 | 已在 `docs/acceptance-screenshots/`。 |
| 2 | `02-system-params-list.png` | 第3章 系统实现 / 第4章 系统测试 | 系统参数维护列表，包含 `PERACCNUM`、`UNITACCNUM` | 证明系统参数模块可访问、账号序列存在 | 是 | 已在 `docs/acceptance-screenshots/`。 |
| 3 | `03-system-params-edit.png` | 第3章 系统实现 | 修改系统参数页 | 证明参数修改入口存在 | 是 | 已在 `docs/acceptance-screenshots/`。 |
| 4 | `04-system-params-add.png` | 第3章 系统实现 | 新增系统参数页 | 证明普通参数新增入口存在 | 是 | 已在 `docs/acceptance-screenshots/`。 |
| 5 | `05-unit-open-form.png` | 第3章 系统实现 | 单位开户表单 | 证明单位开户交易要素页面存在 | 是 | 已在 `docs/acceptance-screenshots/`。 |
| 6 | `unit-open-receipt.png` | 第4章 系统测试 | 单位开户成功回单，显示单位账号、状态、开户日期 | 证明单位开户完整跑通 | 否 | 访问 `/units/open`，提交合法单位资料，截取回单页。 |
| 7 | `06-person-open-and-excel-import.png` | 第3章 系统实现 | 个人开户页，包含手工开户和 Excel 导入入口 | 证明个人开户模块包含批量导入 | 是 | 已在 `docs/acceptance-screenshots/`。 |
| 8 | `person-open-receipt.png` | 第4章 系统测试 | 个人开户成功回单，显示个人账号、姓名、证件号、基数、月缴额 | 证明个人手工开户完整跑通 | 否 | 访问 `/persons/open`，输入正常单位账号并提交合法个人资料，截取回单页。 |
| 9 | `person-excel-import-result.png` | 第4章 系统测试 | Excel 导入结果页，显示成功数量、失败数量、失败原因 | 证明批量导入和错误反馈可验收 | 否 | 在 `/persons/open` 上传测试 Excel，分别截失败批次和成功批次结果。 |
| 10 | `07-unit-edit-search.png` | 第3章 系统实现 | 单位资料修改查询页 | 证明单位资料修改入口存在 | 是 | 已在 `docs/acceptance-screenshots/`。 |
| 11 | `unit-edit-receipt.png` | 第4章 系统测试 | 单位资料修改成功回单 | 证明允许字段可修改、禁止字段未误改 | 否 | 查询正常单位账号，修改允许字段并提交，截取回单页。 |
| 12 | `08-person-edit-search.png` | 第3章 系统实现 | 个人资料修改查询页 | 证明个人资料修改入口存在 | 是 | 已在 `docs/acceptance-screenshots/`。 |
| 13 | `person-edit-receipt.png` | 第4章 系统测试 | 个人资料普通修改成功回单 | 证明姓名/证件信息可修改 | 否 | 查询正常个人账号，修改姓名或证件信息，截取回单页。 |
| 14 | `person-force-conflict.png` | 第4章 系统测试 | 个人资料强制变更冲突确认页 | 证明冲突不强制时会回显占用账户 | 否 | 使用 demo 占用证件号触发冲突，截取确认页。 |
| 15 | `person-force-receipt.png` | 第4章 系统测试 | 强制变更成功回单，显示新建错误账户账号 | 证明强制变更新建错误账户跑通 | 否 | 在冲突确认页点击确认强制变更，截取回单页。 |
| 16 | `09-unit-query-form.png` | 第3章 系统实现 | 单位信息查询入口 | 证明支持账号和名称查询入口 | 是 | 已在 `docs/acceptance-screenshots/`。 |
| 17 | `unit-query-result.png` | 第4章 系统测试 | 单位账号精确查询和名称模糊查询结果 | 证明单位查询输出字段符合要求 | 否 | 访问 `/units/query`，分别按账号和名称查询，截取结果页。 |
| 18 | `10-person-query-form.png` | 第3章 系统实现 | 个人信息查询入口 | 证明支持个人账号和证件号码查询入口 | 是 | 已在 `docs/acceptance-screenshots/`。 |
| 19 | `person-query-result.png` | 第4章 系统测试 | 个人账号或证件号码查询结果 | 证明个人查询输出核心字段 | 否 | 访问 `/persons/query`，按个人账号或证件号查询，截取结果页。 |
| 20 | `maven-build-success.png` | 第4章 系统测试 | Maven 构建成功日志 | 证明编译、测试和打包通过 | 否 | 终端执行 `mvn clean package`，截取 `Tests run: 78...` 和 `BUILD SUCCESS`。 |
| 21 | `tomcat-start-success.png` | 第4章 系统测试 | Tomcat 启动成功日志或浏览器首页 HTTP 200 | 证明 WAR 部署成功 | 否 | 启动 Tomcat 后截取控制台成功日志或首页访问结果。 |
| 22 | `database-check-result.png` | 第4章 系统测试 / 第2章 数据设计 | `TB001/TB002/TB003` 查询结果和字段类型检查 | 证明数据库初始化和字段对齐 | 否 | 执行关键 SQL 查询，截取表数据和字段类型结果。 |

## Word 报告插入建议

1. 第1章用例建模：插入 `DIAGRAMS.md` 中系统用例图。
2. 第2章概要设计：插入功能层次图、系统架构图。
3. 第2章数据设计：插入 ER 图和 `DATABASE_DESIGN.md` 中的表结构说明。
4. 第3章系统实现：插入入口页、表单页和核心模块页面截图。
5. 第4章系统测试：插入成功回单、查询结果、Excel 导入结果、Maven 构建日志、Tomcat 启动日志和数据库查询结果。
6. 附录：可放关键代码方法截图或代码片段，如 `importPersons`、`forceUpdatePerson`、`insertWrongAccountCopy`。
