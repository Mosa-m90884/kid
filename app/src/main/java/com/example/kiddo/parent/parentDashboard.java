package com.example.kiddo.parent;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kiddo.Controller.FirebaseManager;
import com.example.kiddo.Login.LoginActivity;
import com.example.kiddo.R;
import com.example.kiddo.SplashActivity;
import com.example.kiddo.Tasks.ArrangeBedActivity;


public class parentDashboard extends AppCompatActivity {
private  FirebaseManager firebaseManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_parent_dashboard);
firebaseManager=new FirebaseManager();
        Button supervision = findViewById(R.id.supervision);
        Button signOut = findViewById(R.id.parentSignout);
        signOut.setOnClickListener(view -> {
            firebaseManager.signOut(); // تسجيل الخروج من Firebase

            Intent intent = new Intent(this, SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        supervision.setOnClickListener(v -> {
            Intent intent = new Intent(this, childrenActivity.class);
            startActivity(intent);
            // يمكنك إضافة المنطق لتنفيذ المهمة هنا
        });

}}