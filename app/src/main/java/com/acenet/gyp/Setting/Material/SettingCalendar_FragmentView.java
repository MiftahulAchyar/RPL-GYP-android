package com.acenet.gyp.Setting.Material;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acenet.gyp.R;

/**
 * Created by AceNet on 1/8/2016.
 */
public class SettingCalendar_FragmentView extends Fragment {


    public SettingCalendar_FragmentView() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.material_calendarview_frag, container, false);
    }

}

