package com.example.proyectomiguelsambademartnez;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

public class contraseñasUser extends User {
    private ArrayList<PassData> Contraseñas;

    
//Se crea un usuario de contraseñas a partir de un usuario
    contraseñasUser(User us, ArrayList contraseñas) {
        super(us.UserID, us.email);
        this.Contraseñas = contraseñas;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addContraseña(String contraseña, String localizacion, String fechaCambio){
        Contraseñas.add(new PassData(contraseña,localizacion,fechaCambio));
    }

    public ArrayList getContraseñas() {
        return this.Contraseñas;
    }




}

