package com.example.controller;

import android.content.Context;
import android.util.Log;

import com.example.bean.Place_bean;
import com.example.lib.Config;
import com.example.lib.GPSTracker;
import com.example.lib.Http_request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class Parse_controller
{
    Http_request http_request;
    GPSTracker gpsTracker;
    Context mcontext;
    public  Parse_controller(Context context)
    {
        mcontext=context;
        http_request=new Http_request();
        gpsTracker=new GPSTracker(mcontext);

    }// end of constructor

    public ArrayList<Place_bean> get_location_list(String type,String str_red)
    {
        if(gpsTracker.isGPSenable())
        {
            Log.d("GPS","Enable");
            if(gpsTracker.canGetLocation())
            {
                Log.d("Lat",String.valueOf(gpsTracker.getLatitude()));
            }
        }

        String str_lat=String.valueOf(gpsTracker.getLatitude());
        String str_lng=String.valueOf(gpsTracker.getLongitude());
        String a=Config.REQUEST_URL+"types="+type+"&location="+str_lat+","+str_lng+"&radius="+str_red+"&sensor=true&key="+Config.REQUEST_API_KEY;
        Log.d("URL",a);
        String result=http_request.make_http_reuqest(Config.REQUEST_URL+"types="+type+"&location="+str_lat+","+str_lng+"&radius="+str_red+"&sensor=false&key="+Config.REQUEST_API_KEY);
        return parseGoogleParse(result);
    }
    public ArrayList<Place_bean> parseGoogleParse(final String response)
    {
        ArrayList<Place_bean> temp = new ArrayList<Place_bean>();
        try
        {
            JSONObject jsonObject = new JSONObject(response);
            // make an jsonObject in order to parse the response
            if (jsonObject.has("results"))
            {
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                for (int i = 0; i < jsonArray.length(); i++)
                {
                        Place_bean poi = new Place_bean();
                       if (jsonArray.getJSONObject(i).has("name"))
                       {
                             poi.setName(jsonArray.getJSONObject(i).optString("name"));
                             poi.setIcon(jsonArray.getJSONObject(i).optString("icon"));
                             poi.set_id(jsonArray.getJSONObject(i).optString("place_id"));
                             String lat=jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getString("lat");
                             String lng=jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getString("lng");
                             poi.setLatitude(lat);
                             poi.setLongitude(lng);
                             Log.d("long",""+lng);
                             Log.d("Lat:",""+lat);
                             poi.setvicinity(jsonArray.getJSONObject(i).optString("vicinity", " "));
                             if (jsonArray.getJSONObject(i).has("opening_hours"))
                             {
                                if (jsonArray.getJSONObject(i).getJSONObject("opening_hours").has("open_now"))
                                {
                                    if (jsonArray.getJSONObject(i).getJSONObject("opening_hours").getString("open_now").equals("true"))
                                    {
                                        poi.setOpenNow("YES");
                                    }
                                    else
                                    {
                                    poi.setOpenNow("NO");
                                    }
                                }
                            }
                            else
                            {
                                poi.setOpenNow("Not Known");
                            }
                            if(jsonArray.getJSONObject(i).has("photos"))
                            {
                                JSONArray photo_array=new JSONArray();
                                photo_array=jsonArray.getJSONObject(i).getJSONArray("photos");
                                for(int j=0;j<photo_array.length();j++)
                                {
                                   poi.setPhoto_ref(photo_array.getJSONObject(j).optString("photo_reference"));
                                   Log.d("Refer", photo_array.getJSONObject(j).optString("photo_reference"));
                                }

                            }
                            else
                            {
                                poi.setPhoto_ref("NA");
                                 Log.d("Photos","Not available");
                            }

                            if (jsonArray.getJSONObject(i).has("types"))
                            {
                                JSONArray typesArray = jsonArray.getJSONObject(i).getJSONArray("types");
                                for (int j = 0; j < typesArray.length(); j++)
                                {
                                    poi.setCategory(typesArray.getString(j) + ", " + poi.getCategory());
                                }
                            }
                        }
                        temp.add(poi);
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            return new ArrayList<Place_bean>();
        }
        return temp;

    }// end of parse bean

}// end of parse controller
