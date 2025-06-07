package com.example.to_the_traveler.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.to_the_traveler.R;

public class AnimalDetailFragment extends Fragment {
    private ImageView animalImage;
    private TextView animalName, animalDescription;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_animal_detail, container, false);

        animalImage = view.findViewById(R.id.animal_image);
        animalName = view.findViewById(R.id.animal_name);
        animalDescription = view.findViewById(R.id.animal_description);

        Bundle args = getArguments();
        if (args != null) {
            animalName.setText(args.getString("name"));
            animalDescription.setText(args.getString("description"));
        }

        return view;
    }
}
