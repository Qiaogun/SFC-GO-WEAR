package com.sfc.myapplication;

public class Flag {
    private int colorID;
    private String name;
    private String falgsatus;

    public int getID() {
        return colorID;
    }
    public String getName() {
        return name;
    }
    public String getFlagSatus() {
        return falgsatus;
    }
    public Flag(int colorID, String name, String falgSatus) {
        this.colorID = colorID;
        this.name = name;
        this.falgsatus = falgSatus;
    }
}