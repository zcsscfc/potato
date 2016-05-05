package com.potato.list;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.WrapperListAdapter;

public class SwipeMenuAdapter implements WrapperListAdapter {
    private Context context;
    private ListAdapter listAdapter;
    private OnMenuItemClickListener onMenuItemClickListener;

    public SwipeMenuAdapter(Context context, ListAdapter listAdapter) {
        this.context = context;
        this.listAdapter = listAdapter;
    }

    @Override
    public int getCount() {
        return listAdapter.getCount();
    }

    @Override
    public Object getItem(int position) {
        return listAdapter.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return listAdapter.getItemId(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SwipeMenuLayout swipeMenuLayout = null;
        if (convertView == null) {
            View contentView = listAdapter.getView(position, convertView, parent);
            SwipeMenu swipeMenu = new SwipeMenu(context);
            swipeMenu.setViewType(listAdapter.getItemViewType(position));
            createMenu(swipeMenu);
            SwipeMenuView swipeMenuView = new SwipeMenuView(swipeMenu, (RefreshSwipeMenuListView) parent);
            SimpleOnSwipeItemClickListener simpleOnSwipeItemClickListener
                    = new SimpleOnSwipeItemClickListener(onMenuItemClickListener);
            swipeMenuView.setOnSwipeItemClickListener(simpleOnSwipeItemClickListener);
            RefreshSwipeMenuListView refreshSwipeMenuListView = (RefreshSwipeMenuListView) parent;
            swipeMenuLayout = new SwipeMenuLayout(contentView, swipeMenuView,
                    refreshSwipeMenuListView.getCloseInterpolator(),
                    refreshSwipeMenuListView.getOpenInterpolator());
            swipeMenuLayout.setPosition(position);
        } else {
            swipeMenuLayout = (SwipeMenuLayout) convertView;
            swipeMenuLayout.closeMenu();
            swipeMenuLayout.setPosition(position);
            listAdapter.getView(position, swipeMenuLayout.getContentView(), parent);
        }
        return swipeMenuLayout;
    }

    public void createMenu(SwipeMenu menu) {
        // Test Code
        SwipeMenuItem item = new SwipeMenuItem(context);
        item.setTitle("Item 1");
        item.setBackground(new ColorDrawable(Color.GRAY));
        item.setWidth(300);
        menu.addMenuItem(item);

        item = new SwipeMenuItem(context);
        item.setTitle("Item 2");
        item.setBackground(new ColorDrawable(Color.RED));
        item.setWidth(300);
        menu.addMenuItem(item);
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.onMenuItemClickListener = onMenuItemClickListener;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        listAdapter.registerDataSetObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        listAdapter.unregisterDataSetObserver(observer);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return listAdapter.areAllItemsEnabled();
    }

    @Override
    public boolean isEnabled(int position) {
        return listAdapter.isEnabled(position);
    }

    @Override
    public boolean hasStableIds() {
        return listAdapter.hasStableIds();
    }

    @Override
    public int getItemViewType(int position) {
        return listAdapter.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return listAdapter.getViewTypeCount();
    }

    @Override
    public boolean isEmpty() {
        return listAdapter.isEmpty();
    }

    @Override
    public ListAdapter getWrappedAdapter() {
        return listAdapter;
    }

}
