package com.example.proyectomiguelsambademartnez;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.google.gson.JsonArray;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class calendarPassData{
    private HashMap<String, String> events = new HashMap<>();
    private List<Integer> posDif = new ArrayList<>();
    private List<PassData> contraseñas;

    calendarPassData(List<PassData> Pass){
        this.contraseñas = Pass;
        String date;
        List<String> e = new ArrayList<>();
        for (int i = 0;i<Pass.size(); i++){
            e.add(Pass.get(i).getPage());
            int j = i+1;
            if(j<Pass.size()) {


                while (Pass.get(i).getCreation_date().equals(Pass.get(j).getCreation_date())) {
                    e.add(Pass.get(j).getPage());
                    j++;
                }
            }
            date = "";
            for (String s : e) {
                date += ", " + s;
            }
            events.put(e.get(i),date);
            posDif.add(i);
            i=j-1;
        }
    }

    public Boolean writeJson(Context context){
        JsonArray Datos = new JsonArray();
        for ( int i=0;i<posDif.size();i++){
            JSONObject data = new JSONObject();
            try {
                String date=contraseñas.get(posDif.get(i)).getCreation_date();
                data.put("date", date);
                data.put("event","Cambiar contraseña de la pagina "+ events.get(date));
                Datos.add(String.valueOf(data));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        try {
            File file = new File(context.getFilesDir(),"calendarJson.json");
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(Datos.toString());
            bufferedWriter.close();
            return true;
        } catch (Exception e) {
            return false;
        }

    }
}
