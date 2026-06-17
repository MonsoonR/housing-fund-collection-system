package com.housingfund.collection.mapper;

import com.housingfund.collection.entity.UnitBasicInfo;
import org.apache.ibatis.annotations.Param;

public interface UnitMapper {

    UnitBasicInfo selectByUnitAccNum(@Param("unitAccNum") String unitAccNum);

    UnitBasicInfo selectNormalByOrgCode(@Param("orgCode") String orgCode);

    int insert(UnitBasicInfo unit);
}
