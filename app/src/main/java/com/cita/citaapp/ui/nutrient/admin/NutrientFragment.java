package com.cita.citaapp.ui.nutrient.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.cita.citaapp.CreateNutrientActivity;
import com.cita.citaapp.LoginActivity;
import com.cita.citaapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NutrientFragment extends Fragment {

    private int adminId;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_nutrient_admin, container, false);

        assert getArguments() != null;
        adminId = getArguments().getInt(LoginActivity.TAG_ADMIN_ID);
        FloatingActionButton fabAddNutrient = root.findViewById(R.id.fab_add_nutrient);

        fabAddNutrient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateNutrientActivity.class);
                intent.putExtra(LoginActivity.TAG_ADMIN_ID, adminId);
                startActivity(intent);
            }
        });

        return root;
    }
}
