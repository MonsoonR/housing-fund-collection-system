package com.housingfund.collection.service;

import com.housingfund.collection.entity.UnitBasicInfo;
import com.housingfund.collection.vo.UnitEditForm;
import com.housingfund.collection.vo.UnitEditResult;
import com.housingfund.collection.vo.UnitOpenForm;
import com.housingfund.collection.vo.UnitOpenResult;
import com.housingfund.collection.vo.UnitQueryForm;
import com.housingfund.collection.vo.UnitQueryResult;

import java.util.List;

public interface UnitService {

    UnitOpenResult openUnit(UnitOpenForm form);

    List<UnitQueryResult> queryUnits(UnitQueryForm form);

    UnitBasicInfo getEditableUnit(String unitAccNum);

    UnitEditResult updateUnit(UnitEditForm form);
}
