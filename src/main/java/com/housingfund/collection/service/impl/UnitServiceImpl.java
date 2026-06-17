package com.housingfund.collection.service.impl;

import com.housingfund.collection.entity.SystemParam;
import com.housingfund.collection.entity.UnitBasicInfo;
import com.housingfund.collection.exception.BusinessException;
import com.housingfund.collection.mapper.ParamMapper;
import com.housingfund.collection.mapper.UnitMapper;
import com.housingfund.collection.service.UnitService;
import com.housingfund.collection.util.AccountNumberUtil;
import com.housingfund.collection.util.DateUtil;
import com.housingfund.collection.util.IdCardUtil;
import com.housingfund.collection.vo.UnitOpenForm;
import com.housingfund.collection.vo.UnitOpenResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Service
public class UnitServiceImpl implements UnitService {

    private static final String UNIT_SEQUENCE_NAME = "UNITACCNUM";
    private static final BigDecimal MIN_RATIO = new BigDecimal("0.050");
    private static final BigDecimal MAX_RATIO = new BigDecimal("0.120");
    private static final Set<String> UNIT_KINDS = Set.of("1", "2", "3", "4", "5");
    private static final Set<String> UNIT_TYPES = Set.of(
            "110", "120", "130", "140", "150", "160", "170", "190", "200", "300", "900");

    private final UnitMapper unitMapper;
    private final ParamMapper paramMapper;

    @Autowired
    public UnitServiceImpl(UnitMapper unitMapper, ParamMapper paramMapper) {
        this.unitMapper = unitMapper;
        this.paramMapper = paramMapper;
    }

    @Override
    @Transactional
    public UnitOpenResult openUnit(UnitOpenForm form) {
        validate(form);

        if (unitMapper.selectNormalByOrgCode(form.getOrgCode()) != null) {
            throw new BusinessException("该单位已建户");
        }

        SystemParam sequence = paramMapper.selectBySeqnameForUpdate(UNIT_SEQUENCE_NAME);
        if (sequence == null) {
            throw new BusinessException("单位账号序号参数不存在");
        }
        if (sequence.getSeq() == null || sequence.getMaxseq() == null) {
            throw new BusinessException("单位账号序号参数不完整");
        }
        if (sequence.getSeq() > sequence.getMaxseq()) {
            throw new BusinessException("单位账号序号已超过最大值");
        }

        String unitAccNum = AccountNumberUtil.formatAccountNumber(sequence.getSeq());
        LocalDate createDate = DateUtil.today();
        UnitBasicInfo unit = buildUnit(form, unitAccNum, createDate);
        int inserted = unitMapper.insert(unit);
        if (inserted == 0) {
            throw new BusinessException("单位开户失败");
        }
        int updated = paramMapper.updateSeq(UNIT_SEQUENCE_NAME, sequence.getSeq() + 1);
        if (updated == 0) {
            throw new BusinessException("单位账号序号更新失败");
        }

        return buildResult(unit);
    }

    private UnitBasicInfo buildUnit(UnitOpenForm form, String unitAccNum, LocalDate createDate) {
        UnitBasicInfo unit = new UnitBasicInfo();
        unit.setUnitAccNum(unitAccNum);
        unit.setUnitName(form.getUnitName());
        unit.setUnitAddr(form.getUnitAddr());
        unit.setOrgCode(form.getOrgCode());
        unit.setUnitKind(form.getUnitKind());
        unit.setUnitType(form.getUnitType());
        unit.setSalaryDate(form.getSalaryDate());
        unit.setPhone(form.getPhone());
        unit.setAgentName(form.getAgentName());
        unit.setAgentIdCard(form.getAgentIdCard());
        unit.setUnitRatio(form.getUnitRatio());
        unit.setPerRatio(form.getPerRatio());
        unit.setRemark(form.getRemark());
        unit.setAccState("0");
        unit.setBalance(BigDecimal.ZERO);
        unit.setBaseNumber(BigDecimal.ZERO);
        unit.setUnitPaySum(BigDecimal.ZERO);
        unit.setPerPaySum(BigDecimal.ZERO);
        unit.setPersNum(0);
        unit.setLastPayDate(DateUtil.defaultLastPayDate());
        unit.setInstCode("0110");
        unit.setOp("111111");
        unit.setCreateDate(createDate);
        return unit;
    }

    private UnitOpenResult buildResult(UnitBasicInfo unit) {
        UnitOpenResult result = new UnitOpenResult();
        result.setUnitAccNum(unit.getUnitAccNum());
        result.setUnitName(unit.getUnitName());
        result.setOrgCode(unit.getOrgCode());
        result.setCreateDate(unit.getCreateDate());
        result.setUnitRatio(unit.getUnitRatio());
        result.setPerRatio(unit.getPerRatio());
        return result;
    }

    private void validate(UnitOpenForm form) {
        if (form == null) {
            throw new BusinessException("单位开户信息不能为空");
        }
        form.setUnitName(requireText(form.getUnitName(), "单位名称不能为空"));
        if (form.getUnitName().length() > 50) {
            throw new BusinessException("单位名称不能超过50个字符");
        }
        form.setUnitAddr(requireText(form.getUnitAddr(), "单位地址不能为空"));
        form.setOrgCode(requireText(form.getOrgCode(), "组织机构代码不能为空"));
        if (form.getOrgCode().length() != 9) {
            throw new BusinessException("组织机构代码长度必须为9位");
        }
        form.setUnitKind(requireText(form.getUnitKind(), "单位类别不能为空"));
        if (!UNIT_KINDS.contains(form.getUnitKind())) {
            throw new BusinessException("单位类别取值不正确");
        }
        form.setUnitType(requireText(form.getUnitType(), "企业类型不能为空"));
        if (!UNIT_TYPES.contains(form.getUnitType())) {
            throw new BusinessException("企业类型取值不正确");
        }
        form.setSalaryDate(requireText(form.getSalaryDate(), "发薪日期不能为空"));
        if (!form.getSalaryDate().matches("0[1-9]|[12][0-9]|3[01]")) {
            throw new BusinessException("发薪日期必须在01到31之间");
        }
        form.setPhone(requireText(form.getPhone(), "联系电话不能为空"));
        form.setAgentName(requireText(form.getAgentName(), "单位经办人不能为空"));
        form.setAgentIdCard(requireText(form.getAgentIdCard(), "经办人身份证号码不能为空"));
        if (!IdCardUtil.isValid(form.getAgentIdCard())) {
            throw new BusinessException("经办人身份证号码不正确");
        }
        validateRatio(form.getUnitRatio(), "单位比例不能为空", "单位比例必须在0.050到0.120之间");
        validateRatio(form.getPerRatio(), "个人比例不能为空", "个人比例必须在0.050到0.120之间");
        form.setRemark(trimToNull(form.getRemark()));
    }

    private void validateRatio(BigDecimal value, String nullMessage, String rangeMessage) {
        if (value == null) {
            throw new BusinessException(nullMessage);
        }
        if (value.compareTo(MIN_RATIO) < 0 || value.compareTo(MAX_RATIO) > 0) {
            throw new BusinessException(rangeMessage);
        }
    }

    private String requireText(String value, String message) {
        String trimmed = trimToNull(value);
        if (trimmed == null) {
            throw new BusinessException(message);
        }
        return trimmed;
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
