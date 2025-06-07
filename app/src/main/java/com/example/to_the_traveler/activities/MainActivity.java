package com.example.to_the_traveler.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.example.to_the_traveler.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private NavController navController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this);
        }


        // חיבור הניווט עם NavController
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);

            // חיבור הניווט לתפריט התחתון
            NavigationUI.setupWithNavController(bottomNavigationView, navController);
        }
    }

    // פונקציה לשינוי שפה באפליקציה
    public void setLocale(String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        // טוען מחדש את האקטיביטי כדי שהשפה תוחל על כל הפרגמנטים
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
