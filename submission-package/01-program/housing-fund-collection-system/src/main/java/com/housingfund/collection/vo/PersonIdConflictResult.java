package com.housingfund.collection.vo;

import java.io.Serializable;

public class PersonIdConflictResult implements Serializable {

    private String currentPerAccNum;
    private String currentPerName;
    private String newPerName;
    private String newIdCard;
    private String occupiedPerAccNum;
    private String occupiedIdCard;
    private String occupiedPerName;
    private String occupiedStatus;
    private String occupiedStatusText;
    private String occupiedUnitName;
    private String occupiedUnitAccNum;
    private String generatedWrongIdCard;

    public String getCurrentPerAccNum() {
        return currentPerAccNum;
    }

    public void setCurrentPerAccNum(String currentPerAccNum) {
        this.currentPerAccNum = currentPerAccNum;
    }

    public String getCurrentPerName() {
        return currentPerName;
    }

    public void setCurrentPerName(String currentPerName) {
        this.currentPerName = currentPerName;
    }

    public String getNewPerName() {
        return newPerName;
    }

    public void setNewPerName(String newPerName) {
        this.newPerName = newPerName;
    }

    public String getNewIdCard() {
        return newIdCard;
    }

    public void setNewIdCard(String newIdCard) {
        this.newIdCard = newIdCard;
    }

    public String getOccupiedPerAccNum() {
        return occupiedPerAccNum;
    }

    public void setOccupiedPerAccNum(String occupiedPerAccNum) {
        this.occupiedPerAccNum = occupiedPerAccNum;
    }

    public String getOccupiedIdCard() {
        return occupiedIdCard;
    }

    public void setOccupiedIdCard(String occupiedIdCard) {
        this.occupiedIdCard = occupiedIdCard;
    }

    public String getOccupiedPerName() {
        return occupiedPerName;
    }

    public void setOccupiedPerName(String occupiedPerName) {
        this.occupiedPerName = occupiedPerName;
    }

    public String getOccupiedStatus() {
        return occupiedStatus;
    }

    public void setOccupiedStatus(String occupiedStatus) {
        this.occupiedStatus = occupiedStatus;
    }

    public String getOccupiedStatusText() {
        return occupiedStatusText;
    }

    public void setOccupiedStatusText(String occupiedStatusText) {
        this.occupiedStatusText = occupiedStatusText;
    }

    public String getOccupiedUnitName() {
        return occupiedUnitName;
    }

    public void setOccupiedUnitName(String occupiedUnitName) {
        this.occupiedUnitName = occupiedUnitName;
    }

    public String getOccupiedUnitAccNum() {
        return occupiedUnitAccNum;
    }

    public void setOccupiedUnitAccNum(String occupiedUnitAccNum) {
        this.occupiedUnitAccNum = occupiedUnitAccNum;
    }

    public String getGeneratedWrongIdCard() {
        return generatedWrongIdCard;
    }

    public void setGeneratedWrongIdCard(String generatedWrongIdCard) {
        this.generatedWrongIdCard = generatedWrongIdCard;
    }
}
