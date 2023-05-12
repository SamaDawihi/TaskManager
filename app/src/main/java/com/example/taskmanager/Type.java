package com.example.taskmanager;

public class Type {
    private int typeId;
    private String name;
    private String icon;

    public Type(int typeId, String name, String icon) {
        this.typeId = typeId;
        this.name = name;
        this.icon = icon;
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
    @Override
    public String toString() {
        return name;
    }
}
