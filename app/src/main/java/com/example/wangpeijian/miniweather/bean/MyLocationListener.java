package com.example.wangpeijian.miniweather.bean;

import android.content.Intent;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.example.wangpeijian.miniweather.myweather.Location;

import java.util.List;

/**
 * Created by wangpeijian on 2016/12/13.
 */
public class MyLocationListener implements BDLocationListener {
    @Override
    public void onReceiveLocation(BDLocation location) {
        //Receive Location
        StringBuffer sb = new StringBuffer(256);
        sb.append("时间 : ");
        sb.append(location.getTime());
        sb.append("\n城市 :");
        sb.append(location.getCity());
        sb.append("\n经度 : ");
        sb.append(location.getLatitude());
        sb.append("\n纬度 : ");
        sb.append(location.getLongitude());
        sb.append("\n半径 : ");
        sb.append(location.getRadius());
        if (location.getLocType() == BDLocation.TypeGpsLocation){// GPS定位结果
            sb.append("\n地址 : ");
            sb.append(location.getAddrStr());
            sb.append("\n状态 : ");
            sb.append("gps定位成功");

        } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){// 网络定位结果
            sb.append("\n地址 : ");
            sb.append(location.getAddrStr());
            //运营商信息
            sb.append("\n状态 : ");
            sb.append("网络定位成功");
        } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
            sb.append("\ndescribe : ");
            sb.append("离线定位成功，离线定位结果也是有效的");
        } else if (location.getLocType() == BDLocation.TypeServerError) {
            sb.append("\ndescribe : ");
            sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
        } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
            sb.append("\ndescribe : ");
            sb.append("网络不同导致定位失败，请检查网络是否通畅");
        } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
            sb.append("\ndescribe : ");
            sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
        }
        List<Poi> list = location.getPoiList();// POI数据
        if (list != null) {
            for (Poi p : list) {
                sb.append("\n位置: ");
                sb.append(p.getName());
            }
        }
        Log.i("BaiduLocationApiDem", sb.toString());
        locationInfo = sb.toString();
    }
    public String getLocationInfo(){
        return locationInfo;
    }
    private String locationInfo="";
}
