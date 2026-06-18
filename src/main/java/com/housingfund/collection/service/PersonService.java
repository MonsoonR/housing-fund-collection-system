package com.housingfund.collection.service;

import com.housingfund.collection.vo.PersonOpenForm;
import com.housingfund.collection.vo.PersonOpenResult;
import com.housingfund.collection.vo.PersonQueryForm;
import com.housingfund.collection.vo.PersonQueryResult;

public interface PersonService {

    PersonOpenResult openPerson(PersonOpenForm form);

    PersonQueryResult queryPerson(PersonQueryForm form);
}
