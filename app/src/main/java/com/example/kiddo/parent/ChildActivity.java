package com.example.kiddo.parent;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kiddo.Controller.FirebaseManager;
import com.example.kiddo.Controller.Predictor;
import com.example.kiddo.Model.Child;
import com.example.kiddo.Model.Point;
import com.example.kiddo.Model.TaskInfo;
import com.example.kiddo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChildActivity extends AppCompatActivity {
    // تعريف المتغيرات
    private FirebaseManager firebaseManager; // لإدارة Firebase
    private int points = 0; // نقاط الطفل
    private String childId = ""; // معرف الطفل
    private TextView textpoint,txtName,txtAge; // لعرض النقاط
    private Button resetButton; // زر إعادة تعيين النقاط
    private ImageButton backButton; // زر الرجوع
    private ListView listView; // لعرض الأنشطة
    private ArrayAdapter<String> adapter; // محول البيانات للقائمة
    private List<String> activities; // قائمة الأنشطة
    private Child child;
    private Predictor predictor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);
        try {//تعريف كلاس معتمد على نموذج التنبؤ المدرب على الشيكات العصبونية
            predictor = new Predictor(this, "model.tflite");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // ربط عناصر الواجهة
        textpoint = findViewById(R.id.child_points);
        txtName = findViewById(R.id.pchild_name);
        txtAge = findViewById(R.id.pchild_age);

        resetButton = findViewById(R.id.reset_button);
        backButton = findViewById(R.id.parent_btn_back);

        // إعداد FirebaseManager
        firebaseManager = new FirebaseManager();
loadInfo();
        // استلام معرف الطفل من Intent
        childId = getIntent().getStringExtra("child_id");

        // إعداد ListView
        listView = findViewById(R.id.list_view);

        // إضافة بيانات تجريبية
        activities = new ArrayList<>();
        activities.add(""); // إضافة عنصر فارغ لتهيئة القائمة

        // إعداد ArrayAdapter للقائمة
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, activities);
        listView.setAdapter(adapter);

        // تحميل المهام والنقاط
        loadTasks();
        showPoints();
        // زر الرجوع
        backButton.setOnClickListener(v -> onBackPressed());
        // إعداد مستمع لزر إعادة تعيين النقاط
        resetButton.setOnClickListener(view -> {
            boolean isEngaged = predictor.predict(Float.parseFloat(child.getOld()), points);
            if(isEngaged)
                Toast.makeText(this,"متفاعل لانجاز وظائفه",Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this,"غير متفاعل لانجاز وظائفه ويحتاج لتوعية",Toast.LENGTH_LONG).show();


            showGiftDialog(points); // عرض حوار اختيار الهدية
        });
    }

    // دالة لعرض نقاط الطفل
    void showPoints() {
        firebaseManager.getChildPoints(childId)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Long points = task.getResult(); // استرجاع النقاط
                        this.points = Math.toIntExact(points); // تحويل النقاط إلى int
                        textpoint.setText( ""+points+" نقاط "); // تحديث واجهة المستخدم
                    } else {
                        // التعامل مع الأخطاء هنا
                    }
                });
    }

    // دالة لعرض حوار اختيار الهدية
    private void showGiftDialog(int currentPoints) {
        // إنشاء الحوار
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_gift_selection, null);
        builder.setView(dialogView);

        // إعداد قائمة الهدايا
        String[] gifts = {"هدية 1 (10 نقاط)", "هدية 2 (20 نقاط)", "هدية 3 (30 نقاط)"};
        int[] pointsRequired = {10, 20, 30}; // النقاط المطلوبة لكل هدية
        final int p = currentPoints; // حفظ النقاط الحالية
        Spinner giftSpinner = dialogView.findViewById(R.id.gift_spinner);

        // إعداد ArrayAdapter لقائمة الهدايا
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gifts);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        giftSpinner.setAdapter(adapter);

        Button giveGiftButton = dialogView.findViewById(R.id.give_gift_button);

        // إعداد الحوار
        AlertDialog dialog = builder.create();

        // عند الضغط على زر منح الهدية
        giveGiftButton.setOnClickListener(v -> {
            int selectedPosition = giftSpinner.getSelectedItemPosition(); // الحصول على الاختيار
            int requiredPoints = pointsRequired[selectedPosition]; // النقاط المطلوبة

            if (p >= requiredPoints) { // التحقق من كفاية النقاط
                firebaseManager.resetPoints(new Point(childId, p - requiredPoints)); // خصم النقاط
                showPoints(); // تحديث النقاط في الواجهة
                Toast.makeText(this, "تم منح الهدية: " + gifts[selectedPosition], Toast.LENGTH_SHORT).show();
                dialog.dismiss(); // إغلاق الحوار
            } else {
                Toast.makeText(this, "عدد النقاط غير كافٍ لمنح هذه الهدية: " + p, Toast.LENGTH_SHORT).show();
            }
        });

        // عرض الحوار
        dialog.show();
    }

    // دالة لتحميل المهام للطفل
    private void loadTasks() {
        firebaseManager.getTasks(childId, getApplicationContext()).addOnCompleteListener(new OnCompleteListener<List<TaskInfo>>() {
            @Override
            public void onComplete(Task<List<TaskInfo>> task) {
                if (task.isSuccessful()) {
                    activities.clear(); // مسح الأنشطة السابقة
                    for (TaskInfo taskItem : task.getResult()) {
                        activities.add(taskItem.getTaskName() + " - " + taskItem.getDate()); // إضافة الأنشطة إلى القائمة
                    }
                    adapter.notifyDataSetChanged(); // تحديث القائمة
                } else {
                    Toast.makeText(ChildActivity.this, "فشل في تحميل المهام", Toast.LENGTH_LONG).show();
                    // التعامل مع الأخطاء هنا
                }
            }
        });
    }

    void loadInfo() {

        firebaseManager.getChildrenId()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        child = task.getResult();
                        txtName.setText(child.getUsername());
                        if(child.getOld()!=null)
                            txtAge.setText(child.getOld());

                    }
                });

    }
}