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
import com.housingfund.collection.vo.PersonQueryForm;
import com.housingfund.collection.vo.PersonQueryResult;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
        assertEquals("居民身份证", saved.getIdType());
        assertEquals("0", saved.getStatus());
        assertEquals(new BigDecimal("400.00"), saved.getUnitMonthPay());
        assertEquals(new BigDecimal("400.00"), saved.getPerMonthPay());
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
        person.setIdType("居民身份证");
        person.setIdCard("11010519491231002X");
        person.setBaseNum(new BigDecimal("1000.00"));
        person.setUnitRatio(new BigDecimal("0.050"));
        person.setPerRatio(new BigDecimal("0.050"));
        person.setUnitMonthPay(new BigDecimal("50.00"));
        person.setPerMonthPay(new BigDecimal("50.00"));
        person.setPerBalance(BigDecimal.ZERO);
        person.setStatus("9");
        person.setCreateTime(LocalDateTime.now().minusDays(1));
        person.setUpdateTime(LocalDateTime.now().minusDays(1));
        return person;
    }

    private static PersonOpenForm validForm() {
        PersonOpenForm form = new PersonOpenForm();
        form.setUnitAccNum("000000000010");
        form.setPerName("李四");
        form.setIdType("居民身份证");
        form.setIdCard("11010519491231002X");
        form.setBaseNum(new BigDecimal("5000"));
        form.setPhone("13800138000");
        form.setAddress("测试地址");
        return form;
    }

    private static PersonQueryResult buildQueryPerson() {
        PersonQueryResult result = new PersonQueryResult();
        result.setUnitName("测试单位");
        result.setUnitAccNum("000000000010");
        result.setPerName("李四");
        result.setPerAccNum("000000000001");
        result.setIdCard("11010519491231002X");
        result.setPerBalance(new BigDecimal("2000.00"));
        result.setCreateTime(LocalDateTime.of(2026, 1, 2, 9, 30));
        result.setLastPayDate(java.time.LocalDate.of(2026, 5, 1));
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
    }
}
