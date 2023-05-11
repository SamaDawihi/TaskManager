package com.example.taskmanager;

public class Event {
    private int eventId;
    private int typeId;
    private String name;
    private String color;
    private String dateTime;
    private String note;
    private int reminderPeriodInMin;
    private int priority;

    public Event(int eventId, int typeId, String name, String color, String dateTime, String note, int reminderPeriodInMin, int priority) {
        this.eventId = eventId;
        this.typeId = typeId;
        this.name = name;
        this.color = color;
        this.dateTime = dateTime;
        this.note = note;
        this.reminderPeriodInMin = reminderPeriodInMin;
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

    public String getColor() {
        return color;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getNote() {
        return note;
    }

    public int getReminderPeriodInMin() {
        return reminderPeriodInMin;
    }

    public int getPriority() {
        return priority;
    }
}

