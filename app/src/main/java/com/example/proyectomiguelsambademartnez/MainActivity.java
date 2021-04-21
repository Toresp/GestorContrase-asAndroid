package com.example.proyectomiguelsambademartnez;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

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
import java.util.concurrent.Executor;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

public class MainActivity extends AppCompatActivity {
    public final static String OBJETO = "UserData";
    public final static String BD = "UserDataB";
    private DataBaseConexion bd;
    private FirebaseAuth mAuth;
    private FireBaseDataConexion Data;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    private String KEY_NAME = "AndroidKey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio_sesion);
        copiarBD();
        mAuth = FirebaseAuth.getInstance();
        this.bd = new DataBaseConexion(this);
        pulsarBoton();
        pulsarTexto();
        pulsarAnonimo();

    }




    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    //Cuando la aplicación se inicie con un usuario aún logeado realizará un inicio de sesión solicitando la huella dactilar del usuario si este dispone de una
    public void onStart() {
        super.onStart();
        try {
            // Check if user is signed in (non-null) and update UI accordingly.
            //mAuth.signOut();
            if(mAuth.getCurrentUser() != null) {
                if (!ComprobarBiometria()) {
                    update(mAuth.getCurrentUser());
                }
                executor = ContextCompat.getMainExecutor(this);
                biometricPrompt = new BiometricPrompt(MainActivity.this,
                        executor, new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errorCode,
                                                      @NonNull CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                        Toast.makeText(getApplicationContext(),
                                "Authentication error: " + errString, Toast.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onAuthenticationSucceeded(
                            @NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        Toast.makeText(getApplicationContext(),
                                getResources().getString(R.string.finger_auth_done), Toast.LENGTH_SHORT).show();
                        update(mAuth.getCurrentUser());
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.finger_auth_fail),
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                });

                promptInfo = new BiometricPrompt.PromptInfo.Builder()
                        .setTitle("Biometric login for my app")
                        .setSubtitle("Log in using your biometric credential")
                        .setNegativeButtonText("Use account password")
                        .build();
                biometricPrompt.authenticate(promptInfo);
            }
        }catch (Exception e) {
                e.printStackTrace();
            }

}

        private void copiarBD() {
        String bddestino = "/data/data/" + getPackageName() + "/databases/"
                + AppBaseDatos.NOME_BD;
        File file = new File(bddestino);
        Log.d("DEPURACIÓN", "Ruta archivo BD: " + bddestino);
        if (file.exists()) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Existencia), Toast.LENGTH_LONG).show();
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
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.copied), Toast.LENGTH_LONG).show();
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
        TextView CreateUser = (TextView) findViewById(R.id.crearUsuario);
        CreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarRegistro();
            }
        });
    }



    private void iniciarRegistro(){
        startActivity(new Intent(this, CrearUsuario.class));
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
                            Toast.makeText(MainActivity.this, getResources().getString(R.string.auth),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
     }

//Peta cuando el usuario nuevo no tiene datos en la nube.
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void update(FirebaseUser user){
        UserData usr;
        String id= user.getUid();
        String email = user.getEmail();
        //Si no existe el usuario de forma local, crea el usuario en la base de datos local y
        // introduce todos los datos de la nube en la base de datos local
        if(!bd.ExistUser(id)) {
            Toast.makeText(MainActivity.this, getResources().getString(R.string.cloud), Toast.LENGTH_SHORT).show();
            Data= new FireBaseDataConexion(FirebaseDatabase.getInstance());
            bd.CrearUsuario(id, email,false);
            Data.SyncFire(id,bd);
            Data.writeUltimaAct(id);

        }
        usr = this.bd.getDatos(id,email);
        Intent intent = new Intent(this, sesion_iniciada.class);
        intent.putExtra(this.OBJETO,usr);
        startActivity(intent);
        finish();
    }
    private void pulsarAnonimo(){
        TextView Anonimo = (TextView) findViewById(R.id.inicioAnonimo);
        Anonimo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarAnonimo();
            }
        });
    }

    public void iniciarAnonimo(){
        startActivity(new Intent(this, usuariosLocales.class));
    }

    public Boolean ComprobarBiometria(){
        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.");
                return true;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Log.e("MY_APP_TAG", "No biometric features available on this device.");
                return false;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.");
                return false;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                // Prompts the user to create credentials that your app accepts.
                return false;

        }
        return false;
    }


}
