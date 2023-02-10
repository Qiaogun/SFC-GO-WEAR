package com.sfc.myapplication;

public class Flag {
    private int colorID;
    private String name;
    private float offset;

    public int getID() {
        return colorID;
    }
    public String getName() {
        return name;
    }
    public float getOffset() {
        return offset;
    }
        public Flag(int colorID, String name, float offset) {
        this.colorID = colorID;
        this.name = name;
        this.offset = offset;
    }
}