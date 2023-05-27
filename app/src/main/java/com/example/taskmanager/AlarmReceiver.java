package com.example.taskmanager;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;
import static android.app.NotificationManager.IMPORTANCE_LOW;
import static android.content.Context.MODE_PRIVATE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    int eventId;
    String eventName;
    int eventPriority;

    @Override
    public void onReceive(Context context, Intent intent) {
        eventId = intent.getExtras().getInt("Event_Id");
        eventName = intent.getExtras().getString("EventName");
        eventPriority = intent.getExtras().getInt("EventPriority");

        Toast.makeText(context, "EVENT ID: " + eventId , Toast.LENGTH_SHORT).show();

        Toast.makeText(context, "Notification set for " + eventName , Toast.LENGTH_SHORT).show();


        // Get the notification manager service
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Get the priority
        int Event_priority=intent.getExtras().getInt("EventPriority");


        // Create a notification channel for Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            //set importance
            int channelImportance;
             switch (Event_priority) {
                case 4: //Urgent
                    channelImportance = NotificationManager.IMPORTANCE_HIGH;
                     break;

                case 3: //High
                    channelImportance = IMPORTANCE_DEFAULT;
                    break;

                case 2: //Medium
                    channelImportance = IMPORTANCE_LOW;
                    break;

                case 1: //Low
                    channelImportance = NotificationManager.IMPORTANCE_MIN;
                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + Event_priority);

            }


             NotificationChannel channel = new NotificationChannel("notification_channel", "Notification Channel", channelImportance);
             //channel.setImportance(IMPORTANCE_LOW);
             notificationManager.createNotificationChannel(channel);

        }

        Intent notificationIntent = new Intent (context, event_Info.class); // specify the activity to open
        notificationIntent.putExtra ("eventId", eventId);// pass some data to the activity
        notificationIntent.putExtra("fromNotification",00);
        PendingIntent contentIntent = PendingIntent.getActivity (context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT); // create the pending intent

        // Create a notification builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notification_channel")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Notification")
                .setContentText("Your event ("+ eventName + ") is coming soon")
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent (contentIntent);

        //set priority
        int notificationPriority;
          switch (Event_priority)
         {
             case 4: //Urgent
                 notificationPriority = NotificationCompat.PRIORITY_HIGH;
                 break;

            case 3: //High
                notificationPriority = NotificationCompat.PRIORITY_DEFAULT;
                break;

            case 2: //Medium
                notificationPriority = NotificationCompat.PRIORITY_LOW;
                break;

            case 1: //Low
                notificationPriority = NotificationCompat.PRIORITY_MIN;
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + Event_priority);
        }
        builder.setPriority(notificationPriority);


        // Show the notification
        notificationManager.notify(0, builder.build());
        Toast.makeText(context, "Notification set for " + eventName , Toast.LENGTH_SHORT).show();

        Log.i("ALARM RECEIVER", "END");
    }
}
