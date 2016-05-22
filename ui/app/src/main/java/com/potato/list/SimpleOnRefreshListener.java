package com.potato.list;

import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.android.potato.PotatoApplication;
import com.google.gson.Gson;
import com.potato.model.PostMain;
import com.potato.model.PostMainData;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SimpleOnRefreshListener implements OnRefreshListener {
    private RefreshSwipeMenuListView refreshSwipeMenuListView;
    private List<PostItem> postItemList;
    private PostItemListAdapter postItemListAdapter;
    private android.os.Handler msgHandler;
    private final int MSG_POST_M_REFRESH = 0;
    private final int MSG_POST_M_LOAD = 1;

    public SimpleOnRefreshListener(RefreshSwipeMenuListView refreshSwipeMenuListView,
                                   List<PostItem> postItemList,
                                   PostItemListAdapter postItemListAdapter) {
        this.refreshSwipeMenuListView = refreshSwipeMenuListView;
        this.postItemList = postItemList;
        this.postItemListAdapter = postItemListAdapter;
        android.os.Looper looper = android.os.Looper.myLooper();
        this.msgHandler = new MessageHandler(looper);
    }

    @Override
    public void onRefresh(final int showToast) {
        new Thread() {
            @Override
            public void run() {
                try {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Request request = new Request.Builder().
                            url("http://ec2-52-196-183-18.ap-northeast-1.compute.amazonaws.com/postm/").
                            build();
                    Response response = okHttpClient.newCall(request).execute();
                    String rspStr = response.body().string();
                    android.os.Message msg = android.os.Message.obtain();
                    msg.arg1 = MSG_POST_M_REFRESH;
                    msg.arg2 = showToast;
                    msg.obj = rspStr;
                    msgHandler.sendMessage(msg);
                } catch (Exception ex) {
                    Toast.makeText(PotatoApplication.getInstance(),
                            "error:" + ex.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }.start();
    }

    @Override
    public void onLoadMore() {
        new Thread() {
            @Override
            public void run() {
                try {
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Request request = new Request.Builder().
                            url("http://ec2-52-196-183-18.ap-northeast-1.compute.amazonaws.com/postm/").
                            build();
                    Response response = okHttpClient.newCall(request).execute();
                    String rspStr = response.body().string();
                    android.os.Message msg = android.os.Message.obtain();
                    msg.arg1 = MSG_POST_M_LOAD;
                    msg.obj = rspStr;
                    msgHandler.sendMessage(msg);
                } catch (Exception ex) {
                    Toast.makeText(PotatoApplication.getInstance(),
                            "error:" + ex.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }.start();
    }

    class MessageHandler extends android.os.Handler {
        public MessageHandler(android.os.Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.arg1) {
                case MSG_POST_M_REFRESH:
                    HandleOnRefresh(msg);
                    break;
                case MSG_POST_M_LOAD:
                    HandleOnLoad(msg);
                    break;
                default:
                    break;
            }
        }

        private void HandleOnRefresh(android.os.Message msg) {
            try {
                String jsonStr = (String) msg.obj;
                PostMainData postMainData = new Gson().fromJson(jsonStr, PostMainData.class);
                List<PostMain> postMainList = postMainData.getData();
                int size = postMainList.size();
                for (int i = 0; i < size; i++) {
                    PostMain postMain = postMainData.getData().get(i);
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
                    SimpleDateFormat sdf3 = new SimpleDateFormat("MM-dd HH:mm");
                    String strCreateTime = sdf3.format(sdf.parseObject(postMain.getCreate_t()));
                    PostItem postItem = new PostItem();
                    postItem.setTitle(postMain.getTitle());
                    postItem.setOrigin(postMain.getOrigin_id());
                    postItem.setTime(strCreateTime);
                    postItem.setPostId(postMain.getPost_id());
                    postItemList.add(postItem);
                }
                refreshSwipeMenuListView.Complete();
                postItemListAdapter.notifyDataSetChanged();
                if (msg.arg2 == 1) {
                    Toast toast = Toast.makeText(PotatoApplication.getInstance(),
                            "刷新成功", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            } catch (Exception ex) {
                Toast.makeText(PotatoApplication.getInstance(),
                        "error:" + ex.toString(), Toast.LENGTH_SHORT).show();
            }
        }

        private void HandleOnLoad(android.os.Message msg) {
            try {
                String jsonStr = (String) msg.obj;
                PostMainData postMainData = new Gson().fromJson(jsonStr, PostMainData.class);
                List<PostMain> postMainList = postMainData.getData();
                int size = postMainList.size();
                for (int i = 0; i < size; i++) {
                    PostMain postMain = postMainData.getData().get(i);
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
                    SimpleDateFormat sdf3 = new SimpleDateFormat("MM-dd HH:mm");
                    String strCreateTime = sdf3.format(sdf.parseObject(postMain.getCreate_t()));
                    PostItem postItem = new PostItem();
                    postItem.setTitle(postMain.getTitle());
                    postItem.setOrigin(postMain.getOrigin_id());
                    postItem.setTime(strCreateTime);
                    postItem.setPostId(postMain.getPost_id());
                    postItemList.add(postItem);
                }
                refreshSwipeMenuListView.Complete();
                postItemListAdapter.notifyDataSetChanged();
                Toast toast = Toast.makeText(PotatoApplication.getInstance(),
                        "刷新成功", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } catch (Exception ex) {
                Toast.makeText(PotatoApplication.getInstance(),
                        "error:" + ex.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
