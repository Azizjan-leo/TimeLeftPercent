package com.example.timelefter;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;

import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.Calendar;

public class TimeService extends Service {
    public static final int START_HOUR = 4;
    public static final int END_HOUR = 24;

    @Nullable
    public static Long getRemainSeconds(){
        long currentSecond =  LocalTime.now().get(ChronoField.SECOND_OF_DAY);
        long finalSecond = hourToSeconds(END_HOUR);
        if(currentSecond < finalSecond)
            return finalSecond - currentSecond;
        return null;
    }

    @Nullable
    public static Double getCurrentSecPercent(){
        int startSecond = hourToSeconds(START_HOUR);
        int endSecond = hourToSeconds(END_HOUR);
        long currentSecond =  LocalTime.now().get(ChronoField.SECOND_OF_DAY);

        if(currentSecond >= startSecond || currentSecond <= endSecond) {

            int secondDiff = startSecond - endSecond;
            double secondValue = (double) 100 / secondDiff;

            return 100 - (secondValue * (startSecond - currentSecond));
        }

        return null;
    }

    public static int hourToSeconds(int hour){
        return hour * 60 * 60;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
