package com.potato.list;

import android.widget.Toast;

import com.android.potato.PotatoApplication;

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
                refreshSwipeMenuListView.complete();
                Toast.makeText(PotatoApplication.getInstance(), "已完成", Toast.LENGTH_SHORT).show();
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {
        refreshSwipeMenuListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    PostItem msgBean = new PostItem();
                    msgBean.setTitle("张某某" + i);
                    msgBean.setOrigin("你好，在么？" + i);
                    msgBean.setTime("上午10:30");
                    postItemList.add(msgBean);
                }
                refreshSwipeMenuListView.complete();
                postItemListAdapter.notifyDataSetChanged();
            }
        }, 2000);
    }
}
