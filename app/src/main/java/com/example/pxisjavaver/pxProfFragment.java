package com.example.pxisjavaver;


import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;


/**
 * A simple {@link Fragment} subclass.
 */
public class pxProfFragment extends Fragment {
    private TextView FNameTV, MNameTV, LNameTV, GenderTV, BDateTV, AddTV, EmailTV;
    private ImageView userQR;
    public pxProfFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_px_prof, container, false);
    }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            FNameTV = (TextView) getView().findViewById(R.id.PFNamTxtV);
            MNameTV = (TextView) getView().findViewById(R.id.PMNamTxtV);
            LNameTV = (TextView) getView().findViewById(R.id.PLNameTxtV);
            GenderTV = (TextView) getView().findViewById(R.id.PGendTxtV);
            BDateTV = (TextView) getView().findViewById(R.id.PBDateTxtV);
            AddTV = (TextView) getView().findViewById(R.id.PAddTxtV);
            EmailTV = (TextView) getView().findViewById(R.id.PEmailTxtV);
            userQR = (ImageView) getView().findViewById(R.id.QRIdIV);

            FNameTV.setText(SharedPrefManager.getInstance(getActivity()).getKeyPxuFname());
            MNameTV.setText(SharedPrefManager.getInstance(getActivity()).getKeyPxuMname());
            LNameTV .setText(SharedPrefManager.getInstance(getActivity()).getKeyPxuLname());
            GenderTV.setText(SharedPrefManager.getInstance(getActivity()).getKeyPxuGender());
            BDateTV.setText(SharedPrefManager.getInstance(getActivity()).getKeyPxuBdate());
            AddTV.setText(SharedPrefManager.getInstance(getActivity()).getKeyPxuAddress());
            EmailTV.setText(SharedPrefManager.getInstance(getActivity()).getKeyPxuEmail());

            QRgen();



        }

    public void QRgen(){
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
