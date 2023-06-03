package com.example.taskmanager;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;
import static android.app.NotificationManager.IMPORTANCE_LOW;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import java.io.Serializable;

public class CreateNotificationChannels{
    static NotificationChannel channelH, channelD, channelL, channelM;
    NotificationManager notificationManager;
    CreateNotificationChannels(Context context){
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channelH = new NotificationChannel("channelH", "IMPORTANCE_HIGH Channel", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channelH);

            channelD = new NotificationChannel("channelD", "IMPORTANCE_DEFAULT Channel", IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channelD);

            channelL = new NotificationChannel("channelL", "IMPORTANCE_LOW Channel", IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(channelL);

            channelM = new NotificationChannel("channelM", "IMPORTANCE_MIN Channel", NotificationManager.IMPORTANCE_MIN);
            notificationManager.createNotificationChannel(channelM);
        }
    }
}
