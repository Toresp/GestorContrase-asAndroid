package com.example.proyectomiguelsambademartnez;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

public class Pop extends AppCompatDialogFragment {
    private EditText Site;
    private EditText password;
    private PopListener listener;
    private String oldpage;
    private String title;
    private String pass;

    Pop(String op, String t, String pa){
        oldpage = op;
        title=t;
        pass=pa;
    }
    Pop(String t){
        title=t;
    }




    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialogcustom,null);
        builder.setView(view)
                .setTitle(title)
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
                        //La variable oldpage se usa solo en caso de que se editen los datos.
                        listener.applyText(pass, site,oldpage);
                    }
                });
        Site = view.findViewById(R.id.editTextUsername);
        password = view.findViewById(R.id.editTextPass);
        Site.setText(oldpage);
        password.setText(pass);
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
        void applyText(String pass, String site, String oldSite);
    }
}
