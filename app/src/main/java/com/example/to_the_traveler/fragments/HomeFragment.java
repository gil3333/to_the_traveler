package com.example.to_the_traveler.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.to_the_traveler.R;
import com.example.to_the_traveler.activities.AdminDashboardActivity;
import com.example.to_the_traveler.activities.MainActivity;
import com.example.to_the_traveler.fragments.RegisterFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeFragment extends Fragment {

    private EditText etEmail, etPassword;
    private Button btnLogin, btnRegister;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        etEmail = view.findViewById(R.id.et_email);
        etPassword = view.findViewById(R.id.et_password);
        btnLogin = view.findViewById(R.id.btn_login);
        btnRegister = view.findViewById(R.id.btn_register); // כפתור הרשמה
        progressBar = view.findViewById(R.id.progressBar);

        btnLogin.setOnClickListener(v -> loginUser());
        btnRegister.setOnClickListener(v -> { // מאזין לכפתור ההרשמה
            Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_registerFragment);

        });

        return view;
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "אנא מלא את כל השדות", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            checkUserRole(user.getUid());
                        }
                    } else {
                        Toast.makeText(getContext(), "שגיאה בהתחברות", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkUserRole(String userId) {
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        boolean isAdmin = documentSnapshot.getBoolean("isAdmin");
                        if (isAdmin) {
                            Toast.makeText(getContext(), "ברוך הבא, מנהל!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getActivity(), AdminDashboardActivity.class));
                        } else {
                            Toast.makeText(getContext(), "כניסה כאורח", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getActivity(), MainActivity.class));
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "שגיאה בבדיקת הרשאות", Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                });
    }
}
