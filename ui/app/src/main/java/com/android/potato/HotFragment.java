package com.android.potato;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.potato.list.SimpleOnRefreshListener;
import com.potato.list.PostItemListAdapter;
import com.potato.list.PostItem;
import com.potato.list.RefreshSwipeMenuListView;
import com.potato.list.SimpleOnMenuItemClickListener;
import com.potato.list.SimpleSwipeMenu;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HotFragment extends Fragment {
    private View view;
    private List<PostItem> postItemList;
    private PostItemListAdapter postItemListAdapter;
    private RefreshSwipeMenuListView refreshSwipeMenuListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.hot_fragment_layout, container, false);
            Bundle bundle = getArguments();
            if (bundle != null) {
                bundle.getString("extra");
            }

            postItemList = new ArrayList<>();
            postItemListAdapter = new PostItemListAdapter(PotatoApplication.getInstance(), postItemList);

            refreshSwipeMenuListView = (RefreshSwipeMenuListView) view.findViewById(R.id.refreshSwipeMenuListView);

            refreshSwipeMenuListView.setAdapter(postItemListAdapter);

            refreshSwipeMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                        long arg3) {
                    if (arg3 == -1) { // id is headerView or footerView
                        return;
                    }
                    int realPosition = (int) arg3;
                    PostItem postItem = (PostItem) postItemListAdapter.getItem(realPosition);
                    Intent intent = new Intent(getContext(), DetailActivity.class);
                    intent.putExtra("postItem", (Serializable) postItem);
                    startActivity(intent);
                }
            });

            refreshSwipeMenuListView.SetListViewMode(RefreshSwipeMenuListView.MODE_BOTH);

            SimpleOnRefreshListener simpleOnRefreshListener = new SimpleOnRefreshListener(
                    refreshSwipeMenuListView, postItemList, postItemListAdapter
            );
            refreshSwipeMenuListView.setOnRefreshListener(simpleOnRefreshListener);

            SimpleSwipeMenu simpleSwipeMenu = new SimpleSwipeMenu();
            refreshSwipeMenuListView.setMenuCreator(simpleSwipeMenu);

            SimpleOnMenuItemClickListener simpleOnMenuItemClickListener = new SimpleOnMenuItemClickListener(
                    refreshSwipeMenuListView, postItemList, postItemListAdapter
            );
            refreshSwipeMenuListView.setOnMenuItemClickListener(simpleOnMenuItemClickListener);
        }
        return view;
    }
}
