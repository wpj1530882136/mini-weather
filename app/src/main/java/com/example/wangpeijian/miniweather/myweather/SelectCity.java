package com.example.wangpeijian.miniweather.myweather;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.content.Intent;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import com.example.wangpeijian.miniweather.R;
import com.example.wangpeijian.miniweather.app.MyApplication;

import java.util.ArrayList;

/**
 * Created by wangpeijian on 2016/10/18.
 */
public class SelectCity extends Activity implements View.OnClickListener {
    private ImageView mBackBtn;
    private TextView mTitleName;
    private ListView mlistView;
    private EditText mEditText;
    MyApplication myApplication;
    ArrayAdapter<String> adapter;
    private ArrayList<String>data;
    private ArrayList<String>tmp_data;
    private void initEditText() {
        TextWatcher mTextWatcher = new TextWatcher(){
            private CharSequence temp;
            private int editStart;
            private int editEnd;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                temp = charSequence;
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                Log.d("myapp","onTextChanged:"+charSequence) ;
                String matchSeqence = charSequence.toString();
                tmp_data.clear();
                for(i=0;i<data.size();i++){
                    if(data.get(i).contains(matchSeqence)){
                        tmp_data.add(data.get(i));
                    }
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (temp.length()> 10) {
                    editStart = mEditText.getSelectionStart();
                    editEnd = mEditText.getSelectionEnd();
                    Toast.makeText(SelectCity.this,"你输⼊入的字数已经超过了限制！", Toast.LENGTH_SHORT).show();
                    editable.delete(editStart-1, editEnd);
                    int tempSelection = editStart;
                    mEditText.setText(editable);
                    //mEditText.setSelection(tempSelection);
                }
                Log.d("myapp","afterTextChanged:") ;
            }

        };
        mEditText = (EditText)findViewById(R.id.search_text);
        mEditText.addTextChangedListener(mTextWatcher);
        mEditText.setText("");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState){
        setContentView(R.layout.select_city);
        super.onCreate(savedInstanceState);
        mTitleName = (TextView)findViewById(R.id.title_name);
        mBackBtn = (ImageView)findViewById(R.id.title_back);
        mBackBtn.setOnClickListener(this);
        myApplication = MyApplication.getInstance();
        int data_len = myApplication.getCityListSize();
        data = new ArrayList<>();
        tmp_data = new ArrayList<>();
        for(int i=0;i<data_len;i++){
            data.add(myApplication.getCity(i));
            tmp_data.add(data.get(i));
        }
        mlistView = (ListView)findViewById(R.id.list_view);

        adapter=new ArrayAdapter<String>(
                this,android.R.layout.simple_list_item_1,tmp_data);
        mlistView.setAdapter(adapter);
        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(SelectCity.this,"你选择了:"+tmp_data.get(i),Toast.LENGTH_SHORT).show();
                mTitleName.setText("当前城市："+tmp_data.get(i));
            }
        });
        initEditText();
    }
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.title_back:
                Intent intent = new Intent();
                int start_pos = mTitleName.getText().toString().indexOf('：');
                int end_pos =  mTitleName.getText().toString().length();
                int pos = myApplication.getCityPos(mTitleName.getText().subSequence(start_pos+1,end_pos).toString());
                intent.putExtra("cityCode",myApplication.getCityNum(pos));
                setResult(RESULT_OK,intent);
                finish();
                break;
            default:
                break;
        }
    }
}
