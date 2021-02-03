package com.example.timelefter;

import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.IBinder;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.os.ConfigurationCompat;

import java.time.LocalTime;
import java.time.temporal.ChronoField;


public class TimeService extends Service {

    public static String getRemainTime(){
        String result = "00:00:00";

        long currentSecond =  LocalTime.now().get(ChronoField.SECOND_OF_DAY);
        long finalSecond = hourToSeconds(App.END_HOUR);
        long diff = finalSecond - currentSecond;

        if((diff) > 0){
            int hours = (int) diff / 3600;
            int minutes = (int) (diff % 3600) / 60;
            int seconds = (int) diff % 60;

            result = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }

        return result;
    }



    @Nullable
    public static Double getCurrentSecPercent(){
        int startSecond = hourToSeconds(App.START_HOUR);
        int endSecond = hourToSeconds(App.END_HOUR);
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
