package com.example.wangpeijian.miniweather.myweather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.wangpeijian.miniweather.R;
import com.example.wangpeijian.miniweather.app.MyApplication;
import com.example.wangpeijian.miniweather.bean.MyLocationListener;
import com.example.wangpeijian.miniweather.bean.TodayWeather;
import android.widget.ImageView;
/**
 * Created by wangpeijian on 2016/12/16.
 */
public class Location extends Activity implements View.OnClickListener{
    public LocationClient mLocationClient = null;
    public MyLocationListener myListener = new MyLocationListener();
    private TextView mLocationText;
    private TextView mLoactionTitleText;
    MyApplication myApplication;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            mLoactionTitleText.setText("定位成功");
            mLocationText.setText(msg.obj.toString());
        }
    };
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=100000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.location);
        super.onCreate(savedInstanceState);
        mLocationText = (TextView)findViewById(R.id.location_text);
        mLoactionTitleText = (TextView)findViewById(R.id.title_location);
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener( myListener );
        myApplication = MyApplication.getInstance();
        initLocation();
        mLocationClient.start();
        queryLocation();
    }
    private void queryLocation(){
        new Thread(new Runnable() {
            @Override
            public void run() {
            if(myListener.getLocationInfo().equals("")){
                    mHandler.postDelayed(this,2000);
            } else{
                Message msg =new Message();
                msg.obj = myListener.getLocationInfo();
                mHandler.sendMessage(msg);
            }
            }
        }).run();
    }
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.title_location_back:
                finish();
                break;
            default:
                break;
        }
    }
}
