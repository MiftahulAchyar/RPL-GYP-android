package com.acenet.gyp.AlarmExample_SUKSES;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.acenet.gyp.R;

/**
 * Created by AceNet on 1/10/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Intent", ""+intent.toString());
        Log.e("Context", ""+context.toString());
        Log.e("Judul", ""+intent.getStringExtra("judul").toString());
        Log.e("Nama Tanaman", ""+intent.getStringExtra("nama_tanaman").toString());
        Notification noti = new Notification.Builder(context)
                .setContentTitle("" + intent.getStringExtra("judul").toString())
                .setContentText("Tanaman " + intent.getStringExtra("nama_tanaman").toString()+"\n")
                .setSmallIcon(R.drawable.logo72)
//                .setStyle(new Notification.BigTextStyle().bigText("Blablalblasdblasdba landlasdlas lbalsdals lsan"))
//                .setStyle(new Notification.BigTextStyle().bigText("Blablalblasdblasdba landlasdlas lbalsdals lsan"))
                .build();
        try {

            NotificationManager notifications = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            noti.flags |= Notification.FLAG_AUTO_CANCEL;
            notifications.notify(
                    Integer.parseInt(intent.getStringExtra("id").toString()),
                    noti);
        }catch (NullPointerException e){
            e.printStackTrace();
            Toast.makeText(context, "Data tidak ada", Toast.LENGTH_SHORT).show();
        }

        // For our recurring task, we'll just display a message
//        Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();
    }

}