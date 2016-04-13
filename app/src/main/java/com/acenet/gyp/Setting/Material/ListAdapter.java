package com.acenet.gyp.Setting.Material;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;

import com.acenet.gyp.R;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private static final String TAG = "CustomAdapter";

    private String[] mDataSet, mDataSet2, mDataset3;
    private int[] mDataset4;
    // BEGIN_INCLUDE(recyclerViewSampleViewHolder)
    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView judul;
        private final TextView interval;
        private final TextView berapa_kali;
        private final ImageView icon;
        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("Kelik", "Element " + getPosition() + " clicked.");
//                    Toast.makeText(ViewHolder.this, "", Toast.LENGTH_SHORT).show();
                }
            });
            judul = (TextView) v.findViewById(R.id.judul);
            interval = (TextView) v.findViewById(R.id.interval);
            berapa_kali = (TextView) v.findViewById(R.id.berapa_kali);
            icon = (ImageView) v.findViewById(R.id.list_icon);
        }

        public TextView getJudul() {return judul;}
        public TextView getInterval() {
            return interval;
        }
        public TextView getBerapa_kali() {
            return berapa_kali;
        }
        public ImageView getIcon() {
            return icon;
        }
    }
    // END_INCLUDE(recyclerViewSampleViewHolder)

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public ListAdapter(String[] dataSet, String[] dataSet2, String[] dataSet3, int[] icon) {
        mDataSet = dataSet;
        mDataSet2 = dataSet2;
        mDataset3 = dataSet3;
        mDataset4 = icon;
    }

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.material_row_item, viewGroup, false);

        return new ViewHolder(v);
    }
    // END_INCLUDE(recyclerViewOnCreateViewHolder)

    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        viewHolder.getJudul().setText(mDataSet[position]);
        viewHolder.getInterval().setText(mDataSet2[position]);
        viewHolder.getBerapa_kali().setText(mDataset3[position]);
        viewHolder.getIcon().setImageResource(mDataset4[position]);
    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.length;
    }
}

