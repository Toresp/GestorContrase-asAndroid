package com.example.proyectomiguelsambademartnez;

import android.content.Context;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class sesion_iniciada extends AppCompatActivity implements Pop.PopListener  {
    contraseñasUser usuario;
    UserDataBase bd;
    LinearLayout Botones;
    TextView Iniciado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sesion_iniciada);
        usuario = (contraseñasUser) getIntent().getSerializableExtra(MainActivity.OBJETO);
        this.bd = new UserDataBase(this);
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
                txt.setText(((PassData)usuario.getContraseñas().get(i)).getLocalizacion());
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
    public void applyText(String site, String pass) {
        if (bd.AñadirContraseña(usuario.getEmail(), site, pass)) {
            usuario.addContraseña(pass, site,LocalDate.now().format(DateTimeFormatter.ofPattern("YY-MM-DD")));
            Toast.makeText(getApplicationContext(), "Pagina añadida", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(getApplicationContext(), "Error Inesperado", Toast.LENGTH_SHORT).show();
        CargarContraseñas();


    }
}
