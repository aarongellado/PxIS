package com.example.pxisjavaver;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

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
public class drAptmtFRagment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<drAptListItem> listItems;
    private ProgressDialog progressDialog;

    public drAptmtFRagment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dr_aptmt_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView)getView().findViewById(R.id.drAptRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        listItems = new ArrayList<>();

        progressDialog = new ProgressDialog(getContext());

        loadAppointments();
    }

    private void loadAppointments() {final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading Appointments");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_DR_APPOINTMENT_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONArray obj = new JSONArray(response);
                            for(int i=0;i<obj.length();i++){
                                JSONObject listObj = obj.getJSONObject(i);
                                String drAptDateFetched = listObj.getString("aptDate");
                                String drAptPurposeFetched = listObj.getString("aptPurpose");
                                String drAptNameFetched = listObj.getString("pxName");
                                drAptListItem item = new drAptListItem(drAptPurposeFetched,drAptNameFetched,drAptDateFetched);
                                listItems.add(item);

                            }
                            adapter = new drAptListAdapter(listItems,getContext());
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
        );
        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);
    }
}
