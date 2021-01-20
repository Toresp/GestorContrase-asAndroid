package com.example.proyectomiguelsambademartnez;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


//Esta clase se ocupará de realizar todas las conexiones con Firebase.
public class FireBaseDataConexion {
    FirebaseDatabase database;

    FireBaseDataConexion(FirebaseDatabase database){
        this.database=database;
    }

    public void writeFire(contraseñasUser us){
        DatabaseReference myRef = database.getReference("message");
        myRef.setValue("Hello, World!");

    }
    public void ReadFire(){

    }
}
