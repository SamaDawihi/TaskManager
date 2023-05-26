package com.example.taskmanager;

import static android.app.PendingIntent.getActivity;
import static android.content.Context.ALARM_SERVICE;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import java.util.List;

public class TaskManagerController {
    private MyTasksDB dbHelper;

    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    public TaskManagerController(Context c) {
        dbHelper = new MyTasksDB(c);
    }

    void addType(String name, int color) {
        dbHelper.addType(name, color);
    }

    public List<Type> getAllTypes() {
        return dbHelper.getAllTypes();
    }

    public int addEvent(String fName, int fType, int fColor, String fDateTime, String fNote, int fRemainderDuration, String fReminderUnit, int fPriority, String fDate, String fTime, Context context) {
        int added = dbHelper.addEvent(fName, fType, fColor, fDateTime, fNote, fRemainderDuration, fReminderUnit, fPriority);

        if (added != -1 && false) {

            // Perform notification and add to calendar methods
            alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date;
            try {
                date = format.parse(fDateTime);

                // Create a calendar object with the selected date and time
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);

                // Create an intent to start the AlarmReceiver class
                Intent intent = new Intent(context, AlarmReceiver.class);

                // Create a pending intent that will be triggered when the alarm goes off
                pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

                // Set the alarm to the calendar time
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                Toast.makeText(context, "Notification set for " + calendar.YEAR + "/" + (calendar.MONTH + 1) + "/" + calendar.DAY_OF_MONTH + " " + calendar.HOUR_OF_DAY + ":" + calendar.MINUTE, Toast.LENGTH_SHORT).show();



            } catch (ParseException e) {
                e.printStackTrace();
            }

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
    public void removeType(int typeId) {
        dbHelper.removeType(typeId);
    }

    public void removeEvent(int eventId) {
        dbHelper.removeEvent(eventId);
    }

    public void removeTask(int taskId) {
        dbHelper.removeTask(taskId);
    }
}
