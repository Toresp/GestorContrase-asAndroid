package com.example.proyectomiguelsambademartnez;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.google.gson.JsonArray;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class calendarPassData {
    private HashMap<String, String> events = new HashMap<>();
    private List<Integer> posDif = new ArrayList<>();
    private List<PassData> contrasenhas;

    calendarPassData(List<PassData> Pass) {
        this.contrasenhas = Pass;
        String date;
        List<String> e = new ArrayList<>();
        /*Se recorre la lista para juntar todas los eventos con fechas iguales en un mismo String
        y así enviarlo luego en el JSON*/
        for (int i = 0; i < Pass.size(); i++) {
            e.add(Pass.get(i).getPage());
            int j = i + 1;
            if (j < Pass.size()) {
                try {

                    while (Pass.get(i).getCreation_date().equals(Pass.get(j).getCreation_date())) {
                        e.add(Pass.get(j).getPage());
                        j++;
                    }
                } catch (Exception Ex) {

                }
                date = "";
                for (String s : e) {
                    date += s + ", ";
                }
                events.put(contrasenhas.get(i).getCreation_date(), date);
                posDif.add(i);
                e.clear();
                i = j - 1;
            }
        }
    }
    @JavascriptInterface
    public String getJson() {
        JsonArray Datos = new JsonArray();
        for (int i = 0; i < posDif.size(); i++) {
            JSONObject data = new JSONObject();
            try {
                String date = contrasenhas.get(posDif.get(i)).getCreation_date();
                data.put("date", date);
                data.put("event", "Cambiar contraseña de la pagina " + events.get(date));
                Datos.add(String.valueOf(data));
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        return Datos.toString();
    }
}