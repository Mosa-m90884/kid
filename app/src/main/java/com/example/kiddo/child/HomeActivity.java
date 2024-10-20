package com.example.kiddo.child;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kiddo.Controller.FirebaseManager;
import com.example.kiddo.Model.Child;
import com.example.kiddo.R;
import com.example.kiddo.SplashActivity;
import com.example.kiddo.Tasks.ArrangeBedActivity;
import com.example.kiddo.Tasks.ReadHadithActivity;
import com.example.kiddo.Tasks.ReadQuranActivity;
import com.squareup.picasso.Picasso;

import java.security.PrivateKey;

public class HomeActivity extends AppCompatActivity {
    private FirebaseManager firebaseManager;
    private Child child;
    private  ImageView img,profile;
    private  TextView textWelcome;
private   TextView textpoint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide(); // إخفاء AppBar
        }

        Button buttonArrangeBed = findViewById(R.id.buttonArrangeBed);
        Button buttonReadQuran = findViewById(R.id.buttonReadQuran);
        Button buttonReadHadith = findViewById(R.id.buttonReadHadith);
        Button buttonArrangeCloset = findViewById(R.id.buttonArrangeCloset);
         profile = findViewById(R.id.appimageProfile);
         img = findViewById(R.id.imageProfile);

         textpoint = findViewById(R.id.textPoints);
        textWelcome = findViewById(R.id.textWelcome);

        Button signOut = findViewById(R.id.signout);

        firebaseManager = new FirebaseManager();
        profile.setOnClickListener(view -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            loadInfo();
        });
        // استرجاع النقاط
        loadInfo();


        buttonArrangeBed.setOnClickListener(v -> {
            Toast.makeText(this, "تم اختيار: ترتيب السرير", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ArrangeBedActivity.class);
            startActivity(intent);
            // يمكنك إضافة المنطق لتنفيذ المهمة هنا
        });

        buttonReadQuran.setOnClickListener(v -> {
            Toast.makeText(this, "تم اختيار: قراءة القرآن", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ReadQuranActivity.class);
            startActivity(intent);
            // يمكنك إضافة المنطق لتنفيذ المهمة هنا
        });

        buttonReadHadith.setOnClickListener(v -> {
            Toast.makeText(this, "تم اختيار: قراءة الحديث الشريف", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ReadHadithActivity.class);
            startActivity(intent);

            // يمكنك إضافة المنطق لتنفيذ المهمة هنا
        });

        buttonArrangeCloset.setOnClickListener(v -> {
            Toast.makeText(this, "تم اختيار: ترتيب الدولاب", Toast.LENGTH_SHORT).show();
            // يمكنك إضافة المنطق لتنفيذ المهمة هنا
        });


        signOut.setOnClickListener(view -> {
            firebaseManager.signOut(); // تسجيل الخروج من Firebase

            Intent intent = new Intent(this, SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    void loadInfo() {

        firebaseManager.getChildrenId()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        child = task.getResult();
                        // تعيين المعلومات في الحقول
                        Picasso.get()
                                .load(child.getImageUrl())
                                .placeholder(R.drawable.child) // صورة مؤقتة أثناء التحميل
                                .into(img);
                        Picasso.get()
                                .load(child.getImageUrl())
                                .placeholder(R.drawable.child) // صورة مؤقتة أثناء التحميل
                                .into(profile);

                        String la ="مرحبا " +child.getUsername();
                        textWelcome.setText(la);

                    }
                });

        firebaseManager.getChildPoints(firebaseManager.getCurrentUserId())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Long points = task.getResult();
                        textpoint.setText("لديك" + points + "نقطة");
                        // يمكنك تحديث واجهة المستخدم هنا بناءً على قيمة النقاط
                    }
                });
    }
}