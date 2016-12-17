package com.example.wangpeijian.miniweather.myweather;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.example.wangpeijian.miniweather.util.NetUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by wangpeijian on 2016/9/21.
 */
public class MainActivity extends Activity implements View.OnClickListener{
    private ImageView mUpdateBtn;
    private ProgressBar mUpdateProgressBar;
    private TextView cityTv, timeTv, humidityTv, weekTv, pmDataTv, pmQualityTv,  temperatureTv, climateTv, windTv, city_name_Tv;
    private ImageView weatherImg, pmImg;
    private ImageView mSelectCity;
    private ImageView mLocationBtn;
    private static final int UPDATE_TODAY_WEATHER = 1;
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
        city_name_Tv = (TextView) findViewById(R.id.title_city_name);
        cityTv = (TextView) findViewById(R.id.city);
        timeTv = (TextView) findViewById(R.id.time);
        humidityTv = (TextView) findViewById(R.id.humidity);
        weekTv = (TextView) findViewById(R.id.week_today);
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
        /*
        city_name_Tv.setText("N/A");
        cityTv.setText("N/A");
        timeTv.setText("N/A");
        humidityTv.setText("N/A");
        pmDataTv.setText("N/A");
        pmQualityTv.setText("N/A");
        weekTv.setText("N/A");
        temperatureTv.setText("N/A");
        climateTv.setText("N/A");
        windTv.setText("N/A");
        */
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
                        Log.d("my_weather",todayWeather.toString());
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
        weekTv.setText(todayWeather.getDate());
        temperatureTv.setText(todayWeather.getLow()+"~"+todayWeather.getHigh());
        climateTv.setText(todayWeather.getType());
        switch(climateTv.getText().toString()){
            case"多云":
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
            case"大雪":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_daxue);
                break;
            case"雷阵雨":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);
                break;
            case"小雨":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
            case"小雪":
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);
                break;
            default:
                weatherImg.setImageResource(R.drawable.biz_plugin_weather_qing);
                break;
        }
        windTv.setText("风力:"+todayWeather.getFengli());
        Toast.makeText(MainActivity.this,"更新成功！",Toast.LENGTH_SHORT).show();
        //weatherImg.setImageResource(R.drawable.biz_plugin_weather_0_50);
    }
}
