package com.example.to_the_traveler.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.to_the_traveler.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterFragment extends Fragment {

    private EditText etEmail, etUsername, etPassword, etConfirmPassword, etPhone;
    private Button btnRegister;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    public RegisterFragment() {
        // קונסטרקטור ריק חובה לפרגמנט
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        etEmail = view.findViewById(R.id.et_email);
        etUsername = view.findViewById(R.id.et_username);
        etPassword = view.findViewById(R.id.et_password);
        etConfirmPassword = view.findViewById(R.id.et_confirm_password);
        etPhone = view.findViewById(R.id.et_phone);
        btnRegister = view.findViewById(R.id.btn_register);
        progressBar = view.findViewById(R.id.progress_bar);

        btnRegister.setOnClickListener(v -> registerUser());

        return view;
    }

    private void registerUser() {
        String email = etEmail.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        // בדיקות תקינות
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("כתובת אימייל לא תקינה");
            return;
        }
        if (TextUtils.isEmpty(username)) {
            etUsername.setError("שם משתמש נדרש");
            return;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            etPassword.setError("סיסמה חייבת להיות לפחות 6 תווים");
            return;
        }
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("הסיסמאות אינן תואמות");
            return;
        }
        if (TextUtils.isEmpty(phone) || phone.length() < 10) {
            etPhone.setError("מספר טלפון לא תקין");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        // בדיקה אם המשתמש קיים כבר
        db.collection("users").whereEqualTo("email", email).get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "האימייל כבר רשום במערכת", Toast.LENGTH_SHORT).show();
                    } else {
                        checkUsernameAvailability(username, email, password, phone);
                    }
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "שגיאה בבדיקת משתמש", Toast.LENGTH_SHORT).show();
                });
    }

    private void checkUsernameAvailability(String username, String email, String password, String phone) {
        db.collection("users").whereEqualTo("username", username).get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "שם המשתמש כבר תפוס", Toast.LENGTH_SHORT).show();
                    } else {
                        createUser(email, password, username, phone);
                    }
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "שגיאה בבדיקת שם משתמש", Toast.LENGTH_SHORT).show();
                });
    }

    private void createUser(String email, String password, String username, String phone) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            saveUserToFirestore(user.getUid(), email, username, phone);
                        }
                    } else {
                        Toast.makeText(getContext(), "שגיאה ביצירת משתמש", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserToFirestore(String userId, String email, String username, String phone) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("email", email);
        userMap.put("username", username);
        userMap.put("phone", phone);
        userMap.put("isAdmin", false); // ברירת מחדל - לא מנהל

        db.collection("users").document(userId).set(userMap)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "המשתמש נרשם בהצלחה!", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(getView()).navigate(R.id.action_registerFragment_to_homeFragment);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "שגיאה בשמירת נתונים", Toast.LENGTH_SHORT).show());
    }
}
