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
import com.housingfund.collection.vo.UnitEditForm;
import com.housingfund.collection.vo.UnitEditResult;
import com.housingfund.collection.vo.UnitOpenForm;
import com.housingfund.collection.vo.UnitOpenResult;
import com.housingfund.collection.vo.UnitQueryForm;
import com.housingfund.collection.vo.UnitQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class UnitServiceImpl implements UnitService {

    private static final String UNIT_SEQUENCE_NAME = "UNITACCNUM";
    private static final DateTimeFormatter PAY_MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");
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

    @Override
    public List<UnitQueryResult> queryUnits(UnitQueryForm form) {
        validateQuery(form);

        List<UnitBasicInfo> units = new ArrayList<>();
        if (form.getUnitAccNum() != null) {
            UnitBasicInfo unit = unitMapper.selectByUnitAccNum(form.getUnitAccNum());
            if (unit != null) {
                units.add(unit);
            }
        } else {
            units.addAll(unitMapper.selectByUnitNameLike(form.getUnitName()));
        }

        List<UnitQueryResult> results = new ArrayList<>();
        for (UnitBasicInfo unit : units) {
            results.add(buildQueryResult(unit));
        }
        return results;
    }

    @Override
    public UnitBasicInfo getEditableUnit(String unitAccNum) {
        String normalizedUnitAccNum = validateUnitAccNum(unitAccNum);
        UnitBasicInfo unit = unitMapper.selectByUnitAccNum(normalizedUnitAccNum);
        ensureEditable(unit);
        return unit;
    }

    @Override
    @Transactional
    public UnitEditResult updateUnit(UnitEditForm form) {
        validateEdit(form);

        UnitBasicInfo existing = unitMapper.selectByUnitAccNum(form.getUnitAccNum());
        ensureEditable(existing);
        if (!hasEditableChanges(existing, form)) {
            throw new BusinessException("请至少修改一项单位资料");
        }
        UnitBasicInfo duplicate = unitMapper.selectDuplicateOrgCodeAndUnitName(
                form.getOrgCode(), form.getUnitName(), form.getUnitAccNum());
        if (duplicate != null) {
            throw new BusinessException("修改后的组织机构代码和单位名称已被其他单位占用");
        }

        UnitBasicInfo update = buildEditableUpdate(form);
        int updated = unitMapper.updateEditableFields(update);
        if (updated == 0) {
            throw new BusinessException("单位资料修改失败");
        }
        return buildEditResult(update);
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

    private UnitBasicInfo buildEditableUpdate(UnitEditForm form) {
        UnitBasicInfo unit = new UnitBasicInfo();
        unit.setUnitAccNum(form.getUnitAccNum());
        unit.setUnitName(form.getUnitName());
        unit.setUnitAddr(form.getUnitAddr());
        unit.setOrgCode(form.getOrgCode());
        unit.setUnitKind(form.getUnitKind());
        unit.setUnitType(form.getUnitType());
        unit.setSalaryDate(form.getSalaryDate());
        unit.setPhone(form.getPhone());
        unit.setAgentName(form.getAgentName());
        unit.setAgentIdCard(form.getAgentIdCard());
        unit.setRemark(form.getRemark());
        return unit;
    }

    private UnitEditResult buildEditResult(UnitBasicInfo unit) {
        UnitEditResult result = new UnitEditResult();
        result.setUnitAccNum(unit.getUnitAccNum());
        result.setUnitName(unit.getUnitName());
        result.setOrgCode(unit.getOrgCode());
        result.setPhone(unit.getPhone());
        result.setAgentName(unit.getAgentName());
        result.setResultMessage("修改成功");
        result.setUpdateTime(LocalDateTime.now());
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

    private void validateEdit(UnitEditForm form) {
        if (form == null) {
            throw new BusinessException("单位资料修改信息不能为空");
        }
        form.setUnitAccNum(validateUnitAccNum(form.getUnitAccNum()));
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
        form.setRemark(trimToNull(form.getRemark()));
    }

    private void validateQuery(UnitQueryForm form) {
        if (form == null) {
            throw new BusinessException("请输入单位账号或单位名称");
        }
        form.setUnitAccNum(trimToNull(form.getUnitAccNum()));
        form.setUnitName(trimToNull(form.getUnitName()));
        if (form.getUnitAccNum() == null && form.getUnitName() == null) {
            throw new BusinessException("请输入单位账号或单位名称");
        }
        if (form.getUnitAccNum() != null && !AccountNumberUtil.isValidAccountNumber(form.getUnitAccNum())) {
            throw new BusinessException("单位账号长度必须为12位");
        }
    }

    private String validateUnitAccNum(String unitAccNum) {
        String normalizedUnitAccNum = requireText(unitAccNum, "单位账号不能为空");
        if (!AccountNumberUtil.isValidAccountNumber(normalizedUnitAccNum)) {
            throw new BusinessException("单位账号长度必须为12位");
        }
        return normalizedUnitAccNum;
    }

    private void ensureEditable(UnitBasicInfo unit) {
        if (unit == null) {
            throw new BusinessException("单位账号不存在");
        }
        if ("9".equals(unit.getAccState())) {
            throw new BusinessException("已销户单位不能修改");
        }
    }

    private boolean hasEditableChanges(UnitBasicInfo existing, UnitEditForm form) {
        return !same(existing.getUnitName(), form.getUnitName())
                || !same(existing.getUnitAddr(), form.getUnitAddr())
                || !same(existing.getOrgCode(), form.getOrgCode())
                || !same(existing.getUnitKind(), form.getUnitKind())
                || !same(existing.getUnitType(), form.getUnitType())
                || !same(existing.getSalaryDate(), form.getSalaryDate())
                || !same(existing.getPhone(), form.getPhone())
                || !same(existing.getAgentName(), form.getAgentName())
                || !same(existing.getAgentIdCard(), form.getAgentIdCard())
                || !same(existing.getRemark(), form.getRemark());
    }

    private boolean same(String left, String right) {
        return Objects.equals(trimToNull(left), trimToNull(right));
    }

    private UnitQueryResult buildQueryResult(UnitBasicInfo unit) {
        UnitQueryResult result = new UnitQueryResult();
        result.setUnitName(unit.getUnitName());
        result.setUnitAccNum(unit.getUnitAccNum());
        result.setUnitAddr(unit.getUnitAddr());
        result.setAgentName(unit.getAgentName());
        result.setPhone(unit.getPhone());
        result.setBalance(zeroIfNull(unit.getBalance()));
        result.setUnitRatio(zeroIfNull(unit.getUnitRatio()));
        result.setPerRatio(zeroIfNull(unit.getPerRatio()));
        result.setTotalRatio(result.getUnitRatio().add(result.getPerRatio()));
        result.setLastPayMonth(formatPayMonth(unit.getLastPayDate()));
        result.setUnitMonthPay(zeroIfNull(unit.getUnitPaySum()));
        result.setPerMonthPay(zeroIfNull(unit.getPerPaySum()));
        result.setTotalMonthPay(result.getUnitMonthPay().add(result.getPerMonthPay()));
        result.setPersNum(unit.getPersNum());
        result.setAccState(unit.getAccState());
        result.setAccStateText(unitStateText(unit.getAccState()));
        return result;
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

    private BigDecimal zeroIfNull(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private String formatPayMonth(LocalDate value) {
        return value == null ? "" : value.format(PAY_MONTH_FORMATTER);
    }

    private String unitStateText(String accState) {
        if ("0".equals(accState)) {
            return "正常";
        }
        return "非正常";
    }
}
