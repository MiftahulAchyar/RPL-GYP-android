package com.acenet.gyp.Index;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.acenet.gyp.B_Config;
import com.acenet.gyp.B_ListTanaman;
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
import java.util.List;

/**
 * Created by AceNet on 1/8/2016.
 */
public class SearchableActivity extends AppCompatActivity {
    Toolbar toolbar;
    String param[] = new String[2];
    private List<B_ListTanaman> listSuperHeroes;

    //Creating Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index_search_activity);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        /*layoutManager = new LinearLayoutManager(this);*/
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        //Initializing our superheroes list
        listSuperHeroes = new ArrayList<>();

        param[0] = getIntent().getStringExtra("nama");
        param[1] = getIntent().getStringExtra("url");
        setToolbar();
        getSearch();
    }
    public void getSearch(){
        Log.e("Search", "" + param[1]);
        try {
            final ProgressDialog loading = ProgressDialog.show(this, "Mencari Tanaman", "Harap tunggu...", false, false);
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(param[1],
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            //Dismissing progress dialog
                            loading.dismiss();
                            //calling method to parse json array
                            parseData(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Log.e("VolleyError: " , error.toString());
                            loading.dismiss();
                            Toast.makeText(SearchableActivity.this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                        }
                    });
            //Creating request queue
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            //Adding request to the queue
            requestQueue.add(jsonArrayRequest);
        }catch (NullPointerException n){
            //Toast.makeText(SearchableActivity.this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
            Log.e("Err",""+n);
        }

//        String q = intent.getStringExtra(SearchManager.QUERY);


    }
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

            } catch (JSONException e) {
                e.printStackTrace();
            }
            listSuperHeroes.add(superHero);
        }
        //Finally initializing our adapter
        adapter = new B_Grid_SEARCH(listSuperHeroes, this);
        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);

    }

    public void setToolbar(){
        toolbar = (Toolbar)findViewById(R.id.toolbar_deskripsi_tanaman);
        toolbar.setTitle("Hasil pencarian: " + param[0]);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id==android.R.id.home)
            finish();
//            NavUtils.navigateUpFromSameTask(this);
        return true;
    }
}
