package com.yezi.yezibbs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yezi.baselibrary.exception.ExceptionCrashHandler;
import com.yezi.baselibrary.ioc.ViewById;
import com.yezi.baselibrary.ioc.ViewOnClick;
import com.yezi.baselibrary.ioc.ViewUtils;
import com.yezi.framelibrary.BaseSkinActivity;

import java.util.HashMap;


/**
 * @author yez
 */
public class MainActivity extends BaseSkinActivity {
    @ViewById(R.id.hello_text)
    private TextView mTextView;
    private static final String TAG = "MainActivity";

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main);
        ViewUtils.inject(this);
    }

    @Override
    protected void initData() {
      //  checkCrashAndUpload();
        mTextView.setText("test");
    }

    private void checkCrashAndUpload() {
        HashMap<String,String> map = ExceptionCrashHandler.getInstance().getCrashLogFile();
        if(map == null) {
            return;
        }
        for(String key:map.keySet()){
            System.out.println("Key: "+key+" Value: "+map.get(key));
        }
    }

    @Override
    protected void initActionBar() {
        int a = 2/0;
    }

    @ViewOnClick(R.id.hello_text)
    private void onClick(View view){
        Toast.makeText(this,"onClick",Toast.LENGTH_LONG).show();
    }
}
