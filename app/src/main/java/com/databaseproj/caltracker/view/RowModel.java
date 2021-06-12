package com.databaseproj.caltracker.view;

public class RowModel {
    private String label = null;
    private String details = null;

    RowModel(String label) {
        this.label = label;
    }

    RowModel(String label, String details) {
        this.label = label;
        this.details = details;
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
}
