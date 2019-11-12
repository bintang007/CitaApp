package com.cita.citaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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

public class CreateNutrientActivity extends AppCompatActivity {
    private static final String URL = Server.URL + "store_nutrient.php",
            TAG = CreateChildActivity.class.getSimpleName(),
            TAG_MESSAGE = "message", TAG_JSON_OBJ = "json_obj_req",
            TAG_ADMIN_ID = "admin_id", TAG_NUTRIENT_NAME = "nutrient_name", TAG_CARBOHYDRATE = "carbohydrate",
            TAG_CALORIES = "calories", TAG_FAT = "fat", TAG_PROTEIN = "protein";

    private ProgressDialog progressDialog;

    private TextInputLayout tilNutrientName, tilCarbohydrate, tilCalories, tilFat, tilProtein;
    private TextInputEditText tietNutrientName, tietCarbohydrate, tietCalories, tietFat, tietProtein;

    private ConnectivityManager connectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_nutrient);

        setTitle("Form Tambah Data Kandungan Gizi");

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
//        if (!(connectivityManager.getActiveNetworkInfo() != null
//                && connectivityManager.getActiveNetworkInfo().isAvailable()
//                && connectivityManager.getActiveNetworkInfo().isConnected())) {
//            Toast.makeText(getApplicationContext(), "Tidak ada koneksi internet",
//                    Toast.LENGTH_SHORT).show();
//        }

        tilNutrientName = findViewById(R.id.til_nutrient_name);
        tilCarbohydrate = findViewById(R.id.til_carbohydrate);
        tilCalories = findViewById(R.id.til_calories);
        tilFat = findViewById(R.id.til_fat);
        tilProtein = findViewById(R.id.til_protein);
        tietNutrientName = findViewById(R.id.tiet_nutrient_name);
        tietCarbohydrate = findViewById(R.id.tiet_carbohydrate);
        tietCalories = findViewById(R.id.tiet_calories);
        tietFat = findViewById(R.id.tiet_fat);
        tietProtein = findViewById(R.id.tiet_protein);

        MaterialButton btnCancel = findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        MaterialButton btnNext = findViewById(R.id.btn_next);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Objects.requireNonNull(tietNutrientName.getText()).toString().isEmpty()) {
                    tilNutrientName.setError("Kolom tidak boleh kosong");
                } else {
                    tilNutrientName.setError(null);
                }
                if (Objects.requireNonNull(tietCarbohydrate.getText()).toString().isEmpty()) {
                    tilCarbohydrate.setError("Kolom tidak boleh kosong");
                } else {
                    tilCarbohydrate.setError(null);
                }
                if (Objects.requireNonNull(tietCalories.getText()).toString().isEmpty()) {
                    tilCalories.setError("Kolom tidak boleh kosong");
                } else {
                    tilCalories.setError(null);
                }
                if (Objects.requireNonNull(tietFat.getText()).toString().isEmpty()) {
                    tilFat.setError("Kolom tidak boleh kosong");
                } else {
                    tilFat.setError(null);
                }
                if (Objects.requireNonNull(tietProtein.getText()).toString().isEmpty()) {
                    tilProtein.setError("Kolom tidak boleh kosong");
                } else {
                    tilProtein.setError(null);
                }

                textChangedListener(tietNutrientName, tilNutrientName);
                textChangedListener(tietCarbohydrate, tilCarbohydrate);
                textChangedListener(tietCalories, tilCalories);
                textChangedListener(tietFat, tilFat);
                textChangedListener(tietProtein, tilProtein);

                String nutrientName = tietNutrientName.getText().toString();
                String carbohydrate = tietCarbohydrate.getText().toString();
                String calories = tietCalories.getText().toString();
                String fat = tietFat.getText().toString();
                String protein = tietProtein.getText().toString();

//                if (nutrientName.trim().length() > 0 && carbohydrate.trim().length() > 0
//                        && calories.trim().length() > 0 && fat.trim().length() > 0
//                        && protein.trim().length() > 0) {
//                    if ((connectivityManager.getActiveNetworkInfo() != null
//                            && connectivityManager.getActiveNetworkInfo().isAvailable()
//                            && connectivityManager.getActiveNetworkInfo().isConnected())) {
//                        store(nutrientName, carbohydrate, calories, fat, protein);
//                    } else {
//                        Toast.makeText(getApplicationContext(), "Tidak ada koneksi internet",
//                                Toast.LENGTH_SHORT).show();
//                    }
//
//                }
                store(nutrientName, carbohydrate, calories, fat, protein);

            }
        });

    }

    private void store(final String nutrientName, final String carbohydrate, final String calories, final String fat, final String protein) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memproses ...");
        showProgressDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Store Response: " + response);
                        hideProgressDialog();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(getApplicationContext(),
                                    jsonObject.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CreateNutrientActivity.this, AdminMainActivity.class);
                            finish();
                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Store Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                hideProgressDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(TAG_ADMIN_ID, String.valueOf(getIntent().getIntExtra(LoginActivity.TAG_ADMIN_ID, 0)));
                params.put(TAG_NUTRIENT_NAME, nutrientName);
                params.put(TAG_CARBOHYDRATE, carbohydrate);
                params.put(TAG_CALORIES, calories);
                params.put(TAG_FAT, fat);
                params.put(TAG_PROTEIN, protein);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, TAG_JSON_OBJ, getApplicationContext());
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

    private void showProgressDialog() {
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    private void hideProgressDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
