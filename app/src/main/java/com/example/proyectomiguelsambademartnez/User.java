package com.example.proyectomiguelsambademartnez;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

//Se trata de la clase que utiliza el usuario básico.
public class User implements Serializable {
    @SerializedName("EmailOrUser") protected String email, UserID;


    User(String id, String email) {
        this.UserID = id;
        this.email = email;
    }
    User(){
        UserID="ANONIMUS";
        email=null;
    }


    public String getEmail() {
        return email;
    }


}