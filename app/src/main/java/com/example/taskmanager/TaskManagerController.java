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

    void addType(String name, int color) {
        dbHelper.addType(name, color);

    }

    public List<Type> getAllTypes() {
        return dbHelper.getAllTypes();
    }

    public int addEvent(String fName, int fType, int fColor, String fDateTime, String fNote, int fRemainderDuration, String fReminderUnit, int fPriority, Context context) {
        int added = dbHelper.addEvent(fName, fType, fColor, fDateTime, fNote, fRemainderDuration, fReminderUnit, fPriority);
        if(added != -1){
            //notification and add to calendar methods

            //notification and add to calendar methods
        }
        return added;
    }

    public int addTask(int eventId, String description){
        return dbHelper.addTask(eventId, description);
    }
    boolean doesTypeExist(int typeId){
        return dbHelper.doesTypeExist(typeId);
    }

}
