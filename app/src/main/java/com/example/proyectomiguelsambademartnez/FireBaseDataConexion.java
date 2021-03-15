package com.example.proyectomiguelsambademartnez;

import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


//Esta clase se ocupará de realizar todas las conexiones con Firebase.
public class FireBaseDataConexion {
    FirebaseDatabase database;
    private DatabaseReference myRef;

    FireBaseDataConexion(FirebaseDatabase database){
        this.database=database;
    }
    //Los datos de el primer usuario se suben los de el segundo no.
    public void writeFire(UserData us) {
        myRef  = database.getReference();
        myRef.child(us.UserID).child("contraseñas").setValue(us.encryptContraseñas());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void writeUltimaAct(String id){
        myRef = database.getReference();
        myRef.child(id).child("ultima_conexion").setValue(LocalDateTime.now().toString());
    }
    public void SyncFire(final String id, final DataBaseConexion bd){

        myRef = database.getReference(id+"/contraseñas");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {


                    List<PassData> Datos = new ArrayList();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        PassData data = dataSnapshot.getValue(PassData.class);
                        Datos.add(data);
                    }
                    bd.AñadirContraseña(Datos, id, false);

                }catch(Exception Ex){
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
