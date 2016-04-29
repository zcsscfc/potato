package com.android.potato;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

public class MainActivity extends FragmentActivity {
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
        tintManager.setStatusBarTintResource(R.color.color_11);
        tintManager.setNavigationBarTintResource(R.color.color_11);
        // 设置边距，保证对齐
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        LinearLayout linearLayoutTitleContainer = (LinearLayout) findViewById(R.id.linearLayoutTitleContainer);
        linearLayoutTitleContainer.setPadding(0, config.getPixelInsetTop(true)/3+10, 0, 0);
    }
}
