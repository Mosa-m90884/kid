package com.example.kiddo.Tasks;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kiddo.Controller.FirebaseManager;
import com.example.kiddo.Controller.PointsManager;
import com.example.kiddo.Login.LoginActivity;
import com.example.kiddo.R;
import com.google.firebase.auth.FirebaseAuth;

public class ArrangeBedActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView imageBed;
    private Button button2;
    private Uri imageUri; // لحفظ رابط الصورة التي تم اختيارها
    private PointsManager pointsManager; // تعريف PointsManager
    Bitmap imageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrange_bed);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide(); // إخفاء AppBar
        }
        ImageButton buttonBack = findViewById(R.id.buttonBack);
        imageBed = findViewById(R.id.imageBed);
        button2 = findViewById(R.id.button22);

        Button buttonOpenCamera = findViewById(R.id.buttonOpenCamera);
        pointsManager = new PointsManager(this); // تهيئة PointsManager

        buttonBack.setOnClickListener(v -> finish()); // العودة للصفحة السابقة

        buttonOpenCamera.setOnClickListener(v -> {
           /**   Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
             startActivityForResult(intent, 1);**/
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        });
        button2.setOnClickListener(view -> {
            if (imageBitmap != null) {
                pointsManager.showCompletionDialogWithImage(this,"ترتيب السرير- " ,imageBitmap);

            }

        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {

            imageUri = data.getData();
            Bundle extras = data.getExtras(); // الحصول على رابط الصورة
            imageBitmap = (Bitmap) extras.get("data");
            imageBed.setImageBitmap(imageBitmap); // تعيين الصورة الملتقطة في ImageView
            if (imageBitmap != null) {

                // firebaseManager.storeBedMakingInfo(this, imageBitmap);
                pointsManager.showCompletionDialogWithImage(this,"ترتيب السرير- " ,imageBitmap);
            }
        }
    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // تم منح الإذن
            } else {
                // تم رفض الإذن
            }
        }
    }

}