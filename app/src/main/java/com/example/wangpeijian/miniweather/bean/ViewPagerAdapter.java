package com.example.wangpeijian.miniweather.bean;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wangpeijian.miniweather.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by wangpeijian on 2016/12/17.
 */
public class ViewPagerAdapter extends PagerAdapter {
    private List<View> views;
    private Context context;
    private TextView []weekTv,temperatureTv,climateTv,windTv;
    private ImageView[]  weatherImg;

    public ViewPagerAdapter(List<View>views, Context context){
        this.views = views;
        this.context = context;

        weekTv = new TextView[4];
        weekTv[0]=(TextView)views.get(0).findViewById(R.id.week_today_1);
        weekTv[1]=(TextView)views.get(0).findViewById(R.id.week_today_2);
        weekTv[2]=(TextView)views.get(1).findViewById(R.id.week_today_3);
        weekTv[3]=(TextView)views.get(1).findViewById(R.id.week_today_4);
        temperatureTv = new TextView[4];
        temperatureTv[0]=(TextView)views.get(0).findViewById(R.id.temperature_1);
        temperatureTv[1]=(TextView)views.get(0).findViewById(R.id.temperature_2);
        temperatureTv[2]=(TextView)views.get(1).findViewById(R.id.temperature_3);
        temperatureTv[3]=(TextView)views.get(1).findViewById(R.id.temperature_4);
        climateTv = new TextView[4];
        climateTv[0]=(TextView)views.get(0).findViewById(R.id.climate_1);
        climateTv[1]=(TextView)views.get(0).findViewById(R.id.climate_2);
        climateTv[2]=(TextView)views.get(1).findViewById(R.id.climate_3);
        climateTv[3]=(TextView)views.get(1).findViewById(R.id.climate_4);
        windTv = new TextView[4];
        windTv[0]=(TextView)views.get(0).findViewById(R.id.wind_1);
        windTv[1]=(TextView)views.get(0).findViewById(R.id.wind_2);
        windTv[2]=(TextView)views.get(1).findViewById(R.id.wind_3);
        windTv[3]=(TextView)views.get(1).findViewById(R.id.wind_4);
        weatherImg = new ImageView[4];
        weatherImg[0]=(ImageView) views.get(0).findViewById(R.id.weather_img_1);
        weatherImg[1]=(ImageView) views.get(0).findViewById(R.id.weather_img_2);
        weatherImg[2]=(ImageView) views.get(1).findViewById(R.id.weather_img_3);
        weatherImg[3]=(ImageView) views.get(1).findViewById(R.id.weather_img_4);

    }
    @Override
    public int getCount(){
        return views.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = views.get(position);


        TextView tmp = (TextView)view.findViewById(R.id.week_today_1);
           if(tmp!=null){
               String key = "week_today_1";
               tmp.setTag(key);
           }
        container.addView(view);

        return views.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }

    @Override
    public boolean isViewFromObject(View view, Object o){
        return (view==o);
    }
    public void update(TodayWeather todayWeather) {
        for (int i = 0; i < 4; i++) {
            weekTv[i].setText(todayWeather.getDate(i + 1));
            temperatureTv[i].setText(todayWeather.getLow(i + 1) + "~" + todayWeather.getHigh(i + 1));
            climateTv[i].setText(todayWeather.getType(i + 1));
            windTv[i].setText(todayWeather.getFengli(i + 1));
        }
        for (int i = 0; i < 4; i++) {
            switch (climateTv[i].getText().toString()) {
                case "多云":
                    weatherImg[i].setImageResource(R.drawable.biz_plugin_weather_duoyun);
                    break;
                case "暴雨":
                    weatherImg[i].setImageResource(R.drawable.biz_plugin_weather_baoyu);
                    break;
                case "暴雪":
                    weatherImg[i].setImageResource(R.drawable.biz_plugin_weather_baoxue);
                    break;
                case "大暴雨":
                    weatherImg[i].setImageResource(R.drawable.biz_plugin_weather_dabaoyu);
                    break;
                case "大雪":
                    weatherImg[i].setImageResource(R.drawable.biz_plugin_weather_daxue);
                    break;
                case "雷阵雨":
                    weatherImg[i].setImageResource(R.drawable.biz_plugin_weather_leizhenyu);
                    break;
                case "小雨":
                    weatherImg[i].setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
                case "小雪":
                    weatherImg[i].setImageResource(R.drawable.biz_plugin_weather_xiaoxue);
                    break;
                case "雾":
                    weatherImg[i].setImageResource(R.drawable.biz_plugin_weather_wu);
                    break;
                default:
                    weatherImg[i].setImageResource(R.drawable.biz_plugin_weather_qing);
                    break;
            }
        }
    }

}
