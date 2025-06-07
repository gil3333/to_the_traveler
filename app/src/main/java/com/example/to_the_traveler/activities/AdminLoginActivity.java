package com.example.to_the_traveler.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.to_the_traveler.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdminLoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        etEmail = findViewById(R.id.et_admin_email);
        etPassword = findViewById(R.id.et_admin_password);
        btnLogin = findViewById(R.id.btn_admin_login);
        progressBar = findViewById(R.id.progressBar);

        btnLogin.setOnClickListener(v -> loginAdmin());
    }

    private void loginAdmin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "אנא מלא את כל השדות", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            checkIfUserIsAdmin(user.getUid(), email);
                        }
                    } else {
                        Toast.makeText(this, "שגיאה בהתחברות", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkIfUserIsAdmin(String userId, String email) {
        DocumentReference userRef = db.collection("users").document(userId);
        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Boolean isAdmin = documentSnapshot.getBoolean("isAdmin");
                if (isAdmin != null && isAdmin) {
                    Toast.makeText(this, "ברוך הבא, מנהל!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AdminLoginActivity.this, AdminDashboardActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "אין לך הרשאות מנהל", Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                }
            } else {
                // אם אין מסמך, ניצור אחד חדש כמשתמש רגיל
                createUserDocument(userId, email, false);
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "שגיאה בבדיקת הרשאות", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
        });
    }

    private void createUserDocument(String userId, String email, boolean isAdmin) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("isAdmin", isAdmin);

        db.collection("users").document(userId)
                .set(userData)
                .addOnSuccessListener(aVoid -> {
                    if (isAdmin) {
                        startActivity(new Intent(AdminLoginActivity.this, AdminDashboardActivity.class));
                    } else {
                        startActivity(new Intent(AdminLoginActivity.this, MainActivity.class));
                    }
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "שגיאה ביצירת משתמש", Toast.LENGTH_SHORT).show());
    }
}
