package com.example.taskmanager;

import static android.app.PendingIntent.getActivity;
import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.NOTIFICATION_SERVICE;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;
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
    String Event_name;
    int Event_priority;
    int Event_Id;
    int mints;
    long eventTime, reminderTime;



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
        int added = dbHelper.addEvent(fName, fType, fColor, fDateTime, fNote, fRemainderDuration, fReminderUnit, fPriority, "Pending");

        Event_name=fName;
        Event_priority=fPriority;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        if (added != -1 ) { // & false
            Event_Id=added;
            Log.i("AddEventController", "Start Notification");
            // Perform notification methods
            alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            try {
                date = format.parse(fDateTime);

                // Create a calendar object with the selected date and time
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);

                //check the duration
                mints = 0;
                switch (fReminderUnit) {
                    case "Minutes": // Minutes
                        mints = fRemainderDuration;
                        break;
                    case "Hours": // Hours
                        mints = fRemainderDuration * 60;
                        break;
                    case "Day": // Days
                        mints = fRemainderDuration * 24 * 60;
                        break;
                    case "Week": // Weeks
                        mints = fRemainderDuration * 7 * 24 * 60;
                        break;
                    case "month": // Months
                        mints = fRemainderDuration * 30 * 24 * 60;
                        break;
                }

                // Calculate the reminder time in milliseconds

                reminderTime = eventTime - mints * 60000;


                // Create an intent to start the AlarmReceiver class
                Intent intent = new Intent(context, AlarmReceiver.class);
                intent.putExtra("EventName", fName);
                intent.putExtra("EventPriority", fPriority);
                intent.putExtra("Event_Id", added);

                // Create a pending intent that will be triggered when the alarm goes off
                pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);


                // Set the alarm to the calendar time
                alarmManager.set(AlarmManager.RTC_WAKEUP, reminderTime, pendingIntent);
                Toast.makeText(context, "Notification set for " + calendar.YEAR + "/" + (calendar.MONTH + 1) + "/" + calendar.DAY_OF_MONTH + " " + calendar.HOUR_OF_DAY + ":" + calendar.MINUTE, Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "priority is " +fPriority , Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "Event name is  " +fName , Toast.LENGTH_SHORT).show();

                Log.i("AddEventController", "Start Calendar");


                //calendar methods
                // Create an intent to add an event to the calendar
                Intent calendarIntent = null;

                calendarIntent = new Intent(Intent.ACTION_INSERT);
                calendarIntent.setData(CalendarContract.Events.CONTENT_URI);
                calendarIntent.putExtra(CalendarContract.Events.TITLE, fName);
                calendarIntent.putExtra(CalendarContract.Events.DESCRIPTION, fNote);
                calendarIntent.putExtra(CalendarContract.Events.EVENT_COLOR, fColor);// Set the event color here
                calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, date.getTime());
                calendarIntent.putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);


                // Check if there is a calendar app available to handle the intent
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    // Start the activity with the intent
                    Log.i("AddEventController", "Will start Calendar Intent");
                    context.startActivity(calendarIntent);
                    //Toast.makeText(context, "calendar app found", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle the case where no calendar app is available
                    Log.i("AddEventController", "No calendar app found");
                    //Toast.makeText(context, "No calendar app found", Toast.LENGTH_SHORT).show();
                }



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

    public void updateEventById(int eventId, String name, int typeId, int color, String dateTime, String note, int reminderDuration, String reminderUnit, int priority, Context context) {
        dbHelper.updateEventById(eventId, name, typeId, color, dateTime, note, reminderDuration, reminderUnit, priority);

        removeEvent(eventId, context);
        addEvent(name, typeId, color, dateTime, note, reminderDuration, reminderUnit,priority, context);

    }

    public void updateTaskById(int taskId, String description) {
        dbHelper.updateTaskById(taskId, description);
    }

    public void changeTaskStatus(int taskId, boolean done) {
        dbHelper.changeTaskStatus(taskId, done);
    }
    void changeEventState(int eventId, String state){
        dbHelper.changeEventState(eventId, state);
    }
    public void removeType(int typeId) {
        dbHelper.removeType(typeId);
    }

    public void removeEvent(int eventId, Context context) {
        dbHelper.removeEvent(eventId);


        // Create an intent with the same class and extras as the one used to set the alarm
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("EventName", Event_name);
        intent.putExtra("EventPriority", Event_priority);
        intent.putExtra("Event_Id", eventId);
        Toast.makeText(context, "id is "+eventId, Toast.LENGTH_SHORT).show();

        // Create a pending intent with the same id and intent as the one used to set the alarm
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Event_Id, intent, PendingIntent.FLAG_IMMUTABLE);


        // Get the alarm manager service
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        // Cancel the alarm and the pending intent
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
        Toast.makeText(context, "notification cancled", Toast.LENGTH_SHORT).show();

        //remove event from calendar app
        Uri eventUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, Event_Id);
        ContentValues event = new ContentValues();
        event.put(CalendarContract.Events.DELETED, 1);
        context.getContentResolver().update(eventUri, event, null, null);

    }

    public void removeTask(int taskId) {
        dbHelper.removeTask(taskId);
    }
}
