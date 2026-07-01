package com.housingfund.collection.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class UnitBasicInfo implements Serializable {

    private String unitAccNum;
    private String unitName;
    private String unitAddr;
    private String orgCode;
    private String unitKind;
    private String unitType;
    private String salaryDate;
    private String phone;
    private String agentName;
    private String agentIdCard;
    private BigDecimal unitRatio;
    private BigDecimal perRatio;
    private String remark;
    private String accState;
    private BigDecimal balance;
    private BigDecimal baseNumber;
    private BigDecimal unitPaySum;
    private BigDecimal perPaySum;
    private Integer persNum;
    private LocalDate lastPayDate;
    private String instCode;
    private String op;
    private LocalDate createDate;

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

    public String getUnitAddr() {
        return unitAddr;
    }

    public void setUnitAddr(String unitAddr) {
        this.unitAddr = unitAddr;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getUnitKind() {
        return unitKind;
    }

    public void setUnitKind(String unitKind) {
        this.unitKind = unitKind;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getSalaryDate() {
        return salaryDate;
    }

    public void setSalaryDate(String salaryDate) {
        this.salaryDate = salaryDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAgentIdCard() {
        return agentIdCard;
    }

    public void setAgentIdCard(String agentIdCard) {
        this.agentIdCard = agentIdCard;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAccState() {
        return accState;
    }

    public void setAccState(String accState) {
        this.accState = accState;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBaseNumber() {
        return baseNumber;
    }

    public void setBaseNumber(BigDecimal baseNumber) {
        this.baseNumber = baseNumber;
    }

    public BigDecimal getUnitPaySum() {
        return unitPaySum;
    }

    public void setUnitPaySum(BigDecimal unitPaySum) {
        this.unitPaySum = unitPaySum;
    }

    public BigDecimal getPerPaySum() {
        return perPaySum;
    }

    public void setPerPaySum(BigDecimal perPaySum) {
        this.perPaySum = perPaySum;
    }

    public Integer getPersNum() {
        return persNum;
    }

    public void setPersNum(Integer persNum) {
        this.persNum = persNum;
    }

    public LocalDate getLastPayDate() {
        return lastPayDate;
    }

    public void setLastPayDate(LocalDate lastPayDate) {
        this.lastPayDate = lastPayDate;
    }

    public String getInstCode() {
        return instCode;
    }

    public void setInstCode(String instCode) {
        this.instCode = instCode;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }
}
