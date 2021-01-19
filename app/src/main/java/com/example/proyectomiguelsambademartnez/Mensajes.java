package com.example.proyectomiguelsambademartnez;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;

public class Mensajes extends DialogFragment {
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("No se pudo iniciar la sesión").setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("El login o el password que has introducido son incorrectos. Compruébalos y vuelve a intentarlo de nuevo.")
                .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Código asociado al botón Aceptar. Por ejemplo:
                        //Toast.makeText(getActivity(), "PULSADA OPCION BOA", Toast.LENGTH_LONG).show();
                    }
                });
        return builder.create();
    }
}
