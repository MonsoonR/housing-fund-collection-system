package com.housingfund.collection.service;

import com.housingfund.collection.vo.PersonOpenForm;
import com.housingfund.collection.vo.PersonOpenResult;

public interface PersonService {

    PersonOpenResult openPerson(PersonOpenForm form);
}
