package com.example.to_the_traveler.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.to_the_traveler.R;
import com.example.to_the_traveler.activities.MainActivity;

import java.util.Locale;

public class SettingsFragment extends Fragment {

    private Button btnChangeLanguage;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        btnChangeLanguage = view.findViewById(R.id.btn_change_language);
        sharedPreferences = requireActivity().getSharedPreferences("Settings", getContext().MODE_PRIVATE);

        // קבלת השפה הנוכחית
        String currentLanguage = sharedPreferences.getString("language", "en");
        updateButtonText(currentLanguage);

        btnChangeLanguage.setOnClickListener(v -> {
            String newLanguage = currentLanguage.equals("en") ? "iw" : "en";
            setLocale(newLanguage);
        });

        return view;
    }
    private void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = new Configuration(resources.getConfiguration());
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());

        // שמירת השפה החדשה בזיכרון של האפליקציה
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("language", languageCode);
        editor.apply();

        // אתחול מחדש של האפליקציה כדי לעדכן את כל הפרגמנטים
        Intent intent = new Intent(requireActivity(), MainActivity.class);
        requireActivity().finish();
        startActivity(intent);
    }

    private void updateButtonText(String languageCode) {
        if (languageCode.equals("en")) {
            btnChangeLanguage.setText(getString(R.string.change_language_to_hebrew));
        } else {
            btnChangeLanguage.setText(getString(R.string.change_language_to_english));
        }
    }
}
