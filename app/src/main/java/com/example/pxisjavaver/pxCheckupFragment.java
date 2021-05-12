package com.example.pxisjavaver;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class pxCheckupFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private FloatingActionButton drDiagsBtn;
    private EditText drDiagsET;
    private List<chkupListItem> listItems;
    private ProgressDialog progressDialog;

    public pxCheckupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_px_checkup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) getView().findViewById(R.id.pxCheckupRecylclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        listItems = new ArrayList<>();
        drDiagsBtn = getView().findViewById(R.id.newDiagsBtn);

        progressDialog = new ProgressDialog(getContext());
        if(SharedPrefManager.getInstance(getContext()).getKeyPxuUserAuth().equals("doctor")){
            drDiagsBtn.show();
        } else {
            drDiagsBtn.hide();
        }
        drDiagsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View view1 = getLayoutInflater().inflate(R.layout.alert_dialog_diagnosis,null);
                builder.setView(view1).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                writeDiags();
                            }
                        });
                        drDiagsET = view1.findViewById(R.id.drDiagsET);

                builder.show();
                
            }
        });

        loadCheckupHistory();
    }

    private void writeDiags() {
        final String pxu = SharedPrefManager.getInstance(getActivity()).getKeyPxuId();
        final String drName = SharedPrefManager.getInstance(getActivity()).getKeyPxuFname()
                + " " + SharedPrefManager.getInstance(getActivity()).getKeyPxuMname()
                + " " + SharedPrefManager.getInstance(getActivity()).getKeyPxuLname();
        final String drDiags = drDiagsET.getText().toString().trim();

        progressDialog.setMessage("Writing Prescription");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_CHECKUP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    drDiagsET.getText().clear();
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
                params.put("drDiag", drDiags);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void loadCheckupHistory() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading Checkup History");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_CHECKUP_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONArray obj = new JSONArray(response);
                            for (int i = 0; i < obj.length(); i++) {
                                JSONObject listObj = obj.getJSONObject(i);
                                String chkupDateFetched = listObj.getString("dateCreated");
                                String chkupDiagsFetched = listObj.getString("diagnosis");
                                String chkipDocFetched = listObj.getString("doctorName");
                                chkupListItem item = new chkupListItem(chkupDateFetched, chkupDiagsFetched, chkipDocFetched);
                                listItems.add(item);

                            }
                            adapter = new chkupListAdapter(listItems, getContext());
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
