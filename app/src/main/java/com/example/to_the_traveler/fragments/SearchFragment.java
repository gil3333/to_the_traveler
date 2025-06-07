package com.example.to_the_traveler.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

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

public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private SearchView searchView;
    private AnimalAdapter adapter;
    private List<Animal> animalList;
    private List<Animal> filteredList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchView = view.findViewById(R.id.search_view);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadAnimalData();

        adapter = new AnimalAdapter(getContext(), filteredList);
        recyclerView.setAdapter(adapter);

        // הצגת כל הרשימה כברירת מחדל
        filterAnimals("");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterAnimals(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterAnimals(newText);
                return true;
            }
        });

        return view;
    }

    private void loadAnimalData() {
        animalList = new ArrayList<>();
        animalList.add(new Animal("אריה", "מלך החיות", R.drawable.lion, "https://he.wikipedia.org/wiki/אריה"));
        animalList.add(new Animal("פיל", "היונק היבשתי הגדול ביותר", R.drawable.elephant, "https://he.wikipedia.org/wiki/פיל"));
        animalList.add(new Animal("נמר", "טורף מהיר וחזק", R.drawable.tiger, "https://he.wikipedia.org/wiki/נמר"));
        animalList.add(new Animal("זאב", "חיה חכמה ומנהיגת להקה", R.drawable.wolf, "https://he.wikipedia.org/wiki/זאב"));

        filteredList = new ArrayList<>(animalList);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void filterAnimals(String query) {
        if (query == null || query.trim().isEmpty()) {
            filteredList.clear();
            filteredList.addAll(animalList);
        } else {
            query = query.toLowerCase().trim();
            filteredList.clear();

            for (Animal animal : animalList) {
                if (animal.getName().toLowerCase().contains(query)) {
                    filteredList.add(animal);
                }
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(getContext(), "לא נמצאו תוצאות", Toast.LENGTH_SHORT).show();
        }

        adapter.notifyDataSetChanged();
    }
}
