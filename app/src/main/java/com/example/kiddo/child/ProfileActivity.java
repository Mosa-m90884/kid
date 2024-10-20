package com.example.kiddo.child;
import android.content.Intent;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.widget.ArrayAdapter;

import com.example.kiddo.Controller.FirebaseManager;
import com.example.kiddo.Model.Child;
import com.example.kiddo.R;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int CAMERA_PERMISSION_CODE = 101;
    private FirebaseManager firebaseManager;
    private ProgressBar progressBar;

    private EditText usernameEditText;
    private Spinner ageSpinner;
    private Button saveButton;
    private ImageView cameraButton;
    private CheckBox boyCheckBox;
    private CheckBox girlCheckBox;
    private ImageView profileImage;
    Child child;
    Bitmap imageBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // تعريف العناصر
        usernameEditText = findViewById(R.id.username_edit);
        ageSpinner = findViewById(R.id.language_spinner);
        saveButton = findViewById(R.id.save);
        cameraButton = findViewById(R.id.openCameraProfile);
        boyCheckBox = findViewById(R.id.child_gender_checkbox);
        girlCheckBox = findViewById(R.id.girl_gender_checkbox);
        profileImage = findViewById(R.id.profile_image);
        progressBar = findViewById(R.id.progressBar3);

        // إعداد الـ Spinner مع عنوان
        String[] ageOptions = {"اختر الفئة العمرية", "3", "4", "5", "6", "7","8","9"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ageOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageSpinner.setAdapter(adapter);
//

        // إعداد زر الكاميرا
        cameraButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
            } else {
                openCamera();
            }
        });
        firebaseManager =new FirebaseManager();


        // إعداد CheckBox
        boyCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                girlCheckBox.setChecked(false); // إلغاء اختيار CheckBox الفتاة
            }
        });

        girlCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {

                boyCheckBox.setChecked(false); // إلغاء اختيار CheckBox الولد
            }
        });
        setInfo();

        // إعداد زر الحفظ
       saveButton.setOnClickListener(v -> saveChildInfo());
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
             imageBitmap = (Bitmap) extras.get("data");
            profileImage.setImageBitmap(imageBitmap); // تعيين الصورة في ImageView
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Permission denied to access camera", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void saveChildInfo() {

        // قراءة المعلومات من الحقول
        String username = usernameEditText.getText().toString().trim();
        String age = ageSpinner.getSelectedItem().toString();
        boolean isBoy = boyCheckBox.isChecked();
        boolean isGirl = girlCheckBox.isChecked();

        // التحقق من أن جميع الحقول تم ملؤها بشكل صحيح
        if (username.isEmpty() || age.equals("اختر الفئة العمرية") || (!isBoy && !isGirl)) {
            Toast.makeText(this, "يرجى ملء جميع الحقول", Toast.LENGTH_SHORT).show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        // إنشاء كائن Child
child.setUsername(username);
child.setGendar(isBoy ? "طفل" : "طفلة");
child.setOld(age);
        // تخزين المعلومات في Firebase عبر FirebaseManager
        firebaseManager.completProfileInfo(child,imageBitmap,progressBar , this);
        //progressBar.setVisibility(View.INVISIBLE);
/**if(progressBar.isActivated())
//      finish();**/

}

    @Override
    protected void onStop() {
       // Toast.makeText(this, "يرجى ملء جميع الحقول", Toast.LENGTH_SHORT).show();
        setInfo();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        setInfo();

        super.onDestroy();
    }

    void setInfo(){

    firebaseManager.getChildrenId()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    child = task.getResult();
                    // تعيين المعلومات في الحقول
                    Picasso.get()
                            .load(child.getImageUrl())
                            .placeholder(R.drawable.child) // صورة مؤقتة أثناء التحميل
                            .into(profileImage);
                    usernameEditText.setText(child.getUsername());
                    ageSpinner.setSelection(getSpinnerIndex(ageSpinner, child.getOld())); // تعيين العمر
                    if ("طفل".equals(child.getGendar())) {
                        boyCheckBox.setChecked(true);
                        girlCheckBox.setChecked(false);
                    } else {
                        girlCheckBox.setChecked(true);
                        boyCheckBox.setChecked(false);
                    }
                }
            });
}
    private int getSpinnerIndex(Spinner spinner, String age) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(age)) {
                return i;
            }
        }
        return 0; // إرجاع 0 إذا لم يتم العثور على العمر
    }

}