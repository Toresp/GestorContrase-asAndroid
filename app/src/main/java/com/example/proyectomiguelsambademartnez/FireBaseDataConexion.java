package com.example.proyectomiguelsambademartnez;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;


//Esta clase se ocupará de realizar todas las conexiones con Firebase.
public class FireBaseDataConexion {
    FirebaseDatabase database;
    private DatabaseReference myRef;

    FireBaseDataConexion(FirebaseDatabase database){
        this.database=database;
    }

    public void writeFire(UserData us) {
        myRef  = database.getReference();
        myRef.child(us.UserID).child("contraseñas").setValue(us.getContraseñas());
    }
    public List<PassData> ReadFire(String id){
        List<PassData> Datos;
        myRef = database.getReference();
        Datos = (List<PassData>) myRef.child(id).child("contraseñas").get();
        return Datos;
    }
}
