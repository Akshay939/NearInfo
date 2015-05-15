package com.example.nearinfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bean.Place_bean;
import com.example.lib.Config;


public class Details_screen extends Activity {
    Button btn_show,btn_image;
    TextView tv_title,tv_details,tv_vic,tv_open,tv_lat,tv_lng;
    Place_bean placeBean;
    String photo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_details_screen);
        init_object();init_widget();
        try
        {
            placeBean =  (Place_bean)getIntent().getSerializableExtra("obj");
            if(placeBean!=null)
            {
                photo=placeBean.getPhoto_ref();
                if(photo.equalsIgnoreCase("NA"))
                {
                    btn_image.setVisibility(View.GONE);
                }
                else
                {
                    btn_image.setVisibility(View.VISIBLE);
                }
                Log.d("Photo",photo);
                tv_title.setText(placeBean.getName());
                tv_open.setText( "Open Now : "+placeBean.getOpenNow());
                tv_vic.setText("Vicinity : \n"+placeBean.getvicinity());
                tv_lat.setText("Latitude : "+placeBean.getLatitude());
                tv_lng.setText("Longitude : "+placeBean.getLongitude());
                tv_details.setText("Category : " + placeBean.getCategory());
            }

            btn_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    show_alert(Config.PHOTO_REF_URL+photo+"&key="+Config.REQUEST_API_KEY);
                }
            });
            btn_show.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(getApplicationContext(),Place_map.class);
                    i.putExtra("lat",placeBean.getLatitude());
                    i.putExtra("lng",placeBean.getLongitude());
                    i.putExtra("name",placeBean.getName());
                    startActivity(i);
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"No Details Found !!",Toast.LENGTH_LONG).show();
        }
    }
    private void init_object()
    {
        placeBean=new Place_bean();
    }
    private void init_widget()
    {
        btn_show=(Button)findViewById(R.id.btn_show_map);
        btn_image=(Button)findViewById(R.id.btn_show_image);
        tv_details=(TextView)findViewById(R.id.tv_details);
        tv_lat=(TextView)findViewById(R.id.tv_lat);
        tv_lng=(TextView)findViewById(R.id.tv_long);
        tv_vic=(TextView)findViewById(R.id.tv_vicinity);
        tv_open=(TextView)findViewById(R.id.tv_tody_open);
        tv_title=(TextView)findViewById(R.id.tv_title);


    }
    public void show_alert(String path)
    {
        try {
            Log.d("Path",path);
            final AlertDialog alert = new AlertDialog.Builder(this).create();
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.alert, null);
            alert.setView(layout);
            Button btn_ok = (Button) layout.findViewById(R.id.btn_ok);
            WebView webView = (WebView) layout.findViewById(R.id.webView);
            webView.loadUrl(path);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setSupportZoom(true);
            alert.show();
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    alert.dismiss();
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext()," Image Not Found !!",Toast.LENGTH_SHORT).show();
        }
    }// end of show alter
}
