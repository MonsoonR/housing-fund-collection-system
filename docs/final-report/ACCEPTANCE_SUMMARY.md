# 验收前实机验证总结

## 1. 最终 Git 状态

验收前实机验证完成后，工作区曾确认干净。生成本目录报告材料后，需要再次执行 `git status` 并提交文档材料。

关键提交记录包括：

```text
d1c0b07 fix: resolve acceptance runtime issues
9ad96d4 fix: address acceptance blockers
```

当前仓库后续还包含截图整理和首页文案修复提交，最终以实际 `git log --oneline -5` 为准。

## 2. 数据库初始化结果

测试库：

```text
housingfund_collection_acceptance
```

导入顺序：

```text
db/schema.sql
db/data.sql
db/demo-data.sql
```

说明：`db/schema.sql` 包含 `DROP TABLE IF EXISTS TB003/TB002/TB001`，会重建测试表，不能直接对重要数据库执行。

数据库检查结果：

```text
TB001_SEQ: PERACCNUM,UNITACCNUM
TB002_COUNT: 3
TB003_COUNT: 4
TB001.SEQNAME: char(20)
TB002.INSTCODE: char(8)
TB003.INSTCODE: char(8)
主要金额字段: decimal(16,2)
```

## 3. Tomcat 部署路径

验收前实机验证使用路径：

```text
http://127.0.0.1:18090/housingfund-collection/
```

用户本机 IDEA/Tomcat 当前浏览器路径曾显示：

```text
http://localhost:8090/housingfund_collection_war/
```

报告中建议以标准 WAR 名称 `housingfund-collection.war` 和标准上下文 `/housingfund-collection/` 作为正式部署说明。

## 4. 8 个验收入口验证结果

| 入口 | 路径 | 验证结果 |
| --- | --- | --- |
| 系统参数维护 | `/params` | 查询、新增、修改、删除普通参数、保护账号序列参数均通过。 |
| 单位开户 | `/units/open` | 生成单位账号 `000000900100`，`UNITACCNUM.SEQ` 递增，`TB002` 写入。 |
| 个人开户 | `/persons/open` | 手工开户生成个人账号 `000000900100`，`TB003` 写入并更新单位汇总。 |
| 个人 Excel 批量开户 | `/persons/open/import` | 失败批次整体回滚，成功批次导入 2 条。 |
| 单位资料修改 | `/units/edit` | 正常单位允许字段修改成功，销户单位拒绝修改。 |
| 个人资料修改 | `/persons/edit` | 普通修改成功，冲突不强制提示成功，强制变更新建错误账户成功。 |
| 单位信息查询 | `/units/query` | 单位账号精确查询和单位名称模糊查询通过。 |
| 个人信息查询 | `/persons/query` | 个人账号查询和证件号码查询通过。 |

## 5. Excel 批量导入验证结果

失败批次：

- 包含 Excel 内重复证件号。
- 包含不存在单位账号。
- 页面显示成功 0 条和逐行失败原因。
- 数据库未新增失败批次记录。
- `TB001.PERACCNUM.SEQ` 未递增。

成功批次：

```text
000000900101 批量甲 11010519960202002X 基数 5200.00 月缴 416.00/416.00
000000900102 批量乙 110105199603030035 基数 5300.00 月缴 424.00/424.00
```

事务策略：全部成功才提交。

## 6. 个人资料强制变更验证结果

测试场景：目标账户 `000000900001` 使用被 `000000900003` 占用的证件号 `110105199303030033`。

强制变更后：

```text
000000900001 演示张三强制 110105199303030033
000000900003 演示王五     810105199303030033
000000900103 演示王五     910105199303030033
```

验证结论：

- 目标账户获得正确证件号码。
- 原占用账户证件号码释放为 `8` 开头。
- 新建错误账户存在，证件号码为 `9` 开头。
- 新错误账户保留原占用账户姓名、单位、余额、缴存基数、比例、月缴额等信息。
- `TB001.PERACCNUM.SEQ` 正确递增。

## 7. Maven 构建结果

```text
mvn clean package
Tests run: 78, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
WAR: target/housingfund-collection.war
```

## 8. 剩余非阻塞事项

1. 成功回单和查询结果截图仍建议手工补齐。
2. 需要将 `REPORT_DRAFT.md` 内容迁移到 Word 报告模板。
3. Word 报告中需补充姓名、学号、班级、指导教师等个人信息。
4. Mermaid 图需要渲染为图片后插入 Word。
5. 最终提交前应再次执行数据库初始化、Tomcat 访问和 Maven 构建验证。

## 9. 是否建议提交验收

建议提交验收。当前功能、数据库字段、配置安全、批量导入、强制变更、构建测试和实机路径验证均已达到课程设计验收要求。剩余事项主要是报告排版和截图插入，不属于代码阻塞问题。
