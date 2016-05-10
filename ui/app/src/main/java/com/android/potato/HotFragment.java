package com.android.potato;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HotFragment extends Fragment {
    private View view;
    private Context context;
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
        }
        this.context = PotatoApplication.getInstance();
        postItemList = InitialTestData();
        postItemListAdapter = new PostItemListAdapter(context, postItemList);

        refreshSwipeMenuListView = (RefreshSwipeMenuListView) view.findViewById(R.id.refreshSwipeMenuListView);

        refreshSwipeMenuListView.setAdapter(postItemListAdapter);

        //GO TO DETAIL PAGE
        refreshSwipeMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getContext(), DetailActivity.class);
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

        return view;
    }

    private List<PostItem> InitialTestData() {
        List<PostItem> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            PostItem postItem = new PostItem();
            if (i % 2 == 0) {
                postItem.setTitle("发明专利：新疆理化所栽培出食用翘鳞环锈伞菌种");
                postItem.setOrigin("中国农业技术网");
            } else {
                postItem.setTitle("单坡联合双列式育肥暖棚猪舍");
                postItem.setOrigin("中国养殖网");
            }
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
            String str = sdf.format(date);
            postItem.setTime(str);
            list.add(postItem);
        }
        return list;
    }
}
