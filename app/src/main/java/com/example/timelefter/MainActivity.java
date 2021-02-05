package com.example.timelefter;

import android.app.Notification;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    final Handler handler=new Handler();
    TextView textView;
    Boolean showNotification = true;
    ProgressBar progressBar;
    TextView remainTimeTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        remainTimeTv = findViewById(R.id.remainTimeTv);

        textView = findViewById(R.id.TView);

        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(100);
        progressBar.setRotation(-90);
        progressBar.setRotationY(180);

        Intent serviceIntent = new Intent(this, ForegroundService.class);
        if(TimeService.getCurrentSecPercent() > 0)
            ContextCompat.startForegroundService(this, serviceIntent);

        handler.postDelayed(updateTask, App.TIME_DELAYED);
    }

    final Runnable updateTask = new Runnable() {
        @Override
        public void run() {
            if(showNotification)
                updateCurrentTime();
            handler.postDelayed(this, App.TIME_DELAYED);
        }
    };

    public void updateCurrentTime(){

        String result = "Done";
        double currentSecondPercent = TimeService.getCurrentSecPercent();


            if(currentSecondPercent > 0)
            {
                progressBar.setProgress((int)currentSecondPercent);
                showNotification = true;

                result = String.format("%.3f", currentSecondPercent) + "%";
                int intPercent = (int)currentSecondPercent;
                String shortText = "";
                if(intPercent >= 100){
                    shortText = String.valueOf(intPercent);
                }
                else if(intPercent < 100 && intPercent > 9){
                    shortText = intPercent + "%";
                }
                else {
                    shortText = String.format("%.1f", currentSecondPercent) + "%";
                }

                remainTimeTv.setText(TimeService.getRemainTime());
            }
            else
                showNotification = false;

        textView.setText(result);
    }

    // old btns
    public void startService(View v) {
//        String input = editTextInput.getText().toString();
        Intent serviceIntent = new Intent(this, ForegroundService.class);
  //      serviceIntent.putExtra(App.INPUT_STRING, input);
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    public void stopService(View v) {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        stopService(serviceIntent);
    }

}