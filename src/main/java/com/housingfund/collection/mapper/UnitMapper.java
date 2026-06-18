package com.housingfund.collection.mapper;

import com.housingfund.collection.entity.UnitBasicInfo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface UnitMapper {

    UnitBasicInfo selectByUnitAccNum(@Param("unitAccNum") String unitAccNum);

    UnitBasicInfo selectNormalByUnitAccNum(@Param("unitAccNum") String unitAccNum);

    UnitBasicInfo selectNormalByOrgCode(@Param("orgCode") String orgCode);

    int insert(UnitBasicInfo unit);

    int updatePaymentSummary(@Param("unitAccNum") String unitAccNum,
                             @Param("baseNum") BigDecimal baseNum,
                             @Param("unitMonthPay") BigDecimal unitMonthPay,
                             @Param("perMonthPay") BigDecimal perMonthPay);
}
