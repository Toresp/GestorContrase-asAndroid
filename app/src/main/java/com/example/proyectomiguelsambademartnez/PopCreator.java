package com.example.proyectomiguelsambademartnez;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

public class PopCreator extends AppCompatDialogFragment {
    private EditText Username;
    private EditText password;
    private EditText password2;
    private PopCreatorListener listener;
    private String title;
    private Boolean logIn;
    private String logInUsername;
    private ImageButton Typebtn;
    private Boolean hidden =true;


    PopCreator(String title, Boolean log){
        this.title = title;
        this.logIn = log;
    }
    //Crea dos tipos de dialogos distintos en funcion de la variable log-in para así crear un dialogo para solo introducir la contraseña e iniciar sesión o crear uno para registrar un nuevo usuario.
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view;
        if(logIn){
            view = inflater.inflate(R.layout.dialogcustom3, null);
            builder.setView(view)
                    .setTitle(title)
                    .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton(getResources().getString(R.string.login), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String pass = password.getText().toString();
                                listener.logApplyText(pass,logInUsername);
                        }
                    });
            password = view.findViewById(R.id.editTextPass);
            Typebtn = view.findViewById(R.id.TypeButton);
            Typebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(hidden){
                        password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        hidden = false;
                    }

                    else{
                        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        hidden = true;
                    }
                }
            });
        }else {
            view = inflater.inflate(R.layout.dialogcustom2, null);
            builder.setView(view)
                    .setTitle(title)
                    .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton(getResources().getString(R.string.add), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String pass = password.getText().toString();
                            String username = Username.getText().toString();
                            String pass2 = password2.getText().toString();
                            if(pass.length()>4) {
                                if (pass.equals(pass2))
                                    listener.createApplyText(pass, username);
                                else Toast.makeText(getContext(), getResources().getString(R.string.no_match),
                                        Toast.LENGTH_SHORT).show();
                            }else Toast.makeText(getContext(), getResources().getString(R.string.too_short),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
            Username = view.findViewById(R.id.editTextUsername);
            password = view.findViewById(R.id.editTextPass);
            password2 = view.findViewById(R.id.editTextPass2);
        }
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

    public void setLogInUsername(String logInUsername) {
        this.logInUsername = logInUsername;
    }

    public interface PopCreatorListener {
        void createApplyText(String pass, String name);
        void logApplyText(String password,String username);
    }
}