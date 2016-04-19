package com.android.potato;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 创建状态栏的管理实例
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // 激活状态栏设置
        tintManager.setStatusBarTintEnabled(true);
        // 激活导航栏设置
        tintManager.setNavigationBarTintEnabled(true);
        // 设置颜色
        tintManager.setStatusBarTintResource(R.color.colorPrimary);
        tintManager.setNavigationBarTintResource(R.color.colorPrimary);
        // 设置边距，保证对齐
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        TextView textViewHello = (TextView)findViewById(R.id.textViewHello);
        textViewHello.setPadding(0, config.getPixelInsetTop(true), 0, config.getPixelInsetBottom());
    }
}
