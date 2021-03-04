package com.example.proyectomiguelsambademartnez;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class usuariosLocales extends AppCompatActivity implements PopCreator.PopCreatorListener, Pop.PopListener {
    private List<User> Users;
    private DataBaseConexion bd;
    private LinearLayout Usuarios;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_users);
        this.bd = new DataBaseConexion(this);
        Users = bd.getLocalUser();
        Usuarios = (LinearLayout) findViewById(R.id.Usuarios);

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
                        logInDialog();

                    }
                });
            }
        }

    }
    private void createUserDialog() {
        PopCreator DialogPop = new PopCreator("Crear Usuario");
        DialogPop.show(getSupportFragmentManager(), "Crear Usuario");
    }
    private void logInDialog() {
        Pop DialogPop = new Pop("Añadir contraseña");
        DialogPop.show(getSupportFragmentManager(), "Añadir Contraseña");
    }


    @Override
    public void applyText(String pass, String username, String oldSite) {
        if(bd.AuthLocalUser())

    }

    @Override
    public void createApplyText(String pass, String username) {

    }
}
