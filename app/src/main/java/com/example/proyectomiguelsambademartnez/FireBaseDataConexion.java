package com.example.proyectomiguelsambademartnez;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


//Esta clase se ocupará de realizar todas las conexiones con Firebase.
public class FireBaseDataConexion {
    FirebaseDatabase database;
    public static Boolean Send;

    FireBaseDataConexion(FirebaseDatabase database){
        this.database=database;
    }

    public Boolean writeFire(UserData us){
        Send=false;
        DatabaseReference myRef = database.getReference();
        myRef.child(us.UserID).child("contraseñas").setValue(us.getContraseñas()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            Send=true;
            }
        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Send = false;
                    }
                });
        return Send;
    }
    public void ReadFire(){

    }
}
