package com.example.taskmanager;

public class EventModel {
    private int eventId;
    private int typeId;
    private String name;
    private int color;
    private String dateTime;
    private String note;
    private int reminderDuration;
    private String reminderUnit;
    private int priority;

    public EventModel(int eventId, int typeId, String name, int color, String dateTime, String note, int reminderDuration, String reminderUnit, int priority) {
        this.eventId = eventId;
        this.typeId = typeId;
        this.name = name;
        this.color = color;
        this.dateTime = dateTime;
        this.note = note;
        this.reminderDuration = reminderDuration;
        this.reminderUnit = reminderUnit;
        this.priority = priority;
    }

    public int getEventId() {
        return eventId;
    }

    public int getTypeId() {
        return typeId;
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getNote() {
        return note;
    }

    public int getReminderDuration() {
        return reminderDuration;
    }

    public String getReminderUnit() {
        return reminderUnit;
    }

    public int getPriority() {
        return priority;
    }
}
