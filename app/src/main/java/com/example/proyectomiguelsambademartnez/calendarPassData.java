package com.example.proyectomiguelsambademartnez;

import android.os.Build;
import android.util.Log;
import android.webkit.JavascriptInterface;

import androidx.annotation.RequiresApi;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class calendarPassData {
    private HashMap<Long, String> events = new HashMap<>();
    //PosDif guarda las posiciones suma +1 por cada vez que encuentra paginas con fechas de creación distintas
    private List<Integer> posDif = new ArrayList<>();
    private final List<PassData> contrasenhas;

    @RequiresApi(api = Build.VERSION_CODES.O)
    calendarPassData(List<PassData> Pass) {
        this.contrasenhas = Pass;
        String page;
        List<String> e = new ArrayList<>();
        /*Se recorre la lista para juntar todas las paginas con fechas de creación iguales en un mismo String
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
                page = "";
                for (String s : e) {
                    page += s + ", ";
                }

                events.put(toMili(i), page);
                posDif.add(i);
                e.clear();
                i = j - 1;
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    //Se pasan las fechas a milisegundos para su correcto manejo en JavaScript
    private Long toMili(int i){
        LocalDate DD = LocalDate.parse(contrasenhas.get(i).getCreation_date(),DateTimeFormatter.ofPattern("MM-dd-yyyy"));

        LocalDateTime D = DD.atStartOfDay();
        return D.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @JavascriptInterface
    public String data() {
        JsonArray Datos = new JsonArray();
        for (int i = 0; i < posDif.size(); i++) {
            JsonObject data = new JsonObject();
            Long date = toMili(i);
            data.addProperty("date", date);
            data.addProperty("event", "Añadida la página " + events.get(date));
            Datos.add(data);
        }
        return Datos.toString();
    }
}