# 测试用例

测试用例以 `TEST_CASE_TABLE.xlsx` 为准，覆盖首页导航、系统参数维护、单位开户、个人开户、Excel 批量开户、单位资料修改、个人资料修改、单位信息查询、个人信息查询、构建部署和数据库检查。截图证据与 Excel 批量导入源文件位于 `docs/screenshots/`。

## 测试用例总表

| 用例编号 | 模块 | 测试点 | 前置条件 | 测试步骤 | 测试数据 | 预期结果 | 实际结果 | 证据文件 | 结论 | 备注 |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| TC-001 | 首页导航 | 核心入口展示 | Tomcat 已启动，系统可访问 | 访问系统首页，检查导航菜单 | 无 | 显示系统参数维护、单位开户、个人开户、资料修改、信息查询等核心入口 | 首页导航截图显示 7 个核心入口 | `docs/screenshots/01-home-navigation.jpeg` | 通过 |  |
| TC-002 | 系统参数维护 | 系统参数列表查询 | 数据库已初始化 | 进入系统参数维护列表 | 无 | 列表显示 UNITACCNUM、PERACCNUM 等账号序列参数 | 截图显示系统参数列表和账号序列 | `docs/screenshots/02-system-params-list.jpeg` | 通过 |  |
| TC-003 | 系统参数维护 | 账号序列参数保护 | UNITACCNUM/PERACCNUM 存在 | 尝试对关键账号序列执行删除或保护性操作 | UNITACCNUM 或 PERACCNUM | 系统提示关键账号生成参数不可删除或不可破坏 | 截图显示保护提示 | `docs/screenshots/03-system-params-protect.jpeg` | 通过 |  |
| TC-004 | 单位开户 | 单位开户表单展示 | 系统服务正常 | 打开单位开户页面 | 合法单位开户资料 | 页面展示单位名称、地址、组织机构代码、缴存比例等任务书字段 | 截图显示单位开户表单完整字段 | `docs/screenshots/04-unit-open-form.jpeg` | 通过 |  |
| TC-005 | 单位开户 | 单位开户成功 | UNITACCNUM 序列可用 | 提交合法单位开户资料 | 验收测试单位、合法组织机构代码、比例、经办人信息 | 生成 12 位单位公积金账号并显示《公积金开户回单》 | 截图显示单位开户成功回单 | `docs/screenshots/05-unit-open-receipt.jpeg` | 通过 |  |
| TC-006 | 单位开户 | 组织机构代码校验失败 | 打开单位开户页面 | 提交非法组织机构代码 | 组织机构代码不符合 9 位要求 | 页面返回错误提示，不写入单位资料 | 截图显示非法组织机构代码校验提示 | `docs/screenshots/06-unit-open-invalid-orgcode.jpg` | 通过 |  |
| TC-007 | 个人开户 | 个人开户单位信息反显 | 存在正常单位账户 | 在个人开户页面输入单位公积金账号并查询 | 正常单位公积金账号 | 反显单位名称、单位比例、个人比例，再录入个人信息 | 截图显示单位信息已加载和个人开户表单 | `docs/screenshots/07-person-open-unit-loaded.jpeg` | 通过 |  |
| TC-008 | 个人开户 | 个人手工开户成功 | 存在正常单位账户 | 提交合法个人开户资料 | 姓名、身份证号、缴存基数 | 生成个人公积金账号并显示《个人住房公积金开户回单》 | 截图显示个人开户成功回单 | `docs/screenshots/08-person-open-receipt.jpeg` | 通过 |  |
| TC-009 | 个人开户 | 重复证件号码失败 | 系统中已存在相同证件号码账户 | 使用重复身份证号提交个人开户 | 已存在身份证号 | 系统提示该人员已开户或证件号已占用，不新增账户 | 截图显示重复身份证号错误提示 | `docs/screenshots/09-person-open-duplicate-idcard .jpeg` | 通过 | 文件名中原有一个空格，保留原路径 |
| TC-010 | Excel 批量开户 | 5 列 Excel 导入入口 | 存在正常单位账户 | 打开个人开户页批量导入区域 | 单位公积金账号 + 5 列 Excel | 页面说明 Excel 为序号、个人姓名、证件类型、证件号码、缴存基数 5 列 | 截图显示批量导入入口和 5 列说明 | `docs/screenshots/10-excel-import-form-5cols.jpeg` | 通过 |  |
| TC-011 | Excel 批量开户 | 批量导入成功 | 存在正常单位账户和合法 Excel | 上传合法 5 列 Excel 并导入 | `batch-success.xlsx` | 成功导入多条个人账户，失败数为 0 | 截图显示导入成功结果 | `docs/screenshots/11-excel-import-success.jpeg`; `docs/screenshots/batch-success.xlsx` | 通过 |  |
| TC-012 | Excel 批量开户 | 失败批次整体回滚 | 存在正常单位账户和含错误行 Excel | 上传含失败行的 5 列 Excel 并导入 | `batch-fail.xlsx` | 显示失败原因，成功数为 0，不新增个人账户，不递增序号 | 截图显示导入失败和逐行失败原因 | `docs/screenshots/12-excel-import-fail.jpeg`; `docs/screenshots/batch-fail.xlsx` | 通过 | 数据库回滚可结合 TB001/TB003 截图说明 |
| TC-013 | 单位资料修改 | 单位资料查询反显 | 存在正常单位账户 | 进入单位资料修改页面并查询单位账号 | 正常单位公积金账号 | 反显单位基础资料，只允许修改任务书规定字段 | 截图显示单位资料已加载 | `docs/screenshots/13-unit-edit-loaded.jpeg` | 通过 |  |
| TC-014 | 单位资料修改 | 单位资料修改成功 | 存在正常单位账户 | 修改允许字段并提交 | 单位地址、联系电话等允许字段 | 显示《住房公积金账户资料变更回单》 | 截图显示单位资料变更回单 | `docs/screenshots/14-unit-edit-receipt.jpeg` | 通过 |  |
| TC-015 | 单位资料修改 | 销户单位禁止修改 | 存在销户单位账户 | 查询销户单位并尝试修改 | 销户单位公积金账号 | 系统提示已销户单位不能修改 | 截图显示销户单位拒绝修改提示 | `docs/screenshots/15-unit-edit-closed-reject.jpeg` | 通过 |  |
| TC-016 | 个人资料修改 | 个人资料查询反显 | 存在正常个人账户 | 进入个人资料修改页面并查询个人账号 | 正常个人公积金账号 | 反显姓名、证件类型、证件号码及所属单位信息 | 截图显示个人资料已加载 | `docs/screenshots/16-person-edit-loaded.jpeg` | 通过 |  |
| TC-017 | 个人资料修改 | 普通修改成功 | 存在正常个人账户，证件号不冲突 | 修改姓名或证件信息并提交 | 合法姓名或证件信息 | 显示《住房公积金个人变更回单》 | 截图显示个人资料变更回单 | `docs/screenshots/17-person-edit-receipt.jpeg` | 通过 |  |
| TC-018 | 个人资料修改 | 强制变更冲突确认 | 存在两个正常个人账户 | 将目标账户证件号改为已被占用证件号 | 目标账号 A，占用账号 B | 系统显示冲突确认页并回显占用账户信息 | 截图显示强制变更冲突确认页 | `docs/screenshots/18-person-force-conflict.jpeg` | 通过 |  |
| TC-019 | 个人资料修改 | 强制变更成功 | 已进入冲突确认页 | 确认强制变更 | 新证件号与占用账户信息 | 目标账户获得正确证件号，新建错误账户证件号为 9 + 原身份证号后 17 位 | 截图显示强制变更成功回单 | `docs/screenshots/19-person-force-receipt.jpeg` | 通过 |  |
| TC-020 | 单位信息查询 | 单位账号精确查询 | 存在测试单位 | 输入单位公积金账号查询 | 正常单位公积金账号 | 返回单位详情，展示缴存比例和月汇缴金额等任务书字段 | 截图显示单位账号精确查询结果 | `docs/screenshots/20-unit-query-by-account.jpeg` | 通过 |  |
| TC-021 | 单位信息查询 | 单位名称模糊查询 | 存在测试单位 | 输入单位名称关键字查询 | 单位名称关键字 | 返回简短列表，可点击单位名称或查看详情进入详情页 | 截图显示单位名称模糊查询结果 | `docs/screenshots/21-unit-query-by-name.jpeg` | 通过 |  |
| TC-022 | 个人信息查询 | 个人账号查询 | 存在测试个人账户 | 输入个人公积金账号查询 | 正常个人公积金账号 | 返回个人账户详情，展示缴存单位、缴存比例、月汇缴金额和余额 | 截图显示个人账号查询结果 | `docs/screenshots/22-person-query-by-account.jpeg` | 通过 |  |
| TC-023 | 个人信息查询 | 证件号码查询 | 存在测试个人账户 | 输入证件号码查询 | 身份证号码 | 返回对应个人账户详情 | 截图显示证件号码查询结果 | `docs/screenshots/23-person-query-by-idcard.jpeg` | 通过 |  |
| TC-024 | 构建测试 | Maven 构建测试 | 源码完整，Maven 可用 | 执行 `mvn clean package` | 无 | 编译、测试、打包成功，出现 BUILD SUCCESS | 截图显示 Maven BUILD SUCCESS | `docs/screenshots/24-maven-build-success.jpg` | 通过 | 以截图中的终端输出为报告证据 |
| TC-025 | 部署测试 | Tomcat 启动或首页访问成功 | WAR 已部署，数据库配置正确 | 启动 Tomcat 或访问系统首页 | `http://127.0.0.1:18090/housingfund-collection/` | Tomcat 正常启动或首页可访问 | 截图显示 Tomcat 运行状态 | `docs/screenshots/25-tomcat-running.jpg` | 通过 |  |
| TC-026 | 数据库检查 | TB001 账号序列检查 | 数据库已初始化 | 查询 TB001 | `SELECT * FROM TB001` | 存在 UNITACCNUM、PERACCNUM 等账号序列 | 截图显示 TB001 查询结果 | `docs/screenshots/26-db-tb001-seq.jpg` | 通过 |  |
| TC-027 | 数据库检查 | TB002 单位资料检查 | 数据库已初始化并执行单位业务 | 查询 TB002 | `SELECT * FROM TB002` | 单位资料、余额、人数、缴存比例等字段存在且可查 | 截图显示 TB002 查询结果 | `docs/screenshots/27-db-tb002-unit.jpg` | 通过 |  |
| TC-028 | 数据库检查 | TB003 个人资料检查 | 数据库已初始化并执行个人业务 | 查询 TB003 | `SELECT * FROM TB003` | 个人账户、证件信息、缴存基数、月汇缴金额等字段存在且可查 | 截图显示 TB003 查询结果 | `docs/screenshots/28-db-tb003-person.jpg` | 通过 |  |

## 截图证据目录

`TEST_CASE_TABLE.xlsx` 中包含“截图证据目录”工作表。正式迁移到 Word 时，建议按照第4章测试用例表中的“证据文件”列插入对应截图。

## 注意事项

1. 不要把没有截图或日志支撑的测试写成已通过。
2. 第4章 Word 正文可使用精简 8 列表格；本文件保留更完整的前置条件、测试步骤、证据文件和备注。
3. Excel 批量开户的成功源文件为 `docs/screenshots/batch-success.xlsx`，失败回滚源文件为 `docs/screenshots/batch-fail.xlsx`。
