package com.housingfund.collection.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PersonBatchImportResult implements Serializable {

    private int successCount;
    private final List<PersonBatchImportFailure> failures = new ArrayList<>();

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public int getFailureCount() {
        return failures.size();
    }

    public List<PersonBatchImportFailure> getFailures() {
        return Collections.unmodifiableList(failures);
    }

    public void addFailure(int rowNumber, String idCard, String perName, String message) {
        failures.add(new PersonBatchImportFailure(rowNumber, idCard, perName, message));
    }

    public boolean hasFailures() {
        return !failures.isEmpty();
    }
}
