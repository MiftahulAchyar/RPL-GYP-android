package com.acenet.gyp.Index;
import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.acenet.gyp.B_ListTanaman;
import com.acenet.gyp.R;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by AceNet on 12/21/2015.
 */public class DeskripsiAdapter extends RecyclerView.Adapter<DeskripsiAdapter.ViewHolder> {
    private ImageLoader imageLoader, imageLoaderB[];

    private Context context;
    //List of superHeroes
    List<B_ListTanaman> tanamanList;
    Deskripsi_PagerAdapter pagerAdapter;

    TextView[] textView;
    int jum;
    Handler handler = new Handler() ;

    public DeskripsiAdapter(List<B_ListTanaman> superHeroes, Context context){
        super();
        //Getting all the superheroes
        this.tanamanList = superHeroes;
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.deskripsi_tanaman, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        B_ListTanaman b_listTanaman =  tanamanList.get(position);
//        imageLoader = B_CustomVolleyRequest.getInstance(context).getImageLoader();
//        imageLoader.get(b_listTanaman.getImageUrl(), ImageLoader.getImageListener(holder.imageView, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));
//        holder.imageView.setImageUrl(b_listTanaman.getImageUrl(), imageLoader);

//        imageLoaderB = new ImageLoader[3];
//        for(int i=0; i<3; i++){
//            imageLoaderB[i] = B_CustomVolleyRequest.getInstance(context).getImageLoader();
//            imageLoaderB[i].get()
//        }


        pagerAdapter = new Deskripsi_PagerAdapter(context, tanamanList);
        jum = pagerAdapter.getCount();
        textView = new TextView[jum];

        for (int i=0; i<jum; i++){
            textView[i] = new TextView(context);
//            textView[i].setText("O" + i);
            textView[i].setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
//            textView[i].setTextSize(25);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(15, 15);
            layoutParams.setMargins(5, 0, 5, 0);
            holder.linearLayout.addView(textView[i], layoutParams);
        }
        holder.viewPager.setAdapter(pagerAdapter);
        textView[holder.viewPager.getCurrentItem()].setBackgroundColor(context.getResources().getColor(android.R.color.white));
        holder.viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(final int position) {
                textView[position].setBackgroundColor(context.getResources().getColor(android.R.color.white));
                for (int i = 0; i < textView.length; i++) {
                    if (i == position)  continue;
                    textView[i].setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        final ViewHolder viewHolder = holder;
        final Runnable update = new Runnable() {
            int on = viewHolder.viewPager.getCurrentItem();
            @Override
            public void run() {
                if (viewHolder.viewPager.getCurrentItem() == (jum-1)){
//                    mViewPager.setAdapter(mCustomPagerAdapter);
                    on =0;
                    viewHolder.viewPager.setCurrentItem(on, true);
//                    Toast.makeText(MainActivity.this, ""+mViewPager.getCurrentItem(), Toast.LENGTH_SHORT).show();
                }
                viewHolder.viewPager.setCurrentItem(on++, true);
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

        String desc = b_listTanaman.getDescription().replaceAll("&nbsp;","\t").replaceAll("\n\t", "\n");
        holder.textViewDescription.setText (desc);
        holder.textViewName.setText(b_listTanaman.getName());
    }

    @Override
    public int getItemCount() {
        return tanamanList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
//        public NetworkImageView imageView;
        public ViewPager viewPager;
        public TextView textViewName;
        public TextView textViewDescription;
        LinearLayout linearLayout;
        public ViewHolder(View itemView) {
            super(itemView);
//            imageView = (NetworkImageView) itemView.findViewById(R.id.img_thumbnail);

            linearLayout =  (LinearLayout) itemView.findViewById(R.id.image_bullets);
            viewPager = (ViewPager) itemView.findViewById(R.id.pager);

            textViewDescription = (TextView) itemView.findViewById(R.id.deskripsiDeskripsi);
            textViewName = (TextView) itemView.findViewById(R.id.deskripsiNama);
        }
    }
}