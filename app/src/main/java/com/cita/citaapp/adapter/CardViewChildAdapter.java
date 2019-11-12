package com.cita.citaapp.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.cita.citaapp.R;
import com.cita.citaapp.model.Child;

import java.util.ArrayList;

public class CardViewChildAdapter extends RecyclerView.Adapter<CardViewChildAdapter.CardViewViewHolder> {
    private final ArrayList<Child> listChild;
    private final LayoutInflater inflater;

    public CardViewChildAdapter(ArrayList<Child> listChild, FragmentActivity fragmentActivity) {
        this.listChild = listChild;
        inflater = LayoutInflater.from(fragmentActivity);
    }

    @NonNull
    @Override
    public CardViewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.item_cardview_child, viewGroup, false);
        return new CardViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardViewViewHolder holder, int position) {
        Child child = listChild.get(position);
        holder.tvFullName.setText(child.getFullName());
        holder.tvGender.setText(child.getGender());
        holder.tvDateOfBirth.setText(child.getDateOfBirth());
    }

    @Override
    public int getItemCount() {
        return listChild.size();

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
