package com.example.kiddo.Tasks;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kiddo.Constants;
import com.example.kiddo.Controller.FirebaseManager;
import com.example.kiddo.Controller.PointsManager;
import com.example.kiddo.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ReadQuranActivity extends AppCompatActivity {

    private TextView textQuran;
    private Spinner spinnerSurah;
    private PointsManager pointsManager; // تعريف PointsManager
    private int points;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_quran);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide(); // إخفاء AppBar
        }
        Button buttonBack = findViewById(R.id.buttonBack);
        textQuran = findViewById(R.id.textQuran);
        spinnerSurah = findViewById(R.id.spinnerSurah);
        Button buttonOpenQuran = findViewById(R.id.buttonOpenQuran);
        pointsManager = new PointsManager(this); // تهيئة PointsManager
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, Constants.SURAH_NAMES);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSurah.setAdapter(adapter);
        buttonBack.setOnClickListener(v -> finish()); // العودة للصفحة السابقة
        buttonOpenQuran.setOnClickListener(v -> {
            int selectedIndex = spinnerSurah.getSelectedItemPosition(); // الحصول على رقم السورة
             String selectedSurah = spinnerSurah.getSelectedItem().toString();
            String fileName = selectedIndex + 1 + ".txt"; // تأكد من أن أسماء الملفات تتطابق

            // طباعة رقم السورة
            // Toast.makeText(this, "رقم السورة: " + (selectedIndex + 1), Toast.LENGTH_SHORT).show();


            loadQuranText(fileName);
            pointsManager.showCompletionDialog(this,"قراءة القرآن الكريم - " + selectedSurah);

        });
    }

    private void loadQuranText(String filename) {
        StringBuilder quranText = new StringBuilder();
        AssetManager assetManager = getAssets();
        // إضافة البسملة
        quranText.append("بِسْمِ اللَّهِ الرَّحْمَـٰنِ الرَّحِيمِ\n\n");
        try {
            InputStream inputStream = assetManager.open(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                quranText.append(line).append("\n");
            }
            quranText.append("\nصدق الله العظيم");

            textQuran.setText(quranText.toString());
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "خطأ في تحميل نص القرآن", Toast.LENGTH_SHORT).show();
        }
    }


}
