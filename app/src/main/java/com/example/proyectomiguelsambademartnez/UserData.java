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
    UserData(User us, List<PassData> contraseñas) {
        super(us.UserID, us.email);
        for(int i = 0; i < contraseñas.size();i++){
            contraseñas.set(i,PassData.decryptPassData(contraseñas.get(i),us.UserID));
        }
        this.Contraseñas = contraseñas;
    }


    public void addContraseña(String contraseña, String localizacion, String fechaCambio){
        Contraseñas.add(new PassData(contraseña,localizacion,fechaCambio));
    }

    public List<PassData> encryptContraseñas(){
        List<PassData> Datos = new ArrayList();
        for(int i = 0; i < Contraseñas.size();i++){
            String pass = Contraseñas.get(i).getPassword();
            Datos.add(PassData.encriptPassData(Contraseñas.get(i),UserID));
        }
        return Datos;
    }



    public List getContraseñas() {
        return this.Contraseñas;
    }

    public void setContraseñas(List<PassData> contraseñas) {
        Contraseñas = contraseñas;
    }

}

