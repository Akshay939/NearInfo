package com.example.lib;

import java.util.ArrayList;

/**
 * Created by rd on 14/05/2015.
 */
public class Config
{
    final public static String REQUEST_API_KEY="AIzaSyDT7pnw44fSh-JnCDOL5NcR1gNR1qUBezw";
    final public static String REQUEST_URL="https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    final public static String PHOTO_REF_URL="https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";
    final public static String[] type_list={"food","gym","school","hospital","spa","restaurant"};
    public final static int GPS_SERVICES_REQUEST = 500;
    public final static int DB_VER = 1;
    public final static String DB_Name="PLACE_DB";

}
