package com.example.kiddo.Controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kiddo.Model.TaskInfo;
import com.example.kiddo.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import android.content.Intent;

public class PointsManager {
    private SharedPreferences preferences;
    private static final String PREFS_NAME = "MyPrefs";
    private static final String POINTS_KEY = "points";
    private static final String LAST_POINTS_TIME_KEY = "lastPointsTime";
    private FirebaseManager firebaseManager;

    public PointsManager(Context context) {
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        firebaseManager = new FirebaseManager(); // تهيئة FirebaseManager

    }

    public int getPoints() {
        return preferences.getInt(POINTS_KEY, 0);

    }

    public void addPoints(int pointsToAdd) {
        int currentPoints = getPoints();
        currentPoints += pointsToAdd;
        preferences.edit().putInt(POINTS_KEY, currentPoints).apply();
    }

    public void showCompletionDialog(Context context, String taskName) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_complete_task, null);

        TextView textTotalPoints = dialogView.findViewById(R.id.textTotalPoints);
        Button buttonClose = dialogView.findViewById(R.id.buttonClose);
        String resultText = "مرحى..لقد حصلت على نقاط اضافية لقيامي ب"+taskName;
        Button shareButton = dialogView.findViewById(R.id.shareButton);
        shareButton.setOnClickListener(v -> {
            shareOnDefaultSocialMedia(resultText,context);
        });
        // تحديث النقاط في الـ Dialog
        int totalPoints = getPoints(); // الحصول على النقاط الحالية
        textTotalPoints.setText("مجموع النقاط: " + totalPoints);

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();


        if (canAddPoints()) {
            addPoints(10); // أضف 10 نقاط عند قراءة سورة
            TaskInfo taskInfo = new TaskInfo(taskName,
                    getCurrentDate(), null, 10, true);

            buttonClose.setOnClickListener(v -> {
                String userId = firebaseManager.getCurrentUserId(); // استخدام الدالة الجديدة
                if (userId != null) {
                    firebaseManager.storeTaskInfo( taskInfo); // تخزين المعلومات
                } else {
                    Toast.makeText(context, "لم يتم التعرف على المستخدم", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss(); // إغلاق الـ Dialog
            });


        } else {
            Toast.makeText(context, "لقد حصلت على النقاط بالفعل اليوم.", Toast.LENGTH_SHORT).show();
            return;
        }


        dialog.show(); // عرض الـ Dialog
    }


    public void showCompletionDialogWithImage(Context context, String taskName, Bitmap bitmap) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_complete_task, null);

        TextView textTotalPoints = dialogView.findViewById(R.id.textTotalPoints);
        Button buttonClose = dialogView.findViewById(R.id.buttonClose);
        String resultText = "مرحى..لقد حصلت على نقاط اضافية لقيامي ب"+taskName;
        Button shareButton = dialogView.findViewById(R.id.shareButton);
        shareButton.setOnClickListener(v -> {
            shareOnDefaultSocialMedia(resultText,context);
        });
        // تحديث النقاط في الـ Dialog
        int totalPoints = getPoints(); // الحصول على النقاط الحالية
        textTotalPoints.setText("مجموع النقاط: " + totalPoints);

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        if (canAddPoints()) {
            addPoints(10); // أضف 10 نقاط عند قراءة سورة
            TaskInfo taskInfo = new TaskInfo(taskName,
                    getCurrentDate(), null, 10, true);

            buttonClose.setOnClickListener(v -> {
                String userId = firebaseManager.getCurrentUserId(); // استخدام الدالة الجديدة
                if (userId != null) {
                    firebaseManager.storeBedMakingInfo(context, taskInfo, bitmap); // تخزين المعلومات
                } else {
                    Toast.makeText(context, "لم يتم التعرف على المستخدم", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss(); // إغلاق الـ Dialog
            });


        } else {
            Toast.makeText(context, "لقد حصلت على النقاط بالفعل اليوم.", Toast.LENGTH_SHORT).show();
            return;
        }
        dialog.show(); // عرض الـ Dialog

    }

    public boolean canAddPoints() {
        long currentTime = System.currentTimeMillis();
        long lastPointsTime = preferences.getLong(LAST_POINTS_TIME_KEY, 0);

        if (isSameDay(currentTime, lastPointsTime)) {
            preferences.edit().putLong(LAST_POINTS_TIME_KEY, currentTime).apply();
            return true;
        }
        return true;
    }

    private boolean isSameDay(long currentTime, long lastPointsTime) {
        return DateFormat.format("yyyyMMdd", currentTime).equals(DateFormat.format("yyyyMMdd", lastPointsTime));
    }

    private String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }

    private void shareOnDefaultSocialMedia(String content,Context context) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, content);

        // التحقق من وجود تطبيقات تدعم المشاركة
        if (shareIntent.resolveActivity(context.getPackageManager()) != null) {
           context. startActivity(Intent.createChooser(shareIntent, "مشاركة عبر:"));
        } else {
            Toast.makeText(context, "لا يوجد تطبيقات متاحة للمشاركة", Toast.LENGTH_SHORT).show();
        }
    }

}