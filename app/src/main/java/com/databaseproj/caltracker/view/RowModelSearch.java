package com.databaseproj.caltracker.view;

public class RowModelSearch {
    private String label = null;
    private String details = null;
    private String details1 = null;

    private String details2 = null;
    private String details3 = null;



    RowModelSearch(String label) {
        this.label = label;
    }

    public RowModelSearch(String label, String details, String details1, String details2, String details3) {
        this.label = label;
        this.details = details;
        this.details1 = details1;
        this.details2 = details2;
        this.details3 = details3;

    }

    public String getLabel() {
        return (label);
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }


    public String getDetails1() {
        return details1;
    }

    public void setDetails1(String details1) {
        this.details1 = details1;
    }



    public String getDetails2() {
        return details2;
    }

    public void setDetails2(String details2) {
        this.details2 = details2;
    }


    public String getDetails3() {
        return details3;
    }

    public void setDetails3(String details3) {
        this.details3 = details3;
    }


}
