package com.acenet.gyp.Setting;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.acenet.gyp.R;

import java.util.List;

/**
 * Created by AceNet on 1/11/2016.
 */
public class Setting2Adapter extends RecyclerView.Adapter<Setting2Adapter.ViewHolder> {

    private Context context;
    //List of superHeroes
    List<Notif_ListObject> tanamanList;

    public Setting2Adapter(List<Notif_ListObject> superHeroes, Context context){
        super();
        //Getting all the superheroes
        this.tanamanList = superHeroes;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.setting2_activity, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Setting2Adapter.ViewHolder holder, int position) {
        final Notif_ListObject n_listTanaman =  tanamanList.get(position);
        holder.namaTanaman.setText(n_listTanaman.getNama_tanaman());
    }

    @Override
    public int getItemCount() {
        return tanamanList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView namaTanaman;
        public ImageButton btn;

        public ViewHolder(View itemView) {
            super(itemView);

            namaTanaman = (TextView) itemView.findViewById(R.id.namaTanaman);
            btn = (ImageButton) itemView.findViewById(R.id.notifShowButton);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent jadwal = new Intent(context, com.acenet.gyp.Setting.Material.MainActivity.class);
                    jadwal.putExtra("nama_tanaman",namaTanaman.getText().toString());
                    view.getContext().startActivity(jadwal);
                }
            });
        }
    }
}
