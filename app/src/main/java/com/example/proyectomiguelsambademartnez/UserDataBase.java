package com.example.proyectomiguelsambademartnez;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

//Maneja los datos recogidos e introducidos en la base de datos local.
public class UserDataBase {
    private Context context;
    private AppBaseDatos appbd;

    UserDataBase(Context context){
        this.context=context;
        appbd = new AppBaseDatos(this.context);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public contraseñasUser getDatos(String uid,String email) {
        User resultado = new User(uid,email);
        ArrayList <PassData> Datos = new ArrayList<>();
        String consulta = "SELECT password,page,creation_date FROM UPASS WHERE uid=? AND user_email=?";
        contraseñasUser resultadoF = null;
        String[] param = {resultado.UserID,resultado.email};
        SQLiteDatabase sqlLiteDB = appbd.getWritableDatabase();
        Cursor cursor = sqlLiteDB.rawQuery(consulta, param);
        this.depuracion(consulta, param);
        Log.d("DEPURACIÓN", "Nº filas: " + cursor.getCount());
            if(cursor.moveToFirst()){
                do{
                    Datos.add(new PassData(cursor.getString(2),cursor.getString(3),cursor.getString(4)));
                }while(cursor.moveToNext());
            }resultadoF = new contraseñasUser(resultado,Datos);
            sqlLiteDB.close();
            return resultadoF;


        }



    void depuracion(String consulta, String[] param) {
        String texto = "Consulta: " + consulta + " Valores: ";
        for (String p : param) {
            texto += p + " ";
        }
        Log.d("DEPURACIÓN", texto);
    }

    public Boolean AñadirContraseña(String Uid,String email,String pass, String site, String date){
        long result;
        SQLiteDatabase sqlLiteDB = appbd.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("uid",Uid);
        values.put("user_email",email);
        values.put("password",pass);
        values.put("page",site);
        values.put("creation_date",date);
        result = sqlLiteDB.insertWithOnConflict("contraseñas", null, values,SQLiteDatabase.CONFLICT_REPLACE);
        sqlLiteDB.close();
        if (result == -1)
            return false;
        return true;
    }

    public Boolean Existe(String login){
        SQLiteDatabase sqlLiteDB = appbd.getWritableDatabase();
        String[] param = {login};
        String consulta = "SELECT * FROM Usuarios WHERE usuario=?";
        Cursor cursor = sqlLiteDB.rawQuery(consulta, param);
        if(cursor.moveToFirst()){
            appbd.close();
            return true;
        }
        return false;
    }

    public Boolean CrearUsuario(String nomUser,String pass){
        long result;
        SQLiteDatabase sqlLiteDB = appbd.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("usuario",nomUser);
        values.put("contraseña",pass);
        result = sqlLiteDB.insert("Usuarios", null, values);
        sqlLiteDB.close();
        if (result == -1)
            return false;
        return true;
    }



}
