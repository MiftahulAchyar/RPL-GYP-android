package com.acenet.gyp;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.acenet.gyp.Index.B_CustomVolleyRequest;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

/**
 * Created by Belal on 11/9/2015.
 */
public class B_CardAdapter extends RecyclerView.Adapter<B_CardAdapter.ViewHolder> {

    private ImageLoader imageLoader;
    private Context context;

    //List of superHeroes
    List<B_ListTanaman> tanamanList;

    public B_CardAdapter(List<B_ListTanaman> superHeroes, Context context){
        super();
        //Getting all the superheroes
        this.tanamanList = superHeroes;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.b_tanaman_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        B_ListTanaman b_listTanaman =  tanamanList.get(position);

        imageLoader = B_CustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(b_listTanaman.getImageUrl(), ImageLoader.getImageListener(holder.imageView, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));

        holder.imageView.setImageUrl(b_listTanaman.getImageUrl(), imageLoader);
        holder.textViewId.setText(String.valueOf(b_listTanaman.getId()));
        holder.textViewName.setText(b_listTanaman.getName());
        holder.textViewCategory.setText(b_listTanaman.getCategory());
    }

    @Override
    public int getItemCount() {
        return tanamanList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public NetworkImageView imageView;
        public TextView textViewId;
        public TextView textViewName;
        public TextView textViewCategory;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (NetworkImageView) itemView.findViewById(R.id.imageViewHero);
            textViewId = (TextView) itemView.findViewById(R.id.textViewId);
            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            textViewCategory = (TextView) itemView.findViewById(R.id.textViewCategory);
        }
    }
}