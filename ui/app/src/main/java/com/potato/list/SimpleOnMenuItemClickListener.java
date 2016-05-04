package com.potato.list;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.android.potato.PotatoApplication;
import com.android.potato.R;

import java.util.List;

public class SimpleOnMenuItemClickListener implements IOnMenuItemClickListener {
    private RefreshSwipeMenuListView refreshSwipeMenuListView;
    private List<PostItem> postItemList;
    private PostItemListAdapter postItemListAdapter;

    public SimpleOnMenuItemClickListener(RefreshSwipeMenuListView refreshSwipeMenuListView,
                                         List<PostItem> postItemList, PostItemListAdapter postItemListAdapter) {
        this.refreshSwipeMenuListView = refreshSwipeMenuListView;
        this.postItemList = postItemList;
        this.postItemListAdapter = postItemListAdapter;
    }

    @Override
    public void onMenuItemClick(int position, SwipeMenu menu, int index) {
        switch (index) {
            case 0: //第一个选项
                Toast.makeText(PotatoApplication.getInstance(), "您点击的是置顶",
                        Toast.LENGTH_SHORT).show();
                break;
            case 1: //第二个选项
                del(position, refreshSwipeMenuListView
                        .getChildAt(position + 1 - refreshSwipeMenuListView.getFirstVisiblePosition()));
                break;

        }
    }

    private void del(final int index, View v) {
        final Animation animation = (Animation) AnimationUtils.loadAnimation(v.getContext(), R.anim.list_anim);
        animation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                postItemList.remove(index);
                postItemListAdapter.notifyDataSetChanged();
                animation.cancel();
            }
        });
        v.startAnimation(animation);
    }
}
