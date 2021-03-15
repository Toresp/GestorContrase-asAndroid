package com.example.proyectomiguelsambademartnez;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;


public class Calendario extends AppCompatActivity {
    private WebView calendario;
    private List<PassData> contrasenhas;
    private calendarPassData calendarData;

    @SuppressLint("JavascriptInterface")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendario);
        contrasenhas = ((UserData) getIntent().getSerializableExtra(sesion_iniciada.DATOS)).getContraseñas();
        calendario = (WebView) this.findViewById(R.id.calendar);
        calendario.getSettings().setJavaScriptEnabled(true);
        calendario.addJavascriptInterface(new calendarPassData(contrasenhas), "Android");
        calendario.loadUrl("file:///android_asset/index.html");
        calendario.measure(10, 10);
    }


}
