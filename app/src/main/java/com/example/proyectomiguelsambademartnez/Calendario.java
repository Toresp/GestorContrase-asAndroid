package com.example.proyectomiguelsambademartnez;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;


public class Calendario extends AppCompatActivity {
    private WebView calendario;
    private List<PassData> contrasenhas;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @SuppressLint("JavascriptInterface")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendario);
        contrasenhas = ((UserData) getIntent().getSerializableExtra(sesion_iniciada.DATOS)).getContraseñas();
        calendario = (WebView) this.findViewById(R.id.calendar);
        calendario.getSettings().setAllowContentAccess(true);
        calendario.getSettings().setAllowFileAccess(true);
        calendario.getSettings().setDatabaseEnabled(true);
        calendario.getSettings().setDomStorageEnabled(true);
        calendario.getSettings().setJavaScriptEnabled(true);
        calendario.getSettings().setLoadWithOverviewMode(true);
        calendario.getSettings().setUseWideViewPort(false);
        calendario.getSettings().setBuiltInZoomControls(true);
        calendario.getSettings().setForceDark (WebSettings.FORCE_DARK_ON);
        calendario.getSettings().setSupportZoom(true);
        calendario.setWebViewClient(new WebViewClient());
        calendario.setWebChromeClient(new WebChromeClient());
        calendarPassData cal = new calendarPassData(contrasenhas);
        calendario.addJavascriptInterface(cal, "android");
        calendario.loadUrl("file:///android_asset/index.html");
    }


}
