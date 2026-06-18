package com.housingfund.collection.service.impl;

import com.housingfund.collection.entity.SystemParam;
import com.housingfund.collection.entity.UnitBasicInfo;
import com.housingfund.collection.exception.BusinessException;
import com.housingfund.collection.mapper.ParamMapper;
import com.housingfund.collection.mapper.UnitMapper;
import com.housingfund.collection.vo.UnitOpenForm;
import com.housingfund.collection.vo.UnitOpenResult;
import com.housingfund.collection.vo.UnitQueryForm;
import com.housingfund.collection.vo.UnitQueryResult;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class UnitServiceImplTest {

    @Test
    public void openUnitCreatesAccountNumberFromSeqOne() {
        FakeParamMapper paramMapper = new FakeParamMapper();
        paramMapper.insert(buildParam(1L, 999999999999L));
        FakeUnitMapper unitMapper = new FakeUnitMapper();
        UnitServiceImpl service = new UnitServiceImpl(unitMapper, paramMapper);

        UnitOpenResult result = service.openUnit(validForm());

        assertEquals("000000000001", result.getUnitAccNum());
        assertEquals("测试单位", result.getUnitName());
        assertEquals("A12345678", result.getOrgCode());
        assertEquals(LocalDate.now(), result.getCreateDate());
        UnitBasicInfo saved = unitMapper.selectByUnitAccNum("000000000001");
        assertNotNull(saved);
        assertEquals("0", saved.getAccState());
        assertEquals(new BigDecimal("0.080"), saved.getUnitRatio());
        assertEquals(new BigDecimal("0.080"), saved.getPerRatio());
    }

    @Test
    public void openUnitRejectsExistingNormalOrgCode() {
        FakeParamMapper paramMapper = new FakeParamMapper();
        paramMapper.insert(buildParam(1L, 999999999999L));
        FakeUnitMapper unitMapper = new FakeUnitMapper();
        UnitBasicInfo existing = new UnitBasicInfo();
        existing.setUnitAccNum("000000000009");
        existing.setOrgCode("A12345678");
        existing.setAccState("0");
        unitMapper.insert(existing);
        UnitServiceImpl service = new UnitServiceImpl(unitMapper, paramMapper);

        try {
            service.openUnit(validForm());
            fail("Expected BusinessException");
        } catch (BusinessException ex) {
            assertEquals("该单位已建户", ex.getMessage());
        }
    }

    @Test
    public void openUnitRejectsUnitRatioLowerThanFivePercent() {
        UnitServiceImpl service = new UnitServiceImpl(new FakeUnitMapper(), mapperWithSeq(1L, 999999999999L));
        UnitOpenForm form = validForm();
        form.setUnitRatio(new BigDecimal("0.049"));

        try {
            service.openUnit(form);
            fail("Expected BusinessException");
        } catch (BusinessException ex) {
            assertEquals("单位比例必须在0.050到0.120之间", ex.getMessage());
        }
    }

    @Test
    public void openUnitRejectsPersonRatioHigherThanTwelvePercent() {
        UnitServiceImpl service = new UnitServiceImpl(new FakeUnitMapper(), mapperWithSeq(1L, 999999999999L));
        UnitOpenForm form = validForm();
        form.setPerRatio(new BigDecimal("0.121"));

        try {
            service.openUnit(form);
            fail("Expected BusinessException");
        } catch (BusinessException ex) {
            assertEquals("个人比例必须在0.050到0.120之间", ex.getMessage());
        }
    }

    @Test
    public void openUnitRejectsSequenceGreaterThanMaxSeq() {
        UnitServiceImpl service = new UnitServiceImpl(new FakeUnitMapper(), mapperWithSeq(10L, 9L));

        try {
            service.openUnit(validForm());
            fail("Expected BusinessException");
        } catch (BusinessException ex) {
            assertEquals("单位账号序号已超过最大值", ex.getMessage());
        }
    }

    @Test
    public void openUnitIncrementsUnitAccountSequenceAfterSuccess() {
        FakeParamMapper paramMapper = mapperWithSeq(7L, 999999999999L);
        UnitServiceImpl service = new UnitServiceImpl(new FakeUnitMapper(), paramMapper);

        UnitOpenResult result = service.openUnit(validForm());

        assertEquals("000000000007", result.getUnitAccNum());
        assertEquals(Long.valueOf(8L), paramMapper.selectBySeqname("UNITACCNUM").getSeq());
    }

    @Test
    public void queryUnitsByAccountReturnsSingleResult() {
        FakeUnitMapper unitMapper = new FakeUnitMapper();
        unitMapper.insert(buildQueryUnit("000000000010", "测试单位"));
        UnitServiceImpl service = new UnitServiceImpl(unitMapper, mapperWithSeq(1L, 999999999999L));
        UnitQueryForm form = new UnitQueryForm();
        form.setUnitAccNum("000000000010");
        form.setUnitName("忽略名称");

        List<UnitQueryResult> results = service.queryUnits(form);

        assertEquals(1, results.size());
        UnitQueryResult result = results.get(0);
        assertEquals("测试单位", result.getUnitName());
        assertEquals("000000000010", result.getUnitAccNum());
        assertEquals("测试地址", result.getUnitAddr());
        assertEquals("张三", result.getAgentName());
        assertEquals("0551-12345678", result.getPhone());
        assertEquals(new BigDecimal("1000.00"), result.getBalance());
        assertEquals(new BigDecimal("0.080"), result.getUnitRatio());
        assertEquals(new BigDecimal("0.070"), result.getPerRatio());
        assertEquals(new BigDecimal("0.150"), result.getTotalRatio());
        assertEquals("2026-05", result.getLastPayMonth());
        assertEquals(new BigDecimal("800.00"), result.getUnitMonthPay());
        assertEquals(new BigDecimal("700.00"), result.getPerMonthPay());
        assertEquals(new BigDecimal("1500.00"), result.getTotalMonthPay());
        assertEquals(Integer.valueOf(5), result.getPersNum());
        assertEquals("正常", result.getAccStateText());
    }

    @Test
    public void queryUnitsByNameLikeReturnsMultipleResults() {
        FakeUnitMapper unitMapper = new FakeUnitMapper();
        unitMapper.insert(buildQueryUnit("000000000010", "测试单位一"));
        unitMapper.insert(buildQueryUnit("000000000011", "测试单位二"));
        unitMapper.insert(buildQueryUnit("000000000012", "其他单位"));
        UnitServiceImpl service = new UnitServiceImpl(unitMapper, mapperWithSeq(1L, 999999999999L));
        UnitQueryForm form = new UnitQueryForm();
        form.setUnitName("测试");

        List<UnitQueryResult> results = service.queryUnits(form);

        assertEquals(2, results.size());
        assertEquals("000000000010", results.get(0).getUnitAccNum());
        assertEquals("000000000011", results.get(1).getUnitAccNum());
    }

    @Test
    public void queryUnitsRejectsEmptyCondition() {
        UnitServiceImpl service = new UnitServiceImpl(new FakeUnitMapper(), mapperWithSeq(1L, 999999999999L));

        try {
            service.queryUnits(new UnitQueryForm());
            fail("Expected BusinessException");
        } catch (BusinessException ex) {
            assertEquals("请输入单位账号或单位名称", ex.getMessage());
        }
    }

    @Test
    public void queryUnitsReturnsEmptyListWhenNotFound() {
        UnitServiceImpl service = new UnitServiceImpl(new FakeUnitMapper(), mapperWithSeq(1L, 999999999999L));
        UnitQueryForm form = new UnitQueryForm();
        form.setUnitAccNum("000000009999");

        List<UnitQueryResult> results = service.queryUnits(form);

        assertTrue(results.isEmpty());
    }

    private static FakeParamMapper mapperWithSeq(Long seq, Long maxseq) {
        FakeParamMapper mapper = new FakeParamMapper();
        mapper.insert(buildParam(seq, maxseq));
        return mapper;
    }

    private static SystemParam buildParam(Long seq, Long maxseq) {
        SystemParam param = new SystemParam();
        param.setSeqname("UNITACCNUM");
        param.setSeq(seq);
        param.setMaxseq(maxseq);
        param.setSeqDesc("单位账号序号");
        return param;
    }

    private static UnitOpenForm validForm() {
        UnitOpenForm form = new UnitOpenForm();
        form.setUnitName("测试单位");
        form.setUnitAddr("测试地址");
        form.setOrgCode("A12345678");
        form.setUnitKind("1");
        form.setUnitType("150");
        form.setSalaryDate("15");
        form.setPhone("0551-12345678");
        form.setAgentName("张三");
        form.setAgentIdCard("11010519491231002X");
        form.setUnitRatio(new BigDecimal("0.080"));
        form.setPerRatio(new BigDecimal("0.080"));
        form.setRemark("测试备注");
        return form;
    }

    private static UnitBasicInfo buildQueryUnit(String unitAccNum, String unitName) {
        UnitBasicInfo unit = new UnitBasicInfo();
        unit.setUnitAccNum(unitAccNum);
        unit.setUnitName(unitName);
        unit.setUnitAddr("测试地址");
        unit.setOrgCode("A12345678");
        unit.setPhone("0551-12345678");
        unit.setAgentName("张三");
        unit.setUnitRatio(new BigDecimal("0.080"));
        unit.setPerRatio(new BigDecimal("0.070"));
        unit.setAccState("0");
        unit.setBalance(new BigDecimal("1000.00"));
        unit.setBaseNumber(new BigDecimal("10000.00"));
        unit.setUnitPaySum(new BigDecimal("800.00"));
        unit.setPerPaySum(new BigDecimal("700.00"));
        unit.setPersNum(5);
        unit.setLastPayDate(LocalDate.of(2026, 5, 1));
        unit.setCreateDate(LocalDate.of(2026, 1, 1));
        return unit;
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
            for (UnitBasicInfo unit : data.values()) {
                if (orgCode.equals(unit.getOrgCode()) && "0".equals(unit.getAccState())) {
                    return unit;
                }
            }
            return null;
        }

        @Override
        public List<UnitBasicInfo> selectByUnitNameLike(String unitName) {
            List<UnitBasicInfo> results = new ArrayList<>();
            for (UnitBasicInfo unit : data.values()) {
                if (unit.getUnitName() != null && unit.getUnitName().contains(unitName)) {
                    results.add(unit);
                }
            }
            return results;
        }

        @Override
        public int insert(UnitBasicInfo unit) {
            data.put(unit.getUnitAccNum(), unit);
            return 1;
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
}
