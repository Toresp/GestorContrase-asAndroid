package com.example.proyectomiguelsambademartnez;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    public final static String OBJETO = "contraseñaUser";
    public final static String BD = "UserDataB";
    DataBaseConexion bd;
    private FirebaseAuth mAuth;
    FireBaseDataConexion Data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio_sesion);
        copiarBD();
        mAuth = FirebaseAuth.getInstance();
        this.bd = new DataBaseConexion(this);
        pulsarBoton();
        pulsarTexto();

    }




    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onStart() {
        super.onStart();
        try{
            FirebaseAuth.getInstance().signOut();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        update(currentUser);

        }catch (Exception ex){

        }

    }

    private void copiarBD() {
        String bddestino = "/data/data/" + getPackageName() + "/databases/"
                + AppBaseDatos.NOME_BD;
        File file = new File(bddestino);
        Log.d("DEPURACIÓN", "Ruta archivo BD: " + bddestino);
        if (file.exists()) {
            Toast.makeText(getApplicationContext(), "La BD no se va a copiar. Ya existe", Toast.LENGTH_LONG).show();
            return; // Ya existe la BD, salimos del método
        }
        String pathbd = "/data/data/" + getPackageName()
                + "/databases/";
        File filepathdb = new File(pathbd);
        filepathdb.mkdirs();


        InputStream inputstream;
        try {
            inputstream = getAssets().open(AppBaseDatos.NOME_BD);
            OutputStream outputstream = new FileOutputStream(bddestino);
            int tamread;
            byte[] buffer = new byte[2048];
            while ((tamread = inputstream.read(buffer)) > 0) {
                outputstream.write(buffer, 0, tamread);
            }
            inputstream.close();
            outputstream.flush();
            outputstream.close();
            Toast.makeText(getApplicationContext(), "BASE DE DATOS COPIADA", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void pulsarBoton() {
        Button btnLogin = (Button) findViewById(R.id.Login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                iniciarSesion();
            }
        });
    }

    private void pulsarTexto(){
        TextView CreateUser = (TextView) findViewById(R.id.CrearUsuario);
        CreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarRegistro();
            }
        });
    }

    private void iniciarRegistro(){
        startActivity(new Intent(this, CrearUsuario.class));
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void iniciarSesion() {
        String email = ((EditText) findViewById(R.id.usuario)).getText().toString();
        String password = ((EditText) findViewById(R.id.contraseña)).getText().toString();
        //implementar inicio de sesión de Firebase.
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("DEPURACION", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            update(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("DEPURACION", "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Autenticacion fallida.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
     }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void update(FirebaseUser user){
        UserData usr ;
        String id= user.getUid();
        String email = user.getEmail();
        //Si no existe el usuario de forma local, crea el usuario en la base de datos local y
        // pone todos los datos de la nube en la base de datos local
        if(!bd.ExistUser(id)) {
            Toast.makeText(MainActivity.this, "Datos locales de el usuario no existentes buscando en la nube.", Toast.LENGTH_SHORT).show();
            bd.CrearUsuario(id, email);
            Data= new FireBaseDataConexion(FirebaseDatabase.getInstance());
            Data.SyncFire(id,bd);
            Data.writeUltimaAct(id);
        }
        usr = this.bd.getDatos(id,email);
        Intent intent = new Intent(this, sesion_iniciada.class);
        intent.putExtra(this.OBJETO,usr);
        startActivity(intent);
    }

}
