package com.example.proyectomiguelsambademartnez;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class sesion_iniciada extends AppCompatActivity implements Pop.PopListener {
    public final static String DATOS = "usuario";
    private UserData usuario;
    private DataBaseConexion bd;
    private LinearLayout Botones;
    private TextView Iniciado;
    private FloatingActionButton Add;
    private FireBaseDataConexion Data;
    private Boolean Añadir = false;


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
        Add = findViewById(R.id.Añadir);
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Añadir=true;
                openDialog();
            }
        });
        CargarContraseñas();
    }

    //Se crea un dialogo para introducir los datos al añadir una contraseña
    private void openDialog() {
        Pop DialogPop = new Pop("Añadir contraseña");
        DialogPop.show(getSupportFragmentManager(), "Añadir Contraseña");
    }
    private void editDialog(String p, String pass){
        Pop DialogPop = new Pop(p,"Editar contraseña",pass);
        DialogPop.show(getSupportFragmentManager(), "Editar contaseña");
    }

    //Se generan todos los botones y textbox de acuerdo al numero de contraseñas existentes
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void CargarContraseñas() {
        if (usuario.getContraseñas().size() > 0) {
            Botones.removeAllViews();
            for (int i = 0; i < usuario.getContraseñas().size(); i++) {
                LinearLayout l1 = new LinearLayout(this);
                //El l2 encierra los botones que van alineados a la derecha
                LinearLayout l2 = new LinearLayout(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    l2.setGravity(RelativeLayout.ALIGN_RIGHT);
                l1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                l1.setOrientation(LinearLayout.HORIZONTAL);
                l2.setLayoutParams(layoutParams);
                l2.setOrientation(LinearLayout.HORIZONTAL);
                final ImageButton menu = new ImageButton(this.getBaseContext());
                final Button btn = new Button(this.getBaseContext());
                final TextView txt = new TextView(this.getBaseContext());
                final HideButton btnHide = new HideButton(this.getBaseContext(), ((PassData) usuario.getContraseñas().get(i)).getPassword());
                btnHide.setLayoutParams(new LinearLayout.LayoutParams(80,80));
                menu.setLayoutParams(new LinearLayout.LayoutParams(80,80));
                txt.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                txt.setText(((PassData) usuario.getContraseñas().get(i)).getPage());
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
                menu.setBackgroundResource(R.drawable.android_options);
                menu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopup(v, txt.getText().toString(),btnHide.pass);

                    }
                });
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setClipboard(btn.getText().toString());
                        Toast.makeText(getApplicationContext(), "Copiado al portapapeles!", Toast.LENGTH_SHORT).show();
                    }
                });
                l1.addView(txt);
                l2.addView(btnHide);
                l2.addView(menu);
                l1.addView(l2);
                Botones.addView(l1);
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
    //La variable oldpage se usa solo en caso de que se editen los datos.
    public void applyText(String pass, String site, String oldSite) {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).substring(0,10);
        //Se encripta la contraseña antes de ser añadida.
        PassData data = PassData.encriptPassData(new PassData(pass,site,date),usuario.UserID);
        if(Añadir) {
            if (!bd.ExistPage(usuario.UserID, site,false)) {
                if (bd.AñadirContraseña(usuario.UserID, data,false)) {
                    usuario.addContraseña(pass, site, date);
                    Data.writeFire(usuario);
                } else
                    Toast.makeText(getApplicationContext(), "Error Inesperado", Toast.LENGTH_SHORT).show();
                CargarContraseñas();
            } else
                Toast.makeText(getApplicationContext(), "Pagina ya existente, no se puede añadir", Toast.LENGTH_SHORT).show();
        }else{
            if(!bd.editDatos(usuario,oldSite,data,false))
                Toast.makeText(getApplicationContext(), "No se pudo editar", Toast.LENGTH_SHORT).show();
            else{
                Edit(oldSite, new PassData(pass, site, date));
                CargarContraseñas();
                Data.writeFire(usuario);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void actualizarDatos() {
            Data.SyncFire(usuario.UserID, bd);
            Data.writeUltimaAct(usuario.UserID);
            CargarContraseñas();

    }

    public void Delete(String txt) {
        List pass = usuario.getContraseñas();
        pass.remove(new PassData("", txt, ""));
        usuario.setContraseñas(pass);
        bd.DelDatos(usuario.UserID, txt,false);
    }
    public void Edit(String oldSite, PassData data){
        List pass = usuario.getContraseñas();
        pass.set(pass.indexOf(new PassData("", oldSite, "")),data);
        usuario.setContraseñas(pass);
    }

    public void showPopup(View v,final String p, final String pass) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.editordel, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.edit:
                        Añadir = false;
                        editDialog(p,pass);
                        return true;
                    case R.id.del:
                        Delete(p);
                        CargarContraseñas();
                        Data.writeFire(usuario);
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_user, menu);
        menu.removeItem(menu.getItem(3).getItemId());

        return true;

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                actualizarDatos();
                return true;
            //Se muestra en pantalla el menu de opciones.
            case R.id.options:
                Intent intent = new Intent(this, Calendario.class);
                intent.putExtra(this.DATOS,usuario);
                startActivity(intent);

                return true;
            //Se lleva a la pantalla de creación de usuario y todos los datos de anonimo de sustituyen por los nuevos datos creados.
            case R.id.logout:
                Intent datos = new Intent(this, MainActivity.class);
                startActivity(datos);

                return true;

            default:

                return super.onOptionsItemSelected(item);

        }


    }




}





