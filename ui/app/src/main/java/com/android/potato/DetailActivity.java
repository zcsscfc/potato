package com.android.potato;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.potato.list.PostItem;
import com.potato.model.AddFavReceive;
import com.potato.model.AddFavRequest;
import com.potato.model.PostData;
import com.potato.model.PostDetail;
import com.potato.model.AddToReadRequest;
import com.potato.model.AddToReadReceive;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by s1112001 on 2016/5/10.
 */
public class DetailActivity extends Activity {
    private WebView wv_content;
    private android.os.Handler msgHandler;
    private final int MSG_GET_POST_D = 0;
    private final int MSG_ADD_TO_READ = 1;
    private final int MSG_ADD_FAV = 2;
    private PostItem postItem;
    private ImageButton img_btn_back, img_btn_menu;
    private Button btn_to_read, btn_fav;
    PopupMenu popupMenu;
    Menu menu;
    private TextView textViewTitle, textViewOrigin, textViewTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setNavigationBarTintEnabled(false);
            tintManager.setStatusBarTintResource(R.color.color_11);
            tintManager.setNavigationBarTintResource(R.color.color_11);
        }
        setContentView(R.layout.activity_detail);

        img_btn_back = (ImageButton) findViewById(R.id.img_btn_back);
        btn_to_read = (Button) findViewById(R.id.btn_to_read);
        btn_fav = (Button) findViewById(R.id.btn_fav);
        img_btn_menu = (ImageButton) findViewById(R.id.img_btn_menu);

        popupMenu = new PopupMenu(this, findViewById(R.id.img_btn_menu));
        menu = popupMenu.getMenu();
        // 通过XML文件添加菜单项
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        // 监听事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item1:
                        Toast.makeText(DetailActivity.this, "You Click 菜单1", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.item2:
                        Toast.makeText(DetailActivity.this, "You Click 菜单2", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        img_btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(DetailActivity.this, MainActivity.class);
//                startActivity(intent);
                finish();
            }
        });
        btn_to_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToRead();
            }
        });
        btn_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFav();
            }
        });
        img_btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupmenu();
            }
        });

        postItem = (PostItem) getIntent().getSerializableExtra("postItem");
        wv_content = (WebView) findViewById(R.id.wv_content);

        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewOrigin = (TextView) findViewById(R.id.textViewOrigin);
        textViewTime = (TextView) findViewById(R.id.textViewTime);
        textViewTitle.setText(postItem.getTitle());
        textViewOrigin.setText(postItem.getOriginName());
        textViewTime.setText(postItem.getTime());

        getData();
        android.os.Looper looper = android.os.Looper.myLooper();
        msgHandler = new MessageHandler(looper);
    }

    public void popupmenu() {
        popupMenu.show();
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

    class MessageHandler extends android.os.Handler {

        public MessageHandler(android.os.Looper looper) {
            super(looper);
        }

        public void handleMessage(android.os.Message msg) {
            switch (msg.arg1) {
                case MSG_GET_POST_D:
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
                case MSG_ADD_TO_READ:
                    String jsonToRead = (String) msg.obj;
                    try {
                        AddToReadReceive addToReadReceive = new Gson().fromJson(jsonToRead, AddToReadReceive.class);
                        Toast toast = Toast.makeText(PotatoApplication.getInstance(),
                                addToReadReceive.meta.message, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    } catch (Exception ex) {
                        Log.e("DetailActivity01", ex.toString());
                    }
                    break;
                case MSG_ADD_FAV:
                    String jsonFav = (String) msg.obj;
                    try {
                        AddFavReceive addFavReceive = new Gson().fromJson(jsonFav, AddFavReceive.class);
                        Toast toast = Toast.makeText(PotatoApplication.getInstance(),
                                addFavReceive.meta.message, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
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
                            url(AppConfig.SERVER_URL + "postd/" + postItem.getPostId()).
                            build();
                    Response response = client.newCall(request).execute();
                    String rspStr = response.body().string();
                    android.os.Message msg = android.os.Message.obtain();
                    msg.arg1 = MSG_GET_POST_D;
                    msg.obj = rspStr;
                    msgHandler.sendMessage(msg);
                } catch (Exception ex) {
                    Log.e("DetailActivity02", ex.toString());
                }
            }
        }.start();
    }

    private void addToRead() {
        new Thread() {
            @Override
            public void run() {
                try {
                    AddToReadRequest addToReadRequest = new AddToReadRequest();
                    String post_id = postItem.getPostId();
                    addToReadRequest.setPost_id(post_id);
                    String json = new Gson().toJson(addToReadRequest, AddToReadRequest.class);
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    OkHttpClient okHttpClient = new OkHttpClient();
                    RequestBody body = RequestBody.create(JSON, json);
                    Request request = new Request.Builder()
                            .url(AppConfig.SERVER_URL + "user/addtoread")
                            .post(body).build();
                    Response response = okHttpClient.newCall(request).execute();
                    String rspStr = response.body().string();
                    android.os.Message msg = android.os.Message.obtain();
                    msg.arg1 = MSG_ADD_TO_READ;
                    msg.obj = rspStr;
                    msgHandler.sendMessage(msg);
                } catch (Exception ex) {
                    Toast.makeText(PotatoApplication.getInstance(),
                            "error onRefresh:" + ex.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }.start();
    }

    private void addFav() {
        new Thread() {
            @Override
            public void run() {
                try {
                    AddFavRequest addFavRequest = new AddFavRequest();
                    String post_id = postItem.getPostId();
                    addFavRequest.setPost_id(post_id);
                    String json = new Gson().toJson(addFavRequest, AddFavRequest.class);
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    OkHttpClient okHttpClient = new OkHttpClient();
                    RequestBody body = RequestBody.create(JSON, json);
                    Request request = new Request.Builder()
                            .url(AppConfig.SERVER_URL + "user/addfav")
                            .post(body).build();
                    Response response = okHttpClient.newCall(request).execute();
                    String rspStr = response.body().string();
                    android.os.Message msg = android.os.Message.obtain();
                    msg.arg1 = MSG_ADD_FAV;
                    msg.obj = rspStr;
                    msgHandler.sendMessage(msg);
                } catch (Exception ex) {
                    Toast.makeText(PotatoApplication.getInstance(),
                            "error onRefresh:" + ex.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }.start();
    }
}
