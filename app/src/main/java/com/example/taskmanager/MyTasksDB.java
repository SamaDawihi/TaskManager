package com.example.taskmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MyTasksDB extends SQLiteOpenHelper {

    private static String DBNAME = "MyTasks";
    private static int DBVersion = 1;

    private static String TYPE = "TYPE";

    private static String EVENT = "EVENT";

    private static String TASK = "TASK";


    public MyTasksDB(@Nullable Context context) {
        super(context, DBNAME, null, DBVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query =
                "CREATE TABLE " + TYPE + " (" +
                        "typeId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL" +
                        ", name TEXT NOT NULL" +
                        //", icon TEXT" +
                        //", color TEXT NOT NULL" +
                        ")";
        db.execSQL(query);

        query = "INSERT INTO " + TYPE + " (name) VALUES ('Work')";
        db.execSQL(query);

        query = "INSERT INTO " + TYPE + " (name) VALUES ('Personal')";
        db.execSQL(query);

        query =
                "CREATE TABLE " + EVENT + " (" +
                        "eventId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                        "typeId INTEGER REFERENCES Type(typeId) NOT NULL," +
                        "name TEXT NOT NULL" +
                        ", dateTime DATETIME NOT NULL" +
                        ", note TEXT NOT NULL" +
                        //", reminderPeriodInMin INTEGER NOT NULL" +
                        ", priority INTEGER NOT NULL" +
                        ", FOREIGN KEY (typeId) REFERENCES Type(typeId) ON DELETE CASCADE" +
                        ")";
        db.execSQL(query);

        query =
                "CREATE TABLE "+ TASK + " (" +
                        "taskId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                        "eventId INTEGER REFERENCES Event(eventId) NOT NULL," +
                        "name TEXT NOT NULL," +
                        "desription TEXT NOT NULL," +
                        "done BOOLEAN NOT NULL," +
                        "FOREIGN KEY (eventId) REFERENCES Event(eventId) ON DELETE CASCADE" +
                        ")";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Type");
        db.execSQL("DROP TABLE IF EXISTS Event");
        db.execSQL("DROP TABLE IF EXISTS TASK");
        onCreate(db);
    }

    void addType(String name, @Nullable String icon) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", name);
        //values.put("icon", icon);
        //values.put("color", color);

        db.insert("Type", null, values);

        db.close();

    }
    int addEvent(String name, int typeId, String color, String dateTime, String note , int reminder, int priority) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("typeId", typeId);
        values.put("dateTime", dateTime);
        values.put("note", note);
        //values.put("reminderPeriodInMin", reminder);
        values.put("priority", priority);

        int eventId = (int) db.insert("Event", null, values);
        Log.i("ADD_EVENT", "ADDED EVENT ID IS " + String.valueOf(eventId));

        db.close();

        return eventId;
    }
    void addTask(int eventId, String name, boolean done, int priority) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("eventId", eventId);
        values.put("done", done);
        values.put("priority", priority);

        int taskId = (int) db.insert("Task", null, values);
        Log.i("ADD_TASK", "ADDED TASK ID IS " + String.valueOf(taskId));


        db.close();

    }

    public List<Type> getAllTypes() {
        List<Type> typeList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM Type";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int typeId = cursor.getInt(Math.max(cursor.getColumnIndex("typeId"),0));
                String name = cursor.getString(Math.max(cursor.getColumnIndex("name"),0));
                String icon = null; //cursor.getString(Math.max(cursor.getColumnIndex("icon"),0));

                Type type = new Type(typeId, name, icon);
                typeList.add(type);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return typeList;
    }


    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM Task";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int taskId = cursor.getInt(Math.max(cursor.getColumnIndex("taskId"),0));
                int eventId = cursor.getInt(Math.max(cursor.getColumnIndex("eventId"),0));
                String name = cursor.getString(Math.max(cursor.getColumnIndex("name"),0));
                int doneInt = cursor.getInt(Math.max(cursor.getColumnIndex("done"),0));
                boolean done = (doneInt == 1); // Convert 0/1 to boolean
                int priority = cursor.getInt(Math.max(cursor.getColumnIndex("priority"),0));

                Task task = new Task(taskId, eventId, name, done, priority);
                taskList.add(task);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return taskList;
    }


}
