package com.example.taskmanager;

import static android.content.Context.MODE_PRIVATE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {
    public static final String SHARED_PREFS= "sharedPrefs";
    public static final String NOTIFICATION_ID= "notificationid";

    @Override
    public void onReceive(Context context, Intent intent) {

        // Get the notification manager service
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String Event_priority=intent.getExtras().getString("EventPriority");


        // Create a notification channel for Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel;
            //switch (Event_priority) {
             //   case "Urgent":
                    channel = new NotificationChannel("notification_channel", "Notification Channel", NotificationManager.IMPORTANCE_HIGH);
                   /* break;

                case "High":
                    channel = new NotificationChannel("notification_channel", "Notification Channel", NotificationManager.IMPORTANCE_DEFAULT);
                    break;

                case "Medium":
                    channel = new NotificationChannel("notification_channel", "Notification Channel", NotificationManager.IMPORTANCE_LOW);
                    break;

                case "Low":
                    channel = new NotificationChannel("notification_channel", "Notification Channel", NotificationManager.IMPORTANCE_MIN);
                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + Event_priority);

            }*/

            notificationManager.createNotificationChannel(channel);
        }

        // Create a notification builder
        NotificationCompat.Builder builder;

       // switch (Event_priority)
        //{
          //  case "Urgent":
               builder = new NotificationCompat.Builder(context, "notification_channel")
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("Notification")
                        .setContentText("Your event ("+intent.getExtras().getString("EventName")+ ") is coming soon")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setDefaults(Notification.DEFAULT_ALL);
                /*break;

            case "High":
                builder = new NotificationCompat.Builder(context, "notification_channel")
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("Notification")
                        .setContentText("Your event ("+intent.getExtras().getString("EventName")+ ") is coming soon")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setDefaults(Notification.DEFAULT_ALL);
                break;

            case "Medium":
                builder = new NotificationCompat.Builder(context, "notification_channel")
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("Notification")
                        .setContentText("Your event ("+intent.getExtras().getString("EventName")+ ") is coming soon")
                        .setPriority(NotificationCompat.PRIORITY_LOW)
                        .setDefaults(Notification.DEFAULT_ALL);
                break;

            case "Low":
                builder = new NotificationCompat.Builder(context, "notification_channel")
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("Notification")
                        .setContentText("Your event ("+intent.getExtras().getString("EventName")+ ") is coming soon")
                        .setPriority(NotificationCompat.PRIORITY_MIN)
                        .setDefaults(Notification.DEFAULT_ALL);
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + Event_priority);
        }*/

        // Show the notification

        // Get the shared preferences object
        SharedPreferences sharedPreferences = context.getSharedPreferences("notification_prefs", Context.MODE_PRIVATE);

        // Get the current notification ID from the preferences or 0 if not found
        int notificationId = sharedPreferences.getInt("notification_id", 0);

        // Increment the notification ID by 1
        notificationId++;

        // Save the new notification ID to the preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("notification_id", notificationId);
        editor.apply();


        notificationManager.notify(notificationId, builder.build());

    }
}
