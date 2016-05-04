package com.potato.list;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

import java.security.PrivateKey;

public class SimpleOnScrollListener implements OnScrollListener {
    private OnScrollListener mScrollListener;
    private int mTotalItemCount;
    private boolean isFooterVisible = false;

    public SimpleOnScrollListener(OnScrollListener onScrollListener,
                                  int mTotalItemCount, boolean isFooterVisible) {
        this.mScrollListener = onScrollListener;
        this.mTotalItemCount = mTotalItemCount;
        this.isFooterVisible = isFooterVisible;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mScrollListener != null) {
            mScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mTotalItemCount = totalItemCount;
        if (mScrollListener != null) {
            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
        if (firstVisibleItem + visibleItemCount >= totalItemCount) {
            isFooterVisible = true;
        } else {
            isFooterVisible = false;
        }
    }
}
