package com.housingfund.collection.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class PersonBasicInfo implements Serializable {

    private String perAccNum;
    private String unitAccNum;
    private String perName;
    private String idType;
    private String idCard;
    private LocalDate createTime;
    private BigDecimal perBalance;
    private String status;
    private BigDecimal baseNum;
    private BigDecimal unitRatio;
    private BigDecimal perRatio;
    private LocalDate lastPayDate;
    private BigDecimal unitMonthPay;
    private BigDecimal perMonthPay;
    private BigDecimal ypayAmt;
    private BigDecimal ydrawAmt;
    private BigDecimal yinterestBal;
    private String instCode;
    private String op;
    private String remark;

    public String getPerAccNum() {
        return perAccNum;
    }

    public void setPerAccNum(String perAccNum) {
        this.perAccNum = perAccNum;
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

    public LocalDate getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDate createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getPerBalance() {
        return perBalance;
    }

    public void setPerBalance(BigDecimal perBalance) {
        this.perBalance = perBalance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public LocalDate getLastPayDate() {
        return lastPayDate;
    }

    public void setLastPayDate(LocalDate lastPayDate) {
        this.lastPayDate = lastPayDate;
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

    public BigDecimal getYpayAmt() {
        return ypayAmt;
    }

    public void setYpayAmt(BigDecimal ypayAmt) {
        this.ypayAmt = ypayAmt;
    }

    public BigDecimal getYdrawAmt() {
        return ydrawAmt;
    }

    public void setYdrawAmt(BigDecimal ydrawAmt) {
        this.ydrawAmt = ydrawAmt;
    }

    public BigDecimal getYinterestBal() {
        return yinterestBal;
    }

    public void setYinterestBal(BigDecimal yinterestBal) {
        this.yinterestBal = yinterestBal;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
