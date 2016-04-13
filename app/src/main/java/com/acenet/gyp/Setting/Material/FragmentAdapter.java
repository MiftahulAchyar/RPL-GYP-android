package com.acenet.gyp.Setting.Material;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;

import com.acenet.gyp.R;

/**
 * Created by AceNet on 1/1/2016.
 */
public class FragmentAdapter extends FragmentPagerAdapter{
    private Context context;
    private String []titles = {"List"};
    int[] icons = new int[]{
            R.mipmap.ic_list_white_48dp
            };
    private int heightIcon;
    public FragmentAdapter(FragmentManager fm, Context c) {
        super(fm);
        context = c;
        double scale=c.getResources().getDisplayMetrics().density;
        heightIcon=(int)(25*scale+0.5f);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment=null;
        if (position==0){
            fragment=new SettingList_FragmentView();
        }
//        else if (position==2) {
//            fragment = new TanamanDaun();
//        }
        Bundle b = new Bundle();
        b.putInt("position", position);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Drawable d = context.getResources().getDrawable(icons[position]);
        d.setBounds(0, 0, heightIcon, heightIcon);
        ImageSpan is = new ImageSpan(d);
        SpannableString sp = new SpannableString(" ");
        sp.setSpan(is, 0, sp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sp;
    }

}
