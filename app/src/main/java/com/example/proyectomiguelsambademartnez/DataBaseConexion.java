package com.example.proyectomiguelsambademartnez;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


//Maneja los datos recogidos e introducidos en la base de datos local.
public class DataBaseConexion {
    private Context context;
    private static AppBaseDatos appbd;

    DataBaseConexion(Context context){
        this.context=context;
        appbd = new AppBaseDatos(this.context);

    }
//Recoge todos los datos de el usuario desde la base de datos local.
    public UserData getDatos(String uid, String email) {
        User resultado = new User(uid,email);
        String consulta = "";
        List<PassData> Datos = new ArrayList<>();
        if(email != ""){
            consulta = "SELECT page,password,creation_date FROM UPASS WHERE uid=?";

        }else consulta = "SELECT apage,apassword,acreation_date FROM AUPASS WHERE username=?";
        UserData resultadoF = null;
        String[] param = {resultado.UserID};
        try {
            SQLiteDatabase sqlLiteDB = appbd.getWritableDatabase();
            Cursor cursor = sqlLiteDB.rawQuery(consulta, param);
            this.depuracion(consulta, param);
            Log.d("DEPURACIÓN", "Nº filas: " + cursor.getCount());
            if (cursor.moveToFirst()) {
                do {
                    //Fallo extraño al recibir los datos de la base de datos se repite la contraseña donde debería estar la fecha de creación
                    Datos.add(new PassData(cursor.getString(1), cursor.getString(0), cursor.getString(2)));
                } while (cursor.moveToNext());
            }
            sqlLiteDB.close();
        }catch(Exception Ex){
            Ex.printStackTrace();
        }
            resultadoF = new UserData(resultado, Datos);
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
//Si el local es false crea un Usuario que se sube a la nube si es true crea uno local
    public Boolean AñadirContraseña(String id,PassData data,Boolean local){
        long result=-1;
        SQLiteDatabase sqlLiteDB = appbd.getWritableDatabase();
        ContentValues values = new ContentValues();
        if (!local){
            values.put("page",data.getPage());
            values.put("password",data.getPassword());
            values.put("creation_date",data.getCreation_date());
            values.put("uid",id);
            result = sqlLiteDB.insert("UPASS", null, values);

        }else{
            values.put("apage",data.getPage());
            values.put("apassword",data.getPassword());
            values.put("acreation_date",data.getCreation_date());
            values.put("username",id);
            result = sqlLiteDB.insert("AUPASS", null, values);
        }
        sqlLiteDB.close();
        if (result == -1)
            return false;
        return true;
    }

    //Si el local es false crea un Usuario que se sube a la nube si es true crea uno local
    public Boolean AñadirContraseña(List<PassData> data, String id,Boolean local){
        long result=0;
        for (int i=0; i < data.size();i++) {
            SQLiteDatabase sqlLiteDB = appbd.getWritableDatabase();
            ContentValues values = new ContentValues();
            if(!local){
                values.put("page", data.get(i).getPage());
                values.put("password", data.get(i).getPassword());
                values.put("creation_date", data.get(i).getCreation_date());
                values.put("uid", id);
                result = sqlLiteDB.insert("UPASS", null, values);
            }else{
                values.put("apage", data.get(i).getPage());
                values.put("apassword", data.get(i).getPassword());
                values.put("acreation_date", data.get(i).getCreation_date());
                values.put("username", id);
                result = sqlLiteDB.insert("AUPASS", null, values);
            }
            sqlLiteDB.close();
        }
        if (result == -1)
            return false;
        return true;
    }

//Comprueba la existencia en la base de datos local de el id de el usuario y de la pagina vinculada
    public Boolean ExistPage(String id, String page, Boolean local){
        SQLiteDatabase sqlLiteDB = appbd.getWritableDatabase();
        String consulta;
        String[] param = {id,page};
        if(!local){
             consulta = "SELECT * FROM UPASS WHERE uid=? AND page=?";

        }else{
            consulta = "SELECT * FROM AUPASS WHERE username=? AND apage=?";
        }
        Cursor cursor = sqlLiteDB.rawQuery(consulta, param);
        if(cursor.moveToFirst()){
            appbd.close();
            return true;
        }
        return false;
    }
    //Si el local es false crea un Usuario que se sube a la nube si es true crea uno local
    public Boolean ExistUser(String id){
        SQLiteDatabase sqlLiteDB = appbd.getWritableDatabase();
        String[] param = {id};
        String consulta = "SELECT * FROM USER WHERE uid=?";
        Cursor cursor = sqlLiteDB.rawQuery(consulta, param);
        if(cursor.moveToFirst()){
            appbd.close();
            return true;
        }
        return false;
    }


//Si el local es false crea un Usuario que se sube a la nube si es true crea uno local
    public Boolean CrearUsuario(String id,String email,Boolean local){
        long result;
        SQLiteDatabase sqlLiteDB = appbd.getWritableDatabase();
        ContentValues values = new ContentValues();
        if(!local) {
            values.put("uid", id);
            values.put("email", email);
            result = sqlLiteDB.insert("USER", null, values);
        }else {
            values.put("username", id);
            values.put("password", email);
            result = sqlLiteDB.insert("AUSER", null, values);
            sqlLiteDB.close();
        }
        if (result == -1)
            return false;
        return true;
    }

    public Boolean editDatos(UserData us, String oldpage, PassData data,Boolean local){
        int result;
        String consulta;
        ContentValues values = new ContentValues();
        SQLiteDatabase db = appbd.getWritableDatabase();
        String[] Param={us.UserID, oldpage};
        if(!local){
            consulta = "uid=? AND page=? ";
            values.put("page",data.getPage());
            values.put("password",data.getPassword());
            values.put("creation_date",data.getPassword());
            values.put("uid",us.UserID);
            result = db.update("UPASS", values,consulta,Param);
        }
        else{
            consulta = "username=? AND apage=? ";
            values.put("apage",data.getPage());
            values.put("apassword",data.getPassword());
            values.put("acreation_date",data.getPassword());
            values.put("username",us.UserID);
            result = db.update("AUPASS", values,consulta,Param);
        }
            db.close();
            return result!=-1;
    }

    public Boolean DelDatos(String id, String page, Boolean local) {
        int num;
        String whereClause;
        SQLiteDatabase db = appbd.getWritableDatabase();
        String whereArgs[] = {id,page};
        if(!local){
           whereClause = "uid = ? AND page = ?";
            num = db.delete("UPASS",whereClause,whereArgs);
        }else{
            whereClause = "username = ? AND apage = ?";
            num = db.delete("AUPASS",whereClause,whereArgs);
        }
            db.close();
            return num!=-1;
    }


    public List getLocalUser(){
        List<User> Datos = new ArrayList<>();
        String consulta = "SELECT username,password FROM AUSER";
        try {
            SQLiteDatabase sqlLiteDB = appbd.getWritableDatabase();
            Cursor cursor = sqlLiteDB.rawQuery(consulta,null);
            Log.d("DEPURACIÓN", "Nº filas: " + cursor.getCount());
            if (cursor.moveToFirst()) {
                do {
                    Datos.add(new User(cursor.getString(0),""));
                } while (cursor.moveToNext());
            }
            sqlLiteDB.close();
        }catch(Exception Ex){
            Ex.printStackTrace();
        }
        return Datos;
    }

    public Boolean AuthLocalUser(String username, String contraseña) {
        String consulta = "SELECT * FROM AUSER WHERE username=? AND password=?";
        try {
            SQLiteDatabase sqlLiteDB = appbd.getWritableDatabase();
            String param[] = {username, contraseña};
            Cursor cursor = sqlLiteDB.rawQuery(consulta, param);
            this.depuracion(consulta, param);
            Log.d("DEPURACIÓN", "Nº filas: " + cursor.getCount());
            if (cursor.moveToFirst()) {
                return true;
            }
        } catch (Exception Ex) {
            Ex.printStackTrace();
        }
        return false;
    }



}
