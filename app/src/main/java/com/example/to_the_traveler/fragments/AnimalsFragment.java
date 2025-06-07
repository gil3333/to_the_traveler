package com.example.to_the_traveler.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.to_the_traveler.R;
import com.example.to_the_traveler.adapters.AnimalAdapter;
import com.example.to_the_traveler.models.Animal;
import java.util.ArrayList;
import java.util.List;

public class AnimalsFragment extends Fragment {
    private RecyclerView recyclerView;
    private AnimalAdapter adapter;
    private List<Animal> animalList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_animals, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // יצירת רשימת חיות לדוגמה
        animalList = new ArrayList<>();
        animalList.add(new Animal("אריה", "מלך החיות", R.drawable.lion, "https://he.wikipedia.org/wiki/אריה"));
        animalList.add(new Animal("פיל", "היונק היבשתי הגדול ביותר", R.drawable.elephant, "https://he.wikipedia.org/wiki/פיל"));
        animalList.add(new Animal("נמר", "טורף מהיר וחזק", R.drawable.tiger, "https://he.wikipedia.org/wiki/נמר"));
        animalList.add(new Animal("זאב", "חיה חכמה ומנהיגת להקה", R.drawable.wolf, "https://he.wikipedia.org/wiki/זאב"));

        adapter = new AnimalAdapter(getContext(), animalList);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
