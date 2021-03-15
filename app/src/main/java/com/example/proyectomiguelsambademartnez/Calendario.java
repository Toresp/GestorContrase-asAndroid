package com.example.proyectomiguelsambademartnez;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;


public class Calendario extends AppCompatActivity {
    private WebView calendario;
    private List<PassData> contrasenhas;

    @SuppressLint("JavascriptInterface")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendario);
        contrasenhas = ((UserData) getIntent().getSerializableExtra(sesion_iniciada.DATOS)).getContraseñas();
        calendario = (WebView) this.findViewById(R.id.calendar);
        calendario.getSettings().setJavaScriptEnabled(true);
        //Estas settings son para poder ver el calendario como en un pc
        String ua= "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0";
        calendario.getSettings().setUserAgentString(ua);
        calendario.addJavascriptInterface(new calendarPassData(contrasenhas), "Android");
        calendario.loadUrl("file:///android_asset/index.html");
    }


}
