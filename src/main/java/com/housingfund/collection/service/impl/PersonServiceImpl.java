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
import com.housingfund.collection.vo.PersonOpenForm;
import com.housingfund.collection.vo.PersonOpenResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
public class PersonServiceImpl implements PersonService {

    private static final String PERSON_SEQUENCE_NAME = "PERACCNUM";
    private static final String ID_TYPE_RESIDENT = "居民身份证";
    private static final String STATUS_NORMAL = "0";
    private static final String STATUS_CLOSED = "9";

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
            throw new BusinessException("证件类型目前只支持居民身份证");
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
}
