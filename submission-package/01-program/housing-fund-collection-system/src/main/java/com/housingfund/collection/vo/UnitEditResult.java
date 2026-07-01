package com.housingfund.collection.vo;

import java.io.Serializable;
import java.time.LocalDateTime;

public class UnitEditResult implements Serializable {

    private String unitAccNum;
    private String unitName;
    private String orgCode;
    private String phone;
    private String agentName;
    private String resultMessage;
    private LocalDateTime updateTime;

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

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
