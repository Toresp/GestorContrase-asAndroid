package com.example.proyectomiguelsambademartnez;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class usuariosLocales extends AppCompatActivity implements PopCreator.PopCreatorListener {
    private final static String EncryptCode="AFGGFFGHF3947929FGDFG";//Codigo para encriptar las contraseñas locales totalmente aleatorio.
    public final static String DATOS = "DATOS";
    private List<User> Users;
    private DataBaseConexion bd;
    private LinearLayout Usuarios;
    private Button add;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_users);
        this.bd = new DataBaseConexion(this);
        Users = bd.getLocalUser();
        Usuarios = (LinearLayout) findViewById(R.id.Usuarios);
        add = (Button) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUserDialog();
            }
        });
        cargarUsuarios();

    }

    private void cargarUsuarios(){
        if(Users.size()>0){
            Usuarios.removeAllViews();
            for(int i = 0; i < Users.size(); i++){
                final Button btn = new Button(this.getBaseContext());
                btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                btn.setBackgroundResource(R.drawable.whiteborder);
                btn.setText(Users.get(i).UserID);
                btn.setTextColor(getResources().getColor(R.color.white));
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        logInDialog(btn.getText().toString());

                    }
                });
                Usuarios.addView(btn);
            }
        }

    }
    private void createUserDialog() {
        PopCreator DialogPop = new PopCreator(getResources().getString(R.string.crear_us),false);
        DialogPop.show(getSupportFragmentManager(), "Crear Usuario");
    }
    private void logInDialog(String name) {
        PopCreator DialogPop = new PopCreator(getResources().getString(R.string.intro_pass),true);
        DialogPop.setLogInUsername(name);
        DialogPop.show(getSupportFragmentManager(), "Introduzca su contraseña");
    }


    @Override
    public void createApplyText(String pass, String user) {
        if(!bd.ExistUser(user)){
            try {
                pass = (DataProtect.encryptPass(pass,EncryptCode));
            } catch (Exception Ex) {}
            bd.CrearUsuario(user, pass,true);
            Users.add(new User(user,""));
            cargarUsuarios();
        }else{
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.us_exist),Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void logApplyText(String password,String username) {
        try {
            password = (DataProtect.encryptPass(password,EncryptCode));
        } catch (Exception Ex) {}
        if(bd.AuthLocalUser(username,password)){
            UserData us = bd.getDatos(username,"");
            Intent intent = new Intent(this, sesion_anonima.class);
            intent.putExtra(this.DATOS,us);
            startActivity(intent);

        }else{
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.wrong_pass) ,Toast.LENGTH_SHORT).show();
        }
    }
}
