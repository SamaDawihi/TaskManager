package com.example.taskmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.media.metrics.Event;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
                        //", icon TEXT NOT NULL" +
                        ", color INTEGER NOT NULL" +
                        ")";
        db.execSQL(query);

        query = "INSERT INTO " + TYPE + " (name, color) VALUES ('Not Sorted'," + Color.RED +")";
        db.execSQL(query);

        query = "INSERT INTO " + TYPE + " (name, color) VALUES ('Work'," + Color.BLUE +")";
        db.execSQL(query);

        query = "INSERT INTO " + TYPE + " (name, color) VALUES ('Personal'," + Color.GREEN +")";
        db.execSQL(query);

        query =
                "CREATE TABLE " + EVENT + " (" +
                        "eventId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                        "typeId INTEGER REFERENCES Type(typeId) NOT NULL DEFAULT 1," +
                        "name TEXT NOT NULL" +
                        ", color INTEGER NOT NULL" +
                        ", dateTime DATETIME NOT NULL" +
                        ", note TEXT NOT NULL" +
                        ", reminderDuration INTEGER NOT NULL" +
                        ", reminderUnit TEXT NOT NULL" +
                        ", priority INTEGER NOT NULL" +
                        ", FOREIGN KEY (typeId) REFERENCES Type(typeId) ON DELETE CASCADE" +
                        ")";
        db.execSQL(query);

        query =
                "CREATE TABLE "+ TASK + " (" +
                        "taskId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                        "eventId INTEGER REFERENCES Event(eventId) NOT NULL," +
                        //"name TEXT NOT NULL," +
                        "description TEXT NOT NULL," +
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

    void addType(String name, @Nullable String icon, @Nullable String color) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", name);
        //values.put("icon", icon);
        values.put("color", color);

        db.insert("Type", null, values);

        db.close();

    }
    int addEvent(String name, int typeId, int color, String dateTime, String note , int reminderDuration, String reminderUnit, int priority) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("color", color);
        values.put("typeId", typeId);
        values.put("dateTime", dateTime);
        values.put("note", note);
        values.put("reminderDuration", reminderDuration);
        values.put("reminderUnit", reminderUnit);
        values.put("priority", priority);
        int eventId = (int) db.insert("Event", null, values);
        Log.i("ADD_EVENT", "ADDED EVENT ID IS " + String.valueOf(eventId));

        db.close();

        return eventId;
    }
    int addTask(int eventId, String description) {
        Log.i("ADDING_TASK", "ADDED TASK Desc IS " + description);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("description", description);
        values.put("eventId", eventId);
        values.put("done", false);

        int taskId = (int) db.insert("Task", null, values);
        Log.i("ADD_TASK", "ADDED TASK ID IS " + String.valueOf(taskId));


        db.close();
        return taskId;

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
                int color = cursor.getInt(Math.max(cursor.getColumnIndex("color"),0));

                Type type = new Type(typeId, name, icon, color);
                typeList.add(type);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return typeList;
    }

    public List<EventModel> getAllEvents()  {
        List<EventModel> eventList = new ArrayList<>();
        //List<String> List = new ArrayList<>();

        int eventId = 0;
        String name = "";
        String dateTime = "";

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + EVENT;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                eventId = cursor.getInt(Math.max(cursor.getColumnIndex("eventId"), 0));
                int typeId = cursor.getInt(Math.max(cursor.getColumnIndex("typeId"), 0));
                name = cursor.getString(Math.max(cursor.getColumnIndex("name"), 0));
                int color = cursor.getInt(Math.max(cursor.getColumnIndex("color"), 0));
                dateTime = cursor.getString(Math.max(cursor.getColumnIndex("dateTime"), 0));

                String note = cursor.getString(Math.max(cursor.getColumnIndex("note"), 0));
                int reminderDuration = cursor.getInt(Math.max(cursor.getColumnIndex("reminderDuration"), 0));
                String reminderUnit = cursor.getString(Math.max(cursor.getColumnIndex("reminderUnit"), 0));
                int priority = cursor.getInt(Math.max(cursor.getColumnIndex("priority"), 0));


                EventModel event = new EventModel(eventId, typeId, name, color, dateTime, note, reminderDuration, reminderUnit, priority);
                eventList.add(event);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();


        Collections.sort(eventList, new Comparator<EventModel>() {
            public int compare(EventModel a, EventModel b) {
                return b.getDateTime().compareTo(a.getDateTime());
            }
        });

        return eventList;
    }

    public List<String> getEvents(){
        List<EventModel> eventList = new ArrayList<>();
        eventList = getAllEvents();

        List<String> list = new ArrayList<>();

        for(int i = 0; i < eventList.size(); i++){
            list.add(eventList.get(i).getColor());
        }

        return list;
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

    boolean doesTypeExist(int typeId){
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TYPE ;//+ " WHERE typeId = 1";
        Cursor cursor = db.rawQuery(query, null);


        //String query = "SELECT * FROM Type";
        //String[] args = new String[]{String.valueOf(typeId)};
        //Cursor cursor = db.rawQuery(query + " WHERE typeId = ?", args);

        boolean exists = cursor.moveToFirst();

        cursor.close();
        db.close();

        return exists;
    }
}
