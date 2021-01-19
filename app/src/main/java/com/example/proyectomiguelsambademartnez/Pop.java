package com.example.proyectomiguelsambademartnez;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class Pop extends AppCompatDialogFragment {
    private EditText Site;
    private EditText password;
    private PopListener listener;




    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialogcustom,null);
        builder.setView(view)
                .setTitle("Añadir Contraseña")
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String pass = password.getText().toString();
                        String site = Site.getText().toString();
                        listener.applyText(site, pass);
                    }
                });
        Site = view.findViewById(R.id.editTextSite);
        password = view.findViewById(R.id.editTextPass);
        return builder.create();



    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (PopListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

    }

    public interface PopListener {
        void applyText(String Site, String pass);
    }
}
