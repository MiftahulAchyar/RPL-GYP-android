package com.acenet.gyp.Setting;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.acenet.gyp.Dashboard.A_Dashboard_2;
import com.acenet.gyp.Index.Index_Katalog;
import com.acenet.gyp.R;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class Setting1 extends AppCompatActivity implements View.OnClickListener{
    Button jadwalBtn;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting1_activity);
        setToolbar();

        setDrawer(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }
    public void setToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar_deskripsi_tanaman);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(Setting1.this, A_Dashboard_2.class);
                startActivity(home);
                Setting1.this.finish();
            }
        });
        toolbar.setTitleTextColor(0xFFFFFFFF);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Pengaturan");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        moveTaskToBack(true);
        Setting1.this.finish();
        Log.e("Ketombol: ", "Setting1");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==android.R.id.home)
            NavUtils.navigateUpFromSameTask(this);
        return super.onOptionsItemSelected(item);
    }

    private Drawer navigationDrawerLeft;
    private AccountHeader headerNavigationLeft;
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
                        Intent intent = new Intent(Setting1.this, Setting1.class);
                        switch (position) {
                            case 1:intent = new Intent(Setting1.this, A_Dashboard_2.class);
                                break;
                            case 2:
                                intent = new Intent(Setting1.this, Index_Katalog.class);
                                break;
                            case 3:
                                intent = new Intent(Setting1.this, Task.class);
                                break;
                            case 4:
                                intent = new Intent(Setting1.this, Setting1.class);
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


}
