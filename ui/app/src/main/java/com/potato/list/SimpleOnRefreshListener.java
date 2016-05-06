package com.potato.list;

import android.view.Gravity;
import android.widget.Toast;

import com.android.potato.PotatoApplication;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SimpleOnRefreshListener implements OnRefreshListener {
    private RefreshSwipeMenuListView refreshSwipeMenuListView;
    private List<PostItem> postItemList;
    private PostItemListAdapter postItemListAdapter;

    public SimpleOnRefreshListener(RefreshSwipeMenuListView refreshSwipeMenuListView,
                                   List<PostItem> postItemList, PostItemListAdapter postItemListAdapter) {
        this.refreshSwipeMenuListView = refreshSwipeMenuListView;
        this.postItemList = postItemList;
        this.postItemListAdapter = postItemListAdapter;
    }

    @Override
    public void onRefresh() {
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
                Toast toast = Toast.makeText(PotatoApplication.getInstance(), "下拉刷新完成", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }, 2000);
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
        }, 2000);
    }
}
