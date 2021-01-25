package com.example.proyectomiguelsambademartnez;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class sesion_iniciada extends AppCompatActivity implements Pop.PopListener  {
    UserData usuario;
    DataBaseConexion bd;
    LinearLayout Botones;
    TextView Iniciado;
    FireBaseDataConexion Data;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sesion_iniciada);
        usuario = (UserData) getIntent().getSerializableExtra(MainActivity.OBJETO);
        Data = new FireBaseDataConexion(FirebaseDatabase.getInstance());

        this.bd = new DataBaseConexion(this);
        Iniciado = findViewById(R.id.iniciado);
        Iniciado.setText("Contraseñas de " + usuario.email);
        Botones = (LinearLayout) findViewById(R.id.Botones);
        FloatingActionButton Add = findViewById(R.id.Añadir);
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
        CargarContraseñas();


    }
//Se crea un dialogo para introducir los datos al añadir una contraseña
    private void openDialog() {
        Pop DialogPop = new Pop();
        DialogPop.show(getSupportFragmentManager(),"Dialogo Contraseñas");
    }

    //Se generan todos los botones y textbox de acuerdo al numero de contraseñas existentes
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void CargarContraseñas() {
        if (usuario.getContraseñas().size() > 0) {
            Botones.removeAllViews();
            for (int i = 0; i < usuario.getContraseñas().size(); i++) {
                final Button btn = new Button(this.getBaseContext());
                TextView txt = new TextView(this.getBaseContext());
                final HideButton btnHide = new HideButton(this.getBaseContext(),((PassData)usuario.getContraseñas().get(i)).getPassword());
                btnHide.setLayoutParams(new LinearLayout.LayoutParams(80, 80));
                txt.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                txt.setText(((PassData)usuario.getContraseñas().get(i)).getPage());
                txt.setTextColor(Color.parseColor("#FFFFFF"));
                btn.setText("**************");
                btnHide.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (btnHide.ChangeState())
                            btn.setText("**************");
                        else btn.setText(btnHide.pass);
                    }
                });
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setClipboard(btn.getText().toString());
                        Toast.makeText(getApplicationContext(), "Copiado al portapapeles!", Toast.LENGTH_SHORT).show();
                    }
                });
                Botones.addView(txt);
                Botones.addView(btnHide);
                Botones.addView(btn);
            }
        }
    }

    //Añade la funcion copiar al hacer click en el boton que almacena la contraseña.
    private void setClipboard(String text) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) this.getBaseContext().getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) this.getBaseContext().getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void applyText(String pass, String site) {
        String date= LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        if(!bd.ExistPage(usuario.UserID,site)){
        if (bd.AñadirContraseña(usuario.UserID,pass, site,date)){
            usuario.addContraseña(pass, site,date);
            Data.writeFire(usuario);

        }else
            Toast.makeText(getApplicationContext(), "Error Inesperado", Toast.LENGTH_SHORT).show();
        CargarContraseñas();
        }else Toast.makeText(getApplicationContext(), "Pagina ya existente, no se puede añadir", Toast.LENGTH_SHORT).show();
    }

    public void actualizarDatos(){
        Data.ReadFire(usuario.UserID);
    }

    private void checkUserData(){
        if(!usuario.getContraseñas().equals(Data.ReadFire(usuario.UserID))){
            List<PassData> datos = Data.ReadFire(usuario.UserID);
            if(usuario.getContraseñas().size()>datos.size() ||usuario.getContraseñas().size()<datos.size()){
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Se usan los datos locales y los datos en la nube serán actualizados.
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //Se usan los datos en la nube y los locales serán atualizados.
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Los datos en la nube son distintos a los locales, cuales desea utilizar?").setPositiveButton("Datos Locales", dialogClickListener)
                        .setNegativeButton("Datos de la nube", dialogClickListener).show();
            }

        }

    }


}


