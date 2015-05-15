package com.example.nearinfo;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class Place_map extends Activity {
    GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_place_map);
        try
        {
            String str_lat=getIntent().getStringExtra("lat");
            String str_lng=getIntent().getStringExtra("lng");
            String place_name=getIntent().getStringExtra("name");
            if(str_lat !=null && str_lng!=null)
            {
                double lat=Double.parseDouble(str_lat);
                double lng=Double.parseDouble(str_lng);
                LatLng latLng = new LatLng(lat,lng);
                if (googleMap == null) {
                    googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
                }
                googleMap.addMarker(new MarkerOptions().position(latLng).title(place_name));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,6));
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Fail To Load Location : Lat Long Missing",Toast.LENGTH_LONG).show();;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
