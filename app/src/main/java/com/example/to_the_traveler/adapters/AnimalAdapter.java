package com.example.to_the_traveler.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.to_the_traveler.R;
import com.example.to_the_traveler.models.Animal;

import java.util.List;

public class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.AnimalViewHolder> {

    private List<Animal> animalList;
    private Context context;

    public AnimalAdapter(Context context, List<Animal> animalList) {
        this.context = context;
        this.animalList = animalList;
    }

    @NonNull
    @Override
    public AnimalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_animal, parent, false);
        return new AnimalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimalViewHolder holder, int position) {
        Animal animal = animalList.get(position);

        holder.name.setText(animal.getName());
        holder.description.setText(animal.getDescription());
        holder.imageView.setImageResource(animal.getImageResId());

        // אם יש קישור לויקיפדיה - הפעלת הכפתור
        if (animal.getWikiUrl() != null && !animal.getWikiUrl().isEmpty()) {
            holder.wikiLink.setVisibility(View.VISIBLE);
            holder.wikiLink.setOnClickListener(v -> {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(animal.getWikiUrl()));
                context.startActivity(browserIntent);
            });
        } else {
            holder.wikiLink.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return animalList.size();
    }

    public static class AnimalViewHolder extends RecyclerView.ViewHolder {
        TextView name, description, wikiLink;
        ImageView imageView;

        public AnimalViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.animal_name);
            description = itemView.findViewById(R.id.animal_description);
            imageView = itemView.findViewById(R.id.animal_image);
            wikiLink = itemView.findViewById(R.id.wiki_link);
        }
    }
}
