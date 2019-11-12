package com.cita.citaapp;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class CreateChildActivity extends AppCompatActivity {

    private static final String URL = Server.URL + "store_child.php",
            TAG = CreateChildActivity.class.getSimpleName(),
            TAG_MESSAGE = "message", TAG_JSON_OBJ = "json_obj_req",
            TAG_USER_ID = "user_id", TAG_FULL_NAME = "full_name", TAG_GENDER = "gender",
            TAG_DATE_OF_BIRTH = "date_of_birth";

    private ProgressDialog progressDialog;

    private TextInputLayout tilFullName, tilGender, tilDateOfBirth;
    private TextInputEditText tietFullName, tietGender, tietDateOfBirth;

    private ConnectivityManager connectivityManager;
    private static String lvlUser;
    private static int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_child);

        setTitle("Form Tambah Data Anak");
//
//        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        assert connectivityManager != null;
//        if (!(connectivityManager.getActiveNetworkInfo() != null
//                && connectivityManager.getActiveNetworkInfo().isAvailable()
//                && connectivityManager.getActiveNetworkInfo().isConnected())) {
//            Toast.makeText(getApplicationContext(), "Tidak ada koneksi internet",
//                    Toast.LENGTH_SHORT).show();
//        }

        lvlUser = getIntent().getStringExtra(LoginActivity.TAG_LVL_USER);
        userId = getIntent().getIntExtra(LoginActivity.TAG_USER_ID, 0);

        tilFullName = findViewById(R.id.til_full_name);
        tilGender = findViewById(R.id.til_gender);
        tilDateOfBirth = findViewById(R.id.til_date_of_birth);
        tietFullName = findViewById(R.id.tiet_full_name);
        tietGender = findViewById(R.id.tiet_gender);
        tietDateOfBirth = findViewById(R.id.tiet_date_of_birth);

        tietDateOfBirth.setKeyListener(null);

        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(tietDateOfBirth, myCalendar);
            }

        };

        tietDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog =
                        new DatePickerDialog(CreateChildActivity.this, date, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().getTouchables().get(0).performClick();
                datePickerDialog.show();
            }
        });

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
                if (Objects.requireNonNull(tietFullName.getText()).toString().isEmpty()) {
                    tilFullName.setError("Kolom tidak boleh kosong");
                } else {
                    tilFullName.setError(null);
                }
                if (Objects.requireNonNull(tietGender.getText()).toString().isEmpty()) {
                    tilGender.setError("Kolom tidak boleh kosong");
                } else {
                    tilGender.setError(null);
                }
                if (Objects.requireNonNull(tietDateOfBirth.getText()).toString().isEmpty()) {
                    tilDateOfBirth.setError("Kolom tidak boleh kosong");
                } else {
                    tilDateOfBirth.setError(null);
                }

                textChangedListener(tietFullName, tilFullName);
                textChangedListener(tietGender, tilGender);
                textChangedListener(tietDateOfBirth, tilDateOfBirth);

                String fullName = tietFullName.getText().toString();
                String gender = tietGender.getText().toString();
                String dateOfBirth = tietDateOfBirth.getText().toString();

                if (fullName.trim().length() > 0 && gender.trim().length() > 0
                        && dateOfBirth.trim().length() > 0) {
//                    if ((connectivityManager.getActiveNetworkInfo() != null
//                            && connectivityManager.getActiveNetworkInfo().isAvailable()
//                            && connectivityManager.getActiveNetworkInfo().isConnected())) {
//                        store(fullName, gender, dateOfBirth);
//                    } else {
//                        Toast.makeText(getApplicationContext(), "Tidak ada koneksi internet",
//                                Toast.LENGTH_SHORT).show();
//                    }
                    store(fullName, gender, dateOfBirth);

                }

            }
        });
    }

    private void updateLabel(TextInputEditText textInputEditText, Calendar calendar) {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        textInputEditText.setText(sdf.format(calendar.getTime()));
    }

    private void store(final String fullName, final String gender, final String dateOfBirth) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memproses ...");
//        showProgressDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Store Response: " + response);
//                        hideProgressDialog();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(getApplicationContext(),
                                    jsonObject.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CreateChildActivity.this, UserMainActivity.class);
                            intent.putExtra(LoginActivity.TAG_USER_ID, userId);
                            intent.putExtra(LoginActivity.TAG_LVL_USER, lvlUser);
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
                params.put(TAG_USER_ID, String.valueOf(userId));
                params.put(TAG_FULL_NAME, fullName);
                params.put(TAG_GENDER, gender);
                params.put(TAG_DATE_OF_BIRTH, dateOfBirth);
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
