package com.housingfund.collection.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PersonQueryResult implements Serializable {

    private String unitName;
    private String unitAccNum;
    private String perName;
    private String perAccNum;
    private String idCard;
    private BigDecimal perBalance;
    private LocalDateTime createTime;
    private LocalDate lastPayDate;
    private String lastPayMonth;
    private BigDecimal unitRatio;
    private BigDecimal perRatio;
    private BigDecimal totalRatio;
    private BigDecimal unitMonthPay;
    private BigDecimal perMonthPay;
    private BigDecimal totalMonthPay;
    private String status;
    private String statusText;

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

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

    public String getPerAccNum() {
        return perAccNum;
    }

    public void setPerAccNum(String perAccNum) {
        this.perAccNum = perAccNum;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public BigDecimal getPerBalance() {
        return perBalance;
    }

    public void setPerBalance(BigDecimal perBalance) {
        this.perBalance = perBalance;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDate getLastPayDate() {
        return lastPayDate;
    }

    public void setLastPayDate(LocalDate lastPayDate) {
        this.lastPayDate = lastPayDate;
    }

    public String getLastPayMonth() {
        return lastPayMonth;
    }

    public void setLastPayMonth(String lastPayMonth) {
        this.lastPayMonth = lastPayMonth;
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

    public BigDecimal getTotalRatio() {
        return totalRatio;
    }

    public void setTotalRatio(BigDecimal totalRatio) {
        this.totalRatio = totalRatio;
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

    public BigDecimal getTotalMonthPay() {
        return totalMonthPay;
    }

    public void setTotalMonthPay(BigDecimal totalMonthPay) {
        this.totalMonthPay = totalMonthPay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }
}
