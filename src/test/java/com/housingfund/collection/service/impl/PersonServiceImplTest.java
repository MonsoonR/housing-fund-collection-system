package com.housingfund.collection.service.impl;

import com.housingfund.collection.entity.PersonBasicInfo;
import com.housingfund.collection.entity.SystemParam;
import com.housingfund.collection.entity.UnitBasicInfo;
import com.housingfund.collection.exception.BusinessException;
import com.housingfund.collection.mapper.ParamMapper;
import com.housingfund.collection.mapper.PersonMapper;
import com.housingfund.collection.mapper.UnitMapper;
import com.housingfund.collection.vo.PersonOpenForm;
import com.housingfund.collection.vo.PersonOpenResult;
import com.housingfund.collection.vo.PersonBatchImportResult;
import com.housingfund.collection.vo.PersonEditForm;
import com.housingfund.collection.vo.PersonEditResult;
import com.housingfund.collection.vo.PersonIdConflictResult;
import com.housingfund.collection.vo.PersonQueryForm;
import com.housingfund.collection.vo.PersonQueryResult;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class PersonServiceImplTest {

    @Test
    public void openPersonCreatesPersonalAccountForNormalUnit() {
        FakeParamMapper paramMapper = mapperWithSeq(1L, 999999999999L);
        FakeUnitMapper unitMapper = new FakeUnitMapper();
        unitMapper.insert(buildUnit("000000000010"));
        FakePersonMapper personMapper = new FakePersonMapper();
        PersonServiceImpl service = new PersonServiceImpl(personMapper, unitMapper, paramMapper);

        PersonOpenResult result = service.openPerson(validForm());

        assertEquals("000000000001", result.getPerAccNum());
        assertEquals("李四", result.getPerName());
        assertEquals("11010519491231002X", result.getIdCard());
        assertEquals("000000000010", result.getUnitAccNum());
        assertEquals("测试单位", result.getUnitName());
        assertEquals(new BigDecimal("5000.00"), result.getBaseNum());
        assertEquals(new BigDecimal("0.080"), result.getUnitRatio());
        assertEquals(new BigDecimal("0.080"), result.getPerRatio());
        assertEquals(new BigDecimal("400.00"), result.getUnitMonthPay());
        assertEquals(new BigDecimal("400.00"), result.getPerMonthPay());
        assertNotNull(result.getCreateTime());

        PersonBasicInfo saved = personMapper.selectByPerAccNum("000000000001");
        assertNotNull(saved);
        assertEquals("01身份证", saved.getIdType());
        assertEquals("0", saved.getStatus());
        assertEquals(new BigDecimal("400.00"), saved.getUnitMonthPay());
        assertEquals(new BigDecimal("400.00"), saved.getPerMonthPay());
        assertEquals(LocalDate.of(1899, 12, 1), saved.getLastPayDate());
        assertEquals(new BigDecimal("0.00"), saved.getYpayAmt());
        assertEquals(new BigDecimal("0.00"), saved.getYdrawAmt());
        assertEquals(new BigDecimal("0.00"), saved.getYinterestBal());
        assertEquals("0110", saved.getInstCode());
        assertEquals("111111", saved.getOp());
        assertNull(saved.getRemark());
    }

    @Test
    public void getOpenUnitInfoReflectsUnitNameAndRatiosForPersonalOpening() {
        FakeUnitMapper unitMapper = new FakeUnitMapper();
        UnitBasicInfo unit = buildUnit("000000000010");
        unit.setUnitName("开户单位");
        unit.setUnitRatio(new BigDecimal("0.090"));
        unit.setPerRatio(new BigDecimal("0.070"));
        unitMapper.insert(unit);
        PersonServiceImpl service = new PersonServiceImpl(new FakePersonMapper(), unitMapper, mapperWithSeq(1L, 999999999999L));

        PersonOpenForm form = service.getOpenUnitInfo("000000000010");

        assertEquals("000000000010", form.getUnitAccNum());
        assertEquals("开户单位", form.getUnitName());
        assertEquals(new BigDecimal("0.090"), form.getUnitRatio());
        assertEquals(new BigDecimal("0.070"), form.getPerRatio());
        assertEquals("01身份证", form.getIdType());
    }

    @Test
    public void getOpenUnitInfoRejectsClosedUnit() {
        FakeUnitMapper unitMapper = new FakeUnitMapper();
        UnitBasicInfo unit = buildUnit("000000000010");
        unit.setAccState("9");
        unitMapper.insert(unit);
        PersonServiceImpl service = new PersonServiceImpl(new FakePersonMapper(), unitMapper, mapperWithSeq(1L, 999999999999L));

        try {
            service.getOpenUnitInfo("000000000010");
            fail("Expected BusinessException");
        } catch (BusinessException ex) {
            assertEquals("单位账号不存在或状态非正常", ex.getMessage());
        }
    }

    @Test
    public void openPersonRejectsNonGuidanceIdType() {
        FakeUnitMapper unitMapper = new FakeUnitMapper();
        unitMapper.insert(buildUnit("000000000010"));
        PersonServiceImpl service = new PersonServiceImpl(new FakePersonMapper(), unitMapper, mapperWithSeq(1L, 999999999999L));
        PersonOpenForm form = validForm();
        form.setIdType("居民身份证");

        try {
            service.openPerson(form);
            fail("Expected BusinessException");
        } catch (BusinessException ex) {
            assertEquals("证件类型目前只支持01身份证", ex.getMessage());
        }
    }

    @Test
    public void openPersonRejectsMissingNormalUnit() {
        PersonServiceImpl service = new PersonServiceImpl(
                new FakePersonMapper(), new FakeUnitMapper(), mapperWithSeq(1L, 999999999999L));

        try {
            service.openPerson(validForm());
            fail("Expected BusinessException");
        } catch (BusinessException ex) {
            assertEquals("单位账号不存在或状态非正常", ex.getMessage());
        }
    }

    @Test
    public void openPersonRejectsDuplicateNormalPerson() {
        FakeUnitMapper unitMapper = new FakeUnitMapper();
        unitMapper.insert(buildUnit("000000000010"));
        FakePersonMapper personMapper = new FakePersonMapper();
        PersonBasicInfo existing = buildClosedPerson("000000000099");
        existing.setStatus("0");
        personMapper.insert(existing);
        PersonServiceImpl service = new PersonServiceImpl(personMapper, unitMapper, mapperWithSeq(1L, 999999999999L));

        try {
            service.openPerson(validForm());
            fail("Expected BusinessException");
        } catch (BusinessException ex) {
            assertEquals("该人员已开户", ex.getMessage());
        }
    }

    @Test
    public void openPersonRejectsBaseNumNotPositive() {
        PersonServiceImpl service = new PersonServiceImpl(
                new FakePersonMapper(), new FakeUnitMapper(), mapperWithSeq(1L, 999999999999L));
        PersonOpenForm form = validForm();
        form.setBaseNum(BigDecimal.ZERO);

        try {
            service.openPerson(form);
            fail("Expected BusinessException");
        } catch (BusinessException ex) {
            assertEquals("缴存基数必须大于0", ex.getMessage());
        }
    }

    @Test
    public void openPersonRejectsNameLongerThanTwelveChineseCharacters() {
        FakeUnitMapper unitMapper = new FakeUnitMapper();
        unitMapper.insert(buildUnit("000000000010"));
        PersonServiceImpl service = new PersonServiceImpl(new FakePersonMapper(), unitMapper, mapperWithSeq(1L, 999999999999L));
        PersonOpenForm form = validForm();
        form.setPerName("一二三四五六七八九十一二三");

        try {
            service.openPerson(form);
            fail("Expected BusinessException");
        } catch (BusinessException ex) {
            assertEquals("个人姓名不能超过12个汉字", ex.getMessage());
        }
    }

    @Test
    public void openPersonRejectsUnitRatioFromUnitBelowFivePercent() {
        FakeUnitMapper unitMapper = new FakeUnitMapper();
        UnitBasicInfo unit = buildUnit("000000000010");
        unit.setUnitRatio(new BigDecimal("0.049"));
        unitMapper.insert(unit);
        PersonServiceImpl service = new PersonServiceImpl(new FakePersonMapper(), unitMapper, mapperWithSeq(1L, 999999999999L));

        try {
            service.openPerson(validForm());
            fail("Expected BusinessException");
        } catch (BusinessException ex) {
            assertEquals("单位比例必须在0.050到0.120之间", ex.getMessage());
        }
    }

    @Test
    public void openPersonRejectsPersonalRatioFromUnitAboveTwelvePercent() {
        FakeUnitMapper unitMapper = new FakeUnitMapper();
        UnitBasicInfo unit = buildUnit("000000000010");
        unit.setPerRatio(new BigDecimal("0.121"));
        unitMapper.insert(unit);
        PersonServiceImpl service = new PersonServiceImpl(new FakePersonMapper(), unitMapper, mapperWithSeq(1L, 999999999999L));

        try {
            service.openPerson(validForm());
            fail("Expected BusinessException");
        } catch (BusinessException ex) {
            assertEquals("个人比例必须在0.050到0.120之间", ex.getMessage());
        }
    }

    @Test
    public void openPersonIncrementsPersonalAccountSequenceAfterInsert() {
        FakeParamMapper paramMapper = mapperWithSeq(7L, 999999999999L);
        FakeUnitMapper unitMapper = new FakeUnitMapper();
        unitMapper.insert(buildUnit("000000000010"));
        PersonServiceImpl service = new PersonServiceImpl(new FakePersonMapper(), unitMapper, paramMapper);

        PersonOpenResult result = service.openPerson(validForm());

        assertEquals("000000000007", result.getPerAccNum());
        assertEquals(Long.valueOf(8L), paramMapper.selectBySeqname("PERACCNUM").getSeq());
    }

    @Test
    public void openPersonUpdatesUnitSummaryAfterSuccess() {
        FakeParamMapper paramMapper = mapperWithSeq(1L, 999999999999L);
        FakeUnitMapper unitMapper = new FakeUnitMapper();
        UnitBasicInfo unit = buildUnit("000000000010");
        unit.setBaseNumber(new BigDecimal("1000.00"));
        unit.setUnitPaySum(new BigDecimal("80.00"));
        unit.setPerPaySum(new BigDecimal("80.00"));
        unit.setPersNum(2);
        unitMapper.insert(unit);
        PersonServiceImpl service = new PersonServiceImpl(new FakePersonMapper(), unitMapper, paramMapper);

        service.openPerson(validForm());

        UnitBasicInfo updated = unitMapper.selectByUnitAccNum("000000000010");
        assertEquals(new BigDecimal("6000.00"), updated.getBaseNumber());
        assertEquals(new BigDecimal("480.00"), updated.getUnitPaySum());
        assertEquals(new BigDecimal("480.00"), updated.getPerPaySum());
        assertEquals(Integer.valueOf(3), updated.getPersNum());
    }

    @Test
    public void openPersonReactivatesClosedAccountWithoutConsumingSequence() {
        FakeParamMapper paramMapper = mapperWithSeq(11L, 999999999999L);
        FakeUnitMapper unitMapper = new FakeUnitMapper();
        unitMapper.insert(buildUnit("000000000010"));
        FakePersonMapper personMapper = new FakePersonMapper();
        personMapper.insert(buildClosedPerson("000000000088"));
        PersonServiceImpl service = new PersonServiceImpl(personMapper, unitMapper, paramMapper);

        PersonOpenResult result = service.openPerson(validForm());

        assertEquals("000000000088", result.getPerAccNum());
        assertEquals(Long.valueOf(11L), paramMapper.selectBySeqname("PERACCNUM").getSeq());
        assertEquals("0", personMapper.selectByPerAccNum("000000000088").getStatus());
        assertEquals(new BigDecimal("400.00"), personMapper.selectByPerAccNum("000000000088").getUnitMonthPay());
    }

    @Test
    public void importPersonsOpensAllValidExcelRowsAndSkipsBlankRows() throws Exception {
        FakeParamMapper paramMapper = mapperWithSeq(1L, 999999999999L);
        FakeUnitMapper unitMapper = new FakeUnitMapper();
        unitMapper.insert(buildUnit("000000000010"));
        FakePersonMapper personMapper = new FakePersonMapper();
        PersonServiceImpl service = new PersonServiceImpl(personMapper, unitMapper, paramMapper);

        PersonBatchImportResult result = service.importPersons(new ByteArrayInputStream(excelBytes(
                row("000000000010", "李四", "01身份证", "11010519491231002X", "5000.00", "0.080", "0.080"),
                row("", "", "", "", "", "", ""),
                row("000000000010", "王五", "01身份证", "110105195001010012", "6000.00", "0.080", "0.080")
        )), "persons.xlsx");

        assertEquals(2, result.getSuccessCount());
        assertEquals(0, result.getFailureCount());
        assertTrue(result.getFailures().isEmpty());
        assertEquals("000000000001", personMapper.selectByIdCard("11010519491231002X").getPerAccNum());
        assertEquals("000000000002", personMapper.selectByIdCard("110105195001010012").getPerAccNum());
        assertEquals(Long.valueOf(3L), paramMapper.selectBySeqname("PERACCNUM").getSeq());
        assertEquals(Integer.valueOf(2), unitMapper.selectByUnitAccNum("000000000010").getPersNum());
    }

    @Test
    public void importPersonsReportsDuplicateRowsAndDoesNotCreateAnyAccount() throws Exception {
        FakeParamMapper paramMapper = mapperWithSeq(1L, 999999999999L);
        FakeUnitMapper unitMapper = new FakeUnitMapper();
        unitMapper.insert(buildUnit("000000000010"));
        FakePersonMapper personMapper = new FakePersonMapper();
        PersonServiceImpl service = new PersonServiceImpl(personMapper, unitMapper, paramMapper);

        PersonBatchImportResult result = service.importPersons(new ByteArrayInputStream(excelBytes(
                row("000000000010", "李四", "01身份证", "11010519491231002X", "5000.00", "0.080", "0.080"),
                row("000000000010", "李四重复", "01身份证", "11010519491231002X", "5000.00", "0.080", "0.080")
        )), "persons.xlsx");

        assertEquals(0, result.getSuccessCount());
        assertEquals(1, result.getFailureCount());
        assertEquals(3, result.getFailures().get(0).getRowNumber());
        assertEquals("Excel中证件号码重复", result.getFailures().get(0).getMessage());
        assertNull(personMapper.selectByIdCard("11010519491231002X"));
        assertEquals(Long.valueOf(1L), paramMapper.selectBySeqname("PERACCNUM").getSeq());
        assertEquals(Integer.valueOf(0), unitMapper.selectByUnitAccNum("000000000010").getPersNum());
    }

    @Test
    public void importPersonsReportsMissingUnitAndDoesNotCreateAnyAccount() throws Exception {
        FakeParamMapper paramMapper = mapperWithSeq(1L, 999999999999L);
        FakePersonMapper personMapper = new FakePersonMapper();
        PersonServiceImpl service = new PersonServiceImpl(personMapper, new FakeUnitMapper(), paramMapper);

        PersonBatchImportResult result = service.importPersons(new ByteArrayInputStream(excelBytes(
                row("000000000010", "李四", "01身份证", "11010519491231002X", "5000.00", "0.080", "0.080")
        )), "persons.xlsx");

        assertEquals(0, result.getSuccessCount());
        assertEquals(1, result.getFailureCount());
        assertEquals(2, result.getFailures().get(0).getRowNumber());
        assertEquals("单位账号不存在或状态非正常", result.getFailures().get(0).getMessage());
        assertNull(personMapper.selectByIdCard("11010519491231002X"));
        assertEquals(Long.valueOf(1L), paramMapper.selectBySeqname("PERACCNUM").getSeq());
    }

    @Test
    public void queryPersonByAccountReturnsResult() {
        FakePersonMapper personMapper = new FakePersonMapper();
        personMapper.addQueryResult(buildQueryPerson());
        PersonServiceImpl service = new PersonServiceImpl(personMapper, new FakeUnitMapper(), mapperWithSeq(1L, 999999999999L));
        PersonQueryForm form = new PersonQueryForm();
        form.setPerAccNum("000000000001");
        form.setIdCard("11010519491231002X");

        PersonQueryResult result = service.queryPerson(form);

        assertNotNull(result);
        assertEquals("测试单位", result.getUnitName());
        assertEquals("000000000010", result.getUnitAccNum());
        assertEquals("李四", result.getPerName());
        assertEquals("000000000001", result.getPerAccNum());
        assertEquals("11010519491231002X", result.getIdCard());
        assertEquals(new BigDecimal("0.080"), result.getUnitRatio());
        assertEquals(new BigDecimal("0.070"), result.getPerRatio());
        assertEquals(new BigDecimal("0.150"), result.getTotalRatio());
        assertEquals(new BigDecimal("400.00"), result.getUnitMonthPay());
        assertEquals(new BigDecimal("350.00"), result.getPerMonthPay());
        assertEquals(new BigDecimal("750.00"), result.getTotalMonthPay());
        assertEquals("2026-05", result.getLastPayMonth());
        assertEquals("正常", result.getStatusText());
    }

    @Test
    public void queryPersonByIdCardReturnsResult() {
        FakePersonMapper personMapper = new FakePersonMapper();
        personMapper.addQueryResult(buildQueryPerson());
        PersonServiceImpl service = new PersonServiceImpl(personMapper, new FakeUnitMapper(), mapperWithSeq(1L, 999999999999L));
        PersonQueryForm form = new PersonQueryForm();
        form.setIdCard("11010519491231002X");

        PersonQueryResult result = service.queryPerson(form);

        assertNotNull(result);
        assertEquals("000000000001", result.getPerAccNum());
        assertEquals("李四", result.getPerName());
    }

    @Test
    public void queryPersonRejectsEmptyCondition() {
        PersonServiceImpl service = new PersonServiceImpl(new FakePersonMapper(), new FakeUnitMapper(), mapperWithSeq(1L, 999999999999L));

        try {
            service.queryPerson(new PersonQueryForm());
            fail("Expected BusinessException");
        } catch (BusinessException ex) {
            assertEquals("请输入个人账号或身份证号", ex.getMessage());
        }
    }

    @Test
    public void queryPersonRejectsInvalidIdCard() {
        PersonServiceImpl service = new PersonServiceImpl(new FakePersonMapper(), new FakeUnitMapper(), mapperWithSeq(1L, 999999999999L));
        PersonQueryForm form = new PersonQueryForm();
        form.setIdCard("123456");

        try {
            service.queryPerson(form);
            fail("Expected BusinessException");
        } catch (BusinessException ex) {
            assertEquals("身份证号不正确", ex.getMessage());
        }
    }

    @Test
    public void queryPersonReturnsNullWhenNotFound() {
        PersonServiceImpl service = new PersonServiceImpl(new FakePersonMapper(), new FakeUnitMapper(), mapperWithSeq(1L, 999999999999L));
        PersonQueryForm form = new PersonQueryForm();
        form.setPerAccNum("000000009999");

        PersonQueryResult result = service.queryPerson(form);

        assertNull(result);
    }

    @Test
    public void getEditablePersonReturnsExistingNormalPersonWithUnitInfo() {
        FakeUnitMapper unitMapper = new FakeUnitMapper();
        unitMapper.insert(buildUnit("000000000010"));
        FakePersonMapper personMapper = new FakePersonMapper();
        personMapper.insert(buildEditablePerson("000000000001", "李四", "11010519491231002X", "0"));
        PersonServiceImpl service = new PersonServiceImpl(personMapper, unitMapper, mapperWithSeq(1L, 999999999999L));

        PersonEditForm form = service.getEditablePerson("000000000001");

        assertEquals("000000000001", form.getPerAccNum());
        assertEquals("000000000010", form.getUnitAccNum());
        assertEquals("测试单位", form.getUnitName());
        assertEquals("李四", form.getPerName());
        assertEquals("01身份证", form.getIdType());
        assertEquals("11010519491231002X", form.getIdCard());
    }

    @Test
    public void getEditablePersonRejectsMissingPerson() {
        PersonServiceImpl service = new PersonServiceImpl(new FakePersonMapper(), new FakeUnitMapper(), mapperWithSeq(1L, 999999999999L));

        try {
            service.getEditablePerson("000000009999");
            fail("Expected BusinessException");
        } catch (BusinessException ex) {
            assertEquals("个人账号不存在", ex.getMessage());
        }
    }

    @Test
    public void updatePersonRejectsClosedPerson() {
        FakeUnitMapper unitMapper = new FakeUnitMapper();
        unitMapper.insert(buildUnit("000000000010"));
        FakePersonMapper personMapper = new FakePersonMapper();
        personMapper.insert(buildEditablePerson("000000000001", "李四", "11010519491231002X", "9"));
        PersonServiceImpl service = new PersonServiceImpl(personMapper, unitMapper, mapperWithSeq(1L, 999999999999L));
        PersonEditForm form = validEditForm("000000000001");
        form.setPerName("李四修改");

        try {
            service.updatePerson(form);
            fail("Expected BusinessException");
        } catch (BusinessException ex) {
            assertEquals("已销户个人不能修改", ex.getMessage());
        }
    }

    @Test
    public void updatePersonRejectsNoChangedFields() {
        FakeUnitMapper unitMapper = new FakeUnitMapper();
        unitMapper.insert(buildUnit("000000000010"));
        FakePersonMapper personMapper = new FakePersonMapper();
        personMapper.insert(buildEditablePerson("000000000001", "李四", "11010519491231002X", "0"));
        PersonServiceImpl service = new PersonServiceImpl(personMapper, unitMapper, mapperWithSeq(1L, 999999999999L));

        try {
            service.updatePerson(validEditForm("000000000001"));
            fail("Expected BusinessException");
        } catch (BusinessException ex) {
            assertEquals("请至少修改一项个人资料", ex.getMessage());
        }
    }

    @Test
    public void updatePersonChangesNameSuccessfully() {
        FakeUnitMapper unitMapper = new FakeUnitMapper();
        unitMapper.insert(buildUnit("000000000010"));
        FakePersonMapper personMapper = new FakePersonMapper();
        personMapper.insert(buildEditablePerson("000000000001", "李四", "11010519491231002X", "0"));
        PersonServiceImpl service = new PersonServiceImpl(personMapper, unitMapper, mapperWithSeq(1L, 999999999999L));
        PersonEditForm form = validEditForm("000000000001");
        form.setPerName("李四修改");

        PersonEditResult result = service.updatePerson(form);

        assertFalse(result.isForceChanged());
        assertNull(result.getConflictResult());
        assertEquals("000000000001", result.getPerAccNum());
        assertEquals("李四修改", result.getPerName());
        assertEquals("11010519491231002X", result.getIdCard());
        assertEquals("000000000010", result.getUnitAccNum());
        assertEquals("测试单位", result.getUnitName());
        assertEquals("修改成功", result.getResultMessage());
        assertNotNull(result.getUpdateTime());
        assertEquals("李四修改", personMapper.selectByPerAccNum("000000000001").getPerName());
    }

    @Test
    public void updatePersonChangesIdCardSuccessfully() {
        FakeUnitMapper unitMapper = new FakeUnitMapper();
        unitMapper.insert(buildUnit("000000000010"));
        FakePersonMapper personMapper = new FakePersonMapper();
        personMapper.insert(buildEditablePerson("000000000001", "李四", "11010519491231002X", "0"));
        PersonServiceImpl service = new PersonServiceImpl(personMapper, unitMapper, mapperWithSeq(1L, 999999999999L));
        PersonEditForm form = validEditForm("000000000001");
        form.setIdCard("110105195001010012");

        PersonEditResult result = service.updatePerson(form);

        assertEquals("110105195001010012", result.getIdCard());
        assertEquals("110105195001010012", personMapper.selectByPerAccNum("000000000001").getIdCard());
        assertNull(personMapper.selectByIdCard("11010519491231002X"));
    }

    @Test
    public void updatePersonReturnsConflictWhenNewIdCardUsedByAnotherPerson() {
        FakeUnitMapper unitMapper = new FakeUnitMapper();
        unitMapper.insert(buildUnit("000000000010"));
        unitMapper.insert(buildUnit("000000000011"));
        unitMapper.selectByUnitAccNum("000000000011").setUnitName("占用单位");
        FakePersonMapper personMapper = new FakePersonMapper();
        personMapper.insert(buildEditablePerson("000000000001", "李四", "11010519491231002X", "0"));
        PersonBasicInfo occupied = buildEditablePerson("000000000002", "王五", "110105195001010012", "0");
        occupied.setUnitAccNum("000000000011");
        personMapper.insert(occupied);
        PersonServiceImpl service = new PersonServiceImpl(personMapper, unitMapper, mapperWithSeq(1L, 999999999999L));
        PersonEditForm form = validEditForm("000000000001");
        form.setIdCard("110105195001010012");

        PersonEditResult result = service.updatePerson(form);

        assertNotNull(result.getConflictResult());
        assertEquals("该身份证号已被其他个人账户占用，是否强制变更？", result.getResultMessage());
        assertEquals("11010519491231002X", personMapper.selectByPerAccNum("000000000001").getIdCard());
        assertEquals("110105195001010012", personMapper.selectByPerAccNum("000000000002").getIdCard());
    }

    @Test
    public void conflictResultContainsOccupiedPersonAndUnitInfo() {
        FakeUnitMapper unitMapper = new FakeUnitMapper();
        unitMapper.insert(buildUnit("000000000010"));
        unitMapper.insert(buildUnit("000000000011"));
        unitMapper.selectByUnitAccNum("000000000011").setUnitName("占用单位");
        FakePersonMapper personMapper = new FakePersonMapper();
        personMapper.insert(buildEditablePerson("000000000001", "李四", "11010519491231002X", "0"));
        PersonBasicInfo occupied = buildEditablePerson("000000000002", "王五", "110105195001010012", "9");
        occupied.setUnitAccNum("000000000011");
        personMapper.insert(occupied);
        PersonServiceImpl service = new PersonServiceImpl(personMapper, unitMapper, mapperWithSeq(1L, 999999999999L));
        PersonEditForm form = validEditForm("000000000001");
        form.setIdCard("110105195001010012");

        PersonIdConflictResult conflict = service.checkIdCardConflict(form);

        assertNotNull(conflict);
        assertEquals("000000000002", conflict.getOccupiedPerAccNum());
        assertEquals("110105195001010012", conflict.getOccupiedIdCard());
        assertEquals("王五", conflict.getOccupiedPerName());
        assertEquals("9", conflict.getOccupiedStatus());
        assertEquals("销户", conflict.getOccupiedStatusText());
        assertEquals("000000000011", conflict.getOccupiedUnitAccNum());
        assertEquals("占用单位", conflict.getOccupiedUnitName());
    }

    @Test
    public void forceUpdatePersonCreatesWrongAccountAndUpdatesCurrentPerson() {
        FakeUnitMapper unitMapper = new FakeUnitMapper();
        unitMapper.insert(buildUnit("000000000010"));
        FakePersonMapper personMapper = new FakePersonMapper();
        personMapper.insert(buildEditablePerson("000000000001", "李四", "11010519491231002X", "0"));
        PersonBasicInfo occupied = buildEditablePerson("000000000002", "王五", "110105195001010012", "0");
        occupied.setPerBalance(new BigDecimal("1234.56"));
        occupied.setRemark("原占用账户备注");
        personMapper.insert(occupied);
        FakeParamMapper paramMapper = mapperWithSeq(5L, 999999999999L);
        PersonServiceImpl service = new PersonServiceImpl(personMapper, unitMapper, paramMapper);
        PersonEditForm form = validEditForm("000000000001");
        form.setPerName("李四修改");
        form.setIdCard("110105195001010012");

        PersonEditResult result = service.forceUpdatePerson(form);

        assertTrue(result.isForceChanged());
        assertEquals("000000000002", result.getConflictPerAccNum());
        assertEquals("000000000005", result.getWrongAccountPerAccNum());
        assertEquals("110105195001010012", result.getOriginalConflictIdCard());
        assertEquals("910105195001010012", result.getChangedConflictIdCard());
        assertEquals("110105195001010012", personMapper.selectByPerAccNum("000000000001").getIdCard());
        assertEquals("李四修改", personMapper.selectByPerAccNum("000000000001").getPerName());
        assertEquals("910105195001010012", personMapper.selectByPerAccNum("000000000005").getIdCard());
        assertEquals("王五", personMapper.selectByPerAccNum("000000000005").getPerName());
        assertEquals(new BigDecimal("1234.56"), personMapper.selectByPerAccNum("000000000005").getPerBalance());
        assertEquals("原占用账户备注", personMapper.selectByPerAccNum("000000000005").getRemark());
        assertEquals(Long.valueOf(6L), paramMapper.selectBySeqname("PERACCNUM").getSeq());
        assertNotNull(personMapper.selectByPerAccNum("000000000001"));
        assertNotNull(personMapper.selectByPerAccNum("000000000002"));
        assertNotNull(personMapper.selectByPerAccNum("000000000005"));
    }

    @Test
    public void forceUpdatePersonRejectsExistingGeneratedWrongIdCardWithoutChangingData() {
        FakeUnitMapper unitMapper = new FakeUnitMapper();
        unitMapper.insert(buildUnit("000000000010"));
        FakePersonMapper personMapper = new FakePersonMapper();
        PersonBasicInfo current = buildEditablePerson("000000000001", "李四", "11010519491231002X", "0");
        PersonBasicInfo occupied = buildEditablePerson("000000000002", "王五", "110105195001010012", "0");
        PersonBasicInfo wrongIdOwner = buildEditablePerson("000000000003", "赵六", "910105195001010012", "0");
        personMapper.insert(current);
        personMapper.insert(occupied);
        personMapper.insert(wrongIdOwner);
        PersonServiceImpl service = new PersonServiceImpl(personMapper, unitMapper, mapperWithSeq(1L, 999999999999L));
        PersonEditForm form = validEditForm("000000000001");
        form.setIdCard("110105195001010012");

        try {
            service.forceUpdatePerson(form);
            fail("Expected BusinessException");
        } catch (BusinessException ex) {
            assertEquals("强制变更生成的错误身份证号已存在", ex.getMessage());
        }
        assertEquals("11010519491231002X", personMapper.selectByPerAccNum("000000000001").getIdCard());
        assertEquals("110105195001010012", personMapper.selectByPerAccNum("000000000002").getIdCard());
        assertEquals("910105195001010012", personMapper.selectByPerAccNum("000000000003").getIdCard());
    }

    @Test
    public void updatePersonRejectsInvalidIdCard() {
        PersonServiceImpl service = new PersonServiceImpl(new FakePersonMapper(), new FakeUnitMapper(), mapperWithSeq(1L, 999999999999L));
        PersonEditForm form = validEditForm("000000000001");
        form.setIdCard("123456");

        try {
            service.updatePerson(form);
            fail("Expected BusinessException");
        } catch (BusinessException ex) {
            assertEquals("身份证号不正确", ex.getMessage());
        }
    }

    @Test
    public void updatePersonRejectsNonResidentIdType() {
        PersonServiceImpl service = new PersonServiceImpl(new FakePersonMapper(), new FakeUnitMapper(), mapperWithSeq(1L, 999999999999L));
        PersonEditForm form = validEditForm("000000000001");
        form.setIdType("护照");

        try {
            service.updatePerson(form);
            fail("Expected BusinessException");
        } catch (BusinessException ex) {
            assertEquals("证件类型目前只支持01身份证", ex.getMessage());
        }
    }

    @Test
    public void updatePersonChangesOnlyEditableFields() {
        FakeUnitMapper unitMapper = new FakeUnitMapper();
        unitMapper.insert(buildUnit("000000000010"));
        FakePersonMapper personMapper = new FakePersonMapper();
        PersonBasicInfo original = buildEditablePerson("000000000001", "李四", "11010519491231002X", "0");
        original.setUnitAccNum("000000000010");
        original.setBaseNum(new BigDecimal("6000.00"));
        original.setUnitRatio(new BigDecimal("0.090"));
        original.setPerRatio(new BigDecimal("0.070"));
        original.setUnitMonthPay(new BigDecimal("540.00"));
        original.setPerMonthPay(new BigDecimal("420.00"));
        original.setPerBalance(new BigDecimal("1234.56"));
        original.setCreateTime(LocalDate.of(2026, 1, 2));
        original.setLastPayDate(LocalDate.of(2026, 5, 1));
        original.setYpayAmt(new BigDecimal("10.00"));
        original.setYdrawAmt(new BigDecimal("20.00"));
        original.setYinterestBal(new BigDecimal("30.00"));
        original.setInstCode("9999");
        original.setOp("888888");
        original.setRemark("原备注");
        personMapper.insert(original);
        PersonServiceImpl service = new PersonServiceImpl(personMapper, unitMapper, mapperWithSeq(1L, 999999999999L));
        PersonEditForm form = validEditForm("000000000001");
        form.setPerName("李四修改");

        service.updatePerson(form);
        PersonBasicInfo updated = personMapper.selectByPerAccNum("000000000001");

        assertEquals("000000000001", updated.getPerAccNum());
        assertEquals("000000000010", updated.getUnitAccNum());
        assertEquals(new BigDecimal("6000.00"), updated.getBaseNum());
        assertEquals(new BigDecimal("0.090"), updated.getUnitRatio());
        assertEquals(new BigDecimal("0.070"), updated.getPerRatio());
        assertEquals(new BigDecimal("540.00"), updated.getUnitMonthPay());
        assertEquals(new BigDecimal("420.00"), updated.getPerMonthPay());
        assertEquals(new BigDecimal("1234.56"), updated.getPerBalance());
        assertEquals("0", updated.getStatus());
        assertEquals(LocalDate.of(2026, 1, 2), updated.getCreateTime());
        assertEquals(LocalDate.of(2026, 5, 1), updated.getLastPayDate());
        assertEquals(new BigDecimal("10.00"), updated.getYpayAmt());
        assertEquals(new BigDecimal("20.00"), updated.getYdrawAmt());
        assertEquals(new BigDecimal("30.00"), updated.getYinterestBal());
        assertEquals("9999", updated.getInstCode());
        assertEquals("888888", updated.getOp());
        assertEquals("原备注", updated.getRemark());
        assertEquals("李四修改", updated.getPerName());
    }

    @Test
    public void forceUpdatePersonIsTransactional() throws NoSuchMethodException {
        Method method = PersonServiceImpl.class.getMethod("forceUpdatePerson", PersonEditForm.class);

        assertNotNull(method.getAnnotation(Transactional.class));
    }

    private static FakeParamMapper mapperWithSeq(Long seq, Long maxseq) {
        FakeParamMapper mapper = new FakeParamMapper();
        mapper.insert(buildParam(seq, maxseq));
        return mapper;
    }

    private static SystemParam buildParam(Long seq, Long maxseq) {
        SystemParam param = new SystemParam();
        param.setSeqname("PERACCNUM");
        param.setSeq(seq);
        param.setMaxseq(maxseq);
        param.setSeqDesc("个人账号序号");
        return param;
    }

    private static UnitBasicInfo buildUnit(String unitAccNum) {
        UnitBasicInfo unit = new UnitBasicInfo();
        unit.setUnitAccNum(unitAccNum);
        unit.setUnitName("测试单位");
        unit.setAccState("0");
        unit.setUnitRatio(new BigDecimal("0.080"));
        unit.setPerRatio(new BigDecimal("0.080"));
        unit.setBaseNumber(BigDecimal.ZERO);
        unit.setUnitPaySum(BigDecimal.ZERO);
        unit.setPerPaySum(BigDecimal.ZERO);
        unit.setPersNum(0);
        return unit;
    }

    private static PersonBasicInfo buildClosedPerson(String perAccNum) {
        PersonBasicInfo person = new PersonBasicInfo();
        person.setPerAccNum(perAccNum);
        person.setUnitAccNum("000000000009");
        person.setPerName("旧姓名");
        person.setIdType("01身份证");
        person.setIdCard("11010519491231002X");
        person.setBaseNum(new BigDecimal("1000.00"));
        person.setUnitRatio(new BigDecimal("0.050"));
        person.setPerRatio(new BigDecimal("0.050"));
        person.setUnitMonthPay(new BigDecimal("50.00"));
        person.setPerMonthPay(new BigDecimal("50.00"));
        person.setPerBalance(BigDecimal.ZERO);
        person.setStatus("9");
        person.setCreateTime(LocalDate.now().minusDays(1));
        person.setLastPayDate(LocalDate.of(1899, 12, 1));
        person.setYpayAmt(BigDecimal.ZERO);
        person.setYdrawAmt(BigDecimal.ZERO);
        person.setYinterestBal(BigDecimal.ZERO);
        person.setInstCode("0110");
        person.setOp("111111");
        return person;
    }

    private static PersonOpenForm validForm() {
        PersonOpenForm form = new PersonOpenForm();
        form.setUnitAccNum("000000000010");
        form.setPerName("李四");
        form.setIdType("01身份证");
        form.setIdCard("11010519491231002X");
        form.setBaseNum(new BigDecimal("5000"));
        return form;
    }

    private static String[] row(String unitAccNum, String perName, String idType, String idCard,
                                String baseNum, String unitRatio, String perRatio) {
        return new String[]{unitAccNum, perName, idType, idCard, baseNum, unitRatio, perRatio};
    }

    private static byte[] excelBytes(String[]... rows) throws Exception {
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            var sheet = workbook.createSheet("persons");
            Row header = sheet.createRow(0);
            String[] headers = {"单位账号", "姓名", "证件类型", "证件号码", "缴存基数", "单位比例", "个人比例"};
            for (int i = 0; i < headers.length; i++) {
                header.createCell(i).setCellValue(headers[i]);
            }
            for (int r = 0; r < rows.length; r++) {
                Row row = sheet.createRow(r + 1);
                for (int c = 0; c < rows[r].length; c++) {
                    row.createCell(c).setCellValue(rows[r][c]);
                }
            }
            workbook.write(output);
            return output.toByteArray();
        }
    }

    private static PersonEditForm validEditForm(String perAccNum) {
        PersonEditForm form = new PersonEditForm();
        form.setPerAccNum(perAccNum);
        form.setUnitAccNum("000000000010");
        form.setUnitName("测试单位");
        form.setPerName("李四");
        form.setIdType("01身份证");
        form.setIdCard("11010519491231002X");
        return form;
    }

    private static PersonBasicInfo buildEditablePerson(String perAccNum, String perName,
                                                       String idCard, String status) {
        PersonBasicInfo person = new PersonBasicInfo();
        person.setPerAccNum(perAccNum);
        person.setUnitAccNum("000000000010");
        person.setPerName(perName);
        person.setIdType("01身份证");
        person.setIdCard(idCard);
        person.setBaseNum(new BigDecimal("5000.00"));
        person.setUnitRatio(new BigDecimal("0.080"));
        person.setPerRatio(new BigDecimal("0.080"));
        person.setUnitMonthPay(new BigDecimal("400.00"));
        person.setPerMonthPay(new BigDecimal("400.00"));
        person.setPerBalance(BigDecimal.ZERO);
        person.setStatus(status);
        person.setCreateTime(LocalDate.of(2026, 1, 2));
        person.setLastPayDate(LocalDate.of(1899, 12, 1));
        person.setYpayAmt(BigDecimal.ZERO);
        person.setYdrawAmt(BigDecimal.ZERO);
        person.setYinterestBal(BigDecimal.ZERO);
        person.setInstCode("0110");
        person.setOp("111111");
        return person;
    }

    private static PersonQueryResult buildQueryPerson() {
        PersonQueryResult result = new PersonQueryResult();
        result.setUnitName("测试单位");
        result.setUnitAccNum("000000000010");
        result.setPerName("李四");
        result.setPerAccNum("000000000001");
        result.setIdCard("11010519491231002X");
        result.setPerBalance(new BigDecimal("2000.00"));
        result.setCreateTime(LocalDate.of(2026, 1, 2));
        result.setLastPayDate(LocalDate.of(2026, 5, 1));
        result.setUnitRatio(new BigDecimal("0.080"));
        result.setPerRatio(new BigDecimal("0.070"));
        result.setUnitMonthPay(new BigDecimal("400.00"));
        result.setPerMonthPay(new BigDecimal("350.00"));
        result.setStatus("0");
        return result;
    }

    private static class FakeParamMapper implements ParamMapper {
        private final Map<String, SystemParam> data = new LinkedHashMap<>();

        @Override
        public List<SystemParam> selectAll() {
            return new ArrayList<>(data.values());
        }

        @Override
        public List<SystemParam> selectBySeqnameLike(String seqname) {
            return new ArrayList<>();
        }

        @Override
        public SystemParam selectBySeqname(String seqname) {
            return copy(data.get(seqname));
        }

        @Override
        public SystemParam selectBySeqnameForUpdate(String seqname) {
            return copy(data.get(seqname));
        }

        @Override
        public int insert(SystemParam param) {
            data.put(param.getSeqname(), copy(param));
            return 1;
        }

        @Override
        public int update(SystemParam param) {
            data.put(param.getSeqname(), copy(param));
            return 1;
        }

        @Override
        public int updateSeq(String seqname, Long seq) {
            SystemParam param = data.get(seqname);
            if (param == null) {
                return 0;
            }
            param.setSeq(seq);
            return 1;
        }

        @Override
        public int deleteBySeqname(String seqname) {
            return data.remove(seqname) == null ? 0 : 1;
        }

        private SystemParam copy(SystemParam source) {
            if (source == null) {
                return null;
            }
            SystemParam target = new SystemParam();
            target.setSeqname(source.getSeqname());
            target.setSeq(source.getSeq());
            target.setMaxseq(source.getMaxseq());
            target.setSeqDesc(source.getSeqDesc());
            target.setFreeuse1(source.getFreeuse1());
            return target;
        }
    }

    private static class FakeUnitMapper implements UnitMapper {
        private final Map<String, UnitBasicInfo> data = new LinkedHashMap<>();

        @Override
        public UnitBasicInfo selectByUnitAccNum(String unitAccNum) {
            return data.get(unitAccNum);
        }

        @Override
        public UnitBasicInfo selectNormalByUnitAccNum(String unitAccNum) {
            UnitBasicInfo unit = data.get(unitAccNum);
            return unit != null && "0".equals(unit.getAccState()) ? unit : null;
        }

        @Override
        public UnitBasicInfo selectNormalByOrgCode(String orgCode) {
            return null;
        }

        @Override
        public UnitBasicInfo selectDuplicateOrgCodeAndUnitName(String orgCode, String unitName,
                                                               String excludeUnitAccNum) {
            return null;
        }

        @Override
        public List<UnitBasicInfo> selectByUnitNameLike(String unitName) {
            return new ArrayList<>();
        }

        @Override
        public int insert(UnitBasicInfo unit) {
            data.put(unit.getUnitAccNum(), unit);
            return 1;
        }

        @Override
        public int updateEditableFields(UnitBasicInfo unit) {
            return 0;
        }

        @Override
        public int updatePaymentSummary(String unitAccNum, BigDecimal baseNum,
                                        BigDecimal unitMonthPay, BigDecimal perMonthPay) {
            UnitBasicInfo unit = data.get(unitAccNum);
            if (unit == null || !"0".equals(unit.getAccState())) {
                return 0;
            }
            unit.setBaseNumber(unit.getBaseNumber().add(baseNum));
            unit.setUnitPaySum(unit.getUnitPaySum().add(unitMonthPay));
            unit.setPerPaySum(unit.getPerPaySum().add(perMonthPay));
            unit.setPersNum(unit.getPersNum() + 1);
            return 1;
        }
    }

    private static class FakePersonMapper implements PersonMapper {
        private final Map<String, PersonBasicInfo> data = new LinkedHashMap<>();
        private final Map<String, PersonQueryResult> queryByAccount = new LinkedHashMap<>();
        private final Map<String, PersonQueryResult> queryByIdCard = new LinkedHashMap<>();

        void addQueryResult(PersonQueryResult result) {
            queryByAccount.put(result.getPerAccNum(), result);
            queryByIdCard.put(result.getIdCard(), result);
        }

        @Override
        public PersonBasicInfo selectByPerAccNum(String perAccNum) {
            return data.get(perAccNum);
        }

        @Override
        public PersonBasicInfo selectByIdCard(String idCard) {
            for (PersonBasicInfo person : data.values()) {
                if (idCard.equals(person.getIdCard())) {
                    return person;
                }
            }
            return null;
        }

        @Override
        public PersonBasicInfo selectNormalByIdCard(String idCard) {
            PersonBasicInfo person = selectByIdCard(idCard);
            return person != null && "0".equals(person.getStatus()) ? person : null;
        }

        @Override
        public PersonQueryResult selectQueryByPerAccNum(String perAccNum) {
            return queryByAccount.get(perAccNum);
        }

        @Override
        public PersonQueryResult selectQueryByIdCard(String idCard) {
            return queryByIdCard.get(idCard);
        }

        @Override
        public PersonEditForm selectEditableByPerAccNum(String perAccNum) {
            PersonBasicInfo person = data.get(perAccNum);
            if (person == null) {
                return null;
            }
            PersonEditForm form = new PersonEditForm();
            form.setPerAccNum(person.getPerAccNum());
            form.setUnitAccNum(person.getUnitAccNum());
            form.setUnitName("000000000011".equals(person.getUnitAccNum()) ? "占用单位" : "测试单位");
            form.setPerName(person.getPerName());
            form.setIdType(person.getIdType());
            form.setIdCard(person.getIdCard());
            form.setStatus(person.getStatus());
            return form;
        }

        @Override
        public PersonIdConflictResult selectIdCardConflict(String idCard, String excludePerAccNum) {
            for (PersonBasicInfo person : data.values()) {
                if (idCard.equals(person.getIdCard()) && !person.getPerAccNum().equals(excludePerAccNum)) {
                    PersonIdConflictResult result = new PersonIdConflictResult();
                    result.setCurrentPerAccNum(excludePerAccNum);
                    result.setOccupiedPerAccNum(person.getPerAccNum());
                    result.setOccupiedIdCard(person.getIdCard());
                    result.setOccupiedPerName(person.getPerName());
                    result.setOccupiedStatus(person.getStatus());
                    result.setOccupiedStatusText("9".equals(person.getStatus()) ? "销户" : "正常");
                    result.setOccupiedUnitAccNum(person.getUnitAccNum());
                    result.setOccupiedUnitName("000000000011".equals(person.getUnitAccNum()) ? "占用单位" : "测试单位");
                    return result;
                }
            }
            return null;
        }

        @Override
        public int insert(PersonBasicInfo person) {
            data.put(person.getPerAccNum(), person);
            return 1;
        }

        @Override
        public int reactivate(PersonBasicInfo person) {
            if (!data.containsKey(person.getPerAccNum())) {
                return 0;
            }
            data.put(person.getPerAccNum(), person);
            return 1;
        }

        @Override
        public int updateEditableFields(PersonBasicInfo person) {
            PersonBasicInfo existing = data.get(person.getPerAccNum());
            if (existing == null || "9".equals(existing.getStatus()) || isIdCardUsedByAnother(person.getIdCard(), person.getPerAccNum())) {
                return 0;
            }
            existing.setPerName(person.getPerName());
            existing.setIdType(person.getIdType());
            existing.setIdCard(person.getIdCard());
            return 1;
        }

        @Override
        public int updateIdCard(String perAccNum, String idCard) {
            PersonBasicInfo existing = data.get(perAccNum);
            if (existing == null || isIdCardUsedByAnother(idCard, perAccNum)) {
                return 0;
            }
            existing.setIdCard(idCard);
            return 1;
        }

        private boolean isIdCardUsedByAnother(String idCard, String perAccNum) {
            for (PersonBasicInfo person : data.values()) {
                if (idCard.equals(person.getIdCard()) && !person.getPerAccNum().equals(perAccNum)) {
                    return true;
                }
            }
            return false;
        }
    }
}
