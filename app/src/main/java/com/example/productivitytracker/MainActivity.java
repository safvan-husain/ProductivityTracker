package com.example.productivitytracker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {
    private TextView timerTextView;
    private TextView totalHoursTextView;
    private MaterialButton startStopButton;
    private long startTime = 0;
    private boolean isRunning = false;
    private DataBaseHelper dbHelper;
    private ListView listView;
    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = DataBaseHelper.getInstance(this);
//        loadHistory();

        setContentView(R.layout.mainactivity);

        timerTextView = findViewById(R.id.timerTextView);
        startStopButton = findViewById(R.id.startStopButton);
        totalHoursTextView = findViewById(R.id.totalHours);

        totalHoursTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("totalHoursTextView clicked");
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });

        startStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRunning) {
                    startTime = SystemClock.elapsedRealtime();
                    isRunning = true;
                    startStopButton.setText("Stop");
                    startTimer();
                } else {
                    stopTimer();
                }
            }
        });
        System.out.println("onCreate called");
        loadHistory();
    }

    private void loadHistory() {
        String totalProductiveHours = dbHelper.getTotalProductiveHours();
        totalHoursTextView.setText(totalProductiveHours);
    }


    private void startTimer() {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsedTime = SystemClock.elapsedRealtime() - startTime;
                int hours = (int) (elapsedTime / 3600000);
                int minutes = (int) (elapsedTime % 3600000) / 60000;
                int seconds = (int) (elapsedTime % 60000) / 1000;

                String formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                timerTextView.setText(formattedTime);

                if (isRunning) {
                    handler.postDelayed(this, 1000); // Update every 1 second
                }
            }
        });
    }

    private void stopTimer() {
        long durationInMilli = SystemClock.elapsedRealtime() - startTime;
        isRunning = false;
        startStopButton.setText("Start");

        ActivityDescriptionDialogFragment dialogFragment = new ActivityDescriptionDialogFragment();
        dialogFragment.setListener(new ActivityDescriptionDialogFragment.ActivityDescriptionListener() {
            @Override
            public void onActivityDescriptionEntered(String description) {
                // Save the duration and description to the database
                DataModel data = new DataModel(description, (int) durationInMilli);
                dbHelper.saveEntry(data);
                timerTextView.setText("0:00");
            }
        });
        dialogFragment.show(getSupportFragmentManager(), "activityDescriptionDialog");
        loadHistory();
    }
}