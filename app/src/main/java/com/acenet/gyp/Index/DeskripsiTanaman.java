package com.acenet.gyp.Index;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.acenet.gyp.AlarmExample_SUKSES.AlarmReceiver;
import com.acenet.gyp.B_Config;
import com.acenet.gyp.B_ListTanaman;
import com.acenet.gyp.D_DbManager;
import com.acenet.gyp.R;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.os.Handler;

public class DeskripsiTanaman  extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String idParam, namaParam, kategoriParam, gbrParam;
    Toolbar toolbar;

    private List<B_ListTanaman> listSuperHeroes;
    int count;
    Handler handler;

    //Creating Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    D_DbManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deskripsi_recycler_view);
        setContent();
        dbManager= new D_DbManager(this);
//        //Initializing Views
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewDesk);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
//        //Initializing our superheroes list
        listSuperHeroes = new ArrayList<>();
        //Calling method to get data
        getDeskripsi();
    }


    //This method will get data from the web api
    private void getDeskripsi(){
        //Showing a progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Loading Data", "Please wait...",false,false);

        //Creating a json array request
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(B_Config.DESKRIPSI_URL+"/"+kategoriParam+"/"+idParam,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {loading.dismiss();parseDeskripsi(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DeskripsiTanaman.this, "VolleyError: "+ error.toString(), Toast.LENGTH_SHORT).show();
                        Log.e("Volley-E:", error.toString());
                        loading.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(DeskripsiTanaman.this);
                        builder.setMessage("Maaf, Koneksi Error\nMau mencoba lagi? ")
                                .setCancelable(false)
                                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        getDeskripsi();
                                    }
                                })
                                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                }).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    //This method will parse json data
    private void parseDeskripsi(JSONArray array){
        for(int i = 0; i<array.length(); i++) {
            B_ListTanaman superHero = new B_ListTanaman();
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                superHero.setName(namaParam);
                superHero.setGambar0("");
                superHero.setGambar1("");
                superHero.setGambar2("");
                superHero.setGambar3("");

                if(!json.getString(B_Config.TAG_GAMBAR0).equals(""))    superHero.setGambar0(json.getString(B_Config.TAG_GAMBAR0));
                if(!json.getString(B_Config.TAG_GAMBAR1).equals(""))    superHero.setGambar1(json.getString(B_Config.TAG_GAMBAR1));
                if(!json.getString(B_Config.TAG_GAMBAR2).equals(""))    superHero.setGambar2(json.getString(B_Config.TAG_GAMBAR2));
                if(!json.getString(B_Config.TAG_GAMBAR3).equals(""))    superHero.setGambar3(json.getString(B_Config.TAG_GAMBAR3));

                superHero.setDescription(json.getString(B_Config.TAG_CONTENT));
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("JSON-E:", e.toString());
            }
            listSuperHeroes.add(superHero);
        }
        adapter = new DeskripsiAdapter(listSuperHeroes, this);
        recyclerView.setAdapter(adapter);
    }
    public void setContent(){
        Intent intent = getIntent();
        idParam = intent.getStringExtra("id");

        kategoriParam = intent.getStringExtra("kategori");
        namaParam = intent.getStringExtra("nama");
//        Toast.makeText(DeskripsiTanaman.this, "ID:"+idParam+"\nKategori: "+kategoriParam+"\nNama: "+namaParam+
//                "\nGambar: "+gbrParam, Toast.LENGTH_LONG).show();
        toolbar = (Toolbar) findViewById(R.id.toolbar_deskripsi_tanaman);
        toolbar.setTitle("Deskripsi Tanaman");
        toolbar.setTitleTextColor(0xFFFFFFFF);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notif_request_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.notifRequest){
            dialogRequestNotif();
        }if (id==android.R.id.home)
            finish();
//            NavUtils.navigateUpFromSameTask(this);
        return true;
    }

    private void getNotif(String kategori, String nama){
        String url= B_Config.NOTIF_URL+"Semua/"+ nama.replace(" ","/");;
        if(kategori.equals("Bunga")) url = B_Config.NOTIF_URL+"Bunga/"+ nama.replace(" ","/");
        else if(kategori.equals("Daun")) url = B_Config.NOTIF_URL+"Daun/"+ nama.replace(" ","/");

        Log.e("Notif-URL", url);
        final ProgressDialog load = ProgressDialog.show(this, "Mengambil Data Perawatan", "Harap tunggu...", false, false);
        //Creating a json array request
        final String finalURL = url;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(finalURL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Dismissing progress dialog
                        load.dismiss();
                        //calling method to parse json array
                        Log.e("Response", ""+response);
                        parseNotif(response);
                        Toast.makeText(DeskripsiTanaman.this, "Permintaan berhasil ditambahkan", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(DeskripsiTanaman.this, "Gagal menambahkan permintaan", Toast.LENGTH_LONG).show();
                        Log.e("VolleyError: ", error.toString());
                        load.dismiss();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding request to the queue
        requestQueue.add(jsonArrayRequest);
    }
    private  void parseNotif(JSONArray array){
        for (int i = 0; i < array.length(); i++) {
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                Log.e("JSON-sukses",""+json.getString(B_Config.TAG_NAMA_TANAMAN));
                simpan(
                        json.getString(B_Config.TAG_NAMA_TANAMAN),
                        json.getString(B_Config.TAG_JUDUL),
                        Integer.parseInt(json.getString(B_Config.TAG_INTERVAL_HARI)),
                        Integer.parseInt(json.getString(B_Config.TAG_BERAPA_KALI))
                );
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("JSON-Notif-Err","Error");
            }
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch (position){
            case 0:
                getData("Semua");
                break;
            case 1:
                getData("Bunga");
                break;
            case 2:
                getData("Daun");
                break;
        }
        cari.setAdapter(arrayAdapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    /*-----------------------------------------------------------------------------*/
    AutoCompleteTextView cari;
    String[] listTanaman;
    ArrayAdapter<String> arrayAdapter;
    PendingIntent pendingIntent;

    public void dialogRequestNotif() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DeskripsiTanaman.this);
        builder.setMessage("Mau menambahkan ke tugas? ")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                        D.this.finish();
                        getNotif(kategoriParam, namaParam);
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    protected void simpan(String nama, String judul, int interval, int berapa){
        try {
            dbManager.addRow(
                    nama, judul, interval, berapa
            );
            dbManager.addTanaman(nama);

            setAlarm(nama, judul, interval, berapa);

            Log.e("Simpan",
                    nama + "\n" + judul + "\n" + interval + "\n" + berapa + " kali");
        }catch (Exception e){
            e.printStackTrace();
            Log.e("Gagal Simpan", e.toString());
            Toast.makeText(getBaseContext(), "Gagal Simpan: "+e.toString(), Toast.LENGTH_LONG).show();
        }
    }
    private void getData(String url) {
        //Initializing our superheroes list
        listSuperHeroes = new ArrayList<>();
        //Showing a progress dialog
        if(url=="Bunga")      url = B_Config.BUNGA_URL;
        else if(url=="Daun") url =B_Config.DAUN_URL;
        else                     url =B_Config.DATA_URL;

        Log.e("URL ", url);
        final ProgressDialog loading = ProgressDialog.show(this, "Mengambil Daftar Tanaman ", "Harap tunggu...", false, false);
        //Creating a json array request

        final String finalU1 = url;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(finalU1,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Dismissing progress dialog
                        loading.dismiss();
                        //calling method to parse json array
                        Log.e("Response", ""+response);
                        parseData(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(DeskripsiTanaman.this, "VolleyError: " + error.toString(), Toast.LENGTH_SHORT).show();
                        loading.dismiss();

                        AlertDialog.Builder builder = new AlertDialog.Builder(DeskripsiTanaman.this);
                        builder.setMessage("Maaf, Koneksi Error\nMau mencoba lagi? ")
                                .setCancelable(false)
                                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {getData(finalU1);
                                    }
                                })
                                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {dialog.cancel();}
                                }).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding request to the queue
        requestQueue.add(jsonArrayRequest);
    }

    private void parseData(JSONArray array) {
        listTanaman = new String[array.length()];

        for (int i = 0; i < array.length(); i++) {
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);

                listTanaman[i] = json.getString(B_Config.TAG_NAME);
                Log.e("List"+i, ""+listTanaman[i]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        arrayAdapter = new ArrayAdapter<String>(this,
                R.layout.autocomplete_act, listTanaman);
        cari.setAdapter(arrayAdapter);
    }

    public void setAlarm(String nama, String judul, int interval, int berapa){
        Intent alarmIntent = new Intent(DeskripsiTanaman.this, AlarmReceiver.class);

        alarmIntent.putExtra("nama_tanaman", nama);
        alarmIntent.putExtra("judul", "Waktunya "+judul);

        pendingIntent = PendingIntent.getBroadcast(DeskripsiTanaman.this, 0, alarmIntent, 0);
        startAlarm(interval);
    }
    public void startAlarm(long interval){
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        interval = interval *1000 ; //*3600*24

        /* Set the alarm to start at 6:30 AM */
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 4);
        calendar.set(Calendar.MINUTE, 18);

        /* Repeating on every 20 minutes interval */
        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                interval, pendingIntent);
    }
}