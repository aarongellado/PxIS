package com.example.pxisjavaver;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterUser extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private Spinner GenderSpinner;
    private ProgressDialog progressDialog;
    EditText dateText;
    EditText UName,Pass,ConPass,Email,FName,MName,LName,Age,Address;
    Button SignUp;
    String GenderSelect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, PxProfileActivity.class));
            return;
        }


        UName = (EditText) findViewById(R.id.rUserEdtTxt);
        Pass = (EditText) findViewById(R.id.rPassEdtTxt);
        ConPass = (EditText) findViewById(R.id.rConPassEdtTxt);
        Email = (EditText) findViewById(R.id.rEmailEdtTxt);
        FName = (EditText) findViewById(R.id.rFirNamEdtTxt);
        MName = (EditText) findViewById(R.id.rMidNamEdtTxt);
        LName = (EditText) findViewById(R.id.rLasNamEdtTxt);
        Age = (EditText) findViewById(R.id.rAgeEdtTxt);
        Address = (EditText) findViewById(R.id.rAddEdtTxt);

        SignUp = (Button) findViewById(R.id.rSubBtn);

        progressDialog = new ProgressDialog(this);

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        dateText = findViewById(R.id.rBirDatEdtTxt);
        GenderSpinner = findViewById(R.id.rGendSpinr);
        List<String> Genders = new ArrayList<>();
        Genders.add(0,"Select Gender");
        Genders.add("Male");
        Genders.add("Female");

        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,Genders);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        GenderSpinner.setAdapter(dataAdapter);
        GenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(adapterView.getItemAtPosition(position).equals("Select Gender")){
                    //do nothing
                }
                else{
                    GenderSelect = adapterView.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        dateText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    showDatePickerDialog();
                }
            }
        });

    }

    private void registerUser() {
        final String sUname = UName.getText().toString().trim();
        final String sPass = Pass.getText().toString().trim();
        final String sConPass = ConPass.getText().toString().trim();
        final String sEmail = Email.getText().toString().trim();
        final String sFName = FName.getText().toString().trim();
        final String sMName = MName.getText().toString().trim();
        final String sLName = LName.getText().toString().trim();
        final String sAge = Age.getText().toString().trim();
        final String sAddress = Address.getText().toString().trim();
        final String sDateText = dateText.getText().toString().trim();

        progressDialog.setMessage("Registering User...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Toast.makeText(getApplicationContext(),jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("pxu",sUname);
                params.put("pxp",sPass);
                params.put("email", sEmail);
                params.put("FirstName", sFName);
                params.put("MiddleName",sMName);
                params.put("LastName", sLName);
                params.put("Age", sAge);
                params.put("Address", sAddress);
                params.put("BirthDate", sDateText);
                params.put("Gender",GenderSelect);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int yyyy, int mm, int dd) {
        mm = mm+1;
        String date = + yyyy +"/" + mm + "/" + dd;
        dateText.setText(date);
    }
}
