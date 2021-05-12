package com.example.pxisjavaver;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText UsernameET, PasswordET;
    private Button buttonLogin;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            if (SharedPrefManager.getInstance(this).getKeyPxuUserAuth().equals("doctor")) {
                startActivity(new Intent(this, DrProfileActivity.class));
                return;
            } else {
                startActivity(new Intent(this, PxProfileActivity.class));
                return;
            }
        }

        UsernameET = (EditText) findViewById(R.id.UsernameET);
        PasswordET = (EditText) findViewById(R.id.PasswordET);
        buttonLogin = (Button) findViewById(R.id.LoginBtn);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging in");


        buttonLogin.setOnClickListener(this);
    }


    private void userLogin() {
        final String username = UsernameET.getText().toString().trim();
        final String password = PasswordET.getText().toString().trim();

        progressDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                SharedPrefManager.getInstance(getApplicationContext())
                                        .userLogin(
                                                obj.getString("pxu"),
                                                obj.getString("userAuth"),
                                                obj.getString("email"),
                                                obj.getString("FirstName"),
                                                obj.getString("MiddleName"),
                                                obj.getString("LastName"),
                                                obj.getString("Age"),
                                                obj.getString("Address"),
                                                obj.getString("BirthDate"),
                                                obj.getString("Gender")
                                        );
                                if (SharedPrefManager.getInstance(getApplicationContext()).getKeyPxuUserAuth().equals("doctor")) {
                                    startActivity(new Intent(getApplicationContext(), DrProfileActivity.class));
                                    finish();
                                } else {
                                    startActivity(new Intent(getApplicationContext(), PxProfileActivity.class));
                                    finish();
                                }


                            } else {
                                Toast.makeText(
                                        getApplicationContext(),
                                        obj.getString("message"),
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(
                                getApplicationContext(),
                                error.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("pxu", username);
                params.put("pxp", password);
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void RegUser(View view) {
        Intent intent = new Intent(this, RegisterUser.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        if (view == buttonLogin) {
            userLogin();
        }
    }
}
