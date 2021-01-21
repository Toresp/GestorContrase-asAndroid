package com.example.proyectomiguelsambademartnez;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;



public class PassData implements Serializable {
    @SerializedName("password") private String password;
    @SerializedName("page") private String page;
    @SerializedName("creation_date") private String creation_date;


    PassData(String pass, String local, String fech) {
        password = pass;
        page = local;
        creation_date =fech;
    }



    public String getPage() {
        return page;
    }

    public String getPassword() {
        return password;
    }

    public String getCreation_date() {
        return creation_date;
    }
}


