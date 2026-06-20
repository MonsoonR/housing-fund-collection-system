# 核心代码说明

## 1. 系统参数维护

- 功能说明：维护 `TB001` 系统参数，支持模糊查询、新增、修改、删除普通参数，保护账号序列参数。
- 入口路径：`/params`、`/params/new`、`/params/{seqname}/edit`、`/params/{seqname}/update`、`/params/{seqname}/delete`。
- 关键文件：
  - `src/main/java/com/housingfund/collection/controller/ParamController.java`
  - `src/main/java/com/housingfund/collection/service/ParamService.java`
  - `src/main/java/com/housingfund/collection/service/impl/ParamServiceImpl.java`
  - `src/main/java/com/housingfund/collection/mapper/ParamMapper.java`
  - `src/main/resources/mapper/ParamMapper.xml`
  - `src/main/webapp/WEB-INF/jsp/param/list.jsp`
  - `src/main/webapp/WEB-INF/jsp/param/form.jsp`
- 关键方法：`findBySeqnameLike`、`create`、`update`、`delete`。
- 业务规则：`SEQNAME` 新增后不可修改；`SEQ` 和 `MAXSEQ` 必须为正数且 `SEQ <= MAXSEQ`；`UNITACCNUM` 和 `PERACCNUM` 禁止删除。
- 数据库操作：查询、插入、更新、删除 `TB001`。
- 验收点：查询账号序列参数、新增普通参数、修改普通参数、删除普通参数、删除账号序列时提示禁止。

## 2. 单位开户

- 功能说明：根据单位资料建立单位公积金账户。
- 入口路径：`/units/open`。
- 关键文件：
  - `UnitController.java`
  - `UnitService.java`
  - `UnitServiceImpl.java`
  - `UnitMapper.java`
  - `UnitMapper.xml`
  - `unit/open.jsp`
  - `unit/receipt.jsp`
- 关键方法：`UnitController#open`、`UnitServiceImpl#openUnit`、`UnitMapper#insert`、`ParamMapper#selectBySeqnameForUpdate`、`ParamMapper#updateSeq`。
- 业务规则：组织机构代码 9 位；单位名称长度受限；经办人身份证合法；发薪日期 `01-31`；单位比例、个人比例在 `0.050-0.120`；正常状态组织机构代码不能重复。
- 数据库操作：锁定 `TB001.UNITACCNUM`，插入 `TB002`，递增 `TB001.SEQ`。
- 验收点：生成 12 位单位账号，`UNITACCNUM.SEQ` 递增，`TB002` 写入状态、开户日期、比例和汇总字段。

## 3. 个人手工开户

- 功能说明：在正常单位下建立个人公积金账户。
- 入口路径：`/persons/open`。
- 关键文件：
  - `PersonController.java`
  - `PersonService.java`
  - `PersonServiceImpl.java`
  - `PersonMapper.java`
  - `PersonMapper.xml`
  - `person/open.jsp`
  - `person/receipt.jsp`
- 关键方法：`PersonController#open`、`PersonServiceImpl#openPerson`、`PersonServiceImpl#validateOpenCandidate`、`PersonServiceImpl#insertNewPerson`、`PersonServiceImpl#reactivateClosedPerson`。
- 业务规则：单位账号必须存在且状态正常；证件类型为 `01身份证`；证件号码合法；正常账户证件号不能重复；销户账户可重启；缴存基数大于 0；比例来自单位资料并再次校验。
- 数据库操作：锁定 `TB001.PERACCNUM`，插入或更新 `TB003`，更新 `TB002` 汇总字段。
- 验收点：个人账号生成、个人资料写入、单位人数和汇总金额同步更新。

## 4. 个人 Excel 批量开户

- 功能说明：从 Excel 批量导入个人开户数据。
- 入口路径：`/persons/open/import`。
- 关键文件：
  - `PersonController#importPersons`
  - `PersonServiceImpl#importPersons`
  - `PersonServiceImpl#parseImportRows`
  - `PersonServiceImpl#validateOpenCandidate`
  - `PersonBatchImportResult.java`
  - `PersonBatchImportFailure.java`
  - `person/open.jsp`
- 关键方法：
  - `importPersons(InputStream inputStream, String originalFilename)`：批量导入总入口。
  - `validateOpenCandidate(PersonOpenForm form)`：复用手工开户校验。
  - `openPerson(PersonOpenForm form)`：逐条执行开户。
- 业务规则：支持 `.xls/.xlsx`；首行为表头；固定列为单位账号、姓名、证件类型、证件号码、缴存基数、单位比例、个人比例；跳过空行；检查 Excel 内重复证件号；单位不存在、单位销户、证件号重复、基数或比例格式错误均记录失败原因。
- 事务策略：全部成功才提交。任一非空行失败时，本批次不新增 `TB003`，不更新 `TB002`，不递增 `TB001.PERACCNUM`。
- 数据库操作：成功批次逐条复用手工开户数据库操作；失败批次整体回滚。
- 验收点：页面显示成功数量、失败数量和逐行失败原因；失败批次数据库无新增记录；成功批次导入 2 条数据。

## 5. 单位资料修改

- 功能说明：修改单位基础资料。
- 入口路径：`/units/edit`、`/units/edit/form`、`/units/edit/search`。
- 关键文件：
  - `UnitController#searchEditForm`
  - `UnitController#update`
  - `UnitServiceImpl#getEditableUnit`
  - `UnitServiceImpl#updateUnit`
  - `UnitMapper#updateEditableFields`
  - `unit/edit.jsp`
  - `unit/edit-receipt.jsp`
- 业务规则：必须按 12 位单位账号查询；销户单位不可修改；不修改任何字段应失败；只允许更新单位基础资料字段，不允许修改账号、比例、余额、汇总字段、状态、机构、柜员、建立日期。
- 数据库操作：按 `UNITACCNUM` 查询 `TB002`；更新 `TB002` 中允许修改的字段。
- 验收点：允许字段更新成功，禁止字段未被误改，销户单位提示不可修改。

## 6. 个人资料修改

- 功能说明：修改个人姓名、证件类型和证件号码，支持证件号冲突强制变更。
- 入口路径：`/persons/edit`、`/persons/edit/form`、`/persons/edit/search`、`/persons/edit/force`。
- 关键文件：
  - `PersonController#update`
  - `PersonController#forceUpdate`
  - `PersonServiceImpl#updatePerson`
  - `PersonServiceImpl#forceUpdatePerson`
  - `PersonServiceImpl#insertWrongAccountCopy`
  - `PersonMapper#selectIdCardConflict`
  - `PersonMapper#updateEditableFields`
  - `person/edit.jsp`
  - `person/edit-conflict.jsp`
  - `person/edit-receipt.jsp`
- 关键方法：
  - `forceUpdatePerson`：强制变更事务入口。
  - `insertWrongAccountCopy`：使用 `TB001.PERACCNUM` 生成新账号，复制占用账户原信息，写入错误账户。
- 业务规则：个人账号必须存在且正常；只允许改姓名、证件类型、证件号码；证件号被其他账户占用时先提示冲突；强制变更新建错误账户保存占用账户原信息，原占用账户证件号改为 `8` 开头，新错误账户证件号为 `9` 开头，目标账户使用正确证件号。
- 数据库操作：查询 `TB003`、插入错误账户、更新占用账户证件号、更新目标账户资料、递增 `PERACCNUM`。
- 验收点：冲突不强制时提示占用账户；强制后新错误账户存在、目标账户拿到正确证件号、原占用账户信息保留。

## 7. 单位信息查询

- 功能说明：查询单位账户信息。
- 入口路径：`/units/query`。
- 关键文件：
  - `UnitController#query`
  - `UnitServiceImpl#queryUnits`
  - `UnitMapper#selectByUnitAccNum`
  - `UnitMapper#selectByUnitNameLike`
  - `unit/query.jsp`
- 业务规则：输入单位账号时优先精确查询；未输入单位账号时按单位名称模糊查询；至少输入一个查询条件。
- 数据库操作：查询 `TB002`，并在 Service 层计算合计比例、合计月缴额等展示字段。
- 验收点：账号精确查询、名称模糊查询、详情显示字段符合指导书输出要求。

## 8. 个人信息查询

- 功能说明：查询个人账户及所属单位信息。
- 入口路径：`/persons/query`。
- 关键文件：
  - `PersonController#query`
  - `PersonServiceImpl#queryPerson`
  - `PersonMapper#selectQueryByPerAccNum`
  - `PersonMapper#selectQueryByIdCard`
  - `person/query.jsp`
- 业务规则：输入个人账号时优先按账号查询；未输入个人账号时按证件号码查询；证件号码需要合法。
- 数据库操作：联查 `TB003` 和 `TB002`，返回个人账户、单位名称、缴存比例、月缴额、余额和状态。
- 验收点：按账号和证件号码均能查到姓名、证件类型/号码、缴存基数、比例、月缴额、余额、状态等核心字段。
