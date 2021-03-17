package com.example.proyectomiguelsambademartnez;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class notificacion extends NotificationCompat.Builder {

    public notificacion(@NonNull Context context, @NonNull String channelId) {
        super(context, channelId);
        setSmallIcon(R.drawable.lock);
        setPriority(NotificationCompat.PRIORITY_DEFAULT);
        setContentTitle("Recordatorio de cambio!");
        setPriority(NotificationCompat.PRIORITY_DEFAULT);


    }

    //Comprueba la fecha de creación de una contraseña en caso de haber sido creada hace 3 meses envia datos para enviar la notificación
    public int notificacionCheck(String date) throws ParseException {
        Date creation_date = new SimpleDateFormat("MM-dd-yyyy").parse(date);
        Date today = new SimpleDateFormat("MM-dd-yyyy").parse(String.valueOf(Calendar.getInstance().getTime()));
        creation_date.setMonth(creation_date.getMonth()+3);
        if(creation_date.equals(today)){
            return 0;
        }
        if (creation_date.getDay()+2 > today.getDay()){
            return 3;
        }
        if (creation_date.getDay()+2 == today.getDay()){
            return 2;
        }
        if(creation_date.getDay()+1 == today.getDay()){
            return 1;
        }
        return -1;



    }

}
