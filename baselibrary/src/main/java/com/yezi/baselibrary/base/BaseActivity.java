package com.yezi.baselibrary.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author : yezi
 * @date : 2020/3/3 11:08
 * desc   :
 * version: 1.0
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();
        initData();
        initActionBar();
    }

    /**
     * 设置内容layout
     */
    protected abstract void setContentView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 初始化标题栏
     */
    protected abstract void initActionBar();

}
