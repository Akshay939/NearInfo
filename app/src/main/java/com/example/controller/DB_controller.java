package com.example.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.Database.Database;
import com.example.bean.Place_bean;
import com.example.lib.Config;
import com.example.lib.Handle_special_char;

import java.util.ArrayList;

/**
 * Created by rd on 15/05/2015.
 */
public class DB_controller
{
    Database database;
    SQLiteDatabase sqLiteDatabase;
    Handle_special_char handle_special_char;
    Context context;
    public  DB_controller(Context context)
    {
        this.context=context;
        database=new Database(this.context, Config.DB_Name,null,Config.DB_VER);
        handle_special_char=new Handle_special_char();
    }
    public int insert_place(Place_bean place_bean)
    {
        handle_special_char=new Handle_special_char();
        sqLiteDatabase=database.getWritableDatabase();
        try
        {
            int count=get_record_count();
            if(count>=20)
            {
                System.out.println("Count is grater then two");
                return 0;
            }
            else {
                ContentValues cv = new ContentValues();
                cv.put("place_id", place_bean.getid());
                cv.put("name", handle_special_char.remove_special_character(place_bean.getName()));
                cv.put("icon", place_bean.getIcon_url());
                cv.put("open_now", place_bean.getOpenNow());
                cv.put("types", place_bean.getCategory());
                cv.put("photo_ref", place_bean.getPhoto_ref());
                cv.put("lat", place_bean.getLatitude());
                cv.put("lng", place_bean.getLongitude());
                cv.put("vicinity", handle_special_char.remove_special_character(place_bean.getvicinity()));
                sqLiteDatabase.insert(Database.Near_info, null, cv);
                return  1;
            }
        }
        catch (SQLiteConstraintException Exception)
        {
            return 3;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return 2;
        }
    }
    public int get_record_count()
    {
        Cursor c = null;
        sqLiteDatabase=database.getReadableDatabase();
        c=sqLiteDatabase.rawQuery("select * from "+Database.Near_info,null);
        if(c.getCount()>0)
        {
            int count=0;
            c.moveToFirst();
            do
            {
                count=count+1;
            }while(c.moveToNext());
            c.close();
            return count;
        }
        else
        {
            return 0;
        }
    }// end of record count
    public ArrayList<Place_bean> get_fav_list()
    {
        Cursor c = null;
        ArrayList<Place_bean> placeBeanArrayList=new ArrayList<Place_bean>();
        sqLiteDatabase=database.getReadableDatabase();
        handle_special_char=new Handle_special_char();
        //   (place_id text , name text,icon text,open_now text,types text,vicinity text,photo_ref text,lat text,lng text)");
        c=sqLiteDatabase.rawQuery("select * from "+Database.Near_info,null);
        if(c.getCount()>0)
        {
            c.moveToFirst();
            do
            {
                Place_bean  placeBean=new Place_bean();
                placeBean.set_id(c.getString(c.getColumnIndex("place_id")));
                placeBean.setName(handle_special_char.add_special_character(c.getString(c.getColumnIndex("name"))));
                placeBean.setIcon(c.getString(c.getColumnIndex("icon")));
                placeBean.setOpenNow(c.getString(c.getColumnIndex("open_now")));
                placeBean.setCategory(c.getString(c.getColumnIndex("types")));
                placeBean.setvicinity(handle_special_char.add_special_character(c.getString(c.getColumnIndex("vicinity"))));
                placeBean.setPhoto_ref(c.getString(c.getColumnIndex("photo_ref")));
                placeBean.setLatitude(c.getString(c.getColumnIndex("lat")));
                placeBean.setLongitude(c.getString(c.getColumnIndex("lng")));
                placeBeanArrayList.add(placeBean);
            }while(c.moveToNext());
            c.close();
            return placeBeanArrayList;
        }
        else
        {

            return null;
        }
    } // end of get fav
}// end of DB_controller
