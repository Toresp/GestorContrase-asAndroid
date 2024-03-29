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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class sesion_anonima extends AppCompatActivity implements Pop.PopListener {
    public final static String Launched = "True";
    public final static String LocalPasswords = "DATA";
    private UserData usuario;
    private DataBaseConexion bd;
    private LinearLayout Botones;
    private TextView Iniciado;
    private FloatingActionButton Add;
    private Boolean Añadir = false;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sesion_anonima);
        this.bd = new DataBaseConexion(this);
        usuario = (UserData) getIntent().getSerializableExtra(usuariosLocales.DATOS);
        this.bd = new DataBaseConexion(this);
        Iniciado = findViewById(R.id.iniciado);
        Iniciado.setText(getResources().getString(R.string.contra_de) + " " + usuario.UserID);
        Botones = (LinearLayout) findViewById(R.id.Botones);
        Add = findViewById(R.id.Añadir);
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Añadir = true;
                openDialog();
            }
        });
        CargarContraseñas();
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_cloud), Toast.LENGTH_SHORT).show();
    }

    //Se crea un dialogo para introducir los datos al añadir una contraseña.
    private void openDialog() {
        Pop DialogPop = new Pop(getResources().getString(R.string.intro_pass));
        DialogPop.show(getSupportFragmentManager(), "Introduzca Contraseña");
    }

    //Se crea un dialogo para editar los datos existentes de el usuario.
    private void editDialog(String p, String pass) {
        Pop DialogPop = new Pop(p, getResources().getString(R.string.edit_pass), pass);
        DialogPop.show(getSupportFragmentManager(), "Editar contaseña");
    }

    //Se generan todos los botones y textbox de acuerdo al numero de contraseñas existentes
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void CargarContraseñas() {
        if (usuario.getContraseñas().size() > 0) {
            Botones.removeAllViews();
            for (int i = 0; i < usuario.getContraseñas().size(); i++) {
                //Consiste de 3 linear layouts horizontales los cuales permiten que se alinee los dos botones del menu y ocultar a la derecha
                LinearLayout l0 = new LinearLayout(this);
                LinearLayout l1 = new LinearLayout(this);
                LinearLayout l2 = new LinearLayout(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                l1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,1.0f));
                l2.setLayoutParams(layoutParams);
                l0.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                l1.setOrientation(LinearLayout.HORIZONTAL);
                l2.setOrientation(LinearLayout.HORIZONTAL);
                l0.setOrientation(LinearLayout.HORIZONTAL);
                final ImageButton menu = new ImageButton(this.getBaseContext());
                final Button btn = new Button(this.getBaseContext());
                final TextView txt = new TextView(this.getBaseContext());
                final HideButton btnHide = new HideButton(this.getBaseContext(), ((PassData) usuario.getContraseñas().get(i)).getPassword());
                btnHide.setLayoutParams(new LinearLayout.LayoutParams(80, 80));
                menu.setLayoutParams(new LinearLayout.LayoutParams(80, 80));
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
                        showPopup(v, txt.getText().toString(), btnHide.pass);

                    }
                });
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setClipboard(btn.getText().toString());
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.clipboard), Toast.LENGTH_SHORT).show();
                    }
                });
                l1.addView(txt);
                l2.addView(btnHide);
                l2.addView(menu);
                l0.addView(l1);
                l0.addView(l2);
                Botones.addView(l0);
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
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        //Se encripta la contraseña antes de ser añadida.
        PassData data = PassData.encriptPassData(new PassData(pass, site, date), usuario.UserID);
        if (Añadir) {
            if (!bd.ExistPage(usuario.UserID, site, true)) {
                if (bd.AñadirContraseña(usuario.UserID, data, true)) {
                    usuario.addContraseña(pass, site, date);
                } else
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                CargarContraseñas();
            } else
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.ya_existe), Toast.LENGTH_SHORT).show();
        } else {
            if (!bd.editDatos(usuario, oldSite, data, true))
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_editable), Toast.LENGTH_SHORT).show();
            else {
                Edit(oldSite, new PassData(pass, site, date));
                CargarContraseñas();
            }
        }
    }


    public void Delete(String txt) {
        List pass = usuario.getContraseñas();
        pass.remove(new PassData("", txt, ""));
        usuario.setContraseñas(pass);
        bd.DelDatos(usuario.UserID, txt, true);
    }

    public void Edit(String oldSite, PassData data) {
        List pass = usuario.getContraseñas();
        pass.set(pass.indexOf(new PassData("", oldSite, "")), data);
        usuario.setContraseñas(pass);
    }

    public void showPopup(View v, final String p, final String pass) {
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
                        editDialog(p, pass);
                        return true;
                    case R.id.del:
                        Delete(p);
                        CargarContraseñas();
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
        menu.removeItem(menu.getItem(2).getItemId());
        menu.removeItem(menu.getItem(0).getItemId());

        return true;

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //Se muestra en pantalla el menu de opciones.
            case R.id.options:
                Toast.makeText(this, R.string.VerCalendarOnline, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.signin:
                Intent datos = new Intent(this, CrearUsuario.class);
                datos.putExtra(Launched, true);
                datos.putExtra(LocalPasswords, usuario);
                //Se lleva a la pantalla de creación de usuario y todos los datos de anonimo de sustituyen por los nuevos datos creados.
                startActivity(datos);
                return true;
            case R.id.delAcc:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.DialogDel)
                        .setPositiveButton(R.string.afirmativo, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                bd.DeleteLocalUser(usuario.UserID);
                                goBack();

                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                builder.show();


            default:

                return super.onOptionsItemSelected(item);

        }
    }

    private void goBack() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
