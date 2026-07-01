package com.housingfund.collection.mapper;

import com.housingfund.collection.entity.PersonBasicInfo;
import com.housingfund.collection.vo.PersonEditForm;
import com.housingfund.collection.vo.PersonIdConflictResult;
import com.housingfund.collection.vo.PersonQueryResult;
import org.apache.ibatis.annotations.Param;

public interface PersonMapper {

    PersonBasicInfo selectByPerAccNum(@Param("perAccNum") String perAccNum);

    PersonBasicInfo selectByIdCard(@Param("idCard") String idCard);

    PersonBasicInfo selectNormalByIdCard(@Param("idCard") String idCard);

    PersonQueryResult selectQueryByPerAccNum(@Param("perAccNum") String perAccNum);

    PersonQueryResult selectQueryByIdCard(@Param("idCard") String idCard);

    PersonEditForm selectEditableByPerAccNum(@Param("perAccNum") String perAccNum);

    PersonIdConflictResult selectIdCardConflict(@Param("idCard") String idCard,
                                                @Param("excludePerAccNum") String excludePerAccNum);

    int insert(PersonBasicInfo person);

    int reactivate(PersonBasicInfo person);

    int updateEditableFields(PersonBasicInfo person);

    int updateIdCard(@Param("perAccNum") String perAccNum, @Param("idCard") String idCard);
}
