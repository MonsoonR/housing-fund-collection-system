package com.housingfund.collection.service;

import com.housingfund.collection.vo.PersonOpenForm;
import com.housingfund.collection.vo.PersonOpenResult;
import com.housingfund.collection.vo.PersonBatchImportResult;
import com.housingfund.collection.vo.PersonEditForm;
import com.housingfund.collection.vo.PersonEditResult;
import com.housingfund.collection.vo.PersonIdConflictResult;
import com.housingfund.collection.vo.PersonQueryForm;
import com.housingfund.collection.vo.PersonQueryResult;

import java.io.InputStream;

public interface PersonService {

    PersonOpenForm getOpenUnitInfo(String unitAccNum);

    PersonOpenResult openPerson(PersonOpenForm form);

    PersonBatchImportResult importPersons(InputStream inputStream, String originalFilename, String unitAccNum);

    PersonQueryResult queryPerson(PersonQueryForm form);

    PersonEditForm getEditablePerson(String perAccNum);

    PersonIdConflictResult checkIdCardConflict(PersonEditForm form);

    PersonEditResult updatePerson(PersonEditForm form);

    PersonEditResult forceUpdatePerson(PersonEditForm form);
}
