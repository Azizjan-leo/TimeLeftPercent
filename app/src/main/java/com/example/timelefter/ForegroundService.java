package com.example.timelefter;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Icon;
import android.os.Debug;
import android.os.Handler;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ForegroundService extends Service {
    final Handler handler=new Handler();
    Notification.Builder builder = null;
    NotificationManagerCompat notificationManagerCompat = null;

    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        builder = new Notification.Builder(this, App.CHANNEL_ID);
        builder.setOngoing(true);

        notificationManagerCompat = NotificationManagerCompat.from(this);
        updateCurrentTime();
        startForeground(1, builder.build());

        handler.postDelayed(updateTask, App.TIME_DELAYED);

        //stopSelf();
        return START_REDELIVER_INTENT;
    }

    public void updateCurrentTime(){

        if(TimeService.getCurrentSecPercent() != null) {
            double currentSecondPercent = TimeService.getCurrentSecPercent();
            String result = String.format("%.3f", currentSecondPercent) + "%";
            int intPercent = (int) currentSecondPercent;
            String shortText;
            if (intPercent >= 100) {
                shortText = String.valueOf(intPercent);
            }
            else if (intPercent < 100 && intPercent > 9) {
                shortText = intPercent + "%";
            } else {
                shortText = String.format("%.1f", currentSecondPercent) + "%";
            }

            //convert text to bitmap
            Bitmap bitmap = createBitmapFromString(shortText.trim());

            //setting bitmap to status bar icon.
            builder.setSmallIcon(Icon.createWithBitmap(bitmap));

            builder.setContentText(result);

            notificationManagerCompat.notify(App.NOTIFICATION_ID, builder.build());

            createNotificationChannel();
        }

    }

    private void createNotificationChannel() {
        CharSequence name = "testing";
        String description = "i'm testing this notification";
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel channel = new NotificationChannel(App.CHANNEL_ID, name, importance);
        channel.setDescription(description);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        assert notificationManager != null;
        notificationManager.createNotificationChannel(channel);
    }

    private Bitmap createBitmapFromString(String inputNumber) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(100);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.WHITE);
        Rect textBounds = new Rect();
        paint.getTextBounds(inputNumber, 0, inputNumber.length(), textBounds);

        Bitmap bitmap = Bitmap.createBitmap(textBounds.width() + 10, 70,
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        canvas.drawText(inputNumber, textBounds.width() / 2 + 5, 70, paint);
        return bitmap;
    }

    final Runnable updateTask = new Runnable() {
        @Override
        public void run() {
            if(TimeService.getCurrentSecPercent() != null)
                updateCurrentTime();
            handler.postDelayed(this, App.TIME_DELAYED);
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

