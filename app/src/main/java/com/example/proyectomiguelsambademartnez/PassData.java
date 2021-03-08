package com.example.proyectomiguelsambademartnez;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;


public class PassData implements Serializable {
    @SerializedName("password") private String password;
    @SerializedName("page") private String page;
    @SerializedName("creation_date") private String creation_date;

    PassData() {}

    PassData(String pass, String pagina, String fech) {
        password = pass;
        page = pagina;
        creation_date =fech;
    }

    public static PassData encriptPassData(PassData D,String Id){
        try{
            return new PassData(DataProtect.encryptPass(D.password,Id),D.page,D.creation_date);

        }catch(Exception Ex){
            return D;
        }
    }

    public static PassData decryptPassData(PassData D,String Id){
        try{
            return new PassData(DataProtect.decryptPass(D.password,Id),D.page,D.creation_date);


        }catch(Exception Ex){
            return D;
        }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PassData passData = (PassData) o;
        return page.equals(passData.page);
    }

    @Override
    public int hashCode() {
        return Objects.hash(page);
    }
}


