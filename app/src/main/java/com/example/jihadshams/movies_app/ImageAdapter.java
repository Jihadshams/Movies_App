package com.example.jihadshams.movies_app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by Jihad Shams on 11/3/2016.
 */

public class ImageAdapter extends BaseAdapter {
    List<String> poster_paths;
    Context context;

    public final String LOG_TAG = "sso";

    //public final String LOG_TAG = "sso";

    /////////// ////////
    public ImageAdapter(Context context,List<String> poster_paths){
        Log.e(LOG_TAG,"EROOOOOOOOOOOR" );//constructor
        this.context=context;
        this.poster_paths=poster_paths;
        for (int i=0;i<poster_paths.size();i++)
        {
            //here
           Log.e(LOG_TAG,poster_paths.get(i)+"");
      }
    }

    @Override
    public int getCount() {
        return poster_paths.size();
    }                 //////////////////

    @Override
    public String getItem(int i) {   //return URL
        return poster_paths.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    class ViewHolder{
        ImageView imageView;
    }
    public void clear(int i)
    {
        poster_paths.remove(i);
        notifyDataSetChanged();
    }
    //   MovieFragment mf=new MovieFragment();

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder=new ViewHolder();
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.gridview_item,null);  //
            holder.imageView=(ImageView)convertView.findViewById(R.id.imageview_item); // name of item ImageView
            convertView.setTag(holder);
        }else{

            holder=(ViewHolder)convertView.getTag();
        }
        Log.e(LOG_TAG, "PICASSO");
        Picasso.with(context).load(getItem(i)).into(holder.imageView);
        return convertView;
    }

}

