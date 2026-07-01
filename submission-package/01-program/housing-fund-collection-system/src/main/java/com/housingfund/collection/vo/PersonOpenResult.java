package com.housingfund.collection.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class PersonOpenResult implements Serializable {

    private String perAccNum;
    private String perName;
    private String idCard;
    private String unitAccNum;
    private String unitName;
    private BigDecimal baseNum;
    private BigDecimal unitRatio;
    private BigDecimal perRatio;
    private BigDecimal unitMonthPay;
    private BigDecimal perMonthPay;
    private LocalDate createTime;

    public String getPerAccNum() {
        return perAccNum;
    }

    public void setPerAccNum(String perAccNum) {
        this.perAccNum = perAccNum;
    }

    public String getPerName() {
        return perName;
    }

    public void setPerName(String perName) {
        this.perName = perName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

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

    public BigDecimal getBaseNum() {
        return baseNum;
    }

    public void setBaseNum(BigDecimal baseNum) {
        this.baseNum = baseNum;
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

    public BigDecimal getUnitMonthPay() {
        return unitMonthPay;
    }

    public void setUnitMonthPay(BigDecimal unitMonthPay) {
        this.unitMonthPay = unitMonthPay;
    }

    public BigDecimal getPerMonthPay() {
        return perMonthPay;
    }

    public void setPerMonthPay(BigDecimal perMonthPay) {
        this.perMonthPay = perMonthPay;
    }

    public LocalDate getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDate createTime) {
        this.createTime = createTime;
    }
}
