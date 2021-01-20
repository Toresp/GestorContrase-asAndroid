package com.example.proyectomiguelsambademartnez;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class PassData implements Serializable {
    @SerializedName("password") private String password;
    @SerializedName("page") private String localizacion;
    @SerializedName("creation_date") private String fechaCambio;


    PassData(String pass, String local, String fech) {
        password = pass;
        localizacion = local;
        LocalDate fecha= LocalDate.parse(fech, DateTimeFormatter.ofPattern("YY-MM-DD"));
        fechaCambio=fecha.toString();
    }



    public String getLocalizacion() {
        return localizacion;
    }

    public String getPassword() {
        return password;
    }

    public String getFechaCambio() {
        return fechaCambio;
    }
}

