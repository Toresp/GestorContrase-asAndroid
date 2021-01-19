package com.example.proyectomiguelsambademartnez;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Button;

public class HideButton extends androidx.appcompat.widget.AppCompatButton {
    public String pass;
    public boolean hided;
    public HideButton(Context context, String p) {
        super(context);
        pass = p;
        super.setBackgroundResource(R.drawable.eye);
        hided=true;
        super.setText("");
    }
    public Boolean ChangeState(){
        if (hided) {
            hided = false;
            return hided;
        }
        else hided=true;
        return hided;
    }



}
