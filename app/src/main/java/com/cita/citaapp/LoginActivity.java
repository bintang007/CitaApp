package com.cita.citaapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cita.citaapp.app.AppController;
import com.cita.citaapp.utils.Server;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Bintang 01/10/2019
 */

public class LoginActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private TextInputEditText tietEmailAddress, tietPassword;
    private TextInputLayout tilEmailAddress, tilPassword;

    private int success;
    private ConnectivityManager connectivityManager;

    private static final String URL = Server.URL + "login.php",
            TAG = LoginActivity.class.getSimpleName(),
            TAG_SUCCESS = "success",
            TAG_JSON_OBJ = "json_obj_req";

    public static final String TAG_LVL_USER = "lvl_user", TAG_ADMIN_ID = "admin_id",
            TAG_USER_ID = "user_id", TAG_FULL_NAME = "full_name",
            TAG_EMAIL_ADDRESS = "email_address", TAG_MESSAGE = "message";

//    private SharedPreferences sharedPreferences;

//    public static final String MY_SHARED_PREFERENCES = "my_shared_preferences";
//    public static final String SESSION_STATUS = "session_status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Masuk Akun");

        ScrollView layout = findViewById(R.id.sv_login);
        MaterialButton btnNext = findViewById(R.id.btn_next);
        TextView tvRegister = findViewById(R.id.tv_register);
        TextView tvForgotPassword = findViewById(R.id.tv_reset_password);
        tietEmailAddress = findViewById(R.id.tiet_email_address);
        tietPassword = findViewById(R.id.tiet_password);
        tilEmailAddress = findViewById(R.id.til_email_address);
        tilPassword = findViewById(R.id.til_password);

        tvRegister.setPaintFlags(tvRegister.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvForgotPassword.setPaintFlags(tvForgotPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        //Cek session login jika bernilai true maka lanjut ke UserMainActivity
//        sharedPreferences = getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
//        boolean session = sharedPreferences.getBoolean(SESSION_STATUS, false);
//        String lvlUser = sharedPreferences.getString(TAG_LVL_USER, null);
//        int userId = sharedPreferences.getInt(TAG_USER_ID, 0);
//        int adminId = sharedPreferences.getInt(TAG_ADMIN_ID, 0);
//        String fullName = sharedPreferences.getString(TAG_FULL_NAME, null);
//        String emailAddress = sharedPreferences.getString(TAG_EMAIL_ADDRESS, null);

//        assert lvlUser != null;
//        if (session && lvlUser.equals("user")) {
//            Intent intent = new Intent(LoginActivity.this, UserMainActivity.class);
//            intent.putExtra(TAG_LVL_USER, lvlUser);
//            intent.putExtra(TAG_USER_ID, userId);
//            intent.putExtra(TAG_FULL_NAME, fullName);
//            intent.putExtra(TAG_EMAIL_ADDRESS, emailAddress);
//            finish();
//            startActivity(intent);
//
//        } else if (session && lvlUser.equals("admin")) {
//            Intent intent = new Intent(LoginActivity.this, AdminMainActivity.class);
//            intent.putExtra(TAG_LVL_USER, lvlUser);
//            intent.putExtra(TAG_ADMIN_ID, adminId);
//            intent.putExtra(TAG_FULL_NAME, fullName);
//            intent.putExtra(TAG_EMAIL_ADDRESS, emailAddress);
//            finish();
//            startActivity(intent);
//
//        }

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Cek kolom tidak boleh kosong
                if (Objects.requireNonNull(tietEmailAddress.getText()).toString().isEmpty()) {
                    tilEmailAddress.setError("Kolom tidak boleh kosong");
                } else {
                    tilEmailAddress.setError(null);
                }

                if (Objects.requireNonNull(tietPassword.getText()).toString().isEmpty()) {
                    tilPassword.setError("Kolom tidak boleh kosong");
                } else {
                    tilPassword.setError(null);
                }

                textChangedListener(tietEmailAddress, tilEmailAddress);
                textChangedListener(tietPassword, tilPassword);

                String emailAddress = tietEmailAddress.getText().toString();
                String password = tietPassword.getText().toString();

                checkLogin(emailAddress, password);
            }
        });
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "Lagi otw...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void textChangedListener(TextInputEditText textInputEditText, final TextInputLayout textInputLayout) {
        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                validateEditText(editable, textInputLayout);
            }
        });

        textInputEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    validateEditText((((EditText) view).getText()), textInputLayout);
                }
            }
        });
    }

    private void validateEditText(Editable editable, TextInputLayout textInputLayout) {
        if (TextUtils.isEmpty((editable))) {
            textInputLayout.setError("Kolom tidak boleh kosong");
        } else {
            textInputLayout.setError(null);
        }
    }

    private void checkLogin(final String emailAddress, final String password) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Masuk ...");
//        showProgressDialog();

        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Login Response: " + response);
//                        hideProgressDialog();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            success = jsonObject.getInt(TAG_SUCCESS);

                            // Check untuk error node di JSON
                            if (success == 1) {
                                String lvlUser = jsonObject.getString(TAG_LVL_USER);
                                if (lvlUser.equals("user")) {
                                    String fullName = jsonObject.getString(TAG_FULL_NAME);
                                    String emailAddress = jsonObject.getString(TAG_EMAIL_ADDRESS);
                                    int userId = jsonObject.getInt(TAG_USER_ID);

                                    Toast.makeText(getApplicationContext(), jsonObject.getString(TAG_MESSAGE),
                                            Toast.LENGTH_LONG).show();

                                    // Menyimpan login ke session
//                                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                                    editor.putBoolean(SESSION_STATUS, true);
//                                    editor.putString(TAG_LVL_USER, lvlUser);
//                                    editor.putInt(TAG_USER_ID, userId);
//                                    editor.putString(TAG_FULL_NAME, fullName);
//                                    editor.putString(TAG_EMAIL_ADDRESS, emailAddress);
//                                    editor.apply();

                                    // Memanggil UserMainActivity
                                    Intent intent = new Intent(LoginActivity.this,
                                            UserMainActivity.class);
                                    intent.putExtra(TAG_LVL_USER, lvlUser);
                                    intent.putExtra(TAG_USER_ID, userId);
                                    intent.putExtra(TAG_FULL_NAME, fullName);
                                    intent.putExtra(TAG_EMAIL_ADDRESS, emailAddress);
                                    finish();
                                    startActivity(intent);
                                } else if (lvlUser.equals("admin")) {
                                    String fullName = jsonObject.getString(TAG_FULL_NAME);
                                    String emailAddress = jsonObject.getString(TAG_EMAIL_ADDRESS);
                                    int adminId = jsonObject.getInt(TAG_ADMIN_ID);

                                    Toast.makeText(getApplicationContext(), jsonObject.getString(TAG_MESSAGE),
                                            Toast.LENGTH_LONG).show();

                                    // Menyimpan login ke session
//                                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                                    editor.putBoolean(SESSION_STATUS, true);
//                                    editor.putString(TAG_LVL_USER, lvlUser);
//                                    editor.putInt(TAG_ADMIN_ID, adminId);
//                                    editor.putString(TAG_FULL_NAME, fullName);
//                                    editor.putString(TAG_EMAIL_ADDRESS, emailAddress);
//                                    editor.apply();

                                    // Memanggil AdminMainActivity
                                    Intent intent = new Intent(LoginActivity.this,
                                            AdminMainActivity.class);
                                    intent.putExtra(TAG_LVL_USER, lvlUser);
                                    intent.putExtra(TAG_ADMIN_ID, adminId);
                                    intent.putExtra(TAG_FULL_NAME, fullName);
                                    intent.putExtra(TAG_EMAIL_ADDRESS, emailAddress);
                                    finish();
                                    startActivity(intent);
                                }


                            } else {
                                Toast.makeText(getApplicationContext(), jsonObject.getString(TAG_MESSAGE),
                                        Toast.LENGTH_LONG).show();
                            }


                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Login Error: " + error.getMessage());
                        Toast.makeText(getApplicationContext(), error.getMessage(),
                                Toast.LENGTH_LONG).show();
                        hideProgressDialog();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                //Posting paramater ke login url
                Map<String, String> params = new HashMap<>();
                params.put("email_address", emailAddress);
                params.put("password", password);
                return params;
            }
        };

        //Menambahkan request ke daftar request
//        AppController.getInstance().addToRequestQueue(stringRequest, TAG_JSON_OBJ, getApplicationContext());
        mRequestQueue.add(stringRequest);
    }

    private void showProgressDialog() {
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    private void hideProgressDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.hide();
        }
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}

