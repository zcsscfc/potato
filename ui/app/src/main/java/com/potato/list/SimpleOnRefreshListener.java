package com.potato.list;

import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.android.potato.PotatoApplication;
import com.google.gson.Gson;
import com.potato.model.PostMain;
import com.potato.model.PostMainList;

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
    private final int MSG_GET_POST_M_LIST = 0;

    public SimpleOnRefreshListener(RefreshSwipeMenuListView refreshSwipeMenuListView,
                                   List<PostItem> postItemList, PostItemListAdapter postItemListAdapter) {
        this.refreshSwipeMenuListView = refreshSwipeMenuListView;
        this.postItemList = postItemList;
        this.postItemListAdapter = postItemListAdapter;
        android.os.Looper looper = android.os.Looper.myLooper();
        msgHandler = new MessageHandler(looper);
    }

    @Override
    public void onRefresh() {
        new Thread() {
            @Override
            public void run() {
                try {
                    Log.e("lance", currentThread().toString());
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Request request = new Request.Builder().
                            url("http://ec2-52-196-183-18.ap-northeast-1.compute.amazonaws.com/postm/").
                            build();
                    Response response = okHttpClient.newCall(request).execute();
                    String rspStr = response.body().string();
                    android.os.Message msg = android.os.Message.obtain();
                    msg.arg1 = MSG_GET_POST_M_LIST;
                    msg.obj = rspStr;
                    msgHandler.sendMessage(msg);
                } catch (Exception ex) {
                    Log.e("lance_test", ex.toString());
                }
            }
        }.start();
    }

    @Override
    public void onLoadMore() {
        refreshSwipeMenuListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
                    SimpleDateFormat sdf2 = new SimpleDateFormat("-ss");
                    String str = sdf.format(date);
                    String str2 = sdf2.format(date);
                    PostItem postItem = new PostItem();
                    if (i % 2 == 0) {
                        postItem.setTitle("发明专利：新疆理化所栽培出食用翘鳞环锈伞菌种" + i + str2);
                        postItem.setOrigin("中国农业技术网");
                    } else {
                        postItem.setTitle("单坡联合双列式育肥暖棚猪舍");
                        postItem.setOrigin("中国养殖网");
                    }
                    postItem.setTime(str);
                    postItemList.set(i, postItem);
                }
                refreshSwipeMenuListView.Complete();
                postItemListAdapter.notifyDataSetChanged();
            }
        }, 0);
    }

    class MessageHandler extends android.os.Handler {
        public MessageHandler(android.os.Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.arg1) {
                case MSG_GET_POST_M_LIST:
                    String jsonStr = (String) msg.obj;
                    PostMainList result = new Gson().fromJson(jsonStr, PostMainList.class);
                    List<PostMain> postMainList = result.getData();
                    int size = postMainList.size();
                    if (size > 10) size = 10; // only need top 10 item
                    for (int i = 0; i < size; i++) {
                        PostMain postMain = result.getData().get(i);
                        Date date = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
                        SimpleDateFormat sdf3 = new SimpleDateFormat("MM-dd HH:mm");
                        SimpleDateFormat sdf2 = new SimpleDateFormat("-ss");
                        String str = "";
                        try {
                            str = sdf3.format(sdf.parseObject(postMain.getCreate_t()));
                        } catch (Exception ex) {
                            Log.e("lance", ex.toString());
                        }
                        String str2 = sdf2.format(date);
                        PostItem postItem = new PostItem();
                        if (i % 2 == 0) {
                            postItem.setTitle(postMain.getTitle());
                            postItem.setOrigin("中国农业技术网");
                        } else {
                            postItem.setTitle(postMain.getTitle());
                            postItem.setOrigin("中国养殖网");
                        }
                        postItem.setTime(str);
                        postItem.setPostId(postMain.getPost_id());
                        postItemList.set(i, postItem);
                    }
                    refreshSwipeMenuListView.Complete();
                    postItemListAdapter.notifyDataSetChanged();
                    Toast toast = Toast.makeText(PotatoApplication.getInstance(),
                            "刷新成功", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    break;
                default:
                    break;
            }
        }
    }
}
