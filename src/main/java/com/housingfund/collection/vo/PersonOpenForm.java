package com.housingfund.collection.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class PersonOpenForm implements Serializable {

    private String unitAccNum;
    private String unitName;
    private BigDecimal unitRatio;
    private BigDecimal perRatio;
    private String perName;
    private String idType;
    private String idCard;
    private BigDecimal baseNum;

    public String getUnitAccNum() {
        return unitAccNum;
    }

    public void setUnitAccNum(String unitAccNum) {
        this.unitAccNum = unitAccNum;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public BigDecimal getUnitRatio() {
        return unitRatio;
    }

    public void setUnitRatio(BigDecimal unitRatio) {
        this.unitRatio = unitRatio;
    }

    public BigDecimal getPerRatio() {
        return perRatio;
    }

    public void setPerRatio(BigDecimal perRatio) {
        this.perRatio = perRatio;
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

}
