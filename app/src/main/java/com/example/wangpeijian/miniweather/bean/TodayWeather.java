package com.example.wangpeijian.miniweather.bean;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.StringReader;
/**
 * Created by wangpeijian on 2016/10/11.
 */
public class TodayWeather {
    private String city;
    private String updatetime;
    private String wendu;
    private String shidu;
    private String pm25;
    private String quality;
    private String fengxiang;

    private String []fengli;

    private String []date;
    private String []high;
    private String []low;
    private String []type;
    public TodayWeather(){
        fengli = new String[10];
        date = new String[10];
        high = new String[10];
        low = new String[10];
        type = new String[10];
        Log.d("my_weather","hello create");
    }
    public String getCity() {
        return city;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public String getWendu() {
        return wendu;
    }

    public String getShidu() {
        return shidu;
    }

    public String getPm25() {
        return pm25;
    }

    public String getQuality() {
        return quality;
    }

    public String getFengxiang() {
        return fengxiang;
    }

    public String getFengli(int pos) {
        return fengli[pos];
    }

    public String getDate(int pos) {
        return date[pos];
    }

    public String getHigh(int pos) {
        return high[pos];
    }

    public String getLow(int pos) {
        return low[pos];
    }

    public String getType(int pos) {
        return type[pos];
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public void setWendu(String wendu) {
        this.wendu = wendu;
    }

    public void setShidu(String shidu) {
        this.shidu = shidu;
    }

    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public void setFengxiang(String fengxiang) {
        this.fengxiang = fengxiang;
    }

    public void setFengli(int pos,String fengli) {
        this.fengli[pos] = fengli;
    }

    public void setHigh(int pos,String high) {
        this.high[pos] = high;
    }

    public void setDate(int pos, String date) {this.date[pos] = date;}

    public void setLow(int pos,String low) {
        this.low[pos] = low;
    }

    public void setType(int pos,String type) {
        this.type[pos] = type;
    }

    public static TodayWeather parseXML(String xmldata){
        TodayWeather todayWeather = null;
        int fengxiangCount=0;
        int fengliCount =0;
        int dateCount=0;
        int highCount =0;
        int lowCount=0;
        int typeCount =0;
        try {
            XmlPullParserFactory fac = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = fac.newPullParser();
            xmlPullParser.setInput(new StringReader(xmldata));
            boolean is_day = true;
            int eventType = xmlPullParser.getEventType();
            Log.d("myWeather", "parseXML");
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {                    // 判断当前事件是否为文档开始事件
                    case XmlPullParser.START_DOCUMENT:
                        break;                    // 判断当前事件是否为标签元素开始事件
                    case XmlPullParser.START_TAG:
                         if(xmlPullParser.getName().equals("resp" )){
                             todayWeather= new TodayWeather();
                         }
                         if (todayWeather != null) {
                             if(xmlPullParser.getName().equals("day")) is_day = true;
                             else if(xmlPullParser.getName().equals("night"))is_day = false;
                             if (xmlPullParser.getName().equals("city")) {
                                 eventType = xmlPullParser.next() ;
                                 todayWeather.setCity(xmlPullParser.getText());
                             } else if (xmlPullParser.getName().equals("updatetime")) {
                                 eventType = xmlPullParser.next() ;
                                 todayWeather.setUpdatetime(xmlPullParser.getText());
                             } else if (xmlPullParser.getName().equals("shidu")) {
                                 eventType = xmlPullParser.next() ;
                                 todayWeather.setShidu(xmlPullParser.getText());
                             } else if (xmlPullParser.getName().equals("wendu")) {
                                 eventType = xmlPullParser.next();
                                 todayWeather.setWendu(xmlPullParser.getText());
                             } else if (xmlPullParser.getName().equals("pm25")) {
                                 eventType = xmlPullParser.next() ;
                                 todayWeather.setPm25(xmlPullParser.getText());
                             } else if (xmlPullParser.getName().equals("quality")) {
                                 eventType = xmlPullParser.next() ;
                                 todayWeather.setQuality(xmlPullParser.getText());
                             } else if (xmlPullParser.getName().equals("fengxiang")&&fengxiangCount==0) {
                                 eventType = xmlPullParser.next() ;
                                 todayWeather.setFengxiang(xmlPullParser.getText());
                                 fengxiangCount++;
                             } else if (xmlPullParser.getName().equals("fengli")&&fengliCount<=4&&is_day) {
                                 eventType = xmlPullParser.next() ;
                                 todayWeather.setFengli(fengliCount, xmlPullParser.getText());
                                 fengliCount++;
                             } else if (xmlPullParser.getName().equals("date")&&dateCount<=4) {
                                 eventType = xmlPullParser.next() ;
                                 todayWeather.setDate(dateCount, xmlPullParser.getText());
                                 dateCount++;
                             } else if (xmlPullParser.getName().equals("high")&&highCount<=4) {
                                 eventType = xmlPullParser.next() ;
                                 todayWeather.setHigh(highCount,xmlPullParser.getText().substring(2).trim());
                                 highCount++;
                             } else if (xmlPullParser.getName().equals("low") &&lowCount<=4) {
                                 eventType = xmlPullParser.next() ;
                                 todayWeather.setLow(lowCount, xmlPullParser.getText().substring(2).trim());
                                 lowCount++;
                             } else if (xmlPullParser.getName().equals("type")&&typeCount<=4&&is_day) {
                                 eventType = xmlPullParser.next() ;
                                 todayWeather.setType(typeCount, xmlPullParser.getText());
                                 typeCount++;
                             }
                         }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType = xmlPullParser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return todayWeather;
    }
}
