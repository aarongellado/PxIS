package com.example.pxisjavaver;

public class drAptListItem {

    private String drAptPurpose;
    private String drAptName;
    private String drAptDate;

    public drAptListItem(String drAptPurpose, String drAptName, String drAptDate) {
        this.drAptPurpose = drAptPurpose;
        this.drAptName = drAptName;
        this.drAptDate = drAptDate;
    }

    public String getDrAptPurpose() {
        return drAptPurpose;
    }

    public String getDrAptName() {
        return drAptName;
    }

    public String getDrAptDate() {
        return drAptDate;
    }
}
