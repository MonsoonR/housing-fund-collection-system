package com.housingfund.collection.mapper;

import com.housingfund.collection.entity.PersonBasicInfo;
import org.apache.ibatis.annotations.Param;

public interface PersonMapper {

    PersonBasicInfo selectByPerAccNum(@Param("perAccNum") String perAccNum);

    PersonBasicInfo selectByIdCard(@Param("idCard") String idCard);

    PersonBasicInfo selectNormalByIdCard(@Param("idCard") String idCard);

    int insert(PersonBasicInfo person);

    int reactivate(PersonBasicInfo person);
}
