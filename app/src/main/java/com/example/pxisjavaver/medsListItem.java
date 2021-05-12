package com.example.pxisjavaver;

public class medsListItem {
    private String medsDate;
    private String medsPresc;
    private String medsDocName;
    private String medsValidDate;

    public medsListItem(String medsDate, String medsPresc, String medsDocName, String medsValidDate) {
        this.medsDate = medsDate;
        this.medsPresc = medsPresc;
        this.medsDocName = medsDocName;
        this.medsValidDate = medsValidDate;
    }

    public String getMedsDate() {
        return medsDate;
    }

    public String getMedsPresc() {
        return medsPresc;
    }

    public String getMedsDocName() {
        return medsDocName;
    }

    public String getMedsValidDate() {
        return medsValidDate;
    }
}
