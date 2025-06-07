package com.example.to_the_traveler.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.to_the_traveler.R;
import com.example.to_the_traveler.adapters.SharedAnimalAdapter;
import com.example.to_the_traveler.models.SharedAnimal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShareFragment extends Fragment {

    private EditText etFirstName, etLastName, etAnimalName, etAnimalDescription, etAnimalSources;
    private Button btnShareAnimal;
    private RecyclerView recyclerView;
    private SharedAnimalAdapter adapter;
    private List<SharedAnimal> sharedAnimalList;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share, container, false);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = auth.getCurrentUser();

        etFirstName = view.findViewById(R.id.et_first_name);
        etLastName = view.findViewById(R.id.et_last_name);
        etAnimalName = view.findViewById(R.id.et_animal_name);
        etAnimalDescription = view.findViewById(R.id.et_animal_description);
        etAnimalSources = view.findViewById(R.id.et_animal_sources);
        btnShareAnimal = view.findViewById(R.id.btn_share_animal);
        recyclerView = view.findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        sharedAnimalList = new ArrayList<>();
        adapter = new SharedAnimalAdapter(getContext(), sharedAnimalList, false);
        recyclerView.setAdapter(adapter);

        loadSharedAnimals();

        // כפתור השיתוף - רק למשתמש מחובר
        btnShareAnimal.setOnClickListener(v -> {
            if (currentUser == null) {
                Toast.makeText(getContext(), "עליך להתחבר כדי לשתף מידע!", Toast.LENGTH_SHORT).show();
            } else {
                shareAnimalInfo();
            }
        });

        return view;
    }

    private void loadSharedAnimals() {
        db.collection("shared_animals")
                .whereEqualTo("approved", true)
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

    private void shareAnimalInfo() {

        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String animalName = etAnimalName.getText().toString().trim();
        String animalDescription = etAnimalDescription.getText().toString().trim();
        String animalSources = etAnimalSources.getText().toString().trim();

        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(animalName) || TextUtils.isEmpty(animalDescription)) {
            Toast.makeText(getContext(), "אנא מלא את כל השדות", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> animalData = new HashMap<>();
        animalData.put("firstName", firstName);
        animalData.put("lastName", lastName);
        animalData.put("animalName", animalName);
        animalData.put("animalDescription", animalDescription);
        animalData.put("animalSources", animalSources);
        animalData.put("approved", false);
        animalData.put("userId", currentUser.getUid()); // משייכים את ההודעה למשתמש
        if (currentUser == null) {
            Toast.makeText(getContext(), "עליך להתחבר כדי לשתף מידע!", Toast.LENGTH_SHORT).show();

        }
        else {
            db.collection("shared_animals")
                    .add(animalData)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(getContext(), "המידע נשלח לבדיקה ואישור מנהל!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "שגיאה בשמירה", Toast.LENGTH_SHORT).show());
        }

    }
}
