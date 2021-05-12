package com.example.pxisjavaver;


import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;


/**
 * A simple {@link Fragment} subclass.
 */
public class drProfFragment extends Fragment {
    private TextView FNameTV, MNameTV, LNameTV, GenderTV, BDateTV, AddTV, EmailTV;
    private ImageView userQR;
    private Button PxSession;


    public drProfFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dr_prof, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        FNameTV = (TextView) getView().findViewById(R.id.DrPFNamTxtV);
        MNameTV = (TextView) getView().findViewById(R.id.DrPMNamTxtV);
        LNameTV = (TextView) getView().findViewById(R.id.DrPLNameTxtV);
        GenderTV = (TextView) getView().findViewById(R.id.DrPGendTxtV);
        BDateTV = (TextView) getView().findViewById(R.id.DrPBDateTxtV);
        AddTV = (TextView) getView().findViewById(R.id.DrPAddTxtV);
        EmailTV = (TextView) getView().findViewById(R.id.DrPEmailTxtV);
        userQR = (ImageView) getView().findViewById(R.id.DrQRIdIV);
        PxSession = (Button) getView().findViewById(R.id.DrPxSessionBtn);

        FNameTV.setText(SharedPrefManager.getInstance(getActivity()).getKeyPxuFname());
        MNameTV.setText(SharedPrefManager.getInstance(getActivity()).getKeyPxuMname());
        LNameTV .setText(SharedPrefManager.getInstance(getActivity()).getKeyPxuLname());
        GenderTV.setText(SharedPrefManager.getInstance(getActivity()).getKeyPxuGender());
        BDateTV.setText(SharedPrefManager.getInstance(getActivity()).getKeyPxuBdate());
        AddTV.setText(SharedPrefManager.getInstance(getActivity()).getKeyPxuAddress());
        EmailTV.setText(SharedPrefManager.getInstance(getActivity()).getKeyPxuEmail());

        QRgen();

        PxSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(getActivity());
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(false);
                integrator.forSupportFragment(drProfFragment.this).initiateScan();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getContext(), "You cancelled scanning", Toast.LENGTH_LONG).show();
            } else {
                SharedPrefManager.getInstance(getContext()).pxId(result.getContents());
                Intent intent = new Intent(getActivity(), DrPxSessionActivity.class);
                startActivity(intent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void QRgen() {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(SharedPrefManager.getInstance(getActivity()).getKeyPxu(),
                    BarcodeFormat.QR_CODE, 500,500);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            userQR.setImageBitmap(bitmap);
        }catch (WriterException e){
            e.printStackTrace();
        }
    }

}
