package com.example.timeleftpercent;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import static com.example.timeleftpercent.App.CHANNEL_ID;

public class ExampleService extends Service {
    final Handler handler=new Handler();
    Intent widgetUpdateIntent;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String intput = intent.getStringExtra("inputExtra");

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Example Service")
                .setContentText(intput)
                .setSmallIcon(R.drawable.ic_android)
                .setContentIntent(pendingIntent);
        Notification notification = notificationBuilder.build();

        startForeground(1, notification);


        // for widget update
        widgetUpdateIntent = new Intent(this, NewAppWidget.class);
        widgetUpdateIntent.setAction("someFunc");
        // Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
        // since it seems the onUpdate() is only fired on that:
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplication());
        int[] ids = appWidgetManager.getAppWidgetIds(new ComponentName(getApplication(), NewAppWidget.class));
        widgetUpdateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        // widgetUpdateIntent.putExtra("text", "input");
        sendBroadcast(widgetUpdateIntent);

        return START_REDELIVER_INTENT;
    }

    final Runnable updateTask=new Runnable() {
        @Override
        public void run() {
            // for widget update
            widgetUpdateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            // Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
            // since it seems the onUpdate() is only fired on that:
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplication());
            int[] ids = appWidgetManager.getAppWidgetIds(new ComponentName(getApplication(), NewAppWidget.class));
            widgetUpdateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            // widgetUpdateIntent.putExtra("text", "input");
            sendBroadcast(widgetUpdateIntent);
            handler.postDelayed(this,1000);
        }
    };
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}