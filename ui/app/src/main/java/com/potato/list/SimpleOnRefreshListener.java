package com.potato.list;

import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.android.potato.AppConfig;
import com.android.potato.PotatoApplication;
import com.google.gson.Gson;
import com.potato.model.PostMain;
import com.potato.model.PostMainData;
import com.potato.model.PostMainRequest;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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
                    PostMainRequest postMainRequest = new PostMainRequest();
                    postMainRequest.setNum(20);
                    int size = postItemList.size();
                    if (size > 0) {
                        String firstPostId = postItemList.get(0).getPostId();
                        postMainRequest.setFirst_post_id(Long.parseLong(firstPostId));
                    }
                    String json = new Gson().toJson(postMainRequest, PostMainRequest.class);
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    OkHttpClient okHttpClient = new OkHttpClient();
                    RequestBody body = RequestBody.create(JSON, json);
                    Request request = new Request.Builder()
                            .url(AppConfig.SERVER_URL + "postm/")
                            .post(body).build();
                    Response response = okHttpClient.newCall(request).execute();
                    String rspStr = response.body().string();
                    Message msg = Message.obtain();
                    msg.arg1 = MSG_POST_M_REFRESH;
                    msg.arg2 = showToast;
                    msg.obj = rspStr;
                    msgHandler.sendMessage(msg);
                } catch (Exception ex) {
                    Toast.makeText(PotatoApplication.getInstance(),
                            "error onRefresh:" + ex.toString(), Toast.LENGTH_SHORT).show();
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
                    PostMainRequest postMainRequest = new PostMainRequest();
                    postMainRequest.setNum(5);
                    int size = postItemList.size();
                    if (size > 0) {
                        String lastPostId = postItemList.get(size - 1).getPostId();
                        postMainRequest.setLast_post_id(Long.parseLong(lastPostId));
                    }
                    String json = new Gson().toJson(postMainRequest, PostMainRequest.class);
                    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                    OkHttpClient okHttpClient = new OkHttpClient();
                    RequestBody body = RequestBody.create(JSON, json);
                    Request request = new Request.Builder()
                            .url(AppConfig.SERVER_URL + "postm/")
                            .post(body).build();
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
                int size = postMainList.size() - 1;
                int size2 = postItemList.size();
                int count = 0;
                for (int i = size; i >= 0; i--) {
                    count = count + 1;
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
                    postItemList.add(0, postItem);
                    int n = size2 + count - 20;
                    if (n > 0) {
                        postItemList.remove(20);
                    }
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
                        "error HandleOnRefresh:" + ex.toString(), Toast.LENGTH_SHORT).show();
            }
        }

        private void HandleOnLoad(android.os.Message msg) {
            try {
                String jsonStr = (String) msg.obj;
                PostMainData postMainData = new Gson().fromJson(jsonStr, PostMainData.class);
                List<PostMain> postMainList = postMainData.getData();
                int size = postMainList.size();
                int size2 = postItemList.size();
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
                    int n = size2 + i + 1 - 20;
                    if (n > 0) {
                        postItemList.remove(0);
                    }
                }
                refreshSwipeMenuListView.Complete();
                postItemListAdapter.notifyDataSetChanged();
                Toast toast = Toast.makeText(PotatoApplication.getInstance(),
                        "加载成功", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } catch (Exception ex) {
                Toast.makeText(PotatoApplication.getInstance(),
                        "error:" + ex.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
