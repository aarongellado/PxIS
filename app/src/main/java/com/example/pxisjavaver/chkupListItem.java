package com.example.pxisjavaver;

public class chkupListItem {
    private String chkUpDate;
    private String chkUpDiags;
    private String chkUpDoc;

    public chkupListItem(String chkUpDate, String chkUpDiags, String chkUpDoc) {
        this.chkUpDate = chkUpDate;
        this.chkUpDiags = chkUpDiags;
        this.chkUpDoc = chkUpDoc;
    }

    public String getChkUpDate() {
        return chkUpDate;
    }

    public String getChkUpDiags() {
        return chkUpDiags;
    }

    public String getChkUpDoc() {
        return chkUpDoc;
    }
}
