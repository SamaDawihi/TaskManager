package com.example.taskmanager;

import android.content.Context;

import java.util.List;

public class TaskManagerController {
    private MyTasksDB dbHelper;

    public TaskManagerController(Context c) {
        dbHelper = new MyTasksDB(c);
    }

    public List<Type> getAllTypes() {
        return dbHelper.getAllTypes();
    }

    public int addEvent(String fName, int fType, String fColor, String fDateTime, String fNote, int fRemainder, int fPriority) {
        return dbHelper.addEvent(fName, fType, fColor, fDateTime, fNote, fRemainder, fPriority);
    }
}
