package com.example.proyectomiguelsambademartnez;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.Serializable;

public class AppBaseDatos extends SQLiteOpenHelper  {
    public final static String NOME_BD = "localdata.db";
    public final static int VERSION_BD = 1;

    public AppBaseDatos (Context context) {
        super(context, NOME_BD, null, VERSION_BD);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
