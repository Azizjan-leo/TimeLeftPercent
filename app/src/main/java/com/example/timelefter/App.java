package com.example.timelefter;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.core.os.ConfigurationCompat;

import java.util.Locale;

public class App extends Application {
    public static final int NOTIFICATION_ID = 1945;
    public static final String CHANNEL_ID = "TLNotificationServiceChannel";
    public static final String INPUT_STRING = "InputString";
    public static final int START_HOUR = 4;
    public static final int END_HOUR = 22;
    public static final long TIME_DELAYED = 500;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }
    private void createNotificationChannel() {
        NotificationChannel serviceChannel = new NotificationChannel(
                CHANNEL_ID,
                "Time Lefter Notification Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
        );
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(serviceChannel);
    }
}