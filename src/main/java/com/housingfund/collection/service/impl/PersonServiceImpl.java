package com.housingfund.collection.service.impl;

import com.housingfund.collection.entity.PersonBasicInfo;
import com.housingfund.collection.entity.SystemParam;
import com.housingfund.collection.entity.UnitBasicInfo;
import com.housingfund.collection.exception.BusinessException;
import com.housingfund.collection.mapper.ParamMapper;
import com.housingfund.collection.mapper.PersonMapper;
import com.housingfund.collection.mapper.UnitMapper;
import com.housingfund.collection.service.PersonService;
import com.housingfund.collection.util.AccountNumberUtil;
import com.housingfund.collection.util.IdCardUtil;
import com.housingfund.collection.vo.PersonEditForm;
import com.housingfund.collection.vo.PersonEditResult;
import com.housingfund.collection.vo.PersonIdConflictResult;
import com.housingfund.collection.vo.PersonOpenForm;
import com.housingfund.collection.vo.PersonOpenResult;
import com.housingfund.collection.vo.PersonQueryForm;
import com.housingfund.collection.vo.PersonQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Service
public class PersonServiceImpl implements PersonService {

    private static final String PERSON_SEQUENCE_NAME = "PERACCNUM";
    private static final String ID_TYPE_RESIDENT = "01身份证";
    private static final String STATUS_NORMAL = "0";
    private static final String STATUS_CLOSED = "9";
    private static final DateTimeFormatter PAY_MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    private final PersonMapper personMapper;
    private final UnitMapper unitMapper;
    private final ParamMapper paramMapper;

    @Autowired
    public PersonServiceImpl(PersonMapper personMapper, UnitMapper unitMapper, ParamMapper paramMapper) {
        this.personMapper = personMapper;
        this.unitMapper = unitMapper;
        this.paramMapper = paramMapper;
    }

    @Override
    public PersonOpenForm getOpenUnitInfo(String unitAccNum) {
        String normalizedUnitAccNum = validateUnitAccNum(unitAccNum);
        UnitBasicInfo unit = unitMapper.selectNormalByUnitAccNum(normalizedUnitAccNum);
        if (unit == null) {
            throw new BusinessException("单位账号不存在或状态非正常");
        }
        PersonOpenForm form = new PersonOpenForm();
        form.setUnitAccNum(unit.getUnitAccNum());
        form.setUnitName(unit.getUnitName());
        form.setUnitRatio(unit.getUnitRatio());
        form.setPerRatio(unit.getPerRatio());
        form.setIdType(ID_TYPE_RESIDENT);
        return form;
    }

    @Override
    @Transactional
    public PersonOpenResult openPerson(PersonOpenForm form) {
        validate(form);

        UnitBasicInfo unit = unitMapper.selectNormalByUnitAccNum(form.getUnitAccNum());
        if (unit == null) {
            throw new BusinessException("单位账号不存在或状态非正常");
        }

        if (personMapper.selectNormalByIdCard(form.getIdCard()) != null) {
            throw new BusinessException("该人员已开户");
        }

        PersonBasicInfo existing = personMapper.selectByIdCard(form.getIdCard());
        BigDecimal baseNum = form.getBaseNum().setScale(2, RoundingMode.HALF_UP);
        BigDecimal unitMonthPay = calculateMonthPay(baseNum, unit.getUnitRatio());
        BigDecimal perMonthPay = calculateMonthPay(baseNum, unit.getPerRatio());
        LocalDateTime createTime = LocalDateTime.now();

        PersonBasicInfo person;
        if (existing == null) {
            person = insertNewPerson(form, unit, baseNum, unitMonthPay, perMonthPay, createTime);
        } else if (STATUS_CLOSED.equals(existing.getStatus())) {
            person = reactivateClosedPerson(existing.getPerAccNum(), form, unit, baseNum,
                    unitMonthPay, perMonthPay, createTime);
        } else {
            throw new BusinessException("该人员已开户");
        }

        int summaryUpdated = unitMapper.updatePaymentSummary(
                unit.getUnitAccNum(), baseNum, unitMonthPay, perMonthPay);
        if (summaryUpdated == 0) {
            throw new BusinessException("单位汇总信息更新失败");
        }

        return buildResult(person, unit);
    }

    @Override
    public PersonQueryResult queryPerson(PersonQueryForm form) {
        validateQuery(form);

        PersonQueryResult result;
        if (form.getPerAccNum() != null) {
            result = personMapper.selectQueryByPerAccNum(form.getPerAccNum());
        } else {
            result = personMapper.selectQueryByIdCard(form.getIdCard());
        }
        if (result == null) {
            return null;
        }
        fillQueryComputedFields(result);
        return result;
    }

    @Override
    public PersonEditForm getEditablePerson(String perAccNum) {
        String normalizedPerAccNum = validatePersonAccNum(perAccNum);
        PersonEditForm form = personMapper.selectEditableByPerAccNum(normalizedPerAccNum);
        ensureEditable(form);
        form.setStatusText(personStateText(form.getStatus()));
        return form;
    }

    @Override
    public PersonIdConflictResult checkIdCardConflict(PersonEditForm form) {
        validateEditFields(form);
        PersonBasicInfo existing = personMapper.selectByPerAccNum(form.getPerAccNum());
        ensureEditable(existing);
        return findConflict(form, existing);
    }

    @Override
    @Transactional
    public PersonEditResult updatePerson(PersonEditForm form) {
        validateEditFields(form);

        PersonBasicInfo existing = personMapper.selectByPerAccNum(form.getPerAccNum());
        ensureEditable(existing);
        if (!hasEditableChanges(existing, form)) {
            throw new BusinessException("请至少修改一项个人资料");
        }

        PersonIdConflictResult conflict = findConflict(form, existing);
        if (conflict != null) {
            PersonEditResult result = buildEditResult(form, existing, false,
                    conflict.getOccupiedPerAccNum(), conflict.getOccupiedIdCard(),
                    conflict.getGeneratedWrongIdCard());
            result.setConflictResult(conflict);
            result.setResultMessage("该身份证号已被其他个人账户占用，是否强制变更？");
            return result;
        }

        int updated = personMapper.updateEditableFields(buildEditableUpdate(form));
        if (updated == 0) {
            throw new BusinessException("个人资料修改失败");
        }
        return buildEditResult(form, existing, false, null, null, null);
    }

    @Override
    @Transactional
    public PersonEditResult forceUpdatePerson(PersonEditForm form) {
        validateEditFields(form);

        PersonBasicInfo existing = personMapper.selectByPerAccNum(form.getPerAccNum());
        ensureEditable(existing);
        if (!hasEditableChanges(existing, form)) {
            throw new BusinessException("请至少修改一项个人资料");
        }

        PersonIdConflictResult conflict = findConflict(form, existing);
        if (conflict == null) {
            int updated = personMapper.updateEditableFields(buildEditableUpdate(form));
            if (updated == 0) {
                throw new BusinessException("个人资料修改失败");
            }
            return buildEditResult(form, existing, false, null, null, null);
        }

        String wrongIdCard = conflict.getGeneratedWrongIdCard();
        if (personMapper.selectByIdCard(wrongIdCard) != null) {
            throw new BusinessException("强制变更生成的错误身份证号已存在");
        }

        int occupiedUpdated = personMapper.updateIdCard(conflict.getOccupiedPerAccNum(), wrongIdCard);
        if (occupiedUpdated == 0) {
            throw new BusinessException("占用账户身份证号强制变更失败");
        }
        int currentUpdated = personMapper.updateEditableFields(buildEditableUpdate(form));
        if (currentUpdated == 0) {
            throw new BusinessException("个人资料修改失败");
        }

        return buildEditResult(form, existing, true, conflict.getOccupiedPerAccNum(),
                conflict.getOccupiedIdCard(), wrongIdCard);
    }

    private PersonBasicInfo insertNewPerson(PersonOpenForm form, UnitBasicInfo unit, BigDecimal baseNum,
                                           BigDecimal unitMonthPay, BigDecimal perMonthPay,
                                           LocalDateTime createTime) {
        SystemParam sequence = paramMapper.selectBySeqnameForUpdate(PERSON_SEQUENCE_NAME);
        if (sequence == null) {
            throw new BusinessException("个人账号序号参数不存在");
        }
        if (sequence.getSeq() == null || sequence.getMaxseq() == null) {
            throw new BusinessException("个人账号序号参数不完整");
        }
        if (sequence.getSeq() > sequence.getMaxseq()) {
            throw new BusinessException("个人账号序号已超过最大值");
        }

        String perAccNum = AccountNumberUtil.formatAccountNumber(sequence.getSeq());
        PersonBasicInfo person = buildPerson(perAccNum, form, unit, baseNum, unitMonthPay, perMonthPay, createTime);
        int inserted = personMapper.insert(person);
        if (inserted == 0) {
            throw new BusinessException("个人开户失败");
        }
        int seqUpdated = paramMapper.updateSeq(PERSON_SEQUENCE_NAME, sequence.getSeq() + 1);
        if (seqUpdated == 0) {
            throw new BusinessException("个人账号序号更新失败");
        }
        return person;
    }

    private PersonBasicInfo reactivateClosedPerson(String perAccNum, PersonOpenForm form, UnitBasicInfo unit,
                                                   BigDecimal baseNum, BigDecimal unitMonthPay,
                                                   BigDecimal perMonthPay, LocalDateTime createTime) {
        PersonBasicInfo person = buildPerson(perAccNum, form, unit, baseNum, unitMonthPay, perMonthPay, createTime);
        int updated = personMapper.reactivate(person);
        if (updated == 0) {
            throw new BusinessException("销户账户重新启用失败");
        }
        return person;
    }

    private PersonBasicInfo buildPerson(String perAccNum, PersonOpenForm form, UnitBasicInfo unit,
                                        BigDecimal baseNum, BigDecimal unitMonthPay,
                                        BigDecimal perMonthPay, LocalDateTime createTime) {
        PersonBasicInfo person = new PersonBasicInfo();
        person.setPerAccNum(perAccNum);
        person.setUnitAccNum(unit.getUnitAccNum());
        person.setPerName(form.getPerName());
        person.setIdType(form.getIdType());
        person.setIdCard(form.getIdCard());
        person.setPhone(form.getPhone());
        person.setAddress(form.getAddress());
        person.setBaseNum(baseNum);
        person.setUnitRatio(unit.getUnitRatio());
        person.setPerRatio(unit.getPerRatio());
        person.setUnitMonthPay(unitMonthPay);
        person.setPerMonthPay(perMonthPay);
        person.setPerBalance(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
        person.setStatus(STATUS_NORMAL);
        person.setCreateTime(createTime);
        person.setUpdateTime(createTime);
        return person;
    }

    private PersonOpenResult buildResult(PersonBasicInfo person, UnitBasicInfo unit) {
        PersonOpenResult result = new PersonOpenResult();
        result.setPerAccNum(person.getPerAccNum());
        result.setPerName(person.getPerName());
        result.setIdCard(person.getIdCard());
        result.setUnitAccNum(person.getUnitAccNum());
        result.setUnitName(unit.getUnitName());
        result.setBaseNum(person.getBaseNum());
        result.setUnitRatio(person.getUnitRatio());
        result.setPerRatio(person.getPerRatio());
        result.setUnitMonthPay(person.getUnitMonthPay());
        result.setPerMonthPay(person.getPerMonthPay());
        result.setCreateTime(person.getCreateTime());
        return result;
    }

    private PersonBasicInfo buildEditableUpdate(PersonEditForm form) {
        PersonBasicInfo person = new PersonBasicInfo();
        person.setPerAccNum(form.getPerAccNum());
        person.setPerName(form.getPerName());
        person.setIdType(form.getIdType());
        person.setIdCard(form.getIdCard());
        return person;
    }

    private PersonEditResult buildEditResult(PersonEditForm form, PersonBasicInfo existing,
                                             boolean forceChanged, String conflictPerAccNum,
                                             String originalConflictIdCard, String changedConflictIdCard) {
        PersonEditResult result = new PersonEditResult();
        result.setPerAccNum(form.getPerAccNum());
        result.setPerName(form.getPerName());
        result.setIdCard(form.getIdCard());
        result.setUnitAccNum(existing.getUnitAccNum());
        result.setUnitName(findUnitName(existing.getUnitAccNum()));
        result.setForceChanged(forceChanged);
        result.setConflictPerAccNum(conflictPerAccNum);
        result.setOriginalConflictIdCard(originalConflictIdCard);
        result.setChangedConflictIdCard(changedConflictIdCard);
        result.setResultMessage("修改成功");
        result.setUpdateTime(LocalDateTime.now());
        return result;
    }

    private PersonIdConflictResult findConflict(PersonEditForm form, PersonBasicInfo existing) {
        PersonIdConflictResult conflict = personMapper.selectIdCardConflict(form.getIdCard(), form.getPerAccNum());
        if (conflict == null) {
            return null;
        }
        conflict.setCurrentPerAccNum(existing.getPerAccNum());
        conflict.setCurrentPerName(existing.getPerName());
        conflict.setNewPerName(form.getPerName());
        conflict.setNewIdCard(form.getIdCard());
        conflict.setOccupiedStatusText(personStateText(conflict.getOccupiedStatus()));
        conflict.setGeneratedWrongIdCard(generateWrongIdCard(conflict.getOccupiedIdCard()));
        return conflict;
    }

    private String generateWrongIdCard(String occupiedIdCard) {
        if (occupiedIdCard == null || occupiedIdCard.isEmpty()) {
            throw new BusinessException("占用账户身份证号不完整");
        }
        return "9" + occupiedIdCard.substring(1);
    }

    private String findUnitName(String unitAccNum) {
        UnitBasicInfo unit = unitMapper.selectByUnitAccNum(unitAccNum);
        return unit == null ? "" : unit.getUnitName();
    }

    private void validate(PersonOpenForm form) {
        if (form == null) {
            throw new BusinessException("个人开户信息不能为空");
        }
        form.setUnitAccNum(requireText(form.getUnitAccNum(), "单位账号不能为空"));
        if (!AccountNumberUtil.isValidAccountNumber(form.getUnitAccNum())) {
            throw new BusinessException("单位账号长度必须为12位");
        }
        form.setPerName(requireText(form.getPerName(), "个人姓名不能为空"));
        if (form.getPerName().matches("[\\u4e00-\\u9fa5]+") && form.getPerName().length() > 12) {
            throw new BusinessException("个人姓名不能超过12个汉字");
        }
        if (form.getPerName().length() > 50) {
            throw new BusinessException("个人姓名不能超过50个字符");
        }
        form.setIdType(requireText(form.getIdType(), "证件类型不能为空"));
        if (!ID_TYPE_RESIDENT.equals(form.getIdType())) {
            throw new BusinessException("证件类型目前只支持01身份证");
        }
        form.setIdCard(requireText(form.getIdCard(), "证件号码不能为空").toUpperCase());
        if (!IdCardUtil.isValid(form.getIdCard())) {
            throw new BusinessException("身份证号不正确");
        }
        if (form.getBaseNum() == null || form.getBaseNum().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("缴存基数必须大于0");
        }
        form.setPhone(trimToNull(form.getPhone()));
        if (form.getPhone() != null && form.getPhone().length() > 30) {
            throw new BusinessException("联系电话不能超过30个字符");
        }
        form.setAddress(trimToNull(form.getAddress()));
        if (form.getAddress() != null && form.getAddress().length() > 200) {
            throw new BusinessException("联系地址不能超过200个字符");
        }
    }

    private void validateEditFields(PersonEditForm form) {
        if (form == null) {
            throw new BusinessException("个人资料修改信息不能为空");
        }
        form.setPerAccNum(validatePersonAccNum(form.getPerAccNum()));
        form.setPerName(requireText(form.getPerName(), "个人姓名不能为空"));
        if (form.getPerName().matches("[\\u4e00-\\u9fa5]+") && form.getPerName().length() > 12) {
            throw new BusinessException("个人姓名不能超过12个汉字");
        }
        if (form.getPerName().length() > 50) {
            throw new BusinessException("个人姓名不能超过50个字符");
        }
        form.setIdType(requireText(form.getIdType(), "证件类型不能为空"));
        if (!ID_TYPE_RESIDENT.equals(form.getIdType())) {
            throw new BusinessException("证件类型目前只支持01身份证");
        }
        form.setIdCard(requireText(form.getIdCard(), "证件号码不能为空").toUpperCase());
        if (!IdCardUtil.isValid(form.getIdCard())) {
            throw new BusinessException("身份证号不正确");
        }
    }

    private void validateQuery(PersonQueryForm form) {
        if (form == null) {
            throw new BusinessException("请输入个人账号或身份证号");
        }
        form.setPerAccNum(trimToNull(form.getPerAccNum()));
        form.setIdCard(trimToNull(form.getIdCard()));
        if (form.getPerAccNum() == null && form.getIdCard() == null) {
            throw new BusinessException("请输入个人账号或身份证号");
        }
        if (form.getPerAccNum() != null && !AccountNumberUtil.isValidAccountNumber(form.getPerAccNum())) {
            throw new BusinessException("个人账号长度必须为12位");
        }
        if (form.getPerAccNum() == null && form.getIdCard() != null) {
            form.setIdCard(form.getIdCard().toUpperCase());
            if (!IdCardUtil.isValid(form.getIdCard())) {
                throw new BusinessException("身份证号不正确");
            }
        }
    }

    private String validatePersonAccNum(String perAccNum) {
        String normalizedPerAccNum = requireText(perAccNum, "个人账号不能为空");
        if (!AccountNumberUtil.isValidAccountNumber(normalizedPerAccNum)) {
            throw new BusinessException("个人账号长度必须为12位");
        }
        return normalizedPerAccNum;
    }

    private String validateUnitAccNum(String unitAccNum) {
        String normalizedUnitAccNum = requireText(unitAccNum, "单位账号不能为空");
        if (!AccountNumberUtil.isValidAccountNumber(normalizedUnitAccNum)) {
            throw new BusinessException("单位账号长度必须为12位");
        }
        return normalizedUnitAccNum;
    }

    private void ensureEditable(PersonEditForm form) {
        if (form == null) {
            throw new BusinessException("个人账号不存在");
        }
        if (STATUS_CLOSED.equals(form.getStatus())) {
            throw new BusinessException("已销户个人不能修改");
        }
    }

    private void ensureEditable(PersonBasicInfo person) {
        if (person == null) {
            throw new BusinessException("个人账号不存在");
        }
        if (STATUS_CLOSED.equals(person.getStatus())) {
            throw new BusinessException("已销户个人不能修改");
        }
    }

    private boolean hasEditableChanges(PersonBasicInfo existing, PersonEditForm form) {
        return !same(existing.getPerName(), form.getPerName())
                || !same(existing.getIdType(), form.getIdType())
                || !same(existing.getIdCard(), form.getIdCard());
    }

    private void fillQueryComputedFields(PersonQueryResult result) {
        result.setUnitRatio(zeroIfNull(result.getUnitRatio()));
        result.setPerRatio(zeroIfNull(result.getPerRatio()));
        result.setTotalRatio(result.getUnitRatio().add(result.getPerRatio()));
        result.setUnitMonthPay(zeroIfNull(result.getUnitMonthPay()));
        result.setPerMonthPay(zeroIfNull(result.getPerMonthPay()));
        result.setTotalMonthPay(result.getUnitMonthPay().add(result.getPerMonthPay()));
        result.setPerBalance(zeroIfNull(result.getPerBalance()));
        result.setLastPayMonth(result.getLastPayDate() == null ? "" : result.getLastPayDate().format(PAY_MONTH_FORMATTER));
        result.setStatusText(personStateText(result.getStatus()));
    }

    private BigDecimal calculateMonthPay(BigDecimal baseNum, BigDecimal ratio) {
        if (ratio == null) {
            throw new BusinessException("单位缴存比例参数不完整");
        }
        return baseNum.multiply(ratio).setScale(2, RoundingMode.HALF_UP);
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

    private boolean same(String left, String right) {
        return Objects.equals(trimToNull(left), trimToNull(right));
    }

    private BigDecimal zeroIfNull(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private String personStateText(String status) {
        if (STATUS_NORMAL.equals(status)) {
            return "正常";
        }
        if (STATUS_CLOSED.equals(status)) {
            return "销户";
        }
        return "未知";
    }
}
