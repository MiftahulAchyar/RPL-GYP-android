package com.acenet.gyp.Setting;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.acenet.gyp.AlarmExample_SUKSES.AlarmReceiver;
import com.acenet.gyp.B_ListTanaman;

import com.acenet.gyp.D_DbManager;
import com.acenet.gyp.Dashboard.A_Dashboard_2;
import com.acenet.gyp.B_Config;
import com.acenet.gyp.Index.Index_Katalog;

import com.acenet.gyp.R;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import android.support.design.widget.FloatingActionButton;
public class Task extends AppCompatActivity implements  AdapterView.OnItemSelectedListener {
    Toolbar toolbar;
    ImageButton showNotifList;

    //Creating Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    D_DbManager dbManager;
    List<Notif_ListObject> notifListObjects;

    private AutoCompleteTextView cari;
    private Spinner spinner1;
    String[] listTanaman;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting2_recycler_view);
        setToolbar();
        setDrawer(savedInstanceState);

        dbManager = new D_DbManager(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewSetting);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        notifListObjects = new ArrayList<>();

        tampilkanList();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (!checkConnection()) Toast.makeText(Task.this, "Koneksi belum tersambung", Toast.LENGTH_SHORT).show();
                try {
                    dialogRequestNotif();
                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("Dialog Error", e.toString());
                }
            }
        });
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.notif_request_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item){
//        int id = item.getItemId();
//        if(id == R.id.notifRequest){
//            dialogRequestNotif();
//        }if (id==android.R.id.home)
//            NavUtils.navigateUpFromSameTask(this);
//        return true;
//    }
//
    private void tampilkanList(){
        ArrayList<ArrayList<Object>> data = dbManager.ambilTanaman();

        String [] str = new String[data.size()];
        for (int posisi = 0; posisi < data.size(); posisi++) {
            Notif_ListObject list = new Notif_ListObject();
            try {
                ArrayList<Object> baris = data.get(posisi);
                list.setNama_tanaman(new String(baris.get(0).toString()+""));
                Log.e("Daftar notif", baris.get(0).toString());
            } catch (NullPointerException n) {
                n.printStackTrace();
                Log.e("Gagal-ambil-data", "" + n.toString());
            }
            try{
                notifListObjects.add(list);
            }catch (IllegalArgumentException il){
                il.printStackTrace();
                Log.e("Gagal - tambah list", ""+il.toString());
            }catch (NullPointerException il){
                il.printStackTrace();
                Log.e("Gagal - tambah list", "" + il.toString());
                Log.e("Data", list.getNama_tanaman());
            }
        }
        //Finally initializing our adapter
        adapter = new Setting2Adapter(notifListObjects, this);
        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);

    }
    /*----------------------------------------------*/
    public void dialogRequestNotif() {

        final AlertDialog.Builder dialogRequest = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.a_dialog_bantuan2, null);
        dialogRequest.setView(dialogView);

        //Get Kategori String
        spinner1 = (Spinner) dialogView.findViewById(R.id.pilihKategori);
        spinner1.setOnItemSelectedListener(this);

        cari = (AutoCompleteTextView) dialogView.findViewById(R.id.cariBantuan);
        if (checkConnection())      getData("Semua");
        else Toast.makeText(Task.this, "Koneksi Error", Toast.LENGTH_SHORT).show();

        //Tampilkan Dialog
        dialogRequest.setTitle("Bantuan perawatan tanaman");

        cari.setOnEditorActionListener(new AutoCompleteTextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId== EditorInfo.IME_ACTION_DONE || keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER){
                    if (dbManager.getJumlahTanaman()==0)
                        cancelAllAlarm();
//                        cancelAlarm();
                    getNotif(spinner1.getSelectedItemPosition(), cari.getText().toString());
                    startActivity(new Intent(Task.this, Task.class));
                    tampilkanList();
                }
                return false;
            }
        });
        dialogRequest.setPositiveButton("Kirim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Log.e("Kategori = " + spinner1.getSelectedItemPosition(), "Tanaman = " + cari.getText().toString());
                getNotif(spinner1.getSelectedItemPosition(), cari.getText().toString());
                startActivity(new Intent(Task.this, Task.class));
                tampilkanList();
            }
        });
        dialogRequest.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        AlertDialog b = dialogRequest.create();
        b.show();
    }

    private void getNotif(int kategori, String nama){
        String url= B_Config.NOTIF_URL+"Semua/"+ nama.replace(" ","/");;
        if(kategori==1) url = B_Config.NOTIF_URL+"Bunga/"+ nama.replace(" ","/");
        else if(kategori==2) url = B_Config.NOTIF_URL+"Daun/"+ nama.replace(" ","/");

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
                        Toast.makeText(getBaseContext(), "Permintaan berhasil ditambahkan", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getBaseContext(), "Gagal menambahkan permintaan", Toast.LENGTH_LONG).show();
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
                Log.e("JSON-sukses", "" + json.getString(B_Config.TAG_NAMA_TANAMAN));
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
        setAlarm();
    }
    protected void simpan(String nama, String judul, int interval, int berapa){
        try {
            if(!dbManager.checkIsDataAlreadyIn(nama, judul)) {
                    dbManager.addRow(nama, judul, interval, berapa);
            }
            dbManager.addTanaman(nama);
            Log.e("Simpan", nama + "\n" + judul + "\n" + interval + "\n" + berapa + " kali");
        }catch (Exception e){
            e.printStackTrace();
            Log.e("Gagal Simpan", e.toString());
            Toast.makeText(getBaseContext(), "Gagal Simpan: "+e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    PendingIntent pendingIntent;

    public void cancelAlarm(){
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(Task.this, AlarmReceiver.class);

        ArrayList<ArrayList<Object>> data = dbManager.ambilSemuaDataNotifikasi();
        for (int posisi = 0; posisi < data.size(); posisi++) {
            try {
                ArrayList<Object> baris = data.get(posisi);
//                if (.equals(baris.get(0).toString())) {
                pendingIntent = PendingIntent.getBroadcast(Task.this, Integer.parseInt( baris.get(0).toString()), alarmIntent, 0);
                manager.cancel(pendingIntent);
            } catch (NullPointerException n) {
                n.printStackTrace();
                Log.e("Gagal-Cancel Notif", "" + n.toString());
            }catch (Exception n){n.printStackTrace();Log.e("Gagal-Cancel Notif", "" + n.toString());}
        }
    }

    ArrayList<PendingIntent> pendingIntentArray = new ArrayList<PendingIntent>();

    private void cancelAllAlarm(){
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (pendingIntentArray.size()>0){
            for (int i=0; i<pendingIntentArray.size();i++)
                manager.cancel(pendingIntentArray.get(i));
        }
        pendingIntentArray.clear();
    }
    public void setAlarm(){
//        = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        ArrayList<ArrayList<Object>> data = dbManager.ambilSemuaDataNotifikasi();

        AlarmManager manager[]  = new AlarmManager[data.size()];
        for (int posisi = 0; posisi < data.size(); posisi++) {
            Intent alarmIntent = new Intent(Task.this, AlarmReceiver.class);
            try {
                    ArrayList<Object> baris = data.get(posisi);
                    alarmIntent.putExtra("nama_tanaman", baris.get(1).toString());
                    alarmIntent.putExtra("judul", "Waktunya " + baris.get(2).toString());
                    alarmIntent.putExtra("id", "" + baris.get(0).toString());
                    pendingIntent = PendingIntent.getBroadcast(Task.this, Integer.parseInt( baris.get(0).toString()), alarmIntent, 0);

//                    /* Set the alarm to start at 6:30 AM */
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.set(Calendar.HOUR_OF_DAY, 6);
                    calendar.set(Calendar.MINUTE, 30);

                    manager[posisi] = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    int inter = Integer.parseInt(baris.get(0).toString());
                    if(baris.get(2).toString().equals("Pemupukan"))
                        manager[posisi].setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                (1000 * 3600 * inter *24)//Integer.parseInt(baris.get(3).toString()))
                                , pendingIntent);
                    else if(baris.get(2).toString().equals("Penyiraman"))
                        manager[posisi].setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                                (1000 * 3600 * inter*24 + 3000)//Integer.parseInt(baris.get(3).toString()))
                                , pendingIntent);

                pendingIntentArray.add(pendingIntent);
            } catch (NullPointerException n) {
                n.printStackTrace();
                Log.e("Gagal-Start Alarm", "" + n.toString());
            }
        }
    }

    public void startAlarm(long interval){
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Log.e("Detik", ""+interval);
        interval = interval*1000*10;  //*3600  *24

        /* Set the alarm to start at 6:30 AM */
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 6);
        calendar.set(Calendar.MINUTE, 30);

        /* Repeating on every 20 minutes interval */
        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 10, pendingIntent);
    }
    /*---------------------------------------------------------*/
//    private List<B_ListTanaman> listSuperHeroes;
    private void getData(String url) {
        //Initializing our superheroes list
//        listSuperHeroes = new ArrayList<>();
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
                        Log.e( "VolleyError: " , error.toString());
                        Toast.makeText(getBaseContext(), "Gagal mengambil data ", Toast.LENGTH_SHORT).show();
                        loading.dismiss();

                        AlertDialog.Builder builder = new AlertDialog.Builder(Task.this);
                        builder.setMessage("Maaf, Koneksi Error\nMau mencoba lagi? ")
                                .setCancelable(false)
                                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        getData(finalU1);
                                    }
                                })
                                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
//                                        Intent back = new Intent(Index_Katalog.this, Index1.class);
                                    }
                                })
                                .show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding request to the queue
        requestQueue.add(jsonArrayRequest);
    }

    //This method will parse json data
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
    /*---------------------------------------------------------------*/
    public void setDrawer(Bundle savedInstanceState){

        headerNavigationLeft = new AccountHeaderBuilder()
                .withActivity(this)
                .withSavedInstance(savedInstanceState)
                .withThreeSmallProfileImages(false)
                .withHeaderBackground(R.drawable.clip)
//                .addProfiles(
//                        new ProfileDrawerItem().withIcon(getResources().getDrawable(R.drawable.logo72))
//                )
                .build();
        //LIST
        navigationDrawerLeft = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withSliderBackgroundColor(getResources().getColor(R.color.whiteshadow))
                .withAccountHeader(headerNavigationLeft)
                .withDisplayBelowStatusBar(true)
                .withActionBarDrawerToggleAnimated(true)
                .withDrawerGravity(Gravity.LEFT)
                .withSavedInstance(savedInstanceState)
                .withAccountHeader(headerNavigationLeft)
                .withSelectedItem(0)
                .withTranslucentStatusBar(true)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Intent intent = new Intent(Task.this, Index_Katalog.class);
                        switch (position) {
                            case 1:
                                intent = new Intent(Task.this, A_Dashboard_2.class);
                                break;
                            case 2:
                                intent = new Intent(Task.this, Index_Katalog.class);
                                break;
                            case 3:
                                intent = new Intent(Task.this, Task.class);
                                break;
                            case 4:
                                intent = new Intent(Task.this, Setting1.class);
                                break;
                        }
                        startActivity(intent);

                        return false;
                    }
                })
                .build();
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Home").withIcon(getResources().getDrawable(R.drawable.ic_home_black_48dp)));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Index Tanaman").withIcon(getResources().getDrawable(R.drawable.ic_library_books_black_48dp)));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Task").withIcon(getResources().getDrawable(R.drawable.ic_assignment_black_48dp)));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Setting").withIcon(getResources().getDrawable(R.drawable.ic_settings_black_48dp)));
    }
    private Drawer navigationDrawerLeft;
    private AccountHeader headerNavigationLeft;
    public void setToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar_deskripsi_tanaman);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        toolbar.setTitle("Tugas");
        toolbar.setTitleTextColor(0xFFFFFFFF);
//        setSupportActionBar(toolbar);
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

    public boolean checkConnection(){
        ConnectivityManager check = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info = check.getAllNetworkInfo();
        for (int i = 0; i < info.length; i++)
            if (info[i].getState() == NetworkInfo.State.CONNECTED)
                return true;
        return false;
    }
}