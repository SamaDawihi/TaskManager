package com.example.taskmanager;

public class Type {
    private int typeId;
    private String name;
    private String icon;

    private int color;

    public Type(int typeId, String name, String icon, int color) {
        this.typeId = typeId;
        this.name = name;
        this.icon = icon;
        this.color = color;
    }

    public int getTypeId() {
        return typeId;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public int getColor() {
        return color;
    }

    @Override
    public String toString() {
        return name;
    }
}
