	package com.example.Database;

    import android.content.ContentValues;
    import android.content.Context;
    import android.database.Cursor;
    import android.database.sqlite.SQLiteDatabase;
    import android.database.sqlite.SQLiteDatabase.CursorFactory;
    import android.database.sqlite.SQLiteOpenHelper;
    import android.util.Log;

    import com.example.bean.Place_bean;
    import com.example.lib.Handle_special_char;


    import java.util.ArrayList;

    public class Database extends SQLiteOpenHelper
    {
        SQLiteDatabase database;
        Handle_special_char handle_special_char;
        public static final String Near_info="DB_Near_info";
        public Database(Context context, String name, CursorFactory factory,
                        int version)
        {
            super(context, name, factory, version);
            // TODO Auto-generated constructor stub
        }
        @Override
        public void onCreate(SQLiteDatabase db)
        {
            // TODO Auto-generated method stub
            db.execSQL("CREATE TABLE IF NOT EXISTS "+Near_info+"(place_id text PRIMARY KEY NOT NULL , name text,icon text,open_now text,types text,vicinity text,photo_ref text,lat text,lng text)");

        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            // TODO Auto-generated method stub
            onCreate(db);
        }



    }// end of database class
