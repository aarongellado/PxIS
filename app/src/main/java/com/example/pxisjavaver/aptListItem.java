package com.example.pxisjavaver;

public class aptListItem {
    private String aptPurpose;
    private String aptDate;

    public aptListItem(String aptPurpose, String aptDate) {
        this.aptPurpose = aptPurpose;
        this.aptDate = aptDate;
    }

    public String getAptPurpose() {
        return aptPurpose;
    }

    public String getAptDate() {
        return aptDate;
    }
}
