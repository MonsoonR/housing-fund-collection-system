package com.housingfund.collection.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class PersonOpenForm implements Serializable {

    private String unitAccNum;
    private String perName;
    private String idType;
    private String idCard;
    private BigDecimal baseNum;
    private String phone;
    private String address;

    public String getUnitAccNum() {
        return unitAccNum;
    }

    public void setUnitAccNum(String unitAccNum) {
        this.unitAccNum = unitAccNum;
    }

    public String getPerName() {
        return perName;
    }

    public void setPerName(String perName) {
        this.perName = perName;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public BigDecimal getBaseNum() {
        return baseNum;
    }

    public void setBaseNum(BigDecimal baseNum) {
        this.baseNum = baseNum;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
