package com.example.pxisjavaver;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
public class pxMedsFragment extends Fragment implements DatePickerDialog.OnDateSetListener{
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<medsListItem> listItems;
    private FloatingActionButton PrescBtn;
    private ProgressDialog progressDialog;
    private EditText PrescET;
    private EditText PrescValidDate;
    String SPrescVDate;

    public pxMedsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_px_meds, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) getView().findViewById(R.id.pxMedsRecylclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        listItems = new ArrayList<>();
        PrescBtn = getView().findViewById(R.id.newPrescBtn);
        progressDialog = new ProgressDialog(getContext());
        if(SharedPrefManager.getInstance(getContext()).getKeyPxuUserAuth().equals("doctor")){
            PrescBtn.show();
        } else {
            PrescBtn.hide();
        }
        PrescBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View view1 = getLayoutInflater().inflate(R.layout.alert_dialog_prescription, null);
                builder.setView(view1).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        writePrescription();
                    }
                });
                PrescET = view1.findViewById(R.id.drPrescET);
                PrescValidDate = view1.findViewById(R.id.drPrescDate);
                PrescValidDate.setFocusable(false);
                PrescValidDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectDate();
                    }
                });
                builder.show();
            }
        });

        loadMeds();
    }

    private void writePrescription() {
        final String pxu = SharedPrefManager.getInstance(getActivity()).getKeyPxuId();
        final String drName = SharedPrefManager.getInstance(getActivity()).getKeyPxuFname()
                + " " + SharedPrefManager.getInstance(getActivity()).getKeyPxuMname()
                + " " + SharedPrefManager.getInstance(getActivity()).getKeyPxuLname();
        final String prescMeds = PrescET.getText().toString().trim();
        final String prescValDate = PrescValidDate.getText().toString().trim();

        progressDialog.setMessage("Writing Prescription");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_MEDICATIONS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    PrescValidDate.getText().clear();
                    PrescET.getText().clear();
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
                params.put("pxu", pxu);
                params.put("drName", drName);
                params.put("dateValid", prescValDate);
                params.put("medications", prescMeds);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    private void selectDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), this,
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
        mmMod = Integer.toString(mm);
        ddMod = Integer.toString(dd);
        mm = mm + 1;
        if (mm < 10) {
            mmMod = "0" + mm;
        }
        if(dd<10){
            ddMod = "0"+dd;
        }
        date = +yyyy + "/" + mmMod + "/" + ddMod;

        PrescValidDate.setText(date);
        SPrescVDate = PrescValidDate.getText().toString().trim();
    }

    private void loadMeds() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading Checkup History");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_MEDICATIONS_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONArray obj = new JSONArray(response);
                            for (int i = 0; i < obj.length(); i++) {
                                JSONObject listObj = obj.getJSONObject(i);
                                String medsDateFetched = listObj.getString("dateCreated");
                                String medsPrescFetched = listObj.getString("medications");
                                String medsDocNameFetched = listObj.getString("doctorName");
                                String medsValidDateFetched = listObj.getString("dateValid");
                                medsListItem item = new medsListItem(medsDateFetched, medsPrescFetched, medsDocNameFetched, medsValidDateFetched);
                                listItems.add(item);

                            }
                            adapter = new medsListAdapter(listItems, getContext());
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
                if (SharedPrefManager.getInstance(getContext()).getKeyPxuUserAuth().equals("doctor")) {
                    params.put("pxu", SharedPrefManager.getInstance(getActivity()).getKeyPxuId());
                } else {
                    params.put("pxu", SharedPrefManager.getInstance(getActivity()).getKeyPxu());
                }
                return params;
            }
        };
        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

}
