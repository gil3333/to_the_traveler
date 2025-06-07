package com.example.to_the_traveler.activities;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.to_the_traveler.R;
import com.example.to_the_traveler.adapters.SharedAnimalAdapter;
import com.example.to_the_traveler.models.SharedAnimal;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class ManageSharedDataActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SharedAnimalAdapter adapter;
    private List<SharedAnimal> sharedAnimalList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_shared_data);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sharedAnimalList = new ArrayList<>();
        adapter = new SharedAnimalAdapter(this, sharedAnimalList, true); // ✅ מנהל
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loadUnapprovedAnimals();
    }

    private void loadUnapprovedAnimals() {
        db.collection("shared_animals")
                .whereEqualTo("approved", false) // ✅ הצגת רק הודעות שטרם אושרו
                .addSnapshotListener((querySnapshot, error) -> {
                    if (error != null) return;

                    sharedAnimalList.clear();
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        SharedAnimal sharedAnimal = doc.toObject(SharedAnimal.class);
                        sharedAnimalList.add(sharedAnimal);
                    }
                    adapter.notifyDataSetChanged();
                });
    }
}
