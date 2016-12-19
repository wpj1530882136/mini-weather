package com.example.wangpeijian.miniweather.myweather;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.wangpeijian.miniweather.R;
import com.example.wangpeijian.miniweather.bean.MyLocationListener;
import com.example.wangpeijian.miniweather.bean.TodayWeather;
import com.example.wangpeijian.miniweather.bean.ViewPagerAdapter;
import com.example.wangpeijian.miniweather.util.NetUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangpeijian on 2016/9/21.
 */
public class MainActivity extends Activity implements View.OnClickListener, ViewPager.OnPageChangeListener{
    private ImageView mUpdateBtn;
    private ProgressBar mUpdateProgressBar;
    private TextView cityTv, timeTv, humidityTv, pmDataTv, pmQualityTv, city_name_Tv;
    private TextView temperatureTv, climateTv, windTv, weekTv;
    private ImageView  weatherImg;
    private ImageView pmImg;
    private ImageView mSelectCity;
    private ImageView mLocationBtn;
    private static final int UPDATE_TODAY_WEATHER = 1;
    //add view pager
    private ViewPagerAdapter viewPagerAdapter;
    ViewPager viewPager;
    private List<View> views;
    private ImageView[] imageView;
    private int []ids = {R.id.iv1,R.id.iv2};
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case UPDATE_TODAY_WEATHER:
                    updateTodayWeather((TodayWeather) msg.obj);
                    mUpdateBtn.setVisibility(View.VISIBLE);
                    mUpdateProgressBar.setVisibility(View.INVISIBLE);
                    break;
                default:
                    break;
            }
        }
    };
    void initView(){

//add view pager
        LayoutInflater inflater = LayoutInflater.from(this);
        views = new ArrayList<View>();
        views.add(inflater.inflate(R.layout.page1,null));
        views.add(inflater.inflate(R.layout.page2,null));
        viewPagerAdapter = new ViewPagerAdapter(views,this);
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(this);

        weekTv = (TextView)findViewById(R.id.week_today);
        city_name_Tv = (TextView)findViewById(R.id.title_city_name);
        cityTv = (TextView) findViewById(R.id.city);
        timeTv = (TextView) findViewById(R.id.time);
        humidityTv = (TextView) findViewById(R.id.humidity);
        pmDataTv = (TextView) findViewById(R.id.pm_data);
        pmQualityTv = (TextView) findViewById(R.id.pm2_5_quality );
        pmImg = (ImageView) findViewById(R.id.pm2_5_img);

        temperatureTv = (TextView) findViewById(R.id.temperature );
        climateTv = (TextView) findViewById(R.id.climate);
        windTv = (TextView) findViewById(R.id.wind);
        weatherImg = (ImageView) findViewById(R.id.weather_img);

        mUpdateProgressBar = (ProgressBar) findViewById(R.id.title_update_progress);
        mUpdateProgressBar.setVisibility(View.INVISIBLE);
        mLocationBtn = (ImageView)findViewById(R.id.title_location);

        SharedPreferences sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
        String cityCode=sharedPreferences.getString("main_city_code","101010100");
        queryWeather(cityCode);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_info);
        if(NetUtil.getNetWorkState(this)!=NetUtil.NETWORK_NONE){
            Log.d("my_weather","网络OK!");
            Toast.makeText(MainActivity.this,"网络OK!", Toast.LENGTH_LONG).show();
        } else {
            Log.d("my_weather","网络挂了!");
            Toast.makeText(MainActivity.this,"网络挂了!", Toast.LENGTH_LONG).show();
        }
        mUpdateBtn = (ImageView) findViewById(R.id.title_update_btn);
        mUpdateBtn.setOnClickListener(this);
        mSelectCity = (ImageView)findViewById(R.id.title_city_manager);
        mSelectCity.setOnClickListener(this);
        Log.d("my_app","MainActivity->onCreate");
        initView();
        initDot();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == 1){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onClick(View view){
        Log.d("my_weather","onClick");
        if(view.getId() == R.id.title_update_btn){
            mUpdateBtn.setVisibility(View.INVISIBLE);
            mUpdateProgressBar.setVisibility(View.VISIBLE);
            SharedPreferences sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
            String cityCode=sharedPreferences.getString("main_city_code","101010100");
            Log.d("my_weather",cityCode);
            if(NetUtil.getNetWorkState(this)!=NetUtil.NETWORK_NONE){
                queryWeather(cityCode);
            } else {
                Log.d("my_weather","网络挂了!!!");
                Toast.makeText(MainActivity.this,"网络挂了!", Toast.LENGTH_LONG).show();
            }

        } else if(view.getId()==R.id.title_city_manager) {
            Intent intent = new Intent(this,SelectCity.class);
            startActivityForResult(intent,1);
        } else if(view.getId()==R.id.title_location) {
            Toast.makeText(MainActivity.this,"定位", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this,Location.class);
            startActivityForResult(intent,1);
        }
    }

    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String newCityCode= data.getStringExtra("cityCode");
            SharedPreferences sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("main_city_code",newCityCode);
            editor.commit();
            Log.d("myWeather", "选择的城市代码为"+newCityCode);
            if (NetUtil.getNetWorkState(this) != NetUtil.NETWORK_NONE) {
                Log.d("myWeather", "网络OK");
                queryWeather(newCityCode);
            } else {
                Log.d("myWeather", "网络挂了");
                Toast.makeText(MainActivity.this, "网络挂了！", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void  queryWeather(String cityCode){
        final String address="http://wthrcdn.etouch.cn/WeatherApi?citykey="+cityCode;
        //final String address = "http://mobile100.zhangqx.com/calendar.html";
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con=null;
                TodayWeather todayWeather = null;
                try{
                    URL url=new URL(address);
                    con=(HttpURLConnection)url.openConnection();
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(8000);
                    con.setReadTimeout(8000);
                    InputStream in=con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response=new StringBuilder();
                    String str;
                    while((str=reader.readLine())!=null){
                        response.append(str);
                        Log.d("my_weather",str);
                    }
                    String responseStr=response.toString();
                    Log.d("my_weather",responseStr);
                    todayWeather =  TodayWeather.parseXML(responseStr);
                    if(todayWeather != null){
                        Message msg =new Message();
                        msg.what = UPDATE_TODAY_WEATHER;
                        msg.obj=todayWeather;
                        mHandler.sendMessage(msg);

                    }
                }catch(Exception e){
                }
            }
        }).start();
    }
    void updateTodayWeather(TodayWeather todayWeather){
        city_name_Tv.setText(todayWeather.getCity()+"天气");
        cityTv.setText(todayWeather.getCity());
        timeTv.setText(todayWeather.getUpdatetime()+ "发布");
        humidityTv.setText("湿度："+todayWeather.getShidu());
        pmDataTv.setText(todayWeather.getPm25());
        pmQualityTv.setText(todayWeather.getQuality());

        weekTv.setText(todayWeather.getDate(0));
        temperatureTv.setText(todayWeather.getLow(0)+"~"+todayWeather.getHigh(0));
        climateTv.setText(todayWeather.getType(0));
        windTv.setText("风力:"+todayWeather.getFengli(0));
        switch (climateTv.getText().toString()) {
            case "多云":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_duoyun);
                break;
            case "暴雨":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoyu);
                break;
            case "暴雪":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoxue);
                break;
            case "大暴雨":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);
                break;
            case "大雪":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_daxue);
                break;
            case "雾":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_wu);
                break;
            case "雷阵雨":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);
                break;
            case "小雨":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
            case "小雪":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);
                break;
            default:
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_qing);
                break;
        }
        viewPagerAdapter.update(todayWeather);
        Toast.makeText(MainActivity.this,"更新成功！",Toast.LENGTH_SHORT).show();
        //weatherImg.setImageResource(R.drawable.biz_plugin_weather_0_50);
    }
    private void initDot(){
        imageView = new ImageView[ids.length];
        for(int i=0;i<ids.length;i++){
            imageView[i]=(ImageView)findViewById(ids[i]);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for(int i=0;i<ids.length;i++){
            if(i==position){
                imageView[i].setImageResource(R.drawable.page_indicator_focused);
            } else {
                imageView[i].setImageResource(R.drawable.page_indicator_unfocused);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
