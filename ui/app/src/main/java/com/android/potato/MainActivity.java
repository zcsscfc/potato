package com.android.potato;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;

public class MainActivity extends FragmentActivity {
    private DrawerLayout drawerLayout = null;
    private ListView listViewLeftMenu1 = null;
    private LeftMenuListAdapter leftMenuListAdapter = null;
    private ListView listViewLeftMenu2 = null;
    private long exitTime = 0;
    private TextView textViewNickName;
    private static SharedPreferences mPreferences;
    private static SharedPreferences.Editor mEditor;
    private String nickName;
    private ImageView imageViewPhoto = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(false);
        tintManager.setStatusBarTintResource(R.color.color_11);
        tintManager.setNavigationBarTintResource(R.color.color_11);
        // 设置边距，保证对齐
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        LinearLayout linearLayoutTitleContainer = (LinearLayout) findViewById(R.id.linearLayoutTitleContainer);
        linearLayoutTitleContainer.setPadding(0, 0, 0, 0); // Utility.getStatusBarHeight()

        //左侧导航菜单
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        LinearLayout linearLayoutLeftMenu = (LinearLayout) findViewById(R.id.linearLayoutLeftMenu);
        linearLayoutLeftMenu.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true; // 解决 left menu 触摸事件，会影响到，文章列表的触摸事件
            }
        });

        textViewNickName = (TextView) findViewById(R.id.textViewNickName);
        imageViewPhoto = (ImageView) findViewById(R.id.imageViewPhoto);

        UserInfoShared userInfoShared = new UserInfoShared(this);
        nickName = userInfoShared.getNickName();
        if (nickName != "") {
            textViewNickName.setText(nickName);
        }
        String photoDiskPath = userInfoShared.getPhotoDiskPath();
        if (photoDiskPath != "") {
            SetImageViewPhoto(photoDiskPath);
        }

        listViewLeftMenu1 = (ListView) findViewById(R.id.listViewLeftMenu1);
        RelativeLayout relativeLayoutLogin = (RelativeLayout) findViewById(R.id.relativeLayoutLogin);
        relativeLayoutLogin.setPadding(0, 0, 0, 0);
        relativeLayoutLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nickName == "") {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
                    startActivity(intent);
                }
            }
        });

        List<Map<String, Object>> list = getData();
        leftMenuListAdapter = new LeftMenuListAdapter(this, list);
        listViewLeftMenu1.setAdapter(leftMenuListAdapter);
        listViewLeftMenu1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Map<String, String> map = (Map<String, String>) leftMenuListAdapter.getItem(arg2);
                String tv_name = map.get("tv_name");
                Intent intent = new Intent();
                switch (tv_name) {
                    case "我的待读":
                        if (nickName == "") {
                            Toast.makeText(getApplicationContext(), "请登录！", Toast.LENGTH_SHORT).show();
                        } else {
                            intent = new Intent(MainActivity.this, ToReadActivity.class);
                            startActivity(intent);
                        }
                        break;
                    case "我的收藏":

                        if (nickName == "") {
                            Toast.makeText(getApplicationContext(), "请登录！", Toast.LENGTH_SHORT).show();
                        } else {
                            intent = new Intent(MainActivity.this, MyFavActivity.class);
                            startActivity(intent);
                        }
                        break;
                    case "订阅的主题":

                        if (nickName == "") {
                            Toast.makeText(getApplicationContext(), "请登录！", Toast.LENGTH_SHORT).show();
                        } else {
                            intent = new Intent(MainActivity.this, SubTopicActivity.class);
                            startActivity(intent);
                        }
                        break;
                    case "订阅的站点":

                        if (nickName == "") {
                            Toast.makeText(getApplicationContext(), "请登录！", Toast.LENGTH_SHORT).show();
                        } else {
                            intent = new Intent(MainActivity.this, SubSiteActivity.class);
                            startActivity(intent);
                        }
                        break;
                    case "发现主题":
                        intent = new Intent(MainActivity.this, FindTopicActivity.class);
                        startActivity(intent);
                        break;
                    case "发现站点":
                        intent = new Intent(MainActivity.this, FindSiteActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
        ImageView imageViewLeft = (ImageView) findViewById(R.id.imageViewLeft);
        imageViewLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        final ArrayList<String> list2 = new ArrayList<String>();
        listViewLeftMenu2 = (ListView) findViewById(R.id.listViewLeftMenu2);
        list2.add("意见反馈");
        list2.add("检查更新");
        list2.add("分享APP");
        list2.add("关于我们");

        LeftMenuListAdapter2 myArrayAdapter
                = new LeftMenuListAdapter2(this, list2);

        listViewLeftMenu2.setAdapter(myArrayAdapter);
        listViewLeftMenu2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (list2.get(arg2).equals("意见反馈")) {
                    Intent intent = new Intent(MainActivity.this, FeedbackActivity.class);
                    startActivity(intent);
                }
                if (list2.get(arg2).equals("检查更新")) {
                    UpdateManager manager = new UpdateManager(MainActivity.this);
                    // 检查软件更新
                    manager.checkUpdate();
                }
                if (list2.get(arg2).equals("分享APP")) {
                    ImageView img = new ImageView(MainActivity.this);
                    img.setImageResource(R.drawable.share);
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Share App")
                            .setView(img)
                            .setPositiveButton("OK", null)
                            .setCancelable(false)
                            .show();
                }
                if (list2.get(arg2).equals("关于我们")) {
                    Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                    startActivity(intent);
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

    // 重写onKeyDown
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void SetImageViewPhoto(String photoDiskPath) {
        if (photoDiskPath == null || photoDiskPath == "") {
            return;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(photoDiskPath);
        } catch (FileNotFoundException e) {
            Log.e("E000000003", e.toString());
        }
        Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream, null, options);
        try {
            fileInputStream.close();
        } catch (IOException e) {
            Log.e("E000000004", e.toString());
        }
        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageViewPhoto.getDrawable();
        if (bitmapDrawable != null) {
            Bitmap bitmap2 = bitmapDrawable.getBitmap();
            if (null != bitmap2 && !bitmap2.isRecycled()) {
                bitmap2.recycle();
                bitmap2 = null;
            }
        }
        imageViewPhoto.setImageBitmap(null);
        imageViewPhoto.setImageBitmap(bitmap);
    }
}
