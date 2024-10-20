package com.example.kiddo.child;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kiddo.R;

public class PointsActivity extends AppCompatActivity {

    private TextView textTotalPoints;
    private SharedPreferences preferences; // تعريف SharedPreferences
    private int totalPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_complete_task);

        // إعداد SharedPreferences
        loadPreferences();
        totalPoints = preferences.getInt("points", 0);

        textTotalPoints = findViewById(R.id.textTotalPoints);
        textTotalPoints.setText("مجموع النقاط: " + totalPoints);

        TextView textMessage = findViewById(R.id.textMessage);

        Button buttonClose = findViewById(R.id.buttonClose);
        buttonClose.setOnClickListener(v -> finish()); // إغلاق Activity
    }

    private void loadPreferences() {
        preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
    }
}