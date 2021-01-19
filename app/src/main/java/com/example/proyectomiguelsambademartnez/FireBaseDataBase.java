package com.example.proyectomiguelsambademartnez;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


//Esta clase se ocupará de realizar todas las conexiones con Firebase.
public class FireBaseDataBase {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");

    public void writeFire(){
        myRef.setValue("Hello, World!");

    }
    public void ReadFire(){

    }
}
