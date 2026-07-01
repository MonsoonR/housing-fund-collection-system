package com.housingfund.collection.vo;

import java.io.Serializable;

public class UnitQueryForm implements Serializable {

    private String unitAccNum;
    private String unitName;

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
}
