package com.cita.citaapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cita.citaapp.R;
import com.cita.citaapp.model.Child;
import com.cita.citaapp.model.Nutrient;

import java.util.ArrayList;

public class CardViewNutrientAdapter extends RecyclerView.Adapter<CardViewNutrientAdapter.CardViewViewHolder> {
    private final ArrayList<Nutrient> listNutrient;

    public CardViewNutrientAdapter(ArrayList<Nutrient> listNutrient) {
        this.listNutrient = listNutrient;
    }

    @NonNull
    @Override
    public CardViewNutrientAdapter.CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_cardview_child, viewGroup, false);
        return new CardViewNutrientAdapter.CardViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardViewNutrientAdapter.CardViewViewHolder holder, int position) {
        Nutrient child = listNutrient.get(position);

    }

    @Override
    public int getItemCount() {
        return listNutrient.size();

    }

    class CardViewViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvFullName;
        private final TextView tvGender;
        private final TextView tvDateOfBirth;

        CardViewViewHolder(View itemView) {
            super(itemView);
            tvFullName = itemView.findViewById(R.id.tv_full_name);
            tvGender = itemView.findViewById(R.id.tv_gender);
            tvDateOfBirth = itemView.findViewById(R.id.tv_date_of_birth);
        }
    }
}
