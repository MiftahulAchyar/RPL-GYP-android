package com.acenet.gyp.Index;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.acenet.gyp.B_ListTanaman;
import com.acenet.gyp.R;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

/**
 * Created by Belal on 11/9/2015.
 */
public class B_Grid_SEARCH extends RecyclerView.Adapter<B_Grid_SEARCH.ViewHolder> {

    private ImageLoader imageLoader;
    private Context context;

    //List of superHeroes
    List<B_ListTanaman> tanamanList;

    public B_Grid_SEARCH(List<B_ListTanaman> superHeroes, Context context){
        super();
        //Getting all the superheroes
        this.tanamanList = superHeroes;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.index_tanaman_grid, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final B_ListTanaman b_listTanaman =  tanamanList.get(position);

        imageLoader = B_CustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(b_listTanaman.getImageUrl(), ImageLoader.getImageListener(holder.imageView, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));
        holder.imageView.setImageUrl(b_listTanaman.getImageUrl(), imageLoader);
        holder.textViewId.setText(b_listTanaman.getId());
        holder.textViewName.setText(b_listTanaman.getName());
        holder.textViewCategory.setText(b_listTanaman.getCategory());
    }

    @Override
    public int getItemCount() {
        return tanamanList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public NetworkImageView imageView;
        public TextView textViewName;
        public TextView textViewCategory;
        public TextView textViewId;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (NetworkImageView) itemView.findViewById(R.id.img_thumbnail);
            textViewId = (TextView) itemView.findViewById(R.id.tv_id);
            textViewName = (TextView) itemView.findViewById(R.id.tv_species);
            textViewCategory = (TextView) itemView.findViewById(R.id.tv_category);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DeskripsiTanaman.class);
                    intent.putExtra("id", textViewId.getText().toString());
                    intent.putExtra("nama", textViewName.getText().toString());
                    intent.putExtra("kategori",textViewCategory.getText().toString());
                    intent.putExtra("img",imageView.getDrawable().toString());
                    view.getContext().startActivity(intent);
                    Log.d("Kelik: ", textViewName.getText().toString());
                }
            });
        }

    }
}