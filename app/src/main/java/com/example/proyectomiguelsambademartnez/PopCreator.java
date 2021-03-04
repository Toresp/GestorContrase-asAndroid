package com.example.proyectomiguelsambademartnez;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

public class PopCreator extends AppCompatDialogFragment {
    private EditText Username;
    private EditText password;
    private EditText password2;
    private PopCreatorListener listener;
    private String title;
    private Boolean logIn;

    PopCreator(String title, Boolean log){
        this.title = title;
        this.logIn = log;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view;
        if(logIn){
            view = inflater.inflate(R.layout.dialogcustom2, null);
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
                                listener.logApplyText(pass);
                        }
                    });
        }else {
            view = inflater.inflate(R.layout.dialogcustom2, null);
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
                            String username = Username.getText().toString();
                            String pass2 = password2.getText().toString();
                            if (pass == pass2)
                                listener.createApplyText(pass, username);
                            else Toast.makeText(getContext(), "Las contraseñas no coinciden.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        Username = view.findViewById(R.id.editTextUsername);
        password = view.findViewById(R.id.editTextPass);
        password2 = view.findViewById(R.id.editTextPass2);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (PopCreatorListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

    }


    public interface PopCreatorListener {
        void createApplyText(String pass, String username);
        void logApplyText(String username);
    }
}