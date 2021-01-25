package com.example.proyectomiguelsambademartnez;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserData extends User {
    private List<PassData> Contraseñas;

    
//Se crea un usuario de contraseñas a partir de un usuario
    UserData(User us, List contraseñas) {
        super(us.UserID, us.email);
        this.Contraseñas = contraseñas;
    }


    public void addContraseña(String contraseña, String localizacion, String fechaCambio){
        Contraseñas.add(new PassData(contraseña,localizacion,fechaCambio));
    }


    public List getContraseñas() {
        return this.Contraseñas;
    }

    public void setContraseñas(List<PassData> contraseñas) {
        Contraseñas = contraseñas;
    }

}

