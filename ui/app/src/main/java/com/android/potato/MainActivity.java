package com.android.potato;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
    private ListView LeftMenuListView = null;
    LeftMenuListAdapter leftMenuListAdapter = null;
    private ListView LeftMenuListView2 = null;
    private ArrayList<String> list2 = new ArrayList<String>();

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
        LeftMenuListView = (ListView) findViewById(R.id.LeftMenuListView);
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
        LeftMenuListView.setAdapter(leftMenuListAdapter);
        LeftMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        LeftMenuListView2 = (ListView) findViewById(R.id.LeftMenuListView2);
        list2.add("  意见反馈");
        list2.add("  检查更新");
        list2.add("  关于我们");

        ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>

                (this,android.R.layout.simple_list_item_1,list2);

        LeftMenuListView2.setAdapter(myArrayAdapter);

        LeftMenuListView2.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,

                                    long arg3) {

                // TODO Auto-generated method stub
                if(list2.get(arg2).equals("  意见反馈"))
                {
                    Toast.makeText(MainActivity.this, "意见反馈", Toast.LENGTH_LONG).show();
                }
                if(list2.get(arg2).equals("  检查更新"))
                {
                    Toast.makeText(MainActivity.this, "检查更新", Toast.LENGTH_LONG).show();
                }
                if(list2.get(arg2).equals("  关于我们"))
                {
                    Toast.makeText(MainActivity.this, "关于我们", Toast.LENGTH_LONG).show();
                }
            }



        });
    }

    //获取左侧菜单列表数据
    public List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("iv_icon", R.drawable.u288);
        map.put("tv_name", "我的待读");
        list.add(map);
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("iv_icon", R.drawable.u290);
        map2.put("tv_name", "我的收藏");
        list.add(map2);
        Map<String, Object> map3 = new HashMap<String, Object>();
        map3.put("iv_icon", R.drawable.u292);
        map3.put("tv_name", "订阅的主题");
        list.add(map3);
        Map<String, Object> map4 = new HashMap<String, Object>();
        map4.put("iv_icon", R.drawable.u298);
        map4.put("tv_name", "订阅的站点");
        list.add(map4);
        Map<String, Object> map5 = new HashMap<String, Object>();
        map5.put("iv_icon", R.drawable.u294);
        map5.put("tv_name", "发现主题");
        list.add(map5);
        Map<String, Object> map6 = new HashMap<String, Object>();
        map6.put("iv_icon", R.drawable.u296);
        map6.put("tv_name", "发现站点");
        list.add(map6);

        return list;
    }
}
