package com.example.taskmanager;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;
import static android.app.NotificationManager.IMPORTANCE_LOW;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    int eventId;
    String eventName;
    String channelId;
    int EventDuration;
    String EventDurationUnit;
    String notmsg;
    String fword;
    int spacePos;

    @Override
    public void onReceive(Context context, Intent intent) {
        eventId = intent.getExtras().getInt("Event_Id");
        eventName = intent.getExtras().getString("EventName");
        int Event_priority = intent.getExtras().getInt("EventPriority");
        EventDuration = intent.getExtras().getInt("Event_duration");
        EventDurationUnit=intent.getExtras().getString("Event_duration_Unit");

        if (EventDurationUnit.equals("At time of event"))
            notmsg="your event \""+eventName+"\" is coming right now!.";
        else{
            spacePos = EventDurationUnit.indexOf(" ");
            fword = EventDurationUnit.substring(0, spacePos);
            notmsg = "Your event \""+eventName+"\" is coming in " + EventDuration + " " + fword + "!";
            }

        // Get the notification manager service
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Create a notification channel for Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            switch (Event_priority)
            {

                case 1: //High
                    channelId = "channelH";
                    break;
                case 0: //Default
                    channelId = "channelD";
                    break;

                case -1: //Low
                    channelId = "channelL";
                    break;

                case -2: //Min
                    channelId = "channelM";
                    break;
                //Urgent
                default:
                    channelId = "channelH";

            }



        }

        Intent notificationIntent = new Intent (context, event_Info.class); // specify the activity to open
        notificationIntent.putExtra ("eventId", eventId);// pass some data to the activity
        notificationIntent.putExtra("fromNotification",00);
        PendingIntent contentIntent = PendingIntent.getActivity (context, eventId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT); // create the pending intent



        // Create a notification builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(eventName)
                .setContentText(notmsg)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent (contentIntent)
                .setPriority(Event_priority);


        // Show the notification
        notificationManager.notify(eventId, builder.build());
    }
}
