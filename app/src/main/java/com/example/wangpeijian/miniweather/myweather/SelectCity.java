package com.example.wangpeijian.miniweather.myweather;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.content.Intent;
import android.widget.ListView;
import android.widget.Toast;
import android.util.Log;
import com.example.wangpeijian.miniweather.R;
import com.example.wangpeijian.miniweather.app.MyApplication;

/**
 * Created by wangpeijian on 2016/10/18.
 */
public class SelectCity extends Activity implements View.OnClickListener {
    private ImageView mBackBtn;
    private ListView mlistView;
    MyApplication myApplication;
    private String[]data;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        setContentView(R.layout.select_city);
        super.onCreate(savedInstanceState);
        mBackBtn = (ImageView)findViewById(R.id.title_back);
        mBackBtn.setOnClickListener(this);
        myApplication = MyApplication.getInstance();
        int data_len = myApplication.getCityListSize();
        data = new String[data_len];
        for(int i=0;i<data_len;i++){
            data[i] = myApplication.getCity(i);
        }
        mlistView = (ListView)findViewById(R.id.list_view);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(
                this,android.R.layout.simple_list_item_1,data);
        mlistView.setAdapter(adapter);
        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(SelectCity.this,"你选择了:"+data[i],Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("cityCode",myApplication.getCityNum(i));
                Log.d("my_test",myApplication.getCityNum(i));
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.title_back:
                Intent intent = new Intent();
                intent.putExtra("cityCode","101160101");
                Log.d("my_error","helo");
                setResult(RESULT_OK,intent);
                finish();
                break;
            default:
                break;
        }
    }
}
