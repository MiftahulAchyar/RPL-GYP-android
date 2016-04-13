package com.acenet.gyp.Index;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.acenet.gyp.B_ListTanaman;
import com.acenet.gyp.R;
import com.android.volley.Network;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Deskripsi_PagerAdapter extends PagerAdapter {

    NetworkImageView imageView;
    ImageLoader imageLoader;

    Context mContext;
    LayoutInflater mLayoutInflater;
    List<B_ListTanaman> arr_listTanaman;
//    int[] mResources;
    String[] title={
            "Judul Satu",
            "Judul Dua",
            "Judul Tiga",
            "Judul Empat"
    };
    List<String> url;
    int count=0;

    public Deskripsi_PagerAdapter(Context context, List<B_ListTanaman> listTanaman) {
        mContext = context;
        arr_listTanaman = listTanaman;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        url = new ArrayList<String>();
        if (!listTanaman.get(0).getGambar0().equals("")){++count;}
        if (!listTanaman.get(0).getGambar1().equals(""))        ++count;
        if (!listTanaman.get(0).getGambar2().equals(""))        ++count;
        if (!listTanaman.get(0).getGambar3().equals(""))        ++count;

        Log.e("Jumlah gambar", count+"");
//        url = new String[count];
        try {
            if (!listTanaman.get(0).getGambar0().equals(""))        url.add(""+listTanaman.get(0).getGambar0());
            if (!listTanaman.get(0).getGambar1().equals(""))        url.add(""+listTanaman.get(0).getGambar1());
            if (!listTanaman.get(0).getGambar2().equals(""))        url.add(""+listTanaman.get(0).getGambar2());
            if (!listTanaman.get(0).getGambar3().equals(""))        url.add(""+listTanaman.get(0).getGambar3());
//            url[0] = listTanaman.get(0).getGambar0();
//            url[1] = listTanaman.get(0).getGambar1();
//            url[2] = listTanaman.get(0).getGambar2();
//            url[3] = listTanaman.get(0).getGambar3();
        }catch (OutOfMemoryError | IndexOutOfBoundsException ex){
            ex.printStackTrace();
        } catch (Exception ex){
            ex.printStackTrace();
        }


    }

    @Override
    public int getCount() {
        return url.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.deskripsi_pager_item, container, false);
        imageView = (NetworkImageView) itemView.findViewById(R.id.img_thumbnail);

        for(int i=0; i<count; i++){
            imageLoader = B_CustomVolleyRequest.getInstance(mContext).getImageLoader();

//            imageLoader.get(url[position], ImageLoader.getImageListener(imageView, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));
//            imageView.setImageUrl(url[position], imageLoader);
            imageLoader.get(url.get(position), ImageLoader.getImageListener(imageView, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));
            imageView.setImageUrl(url.get(position), imageLoader);
        }

//        new LoadImage(imageView).execute(url[position]);

//        imageView.setImageResource(mResources[position]);

//        TextView textView = (TextView) itemView.findViewById(R.id.textView);
//        textView.setText(title[position]);
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }


//    public void insertImage(ImageLoader image[]){
//        imageLoader = new ImageLoader[image.length];
//        for (int i=0; i<image.length; i++){
//            imageLoader[i] = image[i];
//        }
//    }

}
