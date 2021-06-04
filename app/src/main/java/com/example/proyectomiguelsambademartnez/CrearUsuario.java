package com.example.proyectomiguelsambademartnez;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class CrearUsuario extends AppCompatActivity {
    private EditText user;
    private EditText pass;
    private EditText pass2;
    private TextView volver;
    private DataBaseConexion bd;
    private FirebaseAuth mAuth;
    private Boolean CreatedFrom;
    private Button send;
    private UserData usLocal;
    private FireBaseDataConexion con;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crearusuario);
        mAuth = FirebaseAuth.getInstance();
        con = new FireBaseDataConexion(FirebaseDatabase.getInstance());
        bd = new DataBaseConexion(this);
        volver = findViewById(R.id.volverInicio);
        user = findViewById(R.id.editTextUser);
        pass = findViewById(R.id.editTextPassword);
        pass2 = findViewById(R.id.editTextPassword2);
        send = findViewById(R.id.Send);
        //Averigua si la activity fue lanzada desde la pantalla de inicio de sesión o la pantalla de anónimo.
        try{
            CreatedFrom = (Boolean) getIntent().getSerializableExtra(sesion_anonima.Launched);
            usLocal = (UserData) getIntent().getSerializableExtra(sesion_anonima.LocalPasswords);
            if(CreatedFrom == null)
                CreatedFrom = false;
        }catch (Exception Ex){
            CreatedFrom = false;
        }
        if(!CreatedFrom) {
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Comprobaciones();
                }
            });
            volver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Update();
                }
            });
        }else{
            volver.setText("Volver a la sesión anonima");
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ComprobarYActualizar();
                }
            });
            volver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goBack();
                }
            });
        }


    }
    //Se realizan comprobaciones de acuerdo a que las contraseñas sean iguales y el usuario no exista en la Base de datos.
    private void Comprobaciones() {
        final String[] Datos = new String[]{user.getText().toString(), pass.getText().toString(), pass2.getText().toString()};
        if (TextUtils.isEmpty(Datos[0]) || TextUtils.isEmpty(Datos[1]) || TextUtils.isEmpty(Datos[2]))
            Toast.makeText(getApplicationContext(), "Uno o mas campos vacios, rellene todos los campos", Toast.LENGTH_SHORT).show();
        else {
            if (Datos[1].equals(Datos[2])) {

                mAuth.createUserWithEmailAndPassword(Datos[0], Datos[1]).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Creado usuario correctamente.
                            Log.d("DEPURACION", "createUserWithEmail:success");
                            if(bd.CrearUsuario(mAuth.getUid(), Datos[0],false))
                                Toast.makeText(CrearUsuario.this, "Usuario Creado con exito.",
                                        Toast.LENGTH_SHORT).show();
                            Update();
                        } else {
                            // Fallo al crear usuario.
                            Log.w("DEPURACION", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CrearUsuario.this, "No se ha podido crear el usuario.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else Toast.makeText(CrearUsuario.this, "Las contraseñas no coinciden.",
                    Toast.LENGTH_SHORT).show();
        }
    }
    //Lanza la actividad de inicio.
    private void Update(){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }


    //Retrocede a la actividad de sesión anonima.
    private void goBack(){
        startActivity(new Intent(this, sesion_anonima.class));
        finish();
    }

    //Comprueba los datos del usuario y los registra en la base de datos local una vez firebase complete el registro.
    private void ComprobarYActualizar() {
        final String[] Datos = new String[]{user.getText().toString(), pass.getText().toString(), pass2.getText().toString()};
        if (TextUtils.isEmpty(Datos[0]) || TextUtils.isEmpty(Datos[1]) || TextUtils.isEmpty(Datos[2]))
            Toast.makeText(getApplicationContext(), "Uno o mas campos vacios, rellene todos los campos", Toast.LENGTH_SHORT).show();
        else {
            if (Datos[1].equals(Datos[2])) {
                mAuth.createUserWithEmailAndPassword(Datos[0], Datos[1]).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            usLocal.UserID = mAuth.getUid();
                            usLocal.email = Datos[0];
                            bd.CrearUsuario(usLocal.UserID,usLocal.email,false);
                            bd.AñadirContraseña(usLocal.getContraseñas(),usLocal.UserID,false);

                            con.writeFire(usLocal);
                            Toast.makeText(CrearUsuario.this, "Usuario Creado con Exito y datos en la nube",
                                    Toast.LENGTH_SHORT).show();
                            Update();
                        } else {
                            // Fallo al crear usuario.
                            Log.w("DEPURACION", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CrearUsuario.this, "No se ha podido crear el usuario.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else Toast.makeText(CrearUsuario.this, "Las contraseñas no coinciden.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}




