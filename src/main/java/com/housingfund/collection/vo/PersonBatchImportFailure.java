package com.housingfund.collection.vo;

import java.io.Serializable;

public class PersonBatchImportFailure implements Serializable {

    private int rowNumber;
    private String idCard;
    private String perName;
    private String message;

    public PersonBatchImportFailure() {
    }

    public PersonBatchImportFailure(int rowNumber, String idCard, String perName, String message) {
        this.rowNumber = rowNumber;
        this.idCard = idCard;
        this.perName = perName;
        this.message = message;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPerName() {
        return perName;
    }

    public void setPerName(String perName) {
        this.perName = perName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
