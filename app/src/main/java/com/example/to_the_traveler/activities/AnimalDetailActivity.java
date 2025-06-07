package com.example.to_the_traveler.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.to_the_traveler.R;

public class AnimalDetailActivity extends AppCompatActivity {
    private ImageView animalImageView;
    private TextView animalNameTextView, animalDescriptionTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_detail);

        // אתחול רכיבי ה-XML
        animalImageView = findViewById(R.id.animal_image);
        animalNameTextView = findViewById(R.id.animal_name);
        animalDescriptionTextView = findViewById(R.id.animal_description);

        // קבלת נתונים שהועברו דרך Intent
        Intent intent = getIntent();
        if (intent != null) {
            String animalName = intent.getStringExtra("name");
            String animalDescription = intent.getStringExtra("description");
            int imageResId = intent.getIntExtra("imageResId", R.drawable.error);

            animalNameTextView.setText(animalName);
            animalDescriptionTextView.setText(animalDescription);
            animalImageView.setImageResource(imageResId); // הצגת תמונה מ-drawable
        }
    }
}
