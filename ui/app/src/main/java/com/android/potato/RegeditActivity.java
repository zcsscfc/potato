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
import com.potato.list.PostItem;
import com.potato.model.PostMain;
import com.potato.model.PostMainData;
import com.potato.model.PostMainRequest;
import com.potato.model.UserRegReceive;
import com.potato.model.UserRegRequest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.support.v4.app.ActivityCompat.startActivity;

/**
 * Created by s1112001 on 2016/5/17.
 */
public class RegeditActivity extends Activity {
    EditText et_userid, et_pwd;
    Button btn_regedit;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    //定义Handler对象
    private Handler handler =new Handler(){
        @Override
        //当有消息发送出来的时候就执行Handler的这个方法
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            //只要执行到这里就关闭对话框
            try {
                String jsonStr = (String) msg.obj;
                UserRegReceive userRegReceive = new Gson().fromJson(jsonStr, UserRegReceive.class);
                Toast toast = Toast.makeText(PotatoApplication.getInstance(),
                        userRegReceive.meta.message, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                if (userRegReceive.meta.success == "true") {
                    Intent intent = new Intent(RegeditActivity.this, MainActivity.class);
                    mPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
                    mEditor = mPreferences.edit();
                    mEditor.putString("user_name", et_userid.getText().toString());
                    mEditor.putString("user_id", userRegReceive.data.user_id);
                    mEditor.putString("token", userRegReceive.data.token);
                    mEditor.commit();
                    startActivity(intent);
                }

            } catch (Exception ex) {
                Toast.makeText(PotatoApplication.getInstance(),
                        "error HandleOnRefresh:" + ex.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    };

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
        setContentView(R.layout.activity_regedit);

        ImageButton img_btn_back = (ImageButton) findViewById(R.id.img_btn_back);
        img_btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        et_userid = (EditText) findViewById(R.id.et_userid);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        btn_regedit = (Button) findViewById(R.id.btn_regedit);
        btn_regedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            UserRegRequest userRegRequest = new UserRegRequest();
                            String userid = et_userid.getText().toString();
                            String pwd = new String(Base64.encode(et_pwd.getText().toString().getBytes(), Base64.DEFAULT));
                            userRegRequest.setLog_name(userid);
                            userRegRequest.setPassword(pwd);
                            String json = new Gson().toJson(userRegRequest, UserRegRequest.class);
                            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                            OkHttpClient okHttpClient = new OkHttpClient();
                            RequestBody body = RequestBody.create(JSON, json);
                            Request request = new Request.Builder()
                                    .url(AppConfig.SERVER_URL + "user/reg")
                                    .post(body).build();
                            Response response = okHttpClient.newCall(request).execute();
                            String rspStr = response.body().string();
                            android.os.Message msg = android.os.Message.obtain();
                            msg.obj = rspStr;
                            handler.sendMessage(msg);
                        } catch (Exception ex) {
                            Toast.makeText(PotatoApplication.getInstance(),
                                    "error onRefresh:" + ex.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }.start();
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