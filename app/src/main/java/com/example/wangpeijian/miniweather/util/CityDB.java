package com.example.wangpeijian.miniweather.util;
import com.example.wangpeijian.miniweather.bean.City;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangpeijian on 2016/11/1.
 */
public class CityDB {
    public static String CITY_DB_NAME = "city.db";
    public static String CITY_TABLE_NAME = "city";
    private SQLiteDatabase db=null;

    public CityDB(Context context, String path){
        db = context.openOrCreateDatabase(CITY_DB_NAME,context.MODE_PRIVATE,null);
    }
    public List<City>getAllCity(){
        List<City> list = new ArrayList<City>();
        Cursor c = db.rawQuery("select * from "+CITY_TABLE_NAME,null);
        while(c.moveToNext()){
            String number = c.getString(c.getColumnIndex("number"));
            String city = c.getString(c.getColumnIndex("city"));
            City item = new City(number, city);
            list.add(item);
        }
        return list;
    }
}
