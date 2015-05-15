package com.example.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bean.Place_bean;
import com.example.lib.Config;
import com.example.lib.Http_request;
import com.example.nearinfo.R;


import java.util.ArrayList;

/**
 * Created by rd on 07/05/2015.
 */
public class Row_adapter extends RecyclerView.Adapter<Row_adapter.Row_holder>
{
    ArrayList<Place_bean> place_list;
    Context context;
    Http_request http_request;

    OnItemClickListener  mItemClickListener;
   int option;
    public Row_adapter(ArrayList<Place_bean> place_list, Context context,int option)
    {

        this.place_list=place_list;
        this.context=context;
        http_request=new Http_request();
        this.option=option;

    }
    @Override
    public Row_holder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_adapter, parent, false);
        return new Row_holder(v);
    }

    @Override
    public void onBindViewHolder(Row_holder holder, int position)
    {
        holder.tv_title.setText(place_list.get(position).getName());
        holder.tv_vict.setText(place_list.get(position).getvicinity());
        holder.tv_open_now.setText("Open Today : "+place_list.get(position).getOpenNow());
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ) {
            new load_image(holder.iv_thumb,place_list.get(position).getIcon_url()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            new load_image(holder.iv_thumb,place_list.get(position).getIcon_url()).execute();
        }
    }

    @Override
    public int getItemCount() {
        return place_list.size();
    }
    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
        public void onbtnClick(int position);
    }
    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    class Row_holder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView tv_title,tv_vict,tv_open_now;
        public  ImageView iv_thumb;
        public Button btn_fav;
        public Row_holder(View itemView)
        {
            super(itemView);
            btn_fav=(Button)itemView.findViewById(R.id.btn_fave);
            iv_thumb=(ImageView)itemView.findViewById(R.id.iv_row_image);
            tv_title=(TextView)itemView.findViewById(R.id.tv_row_title);
            tv_open_now=(TextView)itemView.findViewById(R.id.tv_open_now);
            tv_vict=(TextView)itemView.findViewById(R.id.tv_row_address);
            if(option==1) {

                btn_fav.setOnClickListener(this);
            }
            else
            {
                btn_fav.setBackgroundResource(R.drawable.fav_mark);
            }
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {

            if(v.getId()==R.id.btn_fave)
            {
                Log.d("On Click called","Button");
               show_fav_alert(btn_fav,getAdapterPosition());
            }
            else {
                Log.d("On Item called","Button");
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(v, getAdapterPosition()); //OnItemClickListener mItemClickListener;
                } else {
                    Log.d("Listner", "null");
                }
            }
        }


    }
    class load_image extends AsyncTask<Void,Void,Void>
    {
        ImageView iv;
        String url;
        Bitmap bmp;
        load_image(ImageView imageView,String url)
        {
            iv=imageView;
            this.url=url;
            Log.d("Url",url);
        }

        @Override
        protected Void doInBackground(Void... params)
        {

                bmp = http_request.downloadBitmap(url);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(bmp!=null)
            {

                    iv.setImageBitmap(bmp);

            }
        }
    }
    private void show_fav_alert(final Button btn_fav, final int position)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        // Setting Dialog Title
        alertDialog.setTitle("Favorite");
        // Setting Dialog Message
        alertDialog.setMessage("Save As Favorite ??");
        // On pressing Settings button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog,int which)
            {
                btn_fav.setBackgroundResource(R.drawable.fav_mark);

                if (mItemClickListener != null)
                {
                    mItemClickListener.onbtnClick(position); //OnItemClickListener mItemClickListener;
                }
                else
                {
                    Log.d("In side alert","null");
                }

            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();

            }
        });

        // Showing Alert Message
        alertDialog.show();
    }// end of show setting Alert
}// end of row adapter
