package com.example.kiddo.Login;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kiddo.Controller.FirebaseManager;
import com.example.kiddo.InputValidator;
import com.example.kiddo.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SignUpActivity extends AppCompatActivity {

    private EditText usernameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private Button signUpButton;
    private FirebaseManager firebaseManager;
    private ProgressBar progressBar;
    private InputValidator inputValidator;
    private Spinner roleSpinner;
    private TextInputLayout emailInputLayout;
    private TextInputEditText parentEmail;
    private String accountType = "ولد";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseManager = new FirebaseManager();
        inputValidator = new InputValidator();
        usernameEditText = findViewById(R.id.sign_username);
        emailEditText = findViewById(R.id.sign_email);
        passwordEditText = findViewById(R.id.sign_password);
        confirmPasswordEditText = findViewById(R.id.sign_confirm_password);
        signUpButton = findViewById(R.id.sign_sign);
        progressBar = findViewById(R.id.progressBar);
        roleSpinner = findViewById(R.id.sign_account_type);
        emailInputLayout = findViewById(R.id.email_input_layout);
        parentEmail = findViewById(R.id.child_email);
        // تعريف مصفوفة الخيارات مباشرة في الكود
        String[] roles = {"ولد", "أب"};

        // إعداد الـ Spinner باستخدام المصفوفة
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, roles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);


        roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) { // إذا كان الخيار هو "ولد"
                    emailInputLayout.setVisibility(View.VISIBLE);
                } else { // إذا كان الخيار هو "أب"
                    emailInputLayout.setVisibility(View.GONE); // إخفاء حقل البريد الإلكتروني
                    parentEmail.setText(""); // مسح المحتوى

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                emailInputLayout.setVisibility(View.GONE); // إخفاء إذا لم يتم تحديد شيء
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountType = roleSpinner.getSelectedItem().toString(); // الحصول على الدور المحدد

                // التحقق من صحة المدخلات
                boolean isValid = true;
                isValid &= inputValidator.validateUsername(usernameEditText);
                isValid &= inputValidator.validateEmail(emailEditText);
                isValid &= inputValidator.validatePassword(passwordEditText);
                isValid &= inputValidator.validateConfirmPassword(passwordEditText, confirmPasswordEditText);

                if (isValid) {
                    signUp(usernameEditText.getText().toString().trim(),
                            emailEditText.getText().toString().trim(),
                            parentEmail.getText().toString().trim(),
                            passwordEditText.getText().toString());
                }
            }
        });
    }

    private void signUp(String username, String email, String parentEmai, String password) {
        // إظهار شريط التحميل

        progressBar.setVisibility(View.VISIBLE);
        if (accountType.equals("ولد")) {
            firebaseManager.saveChildToFirestore(username, email, parentEmai, password)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(SignUpActivity.this, "تم تسجيل الطفل بنجاح. معرف الطفل: ", Toast.LENGTH_SHORT).show();
                        finish(); // إنهاء Activity تسجيل الطفل
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(SignUpActivity.this, "خطأ في التسجيل: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });

        } else
            // التسجيل باستخدام Firebase Authentication
            firebaseManager.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE); // إخفاء شريط التحميل
                        if (task.isSuccessful()) {
                            // التسجيل ناجح
                            Toast.makeText(SignUpActivity.this, "تم التسجيل بنجاح", Toast.LENGTH_SHORT).show();

                            // الحصول على UID للمستخدم المسجل
                            String userId = firebaseManager.getCurrentUserId();

                            // التحقق من نوع الحساب من السباينر

                            // إضافة معلومات أب
                            firebaseManager.addParentToDatabase(userId, username, email);


                            // الانتقال إلى النشاط الرئيسي
                            //  startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                            // finish();
                        } else {
                            // فشل التسجيل
                            Toast.makeText(SignUpActivity.this, "فشل التسجيل: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
    }

}