package com.android.potato;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.potato.model.UserRegReceive;
import com.potato.model.UserRegRequest;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by s1112001 on 2016/6/13.
 */
public class UserInfoActivity extends Activity {
    EditText et_userid, et_pwd;
    Button btn_logout;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setNavigationBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.color_11);
            tintManager.setNavigationBarTintResource(R.color.color_11);
        }
        setContentView(R.layout.activity_user_info);

        ImageButton img_btn_back = (ImageButton) findViewById(R.id.img_btn_back);
        img_btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        et_userid = (EditText) findViewById(R.id.et_userid);
        et_pwd = (EditText) findViewById(R.id.et_pwd);

        et_userid.setText(mPreferences.getString("user_name",""));

        btn_logout = (Button) findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor = mPreferences.edit();
                mEditor.putString("user_name", "");
                mEditor.putString("user_id", "");
                mEditor.putString("token", "");
                mEditor.commit();

                Intent intent = new Intent(UserInfoActivity.this, MainActivity.class);
                startActivity(intent);

                Toast.makeText(UserInfoActivity.this,"注销成功！",Toast.LENGTH_LONG).show();
            }
        });
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}
