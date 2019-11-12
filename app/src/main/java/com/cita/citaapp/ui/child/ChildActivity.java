package com.cita.citaapp.ui.child;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cita.citaapp.CreateChildActivity;
import com.cita.citaapp.LoginActivity;
import com.cita.citaapp.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cita.citaapp.adapter.CardViewChildAdapter;
import com.cita.citaapp.app.AppController;
import com.cita.citaapp.model.Child;
import com.cita.citaapp.utils.Server;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChildActivity extends AppCompatActivity {
    private static final String URL = Server.URL + "read_child.php";
    private static final String TAG = ChildActivity.class.getSimpleName();
    private static final String TAG_JSON_OBJ = "json_obj_req";
    private static final String TAG_FULL_NAME = "full_name";
    private static final String TAG_GENDER = "gender";
    private static final String TAG_DATE_OF_BIRTH = "date_of_birth";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    private final ArrayList<Child> listChild = new ArrayList<>();

    private RecyclerView rvChilds;
    private TextView tvEmptyChild;
    private int userId;
    private String lvlUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);
        FloatingActionButton fabAddChild = findViewById(R.id.fab_add_child);
        tvEmptyChild = findViewById(R.id.tv_empty_child);
        rvChilds = findViewById(R.id.rv_childs);
        rvChilds.setHasFixedSize(true);
        setTitle("Data Anak");
        userId = getIntent().getIntExtra(LoginActivity.TAG_USER_ID, 0);
        lvlUser = getIntent().getStringExtra(LoginActivity.TAG_LVL_USER);

        fabAddChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChildActivity.this, CreateChildActivity.class);
                intent.putExtra(LoginActivity.TAG_USER_ID, userId);
                intent.putExtra(LoginActivity.TAG_LVL_USER, lvlUser);
                startActivity(intent);
            }
        });

//        if (savedInstanceState == null) {
        initListChild();
        showRecycleCardView();
    }

    private void initListChild() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Read Response: " + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int success = jsonObject.getInt(TAG_SUCCESS);
                            if (success == 1) {
                                JSONArray jsonArrayFullName = jsonObject.getJSONArray(TAG_FULL_NAME);
                                JSONArray jsonArrayGender = jsonObject.getJSONArray(TAG_GENDER);
                                JSONArray jsonArrayDateOfBirth = jsonObject.getJSONArray(TAG_DATE_OF_BIRTH);
                                for (int i = 0; i < jsonArrayFullName.length(); i++) {
                                    Child child = new Child();
                                    child.setFullName(jsonArrayFullName.getString(i));
                                    child.setGender(jsonArrayGender.getString(i));
                                    child.setDateOfBirth(jsonArrayDateOfBirth.getString(i));
                                    listChild.add(child);
                                }
                            } else {
                                tvEmptyChild.setText(jsonObject.getString(TAG_MESSAGE));
                            }
                        } catch (JSONException e) {
                            Toast.makeText(ChildActivity.this, "tiga", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ChildActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(LoginActivity.TAG_USER_ID, String.valueOf(userId));
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, TAG_JSON_OBJ, getApplicationContext());
    }

    private void showRecycleCardView() {
//        rvChilds.setLayoutManager(new LinearLayoutManager(this));
//        CardViewChildAdapter cardViewChildAdapter = new CardViewChildAdapter(listChild, g);
//        rvChilds.setAdapter(cardViewChildAdapter);
    }
}
