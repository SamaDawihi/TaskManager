package com.example.taskmanager;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EventModel  {
    private int eventId;
    private int typeId;
    private String name;
    private int color;
    private String dateTime;
    private String note;
    private int reminderDuration;
    private String reminderUnit;
    private int priority;

    private String state;

    private String calEventId;


    public EventModel(int eventId, int typeId, String name, int color, String dateTime, String note, int reminderDuration, String reminderUnit, int priority, String state, String calEventId) {
        this.eventId = eventId;
        this.typeId = typeId;
        this.name = name;
        this.color = color;
        this.dateTime = dateTime;
        this.note = note;
        this.reminderDuration = reminderDuration;
        this.reminderUnit = reminderUnit;
        this.priority = priority;
        this.state = state;
        this.calEventId = calEventId;
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

    public Date getDate() throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date = format.parse(dateTime);

        return date;
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
    public String getState() {
        return state;
    }

    public String getCalEventId(){return calEventId;}


}
