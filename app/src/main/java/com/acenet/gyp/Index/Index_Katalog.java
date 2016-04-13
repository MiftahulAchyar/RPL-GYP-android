package com.acenet.gyp.Index;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;


import com.acenet.gyp.B_Config;
import com.acenet.gyp.B_ListTanaman;
import com.acenet.gyp.Dashboard.A_Dashboard_2;
import com.acenet.gyp.R;
import com.acenet.gyp.Setting.Setting1;
import com.acenet.gyp.Setting.Task;
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
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Index_Katalog extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Drawer navigationDrawerLeft;
    private AccountHeader headerNavigationLeft;
    //Creating a List of superheroes
    private List<B_ListTanaman> listSuperHeroes;

    //Creating Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    private Toolbar toolbar;
    private Spinner spinner_nav;
    boolean ret = true;

    String[] kategori={
            String.valueOf(R.string.semuakategori),
            String.valueOf(R.string.bungakategori),
            String.valueOf(R.string.daunkategori)};
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index_katalog);
        setToolbar();
        spinner_nav = (Spinner) findViewById(R.id.pilihKategori);
        spinner_nav.setOnItemSelectedListener(this);

        setDrawer(savedInstanceState);

        //Calling method to get data
        if (!checkConnection())
            Toast.makeText(this, "Koneksi belum tersambung", Toast.LENGTH_LONG).show();
        else {
            getData(kategori[0]);
        }
        handleIntent(getIntent());

        try {
            if(getIntent().getStringExtra("kategori_").equals("bunga")){
                getData(kategori[1]);
                spinner_nav.setSelection(1);
            }else if(getIntent().getStringExtra("kategori_").equals("daun")){
                getData(kategori[2]);
                spinner_nav.setSelection(2);
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }

    }

    String getSelectedSpinnerItem(){
        String s = spinner_nav.getSelectedItem().toString();
        if (s.equals(kategori[1])) return kategori[1];
        else if(s.equals(kategori[2])) return kategori[2];
        else return kategori[0];
    }
    void getSearch(String search){
        Intent searchActivity = new Intent(Index_Katalog.this, SearchableActivity.class);
        searchActivity.putExtra("nama", search.replace("/", " "));

        String kateg = getSelectedSpinnerItem();
        if (kateg == kategori[1])       search = B_Config.SEARCH_URL+"Bunga/"+search;
        else if (kateg == kategori[2])  search = B_Config.SEARCH_URL+"Daun/"+search;
        else                            search = B_Config.SEARCH_URL+"Semua/"+search;

        searchActivity.putExtra("url", search);
        startActivity(searchActivity);

    }
    //This method will get data from the web api
    private void getData(String url) {
        //Initializing Views
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        /*layoutManager = new LinearLayoutManager(this);*/
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        //Initializing our superheroes list
        listSuperHeroes = new ArrayList<>();
        //Showing a progress dialog

        if(url==kategori[1])      url =B_Config.BUNGA_URL;
        else if(url==kategori[2]) url =B_Config.DAUN_URL;
        else                     url = B_Config.DATA_URL;
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
                        Log.e("Respone", ""+response.toString());
                        parseData(response);
                        Log.e("Parsing.....","");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.e("VolleyError: " , error.toString());
                        loading.dismiss();

                        AlertDialog.Builder builder = new AlertDialog.Builder(Index_Katalog.this);
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

                                    }
                                })
                                .show();
                    }
                });
        //Creating request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding request to the queue
        requestQueue.add(jsonArrayRequest);
    }

    //This method will parse json data
    private void parseData(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            B_ListTanaman superHero = new B_ListTanaman();
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                superHero.setImageUrl(json.getString(B_Config.TAG_IMAGE_URL));
                superHero.setId(json.getString(B_Config.TAG_ID));
                superHero.setName(json.getString(B_Config.TAG_NAME));
                superHero.setCategory(json.getString(B_Config.TAG_CATEGORY));

                Log.e("JSON", ""+json.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            listSuperHeroes.add(superHero);
        }

        //Finally initializing our adapter
/*        adapter = new B_CardAdapter(listSuperHeroes, this);*/
        adapter = new B_GridAdapter(listSuperHeroes, this);
        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);

    }

    public boolean checkConnection() {
        ConnectivityManager check = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info = check.getAllNetworkInfo();
        for (int i = 0; i < info.length; i++)
            if (info[i].getState() == NetworkInfo.State.CONNECTED)
                return true;
        return false;

    }

    public void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Katalog Tanaman");
        toolbar.setTitleTextColor(0xFFFFFFFF);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        Index_Katalog.this.finish();
        Log.e("Ketombol: ", "Setting1");
    }

    SearchView searchView;
    SearchManager searchManager;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

         searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
         searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(
                        getComponentName()));
        searchView.setBackgroundColor(getResources().getColor(R.color.teal));
        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                // this is your adapter that will be filtered
                Log.e("Query1",newText);
                return true;
            }
            @Override
            public boolean onQueryTextSubmit(String query) {
                // this is your adapter that will be filtered
//                adapter.getFilter().filter(query);
                Log.e("Query2", query);
                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                onSearchRequested();
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
//        Log.e("Intent Action", intent.getAction().toString());
        if (Intent.ACTION_EDIT.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(Index_Katalog.this, query+"", Toast.LENGTH_SHORT).show();
        }
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            query = query.replace(" ", "/");

            Log.e("handle Intent", ""+query);
            Log.e("Query", query+"");
            getSearch(query);
        }
    }


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
                        Intent intent = null;
                        switch (position) {
                            case 1:
                                intent = new Intent(Index_Katalog.this, A_Dashboard_2.class);
                                break;
                            case 2:
                                intent = new Intent(Index_Katalog.this, Index_Katalog.class);
                                break;
                            case 3:
                                intent = new Intent(Index_Katalog.this, Task.class);
                                break;
                            case 4:
                                intent = new Intent(Index_Katalog.this, Setting1.class);
                                break;
                        }
                        try {
                            startActivity(intent);
                        }catch (NullPointerException e){}
                        return false;
                    }
                })
                .build();
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Home").withIcon(getResources().getDrawable(R.drawable.ic_home_black_48dp)));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Index Tanaman").withIcon(getResources().getDrawable(R.drawable.ic_library_books_black_48dp)));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Task").withIcon(getResources().getDrawable(R.drawable.ic_assignment_black_48dp)));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Setting").withIcon(getResources().getDrawable(R.drawable.ic_settings_black_48dp)));
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        //Toast.makeText(Index_Katalog.this, "Klik: "+position, Toast.LENGTH_SHORT).show();
        switch (position){
            case 0:
                getData(kategori[0]);
                break;
            case 1:
                getData(kategori[1]);
                break;
            case 2:
                getData(kategori[2]);
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}