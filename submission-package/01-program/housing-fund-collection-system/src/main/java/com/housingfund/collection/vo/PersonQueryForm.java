package com.housingfund.collection.vo;

import java.io.Serializable;

public class PersonQueryForm implements Serializable {

    private String perAccNum;
    private String idCard;

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
}
