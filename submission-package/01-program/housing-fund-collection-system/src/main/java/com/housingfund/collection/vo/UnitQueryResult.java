package com.housingfund.collection.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class UnitQueryResult implements Serializable {

    private String unitName;
    private String unitAccNum;
    private String unitAddr;
    private String agentName;
    private String phone;
    private BigDecimal balance;
    private BigDecimal unitRatio;
    private BigDecimal perRatio;
    private BigDecimal totalRatio;
    private String lastPayMonth;
    private BigDecimal unitMonthPay;
    private BigDecimal perMonthPay;
    private BigDecimal totalMonthPay;
    private Integer persNum;
    private String accState;
    private String accStateText;

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

    public String getUnitAddr() {
        return unitAddr;
    }

    public void setUnitAddr(String unitAddr) {
        this.unitAddr = unitAddr;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
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

    public String getLastPayMonth() {
        return lastPayMonth;
    }

    public void setLastPayMonth(String lastPayMonth) {
        this.lastPayMonth = lastPayMonth;
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

    public Integer getPersNum() {
        return persNum;
    }

    public void setPersNum(Integer persNum) {
        this.persNum = persNum;
    }

    public String getAccState() {
        return accState;
    }

    public void setAccState(String accState) {
        this.accState = accState;
    }

    public String getAccStateText() {
        return accStateText;
    }

    public void setAccStateText(String accStateText) {
        this.accStateText = accStateText;
    }
}
