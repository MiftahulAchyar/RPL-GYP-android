package com.acenet.gyp.Dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.acenet.gyp.D_DbManager;
import com.acenet.gyp.Index.Index_Katalog;
import com.acenet.gyp.R;
import com.acenet.gyp.Setting.Setting1;
import com.acenet.gyp.Setting.Task;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;

/**
 * Created by AceNet on 1/17/2016.
 */
public class A_Dashboard_2 extends AppCompatActivity  implements View.OnClickListener, AdapterView.OnItemSelectedListener  {

    private Drawer navigationDrawerLeft;
    private AccountHeader headerNavigationLeft;
    Toolbar toolbar;
    Context context;
    D_DbManager dbManager;

    CustomPagerAdapter mCustomPagerAdapter;
    int count;
    Handler handler  = new Handler();

    TextView textView[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_2);
        context = this;
        setToolbar();
        setDrawer(savedInstanceState);
        dbManager = new D_DbManager(this);
        setPager();

        ImageButton kategoridaun = (ImageButton) findViewById(R.id.kategoridaun);
        ImageButton kategoribunga = (ImageButton) findViewById(R.id.kategoribunga);
        kategoribunga.setOnClickListener(this);
        kategoridaun.setOnClickListener(this);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notif_request_menu, menu);
        return true;
    }
    private void setPager(){
        mCustomPagerAdapter = new CustomPagerAdapter(this);

        count  = mCustomPagerAdapter.getCount();
        textView = new TextView[count];
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.image_bullets);
        for (int i=0; i<count; i++){
            textView[i] = new TextView(getBaseContext());
            textView[i].setBackgroundColor(getResources().getColor(android.R.color.darker_gray));

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(15, 15);
            layoutParams.setMargins(5, 0, 5, 0);
            linearLayout.addView(textView[i], layoutParams);
        }


        final ViewPager mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mCustomPagerAdapter);

        mViewPager.setCurrentItem(0);
        textView[mViewPager.getCurrentItem()].setBackgroundColor(getResources().getColor(android.R.color.white));
//        Toast.makeText(MainActivity.this, ""+mViewPager.getCurrentItem(), Toast.LENGTH_SHORT).show();

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                textView[position].setBackgroundColor(getResources().getColor(android.R.color.white));
                for (int i = 0; i < textView.length; i++) {
                    if (i == position)
                        continue;
                    textView[i].setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        final Runnable update = new Runnable() {
            int on = mViewPager.getCurrentItem();
            @Override
            public void run() {
                if (mViewPager.getCurrentItem() == (count-1)){
//                    mViewPager.setAdapter(mCustomPagerAdapter);
                    on =0;
                    mViewPager.setCurrentItem(on, true);
//                    Toast.makeText(MainActivity.this, ""+mViewPager.getCurrentItem(), Toast.LENGTH_SHORT).show();
                }

                mViewPager.setCurrentItem(on++, true);
//                Toast.makeText(MainActivity.this, ""+mViewPager.getCurrentItem(), Toast.LENGTH_SHORT).show();
            }
        };

        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, 500, 5500);
    }
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(A_Dashboard_2.this, Index_Katalog.class);
        switch (view.getId()){
            case R.id.kategoribunga:
                intent.putExtra("kategori_", "bunga");
                break;
            case R.id.kategoridaun:
                intent.putExtra("kategori_", "daun");
        }
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
                        Intent intent = new Intent(context, A_Dashboard_2.class);
                        switch (position) {
                            case 1:
                                break;
                            case 2:
                                intent = new Intent(context, Index_Katalog.class);
                                break;
                            case 3:
                                intent = new Intent(context, Task.class);
                                break;
                            case 4:
                                intent = new Intent(context, Setting1.class);
                                break;
                        }

                        startActivity(intent);

//                        Toast.makeText(context, "Position " + position, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                })
                .build();
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Home").withIcon(getResources().getDrawable(R.drawable.ic_home_black_48dp)));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Index Tanaman").withIcon(getResources().getDrawable(R.drawable.ic_library_books_black_48dp)));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Task").withIcon(getResources().getDrawable(R.drawable.ic_assignment_black_48dp)));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Setting").withIcon(getResources().getDrawable(R.drawable.ic_settings_black_48dp)));
    }
    public void setToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar_deskripsi_tanaman);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        toolbar.setTitle("Grow Your Plant");
        toolbar.setTitleTextColor(0xFFFFFFFF);
//        toolbar.setNavigationIcon(R.drawable.ic_menu_white_48dp);
//        setSupportActionBar(toolbar);
    }

}
