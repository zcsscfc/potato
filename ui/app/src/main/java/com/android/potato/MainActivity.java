package com.android.potato;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.potato.list.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends FragmentActivity {
    private DrawerLayout mDrawerLayout = null;
    private ListView listView = null;
    LeftMenuListAdapter leftMenuListAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.color_11);
        tintManager.setNavigationBarTintResource(R.color.color_11);
        // 设置边距，保证对齐
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        LinearLayout linearLayoutTitleContainer = (LinearLayout) findViewById(R.id.linearLayoutTitleContainer);
        linearLayoutTitleContainer.setPadding(0, Utility.getStatusBarHeight(), 0, 0); // config.getPixelInsetTop(true) / 3 + 10

        //左侧导航菜单
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        listView = (ListView) findViewById(R.id.LeftMenuListView);
        LinearLayout ll_login = (LinearLayout) findViewById(R.id.ll_login);
        ll_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        List<Map<String, Object>> list = getData();
        leftMenuListAdapter = new LeftMenuListAdapter(this, list);
        listView.setAdapter(leftMenuListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                Map<String, String> map = (Map<String, String>) leftMenuListAdapter.getItem(arg2);
                Toast.makeText(MainActivity.this, map.get("tv_name"), Toast.LENGTH_LONG).show();
            }
        });
        ImageView leftMenu = (ImageView) findViewById(R.id.leftMenu);
        leftMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 按钮按下，将抽屉打开
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });
    }

    //获取左侧菜单列表数据
    public List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 5; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("iv_icon", R.mipmap.ic_launcher);
            map.put("tv_name", "设置" + i);
            list.add(map);
        }
        return list;
    }
}
