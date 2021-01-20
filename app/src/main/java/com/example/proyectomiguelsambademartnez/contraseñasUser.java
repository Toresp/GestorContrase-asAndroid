package com.example.proyectomiguelsambademartnez;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class contraseñasUser extends User {
    private List<PassData> Contraseñas;

    
//Se crea un usuario de contraseñas a partir de un usuario
    contraseñasUser(User us, List contraseñas) {
        super(us.UserID, us.email);
        this.Contraseñas = contraseñas;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addContraseña(String contraseña, String localizacion, String fechaCambio){
        Contraseñas.add(new PassData(contraseña,localizacion,fechaCambio));
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getGson(List<PassData> lista){
        return new Gson().toJson(lista);
    }

    public List getContraseñas() {
        return this.Contraseñas;
    }




}

