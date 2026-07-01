package com.housingfund.collection.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class UnitOpenResult implements Serializable {

    private String unitAccNum;
    private String unitName;
    private String orgCode;
    private LocalDate createDate;
    private BigDecimal unitRatio;
    private BigDecimal perRatio;

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

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
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
}
