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

public class AddUsuarioAnon extends AppCompatActivity {
    EditText user;
    EditText pass;
    EditText pass2;
    TextView volver;
    DataBaseConexion bd;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crearusuario);
        mAuth = FirebaseAuth.getInstance();
        bd = new DataBaseConexion(this);
        volver = findViewById(R.id.volverInicio);
        user = findViewById(R.id.editTextUser);
        pass = findViewById(R.id.editTextPassword);
        pass2 = findViewById(R.id.editTextPassword2);
        Button send = new Button(this);
        send = findViewById(R.id.Send);
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



    }
    //Se realizan comprobaciones de acuerdo a que las contraseñas sean iguales y el usuario no exista en la Base de datos.
    private void Comprobaciones() {
        final String[] Datos = new String[]{user.getText().toString(), pass.getText().toString(), pass2.getText().toString()};
        if (TextUtils.isEmpty(Datos[0]) || TextUtils.isEmpty(Datos[1]) || TextUtils.isEmpty(Datos[2]))
            Toast.makeText(getApplicationContext(), "Uno o mas campos vacios, rellene todos los campos", Toast.LENGTH_SHORT).show();
        else {
            if (Datos[1].equals(Datos[2])) {
                //Comprobar con Firebase la existencia de el usuario!!!!!!.

                mAuth.createUserWithEmailAndPassword(Datos[0], Datos[1]).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("DEPURACION", "createUserWithEmail:success");
                            if(bd.CrearUsuario(mAuth.getUid(), Datos[0]))
                                Toast.makeText(AddUsuarioAnon.this, "Usuario Creado con exito.",
                                        Toast.LENGTH_SHORT).show();
                            Update();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("DEPURACION", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(AddUsuarioAnon.this, "No se ha podido crear el usuario.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    private void Update(){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
