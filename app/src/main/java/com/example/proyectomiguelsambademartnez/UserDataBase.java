package com.example.proyectomiguelsambademartnez;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;


//Maneja los datos recogidos e introducidos en la base de datos local.
public class UserDataBase {
    private Context context;
    private static AppBaseDatos appbd;

    UserDataBase(Context context){
        this.context=context;
        appbd = new AppBaseDatos(this.context);

    }
//Recoge todos los datos de el usuario desde la base de datos local.
    @RequiresApi(api = Build.VERSION_CODES.O)
    public contraseñasUser getDatos(String uid,String email) {
        User resultado = new User(uid,email);
        List<PassData> Datos = new ArrayList<>();
        String consulta = "SELECT password,page,creation_date FROM UPASS WHERE uid=?";
        contraseñasUser resultadoF = null;
        String[] param = {resultado.UserID,resultado.email};
        SQLiteDatabase sqlLiteDB = appbd.getWritableDatabase();
        Cursor cursor = sqlLiteDB.rawQuery(consulta, param);
        this.depuracion(consulta, param);
        Log.d("DEPURACIÓN", "Nº filas: " + cursor.getCount());
            if(cursor.moveToFirst()){
                do{
                    Datos.add(new PassData(cursor.getString(1),cursor.getString(2),cursor.getString(3)));
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
//Añade una contraseña a la base de datos local
    public Boolean AñadirContraseña(String id,String pass, String site, String date){
        long result;
        SQLiteDatabase sqlLiteDB = appbd.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("uid",id);
        values.put("password",pass);
        values.put("page",site);
        values.put("creation_date",date);
        result = sqlLiteDB.insertWithOnConflict("contraseñas", null, values,SQLiteDatabase.CONFLICT_REPLACE);
        sqlLiteDB.close();
        if (result == -1)
            return false;
        return true;
    }
//Comprueba la existencia en la base de datos local de el id de el usuario y de la pagina vinculada
    public static Boolean Existe(String id,String page){
        SQLiteDatabase sqlLiteDB = appbd.getWritableDatabase();
        String[] param = {id,page};
        String consulta = "SELECT * FROM UPASS WHERE uid=? AND page=?";
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
