package com.example.proyectomiguelsambademartnez;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PassData {
    private String userID;
    private long dataID;//Se trata de un dato autoincremental.
    private String localizacion;
    private String password;
    private LocalDate fechaCambio;



    @RequiresApi(api = Build.VERSION_CODES.O)
    PassData(String pass, String local, String fech) {
        password = pass;
        localizacion = local;
        fechaCambio = LocalDate.parse(fech, DateTimeFormatter.ofPattern("YY-MM-DD"));

    }

    public String getLocalizacion() {
        return localizacion;
    }

    public String getPassword() {
        return password;
    }

    public LocalDate getFechaCambio() {
        return fechaCambio;
    }
}

