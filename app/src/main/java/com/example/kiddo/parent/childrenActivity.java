package com.example.kiddo.parent;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kiddo.Controller.FirebaseManager;
import com.example.kiddo.Model.Child;
import com.example.kiddo.R;

import java.util.ArrayList;
import java.util.List;

public class childrenActivity extends AppCompatActivity {
    private ListView childrenListView; // ListView لعرض أسماء الأطفال
    private FirebaseManager firebaseManager;
    private String parentId; // معرف الأب (يمكن الحصول عليه من الجلسة)
    private ArrayAdapter<String> adapter; // Adapter لعرض أسماء الأطفال

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children);


        childrenListView = findViewById(R.id.childrenListView);
        firebaseManager = new FirebaseManager();
        // parentId = firebaseManager.getCurrentUserId();// استبدلها بمعرف الأب المناسب
        // جلب البيانات
        loadChildren();
    }

    private void loadChildren() {
        String userId = firebaseManager.getCurrentUserId(); // استخدام الدالة الجديدة

        Toast.makeText(this, ""+userId, Toast.LENGTH_SHORT).show();

        firebaseManager.getChildrenByParentId(userId)
                .addOnSuccessListener(children -> {
                    displayChildren(children);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "خطأ في جلب البيانات: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

        private void displayChildren(List<Child> children) {
            ChildAdapter adapter = new ChildAdapter(this, children);
            childrenListView.setAdapter(adapter);
        }

}