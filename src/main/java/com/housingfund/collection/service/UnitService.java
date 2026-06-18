package com.housingfund.collection.service;

import com.housingfund.collection.vo.UnitOpenForm;
import com.housingfund.collection.vo.UnitOpenResult;
import com.housingfund.collection.vo.UnitQueryForm;
import com.housingfund.collection.vo.UnitQueryResult;

import java.util.List;

public interface UnitService {

    UnitOpenResult openUnit(UnitOpenForm form);

    List<UnitQueryResult> queryUnits(UnitQueryForm form);
}
