package com.example.wangpeijian.miniweather.bean;

/**
 * Created by wangpeijian on 2016/11/1.
 */
public class City {
    private String city;
    private String number;
    public City(String num, String city) {
        this.number = num;
        this.city = city;
    }
    public void SetCity(String city){
        this.city  = city;
    }
    public String getCity(){
        return city;
    }
    public String getCityNumber(){return number;}
}
