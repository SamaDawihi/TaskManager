package com.example.taskmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import java.util.List;

public class TaskManagerController {
    private MyTasksDB dbHelper;

    public TaskManagerController(Context c) {
        dbHelper = new MyTasksDB(c);
    }

    void addType(String name, @Nullable String icon, @Nullable String color) {
        dbHelper.addType(name, icon, color);

    }

    public List<Type> getAllTypes() {
        return dbHelper.getAllTypes();
    }

    public int addEvent(String fName, int fType, int fColor, String fDateTime, String fNote, int fRemainderDuration, String fReminderUnit, int fPriority) {
        return dbHelper.addEvent(fName, fType, fColor, fDateTime, fNote, fRemainderDuration, fReminderUnit, fPriority);
    }

    public int addTask(int eventId, String description){
        return dbHelper.addTask(eventId, description);
    }
    boolean doesTypeExist(int typeId){
        return dbHelper.doesTypeExist(typeId);
    }

}
