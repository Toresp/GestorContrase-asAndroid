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
        List<PassData> Datos = new ArrayList<>();
        String consulta = "SELECT page,password,creation_date FROM UPASS WHERE uid=?";
        UserData resultadoF = null;
        String[] param = {resultado.UserID};
        try {
            SQLiteDatabase sqlLiteDB = appbd.getWritableDatabase();
            Cursor cursor = sqlLiteDB.rawQuery(consulta, param);
            this.depuracion(consulta, param);
            Log.d("DEPURACIÓN", "Nº filas: " + cursor.getCount());
            if (cursor.moveToFirst()) {
                do {
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

        public Boolean editDatos(UserData us, String page, PassData data){
            String consulta = "UPDATE UPASS SET page = ? AND password = ? AND creation_date = ? WHERE uid=? AND page=? ";
            String[] Param={data.getPage(),data.getPassword(),data.getCreation_date(),us.UserID,page};
            try {
                SQLiteDatabase db = appbd.getWritableDatabase();
                db.rawQuery(consulta, Param);
                db.close();
                return true;
            }catch(Exception Ex){
                return false;
            }
        }

        public Boolean DelDatos(String id, String page) {
            String consulta = "DELETE FROM UPASS WHERE uid = ? AND page = ?";
            String[] Param={id,page};
            try{
                SQLiteDatabase db = appbd.getWritableDatabase();
                db.rawQuery(consulta, Param);
                db.close();
                return true;
            }catch(Exception Ex){
                return false;
            }
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
        values.put("page",site);
        values.put("password",pass);
        values.put("creation_date",date);
        values.put("uid",id);
        result = sqlLiteDB.insert("UPASS", null, values);
        sqlLiteDB.close();
        if (result == -1)
            return false;
        return true;
    }

    public Boolean AñadirContraseña(List<PassData> data, String id){
        long result=0;
        for (int i=0; i < data.size();i++) {
            SQLiteDatabase sqlLiteDB = appbd.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("page", data.get(i).getPage());
            values.put("password", data.get(i).getPassword());
            values.put("creation_date", data.get(i).getCreation_date());
            values.put("uid", id);
            if (result==-1)
                sqlLiteDB.insert("UPASS", null, values);
            else
                result = sqlLiteDB.insert("UPASS", null, values);
            sqlLiteDB.close();
        }
        if (result == -1)
            return false;
        return true;
    }

//Comprueba la existencia en la base de datos local de el id de el usuario y de la pagina vinculada
    public  Boolean ExistPage(String id, String page){
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



    public Boolean CrearUsuario(String id,String email){
        long result;
        SQLiteDatabase sqlLiteDB = appbd.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("uid",id);
        values.put("email",email);
        result = sqlLiteDB.insert("USER", null, values);
        sqlLiteDB.close();
        if (result == -1)
            return false;
        return true;
    }



}
