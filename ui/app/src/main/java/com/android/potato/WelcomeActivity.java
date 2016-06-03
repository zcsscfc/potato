package com.android.potato;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.welcome);

        Intent mIntent = new Intent();
        boolean mFirst = isFirstEnter(WelcomeActivity.this,WelcomeActivity.this.getClass().getName());
        if(mFirst) {
            mIntent.setClass(WelcomeActivity.this, GuideActivity.class);
        }else{
            mIntent.setClass(WelcomeActivity.this, MainActivity.class);
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        }
        WelcomeActivity.this.startActivity(mIntent);
        WelcomeActivity.this.finish();
    }

    //****************************************************************
    // 判断应用是否初次加载，读取SharedPreferences中的guide_activity字段
    //****************************************************************
    private static final String SHAREDPREFERENCES_NAME = "my_pref";
    private static final String KEY_GUIDE_ACTIVITY = "guide_activity";
    private boolean isFirstEnter(Context context, String className){
        if(context==null || className==null||"".equalsIgnoreCase(className))return false;
        String mResultStr = context.getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_WORLD_READABLE)
                .getString(KEY_GUIDE_ACTIVITY, "");//取得所有类名 如 com.my.MainActivity
        if(mResultStr.equalsIgnoreCase("false"))
            return false;
        else
            return true;
    }
}
