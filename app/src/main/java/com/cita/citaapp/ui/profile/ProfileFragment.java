package com.cita.citaapp.ui.profile;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
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
    private static final String TAG_JSON_OBJ = "json_obj_req";
    private static final String TAG_FULL_NAME = "full_name";
    private static final String TAG_EMAIL_ADDRESS = "email_address";
    private static final String TAG_PASSWORD = "password";
    private static final String TAG = ProfileFragment.class.getSimpleName();
    private static final String URL = Server.URL + "read_profile.php";

    private int userId;
    private String lvlUser;
    private MaterialCardView mcvFullName, mcvEmailAddress, mcvPassword;
    private TextView tvFullName, tvEmailAddress;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        mcvFullName = root.findViewById(R.id.mcv_full_name);
        mcvEmailAddress = root.findViewById(R.id.mcv_email_address);
        mcvPassword = root.findViewById(R.id.mcv_password);
        tvFullName = root.findViewById(R.id.tv_full_name);
        tvEmailAddress = root.findViewById(R.id.tv_email_address);

        assert getArguments() != null;
        userId = getArguments().getInt(LoginActivity.TAG_USER_ID);
        lvlUser = getArguments().getString(LoginActivity.TAG_LVL_USER);

        showProfile();

        mcvFullName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Nama Lengkap");
                final EditText input = new EditText(getActivity());
                alert.setView(input);

                alert.setPositiveButton("Ubah", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final String fullName = input.getText().toString();
                        StringRequest stringRequest1 = new StringRequest(Request.Method.POST,
                                Server.URL + "update_profile_full_name.php",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.e(ProfileFragment.class.getSimpleName(), response);
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            Toast.makeText(getActivity(),
                                                    jsonObject.getString(LoginActivity.TAG_MESSAGE),
                                                    Toast.LENGTH_SHORT).show();
                                            showProfile();

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
                                params.put(TAG_FULL_NAME, fullName);
                                params.put(LoginActivity.TAG_USER_ID, String.valueOf(userId));
                                return params;
                            }
                        };
                        AppController.getInstance().addToRequestQueue(stringRequest1, TAG_JSON_OBJ, getActivity());
                    }
                });
                alert.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alert.show();
            }
        });
        mcvEmailAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Alamat Email");
                final EditText input = new EditText(getActivity());
                alert.setView(input);

                alert.setPositiveButton("Ubah", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final String emailAddress = input.getText().toString();
                        StringRequest stringRequest1 = new StringRequest(Request.Method.POST,
                                Server.URL + "update_profile_email_address.php",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.e(ProfileFragment.class.getSimpleName(), response);
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            Toast.makeText(getActivity(),
                                                    jsonObject.getString(LoginActivity.TAG_MESSAGE),
                                                    Toast.LENGTH_SHORT).show();
                                            showProfile();

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
                                params.put(TAG_EMAIL_ADDRESS, emailAddress);
                                params.put(LoginActivity.TAG_USER_ID, String.valueOf(userId));
                                return params;
                            }
                        };
                        AppController.getInstance().addToRequestQueue(stringRequest1, TAG_JSON_OBJ, getActivity());
                    }
                });
                alert.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alert.show();
            }
        });
        mcvPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Kata Sandi");
                final EditText input = new EditText(getActivity());
                input.setTransformationMethod(PasswordTransformationMethod.getInstance());
                alert.setView(input);

                alert.setPositiveButton("Ubah", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final String password = input.getText().toString();
                        StringRequest stringRequest1 = new StringRequest(Request.Method.POST,
                                Server.URL + "update_profile_password.php",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.e(ProfileFragment.class.getSimpleName(), response);
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            Toast.makeText(getActivity(),
                                                    jsonObject.getString(LoginActivity.TAG_MESSAGE),
                                                    Toast.LENGTH_SHORT).show();
                                            showProfile();

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
                                params.put(TAG_PASSWORD, password);
                                params.put(LoginActivity.TAG_USER_ID, String.valueOf(userId));
                                return params;
                            }
                        };
                        AppController.getInstance().addToRequestQueue(stringRequest1, TAG_JSON_OBJ, getActivity());
                    }
                });
                alert.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alert.show();
            }
        });

        return root;
    }

    private void showProfile() {

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
                params.put(LoginActivity.TAG_USER_ID, String.valueOf(userId));
                params.put(LoginActivity.TAG_LVL_USER, lvlUser);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, TAG_JSON_OBJ, getActivity());
    }
}
