package com.acenet.gyp.Setting.Material;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.acenet.gyp.D_DbManager;
import com.acenet.gyp.R;

import java.util.ArrayList;

/**
 * Demonstrates the use of {@link RecyclerView} with a {@link LinearLayoutManager} and a
 * {@link GridLayoutManager}.
 */
public class SettingList_FragmentView extends Fragment {

    private static final String TAG = "RecyclerViewFragment";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    private static final int DATASET_COUNT = 60;

    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected LayoutManagerType mCurrentLayoutManagerType;

    protected D_DbManager dbManager;

    protected RecyclerView mRecyclerView;
    protected ListAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected String[] mJudul, mInterval, mBerapaKali;
    protected int[] mIcon;

    int[] icons={R.mipmap.watering, R.mipmap.sun, R.mipmap.pupuk};
    String nama_tanaman;
    String[] titles={"Menyiram", "Memanaskan"};
    String[] interval={"2 hari lagi", "setiap hari"};
    String []berapa_kali;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbManager = new D_DbManager(getActivity());
        // remote server.
        nama_tanaman = getActivity().getIntent().getStringExtra("nama_tanaman");
        Log.e("Tanaman", nama_tanaman);
        getDataNotif();

        initDataset();
    }

    public void getDataNotif(){
        ArrayList<ArrayList<Object>> data = dbManager.ambilTanamanNotifikasi(nama_tanaman);
        titles = new String[data.size()];
        interval = new String[data.size()];
        berapa_kali = new String[data.size()];

        for (int posisi = 0; posisi < data.size(); posisi++) {
            try {
                ArrayList<Object> baris = data.get(posisi);
                Log.e("String0", ""+baris.get(0).toString()+"\n"+nama_tanaman);
                if (nama_tanaman.equals(baris.get(0).toString())) {
                    titles[posisi] = new String(baris.get(1).toString());
                    interval[posisi] = new String(baris.get(2).toString()+"");
                    berapa_kali[posisi] = new String(baris.get(3).toString()+"");

                    Log.e("Jadwal notif", baris.get(0).toString()+"\n"+baris.get(1).toString()+"\n"+baris.get(2).toString()+"\n"+baris.get(3).toString());
                }
            } catch (NullPointerException n) {
                n.printStackTrace();
                Log.e("Gagal-ListFragment", "" + n.toString());
            }
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.material_listview_frag, container, false);
        rootView.setTag(TAG);

        // BEGIN_INCLUDE(initializeRecyclerView)
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewFrag);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());

        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        mAdapter = new ListAdapter(mJudul, mInterval, mBerapaKali, mIcon);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        // END_INCLUDE(initializeRecyclerView)
        return rootView;
    }

    /**
     * Set RecyclerView's LayoutManager to the one given.
     *
     * @param layoutManagerType Type of layout manager to switch to.
     */

    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case GRID_LAYOUT_MANAGER:
                mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
                mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(scrollPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */
    private void initDataset() {
        mJudul = new String[titles.length];
        mInterval = new String[interval.length];
        mBerapaKali = new String[berapa_kali.length];
        mIcon = new int[titles.length];

        for (int i = 0; i < titles.length; i++) {
            if(!titles[i].equals(null)  || !titles[i].equals("")) {
                mJudul[i] = titles[i];
                mInterval[i] = "Setiap " + interval[i] + " hari";
                mBerapaKali[i] = berapa_kali[i] + " kali";
                if (mJudul[i].equals("Penyiraman"))
                    mIcon[i] = icons[0];
                else if (mJudul[i].equals("Pemupukan"))
                    mIcon[i] = icons[2];
            }
        }
    }
}

