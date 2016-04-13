package com.acenet.gyp.Dashboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.acenet.gyp.D_DbManager;
import com.acenet.gyp.R;

public class A_SplashScreen extends Activity{
    private  static  int progress;
//    private ProgressBar progressBar;
    private  int progressStatus=0;
    private Handler handler = new Handler();

    D_DbManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbManager = new D_DbManager(this);
//        dbManager.inisialisasi_data();
        //Toast.makeText(A_SplashScreen.this, "Numrows = "+ dbManager.getJumlahTanaman(), Toast.LENGTH_SHORT).show();

        setContentView(R.layout.a_splash_screen);
        progress=0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressStatus<10){
                    progressStatus = loading();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Intent exit = new Intent(A_SplashScreen.this,  A_Dashboard_2.class);
                        startActivity(exit);
                        finish();
                    }
                });
            }

            private int loading(){
                try {
                    Thread.sleep(200);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                return ++progress;
            }
        }).start();
    }
}