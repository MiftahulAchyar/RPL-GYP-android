package com.acenet.gyp.Setting.Material;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
//import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;


import com.acenet.gyp.R;
import com.acenet.gyp.Tab.SlidingTabLayout;

public class MainActivity extends AppCompatActivity{
    Toolbar toolbar;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    FrameLayout frameCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.material_main_activity);
        setToolbar();
//        frameCalendar = (FrameLayout)findViewById(R.id.);
        viewPager = (ViewPager)findViewById(R.id.vp_tabs);
        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), this));

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.st1_tabs);
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setBackgroundColor(getResources().getColor(R.color.teal));
        slidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.whiteshadow));

        //tab_view tempat meletakkan Icon pada Tab
        //Mengatur tinggi garis bawah Tab
        slidingTabLayout.setCustomTabView(R.layout.tab_view, R.id.tv_tab);

        slidingTabLayout.setViewPager(viewPager);
//        setCalendar();

        Log.e("Nama Tanaman",getIntent().getStringExtra("nama_tanaman"));
    }

    public void setToolbar(){
        toolbar = (Toolbar)findViewById(R.id.tabToolbar);
        toolbar.setTitle("Tugas: "+getIntent().getStringExtra("nama_tanaman"));
        toolbar.setTitleTextColor(0xFFFFFFFF);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
//        toolbar.setBackgroundColor(getResources().getColor(R.color.darkgreen));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==android.R.id.home)
            NavUtils.navigateUpFromSameTask(this);
        return super.onOptionsItemSelected(item);
    }
}

