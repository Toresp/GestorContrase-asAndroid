package com.example.proyectomiguelsambademartnez;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatDialogFragment;

public class Pop extends AppCompatDialogFragment {
    private EditText Site;
    private EditText password;
    private ImageButton Typebtn;
    private ImageButton autoGen;
    private PopListener listener;
    private String oldpage;
    private String title;
    private String pass;
    private Boolean hidden = true;

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
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(getResources().getString(R.string.add), new DialogInterface.OnClickListener() {
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
        Typebtn = view.findViewById(R.id.TypeButton);
        autoGen = view.findViewById(R.id.autogen);

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
        autoGen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password.setText(autoGenPass.GenPass());
            }
        });
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
