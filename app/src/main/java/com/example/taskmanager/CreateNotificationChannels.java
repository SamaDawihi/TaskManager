package com.example.taskmanager;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;
import static android.app.NotificationManager.IMPORTANCE_LOW;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import java.io.Serializable;

public class CreateNotificationChannels implements Serializable {
    static NotificationChannel channelH, channelD, channelL, channelM;
    NotificationManager notificationManager;
    CreateNotificationChannels(Context context){
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channelH = new NotificationChannel("channelH", "Notification Channel", NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(channelH);

        channelD = new NotificationChannel("channelD", "Notification Channel", IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(channelD);

        channelL = new NotificationChannel("channelL", "Notification Channel", IMPORTANCE_LOW);
        notificationManager.createNotificationChannel(channelL);

        channelM = new NotificationChannel("channelM", "Notification Channel", NotificationManager.IMPORTANCE_MIN);
        notificationManager.createNotificationChannel(channelM);
        }
    }

    public NotificationChannel getChannelD() {
        return channelD;
    }

    public NotificationChannel getChannelH() {
        return channelH;
    }

    public NotificationChannel getChannelL() {
        return channelL;
    }

    public NotificationChannel getChannelM() {
        return channelM;
    }
}
