package com.cita.citaapp.ui.child;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cita.citaapp.CreateChildActivity;
import com.cita.citaapp.LoginActivity;
import com.cita.citaapp.R;
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

public class ChildFragment extends Fragment {
    private static final String URL = Server.URL + "read_child.php";
    private static final String TAG = ChildFragment.class.getSimpleName();
    private static final String TAG_FULL_NAME = "full_name";
    private static final String TAG_GENDER = "gender";
    private static final String TAG_DATE_OF_BIRTH = "date_of_birth";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String STATE_LIST = "state_list";

    private final ArrayList<Child> listChild = new ArrayList<>();

    private RecyclerView rvChilds;
    private TextView tvEmptyChild;
    private int userId;
    private String lvlUser;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_child, container, false);

        FloatingActionButton fabAddChild = root.findViewById(R.id.fab_add_child);
        tvEmptyChild = root.findViewById(R.id.tv_empty_child);
        rvChilds = root.findViewById(R.id.rv_childs);
        rvChilds.setHasFixedSize(true);

        assert getArguments() != null;
        userId = getArguments().getInt(LoginActivity.TAG_USER_ID);
        lvlUser = getArguments().getString(LoginActivity.TAG_LVL_USER);

        fabAddChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateChildActivity.class);
                intent.putExtra(LoginActivity.TAG_USER_ID, userId);
                intent.putExtra(LoginActivity.TAG_LVL_USER, lvlUser);
                startActivity(intent);
            }
        });

//        if (savedInstanceState == null) {
        initListChild();
        showRecycleCardView();

//        } else {
//            ArrayList<Child> stateList = savedInstanceState.getParcelableArrayList(STATE_LIST);
//            if (stateList != null) {
//                listChild.addAll(stateList);
//            }
//            showRecycleCardView();
//        }
        return root;
    }

//    @Override
//    public void onSaveInstanceState(@NonNull Bundle bundle) {
//        super.onSaveInstanceState(bundle);
//        bundle.putParcelableArrayList(STATE_LIST, listChild);
//    }

    private void initListChild() {
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
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
                                    Log.e(TAG, listChild.get(i).getFullName());
                                }
                            } else {
                                tvEmptyChild.setText(jsonObject.getString(TAG_MESSAGE));
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getActivity(), "bug", Toast.LENGTH_SHORT).show();
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
                params.put(LoginActivity.TAG_USER_ID, String.valueOf(userId));
                return params;
            }
        };
//        AppController.getInstance().addToRequestQueue(stringRequest, TAG, getActivity());
        mRequestQueue.add(stringRequest);
    }

    private void showRecycleCardView() {
        rvChilds.setLayoutManager(new LinearLayoutManager(getActivity()));
        CardViewChildAdapter cardViewChildAdapter = new CardViewChildAdapter(listChild, getActivity());
        rvChilds.setAdapter(cardViewChildAdapter);
    }

}