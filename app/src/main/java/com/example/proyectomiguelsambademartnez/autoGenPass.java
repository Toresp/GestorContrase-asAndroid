package com.example.proyectomiguelsambademartnez;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class autoGenPass {
    private static char[] Letras = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z','@','@','@','@','@','#','#','#','#' };
    private static Random R;
    //Genera una contraseña aleatoria a partir del Array de arriba.
    public static String GenPass(){
        String pass = "";
        R = new Random();
        for (int i=0; i<16; i++){
            if(R.nextInt(2)==0){
                pass += Letras[R.nextInt(Letras.length)];
            }else{
                pass += R.nextInt(10);
            }
        }
        return pass;
    }

}
