package com.cita.citaapp.ui.profile.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cita.citaapp.LoginActivity;
import com.cita.citaapp.R;
import com.cita.citaapp.app.AppController;
import com.cita.citaapp.utils.Server;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {
    private static final String TAG_FULL_NAME = "full_name";
    private static final String TAG_EMAIL_ADDRESS = "email_address";
    private static final String TAG = ProfileFragment.class.getSimpleName();
    private static final String URL = Server.URL + "read_profile.php";

    private int adminId;
    private String lvlUser;
    private MaterialCardView mcvFullName, mcvEmailAddress, mcvPassword;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile_admin, container, false);

        mcvFullName = root.findViewById(R.id.mcv_full_name);
        mcvEmailAddress = root.findViewById(R.id.mcv_email_address);
        mcvPassword = root.findViewById(R.id.mcv_password);
        final TextView tvFullName = root.findViewById(R.id.tv_full_name);
        final TextView tvEmailAddress = root.findViewById(R.id.tv_email_address);

        assert getArguments() != null;
        adminId = getArguments().getInt(LoginActivity.TAG_ADMIN_ID);
        lvlUser = getArguments().getString(LoginActivity.TAG_LVL_USER);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            tvFullName.setText(jsonObject.getString(TAG_FULL_NAME));
                            tvEmailAddress.setText(jsonObject.getString(TAG_EMAIL_ADDRESS));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(LoginActivity.TAG_ADMIN_ID, String.valueOf(adminId));
                params.put(LoginActivity.TAG_LVL_USER, lvlUser);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, TAG, getActivity());

        mcvFullName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mcvEmailAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mcvPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return root;
    }
}
