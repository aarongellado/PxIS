package com.example.pxisjavaver;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class PrescDialog extends AppCompatDialogFragment {
    private EditText MedsET;
    private EditText ValidDateET;
    private Button CreatePrescBtn;
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.alert_dialog_prescription, null);
        MedsET = view.findViewById(R.id.drPrescET);
        ValidDateET = view.findViewById(R.id.drPrescDate);
        builder.setView(view)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String PrescText = MedsET.getText().toString();
                        String ValDate = ValidDateET.getText().toString();
                    }
                });
        return builder.create();
    }
}
