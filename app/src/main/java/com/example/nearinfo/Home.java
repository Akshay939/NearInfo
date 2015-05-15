package com.example.nearinfo;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.adapter.Row_adapter;
import com.example.bean.Place_bean;
import com.example.controller.DB_controller;
import com.example.controller.Parse_controller;
import com.example.lib.Config;
import com.example.lib.ConnectionDetector;
import com.example.lib.GPSTracker;

import java.io.Serializable;
import java.util.ArrayList;


public class Home extends ActionBarActivity implements View.OnClickListener {

    Spinner sp_type;
    EditText ed_radius;
    RecyclerView r_list;
    Button btn_get;
    ArrayAdapter<String> sp_adapter;
    Parse_controller parse_controller;
    GPSTracker gpsTracker;
    ProgressDialog dialog;
    ArrayList<Place_bean> place_beans;
    Row_adapter row_adapter;
    ConnectionDetector connectionDetector;
    DB_controller db_controller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init_widget();
        init_object();

    }
    private void init_widget()
    {
        sp_type=(Spinner)findViewById(R.id.sp_type);
        sp_adapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_text, Config.type_list);
        sp_adapter.setDropDownViewResource(R.layout.spinner_drop_down);
        sp_type.setAdapter(sp_adapter);
           ed_radius=(EditText)findViewById(R.id.ed_radius);
        r_list=(RecyclerView)findViewById(R.id.r_list);
        r_list.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(Home.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        r_list.setLayoutManager(llm);
        btn_get=(Button)findViewById(R.id.btn_get_list);
        btn_get.setOnClickListener(this);
    }
    private void init_object()
    {
        connectionDetector=new ConnectionDetector(this);
        parse_controller=new Parse_controller(this);
        gpsTracker=new GPSTracker(this);
        db_controller=new DB_controller(this);
        place_beans=new ArrayList<>();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            ArrayList<Place_bean> placeBeanArrayList=db_controller.get_fav_list();
            if(placeBeanArrayList==null)
            {
                Toast.makeText(getApplicationContext(),"No Record Found !!",Toast.LENGTH_SHORT).show();
            }
            else
            {
                if (!placeBeanArrayList.isEmpty())
                {
                    show_alert(placeBeanArrayList, 2);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Place List is Empty !!",Toast.LENGTH_SHORT).show();
                }

            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_get_list :make_request();break;
        }// end of switch case
    }// end of onclick
    private void apply_validation()
    {
        if(ed_radius.length()==0)
        {
            ed_radius.setError("Compulsory");
        }
        else
        {
            String radius=ed_radius.getText().toString();
            String type=(String)sp_type.getSelectedItem();
            Log.d("type",type);
            Log.d("lat",""+gpsTracker.getLatitude());
            Log.d("Radius",radius);
            new fetch_data(type,radius).execute();
        }
    }
    class fetch_data extends AsyncTask<Void,Void,Void>
    {

        String str_type,str_r;
        public fetch_data(String type,String radius)
        {
            str_type=type;
            str_r=radius;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Home.this);
            dialog.setMessage("Loading details please wait..");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
           place_beans= parse_controller.get_location_list(str_type,str_r);
            Log.d("Result",""+place_beans);
          return  null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (dialog != null) {
                dialog.dismiss();
            }
            if(!place_beans.isEmpty()) {
                load_recycler(place_beans, 1, r_list);
            }
            else
            {
                Toast.makeText(getApplicationContext(),"No Record Found !!",Toast.LENGTH_SHORT).show();
            }

        }
    }// end of async task
     private void load_recycler(final ArrayList<Place_bean> place_beans,int option,RecyclerView recyclerView)
     {
        try {
            row_adapter = new Row_adapter(place_beans, Home.this, option);
            recyclerView.setAdapter(row_adapter);
            row_adapter.SetOnItemClickListener(new Row_adapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    //Log.d("Name", "" + place_beans.get(position).getName());
                    Intent intent = new Intent(getApplicationContext(), Details_screen.class);
                    Place_bean placeBean = new Place_bean();
                    placeBean = place_beans.get(position);
                    Log.d("Name", placeBean.getName());
                    intent.putExtra("obj", placeBean);
                    startActivity(intent);
                }

                @Override
                public void onbtnClick(int position) {

                    Place_bean placeBean = place_beans.get(position);
                    if (placeBean != null) {
                        int result = db_controller.insert_place(placeBean);
                        switch (result) {
                            case 0:
                                Toast.makeText(getApplicationContext(), "Max Record size : 20", Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                Toast.makeText(getApplicationContext(), "Place Saved Sucessfully !!", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                Toast.makeText(getApplicationContext(), "DB Error : Insert Record", Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                                Toast.makeText(getApplicationContext(), "Place Already Present !!", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                    Log.d("Name", "" + place_beans.get(position).getName());
                }
            });
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "Fail to load UI!!", Toast.LENGTH_SHORT).show();
        }
     }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Config.GPS_SERVICES_REQUEST) {

        }
    }
    private void make_request()
    {
        GPSTracker tracker = new GPSTracker(this);
        boolean isGPS = tracker.isGPSenable();
        if (isGPS)
        {
            if(connectionDetector.isConnectingToInternet()) {
                apply_validation();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Please Check Internet Connection !!",Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            showSettingsAlert();
        }
    }
    private void showSettingsAlert()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this);
        // Setting Dialog Title
        alertDialog.setTitle("GPS settings");
        // Setting Dialog Message
        alertDialog.setMessage("Please Enable GPS !");
        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog,int which)
            {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent,Config.GPS_SERVICES_REQUEST);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }// end of show setting Alert
    public void show_alert(final ArrayList<Place_bean> place_beans,int option)
    {
        try {
            Log.d("Inside show alert","");
            final AlertDialog alert = new AlertDialog.Builder(Home.this).create();
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.alert, null);
            RecyclerView fave_place=(RecyclerView)layout.findViewById(R.id.fav_list);
            Button btn_close=(Button)layout.findViewById(R.id.btn_ok);
            fave_place.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(Home.this);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            fave_place.setLayoutManager(llm);
            alert.setView(layout);

            load_recycler(place_beans,option,fave_place);
            alert.show();
            btn_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"No Record Found !!",Toast.LENGTH_SHORT).show();
        }
    }// end of show alter
}// end of activity
