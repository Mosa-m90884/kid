package com.example.kiddo.Tasks;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kiddo.Controller.PointsManager;
import com.example.kiddo.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ReadHadithActivity extends AppCompatActivity {

    private TextView textHadith;
    private PointsManager pointsManager; // تعريف PointsManager

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_hadith);

        Button buttonBack = findViewById(R.id.buttonBack);
        textHadith = findViewById(R.id.textHadith);
        Button buttonOpenHadith = findViewById(R.id.buttonOpenHadith);
        pointsManager = new PointsManager(this); // تهيئة PointsManager

        buttonBack.setOnClickListener(v -> finish()); // العودة للصفحة السابقة

        buttonOpenHadith.setOnClickListener(v ->{

            loadHadithText("ahadeth.txt");
            pointsManager.showCompletionDialog(this,"قراءة الحديث الشريف - " );

        }); // استبدل باسم الملف المطلوب




    }

    private void loadHadithText(String filename) {
        StringBuilder hadithText = new StringBuilder();
        AssetManager assetManager = getAssets();

        // إضافة "بسم الله الرحمن الرحيم"
        hadithText.append("بِسْمِ اللَّهِ الرَّحْمَـٰنِ الرَّحِيمِ\n\n");

        try {
            InputStream inputStream = assetManager.open(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                hadithText.append(line).append("\n");
            }

            textHadith.setText(hadithText.toString());
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "خطأ في تحميل نص الحديث", Toast.LENGTH_SHORT).show();
        }
    }
}