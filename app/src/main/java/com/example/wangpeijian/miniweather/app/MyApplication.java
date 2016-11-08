package com.example.wangpeijian.miniweather.app;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import com.example.wangpeijian.miniweather.bean.City;
import com.example.wangpeijian.miniweather.util.CityDB;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangpeijian on 2016/11/1.
 */
public class MyApplication extends Application {
    private static MyApplication myApplication;
    private  List<City>mCityList;
    private  CityDB mCityDB;
    @Override
    public void onCreate(){
        super.onCreate();
        myApplication=this;
        Log.d("my_app","MyApplication->onCreate");
        mCityDB = openCityDB();
        initCityList();
    }
    public static MyApplication getInstance(){
        return myApplication;
    }
    public CityDB openCityDB(){
        String path = "/data"
                + Environment.getDataDirectory().getAbsolutePath()
                +File.separator+getPackageName()
                +File.separator+"databases"
                +File.separator
                + CityDB.CITY_DB_NAME;
                ;
        File db = new File(path);
        if(!db.exists()) {
            String pathfolder = "/data"
                    + Environment.getDataDirectory().getAbsolutePath()
                    + File.separator + getPackageName()
                    + File.separator + "databases"
                    + File.separator;
            File dirFirstFolder = new File(pathfolder);
            if (!dirFirstFolder.exists()) {
                dirFirstFolder.mkdirs();
                Log.i("MyApp", "mkdirs");
            }
            try {
                InputStream is = getAssets().open("city.db");
                FileOutputStream fos = new FileOutputStream(db);
                int len = -1;
                byte[] buffer = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    fos.flush();
                }
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
        return new CityDB(this,path);
    }
    private void initCityList(){
        mCityList = new ArrayList<City>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                prepareCityList();
            }
        }).start();
    }
    private boolean prepareCityList(){
        mCityList = mCityDB.getAllCity();
        for(City city:mCityList){
            String cityName = city.getCity();
            Log.d("my_app",cityName);
        }
        return true;
    }
    public String getCity(int pos){
        return mCityList.get(pos).getCity();
    }
    public String getCityNum(int pos){
        return mCityList.get(pos).getCityNumber();
    }
    public int getCityListSize(){
        return mCityList.size();
    }
}
