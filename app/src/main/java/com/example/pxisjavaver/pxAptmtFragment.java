package com.example.pxisjavaver;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class pxAptmtFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    private Spinner aptPurpose;
    private EditText aptDateEt;
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<aptListItem> listItems;
    String purposeSelect;
    String aptDateChecker;


    public pxAptmtFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_px_aptmt, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ImageView aptDate = (ImageView) getView().findViewById(R.id.pxAptDateIV);
        aptDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });
        Button aptButton = (Button) getView().findViewById(R.id.aReqAptBtn);
        recyclerView = (RecyclerView) getView().findViewById(R.id.pxAptmtRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        listItems = new ArrayList<>();


        aptDateEt = (EditText) getView().findViewById(R.id.pxAptDateET);
        aptDateEt.setFocusable(false);
        aptDateEt.setClickable(false);
        loadAppointments();


        progressDialog = new ProgressDialog(getContext());

        aptPurpose = getView().findViewById(R.id.pxAptPurposeSpnr);
        List<String> Purpose = new ArrayList<>();
        Purpose.add(0, "Purpose of Appointment");
        Purpose.add("Checkup");
        Purpose.add("Laboratory Tests");
        Purpose.add("Medical");
        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, Purpose);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        aptPurpose.setAdapter(dataAdapter);
        aptPurpose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                purposeSelect = adapterView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        aptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (purposeSelect != "Purpose of Appointment") {
                    if (aptDateChecker != null) {
                        requestAppointment();
                        listItems.clear();
                        loadAppointments();
                    } else {
                        Toast.makeText(getContext(), "Select Date", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Select Purpose of Appointment", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void loadAppointments() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading Appointments");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_APPOINTMENT_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONArray obj = new JSONArray(response);
                            for (int i = 0; i < obj.length(); i++) {
                                JSONObject listObj = obj.getJSONObject(i);
                                String aptDateFetched = listObj.getString("aptDate");
                                String aptPurposeFetched = listObj.getString("aptPurpose");
                                aptListItem item = new aptListItem(aptPurposeFetched, aptDateFetched);
                                listItems.add(item);

                            }
                            adapter = new aptListAdapter(listItems, getContext());
                            recyclerView.setAdapter(adapter);
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
                                getContext(),
                                error.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("pxu", SharedPrefManager.getInstance(getActivity()).getKeyPxu());
                return params;
            }
        };
        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
    }


    private void requestAppointment() {
        final String spxu = SharedPrefManager.getInstance(getActivity()).getKeyPxu();
        final String spxName = SharedPrefManager.getInstance(getActivity()).getKeyPxuFname()
                + " " + SharedPrefManager.getInstance(getActivity()).getKeyPxuMname()
                + " " + SharedPrefManager.getInstance(getActivity()).getKeyPxuLname();
        final String saptDate = aptDateEt.getText().toString().trim();
        final String saptPurpose = purposeSelect;

        progressDialog.setMessage("Submitting Appointment Request...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_APPOINTMENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    aptPurpose.setSelection(0);
                    aptDateEt.getText().clear();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("pxu", spxu);
                params.put("pxName", spxName);
                params.put("aptDate", saptDate);
                params.put("aptPurpose", saptPurpose);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(), this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        );
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int yyyy, int mm, int dd) {

        String date, ddMod, mmMod;
        date = "";
        mm = mm + 1;
        mmMod = Integer.toString(mm);
        ddMod = Integer.toString(dd);

        if (mm < 10) {
            mmMod = "0" + mm;
        }
        if(dd<10){
            ddMod = "0"+dd;
        }
        date = +yyyy + "/" + mmMod + "/" + ddMod;

        aptDateEt.setText(date);
        aptDateChecker = aptDateEt.getText().toString().trim();
    }
}
