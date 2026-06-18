package com.housingfund.collection.vo;

import java.io.Serializable;
import java.time.LocalDateTime;

public class PersonEditResult implements Serializable {

    private String perAccNum;
    private String perName;
    private String idCard;
    private String unitAccNum;
    private String unitName;
    private boolean forceChanged;
    private String conflictPerAccNum;
    private String wrongAccountPerAccNum;
    private String originalConflictIdCard;
    private String changedConflictIdCard;
    private String resultMessage;
    private LocalDateTime updateTime;
    private PersonIdConflictResult conflictResult;

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

    public boolean isForceChanged() {
        return forceChanged;
    }

    public void setForceChanged(boolean forceChanged) {
        this.forceChanged = forceChanged;
    }

    public String getConflictPerAccNum() {
        return conflictPerAccNum;
    }

    public void setConflictPerAccNum(String conflictPerAccNum) {
        this.conflictPerAccNum = conflictPerAccNum;
    }

    public String getWrongAccountPerAccNum() {
        return wrongAccountPerAccNum;
    }

    public void setWrongAccountPerAccNum(String wrongAccountPerAccNum) {
        this.wrongAccountPerAccNum = wrongAccountPerAccNum;
    }

    public String getOriginalConflictIdCard() {
        return originalConflictIdCard;
    }

    public void setOriginalConflictIdCard(String originalConflictIdCard) {
        this.originalConflictIdCard = originalConflictIdCard;
    }

    public String getChangedConflictIdCard() {
        return changedConflictIdCard;
    }

    public void setChangedConflictIdCard(String changedConflictIdCard) {
        this.changedConflictIdCard = changedConflictIdCard;
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

    public PersonIdConflictResult getConflictResult() {
        return conflictResult;
    }

    public void setConflictResult(PersonIdConflictResult conflictResult) {
        this.conflictResult = conflictResult;
    }
}
