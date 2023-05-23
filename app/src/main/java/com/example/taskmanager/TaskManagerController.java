package com.example.taskmanager;

import android.content.ContentValues;
import android.content.Context;

import java.util.List;

public class TaskManagerController {
    private MyTasksDB dbHelper;

    public TaskManagerController(Context c) {
        dbHelper = new MyTasksDB(c);
    }

    void addType(String name, int color) {
        dbHelper.addType(name, color);
    }

    public List<Type> getAllTypes() {
        return dbHelper.getAllTypes();
    }

    public int addEvent(String fName, int fType, int fColor, String fDateTime, String fNote, int fRemainderDuration, String fReminderUnit, int fPriority, Context context) {
        int added = dbHelper.addEvent(fName, fType, fColor, fDateTime, fNote, fRemainderDuration, fReminderUnit, fPriority);
        if (added != -1) {
            // Perform notification and add to calendar methods
            // ...

            // Perform notification and add to calendar methods
        }
        return added;
    }

    public int addTask(int eventId, String description) {
        return dbHelper.addTask(eventId, description);
    }

    public EventModel getEventById(int eventId) {
        return dbHelper.getEventById(eventId);
    }

    public List<Task> getTasksByEventId(int eventId) {
        return dbHelper.getTasksByEventId(eventId);
    }

    public void updateEventById(int eventId, String name, int typeId, int color, String dateTime, String note, int reminderDuration, String reminderUnit, int priority) {
        dbHelper.updateEventById(eventId, name, typeId, color, dateTime, note, reminderDuration, reminderUnit, priority);
    }

    public void updateTaskById(int taskId, String description) {
        dbHelper.updateTaskById(taskId, description);
    }

    public void changeTaskStatus(int taskId, boolean done) {
        dbHelper.changeTaskStatus(taskId, done);
    }
}
