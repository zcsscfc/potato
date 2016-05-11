package com.android.potato;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.potato.list.PostItem;
import com.potato.model.PostData;
import com.potato.model.PostDetail;
import com.potato.model.PostMain;
import com.potato.model.PostMainList;

import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by s1112001 on 2016/5/10.
 */
public class DetailActivity extends Activity {
    private TextView tv_title, tv_source_date;
    private WebView wv_content;
    private android.os.Handler msgHandler;
    private final int msg_get_post_m_list = 0;
    private PostItem postItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        postItem = (PostItem) getIntent().getSerializableExtra("postItem");

        ImageButton img_btn_cancel = (ImageButton) findViewById(R.id.img_btn_cancel);
        img_btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_source_date = (TextView) findViewById(R.id.tv_source_date);
        wv_content = (WebView) findViewById(R.id.wv_content);

        getData();
        android.os.Looper looper = android.os.Looper.myLooper();
        msgHandler = new MessageHandler(looper);
    }

    class MessageHandler extends android.os.Handler {

        public MessageHandler(android.os.Looper looper) {
            super(looper);
        }

        public void handleMessage(android.os.Message msg) {
            switch (msg.arg1) {
                case msg_get_post_m_list:
                    tv_title.setText(postItem.getTitle());
                    Date _date = new Date();
                    tv_source_date.setText(postItem.getOrigin() + "  " + postItem.getTime());
                    String jsonStr = (String) msg.obj;
                    try {
                        PostData result = new Gson().fromJson(jsonStr, PostData.class);
                        PostDetail detail = (PostDetail) result.getData();
                        String content = detail.getDetail();
                        wv_content.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
                    } catch (Exception ex) {
                        Log.e("DetailActivity01", ex.toString());
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void getData() {
        new Thread() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().
                            url("http://ec2-52-192-233-37.ap-northeast-1.compute.amazonaws.com/postd/" + postItem.getPostId()).
                            build();
                    Response response = client.newCall(request).execute();
                    String rspStr = response.body().string();
                    android.os.Message msg = android.os.Message.obtain();
                    msg.arg1 = msg_get_post_m_list;
                    msg.obj = rspStr;
                    msgHandler.sendMessage(msg);
                } catch (Exception ex) {
                    Log.e("DetailActivity02", ex.toString());
                }
            }
        }.start();
    }
}
