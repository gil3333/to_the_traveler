package com.example.to_the_traveler.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.to_the_traveler.R;
import com.example.to_the_traveler.models.SharedAnimal;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class SharedAnimalAdapter extends RecyclerView.Adapter<SharedAnimalAdapter.ViewHolder> {

    private List<SharedAnimal> sharedAnimalList;
    private boolean isAdmin;
    private Context context;

    public SharedAnimalAdapter(Context context, List<SharedAnimal> sharedAnimalList, boolean isAdmin) {
        this.context = context;
        this.sharedAnimalList = sharedAnimalList;
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shared_animal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SharedAnimal sharedAnimal = sharedAnimalList.get(position);

        holder.tvName.setText(sharedAnimal.getFirstName() + " " + sharedAnimal.getLastName());
        holder.tvAnimal.setText("החיה: " + sharedAnimal.getAnimalName());
        holder.tvDescription.setText("שיתוף אודות החיה:"+sharedAnimal.getAnimalDescription());

        // הצגת מקורות מידע אם קיימים
        if (sharedAnimal.getAnimalSources() != null && !sharedAnimal.getAnimalSources().isEmpty()) {
            holder.tvSources.setVisibility(View.VISIBLE);
            holder.tvSources.setText("מקורות מידע: " + sharedAnimal.getAnimalSources());
        } else {
            holder.tvSources.setVisibility(View.GONE);
        }

        if (isAdmin) {
            holder.btnApprove.setVisibility(View.VISIBLE);
            holder.btnDelete.setVisibility(View.VISIBLE);

            holder.btnApprove.setOnClickListener(v -> approveAnimal(sharedAnimal.getId(), position));
            holder.btnDelete.setOnClickListener(v -> deleteAnimal(sharedAnimal.getId(), position));
        } else {
            holder.btnApprove.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.GONE);
        }
    }

    private void approveAnimal(String docId, int position) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("shared_animals").document(docId)
                .update("approved", true)
                .addOnSuccessListener(aVoid -> {
                    sharedAnimalList.get(position).setApproved(true);
                    notifyItemChanged(position);
                    Toast.makeText(context, "ההודעה אושרה!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(context, "שגיאה באישור", Toast.LENGTH_SHORT).show());
    }

    private void deleteAnimal(String docId, int position) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("shared_animals").document(docId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    sharedAnimalList.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "השיתוף נמחק!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(context, "שגיאה במחיקה", Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return sharedAnimalList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAnimal, tvDescription, tvSources;
        Button btnApprove, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvAnimal = itemView.findViewById(R.id.tv_animal);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvSources = itemView.findViewById(R.id.tv_sources); // ✅ הוספת מקורות המידע
            btnApprove = itemView.findViewById(R.id.btn_approve);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
