package com.example.to_the_traveler.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.to_the_traveler.R;
import com.example.to_the_traveler.adapters.SharedAnimalAdapter;
import com.example.to_the_traveler.models.SharedAnimal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SharedAnimalAdapter adapter;
    private List<SharedAnimal> sharedAnimalList;
    private FirebaseFirestore db;
    private ProgressBar progressBar;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progress_bar);
        btnLogout = findViewById(R.id.btn_logout);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        sharedAnimalList = new ArrayList<>();
        adapter = new SharedAnimalAdapter(this, sharedAnimalList, true); // מצב מנהל
        recyclerView.setAdapter(adapter);

        loadPendingSharedAnimals(); // טוען שיתופים שממתינים לאישור

        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(AdminDashboardActivity.this, MainActivity.class));
            finish();
        });
    }

    private void loadPendingSharedAnimals() {
        progressBar.setVisibility(View.VISIBLE);
        db.collection("shared_animals")
                .whereEqualTo("approved", false) // רק שיתופים שממתינים לאישור
                .get()
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        sharedAnimalList.clear();
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                                SharedAnimal sharedAnimal = doc.toObject(SharedAnimal.class);
                                if (sharedAnimal != null) {
                                    sharedAnimal.setId(doc.getId());
                                    sharedAnimalList.add(sharedAnimal);
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "שגיאה בטעינת הנתונים", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
