package com.yezi.yezibbs;

import androidx.appcompat.app.AppCompatActivity;



import android.content.Context;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.Log;
import android.widget.TextView;

import com.yezi.baselibrary.ViewID;
import com.yezi.baselibrary.ViewUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {
    @ViewID(R.id.hello_text);
    private TextView mTextView;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtils.inject(this);
    }


}
