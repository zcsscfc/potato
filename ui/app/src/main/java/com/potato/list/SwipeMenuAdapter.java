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
    private ListAdapter listAdapter;
    private Context context;
    private OnMenuItemClickListener onMenuItemClickListener;

    public SwipeMenuAdapter(Context context, ListAdapter listAdapter) {
        this.listAdapter = listAdapter;
        this.context = context;
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
        SwipeMenuLayout layout = null;
        if (convertView == null) {
            View contentView = listAdapter.getView(position, convertView, parent);
            SwipeMenu menu = new SwipeMenu(context);
            menu.setViewType(listAdapter.getItemViewType(position));
            createMenu(menu);
            SwipeMenuView menuView = new SwipeMenuView(menu, (RefreshSwipeMenuListView) parent);
            SimpleOnSwipeItemClickListener simpleOnSwipeItemClickListener
                    = new SimpleOnSwipeItemClickListener(onMenuItemClickListener);
            menuView.setOnSwipeItemClickListener(simpleOnSwipeItemClickListener);
            RefreshSwipeMenuListView listView = (RefreshSwipeMenuListView) parent;
            layout = new SwipeMenuLayout(contentView, menuView, listView.getCloseInterpolator(),
                    listView.getOpenInterpolator());
            layout.setPosition(position);
        } else {
            layout = (SwipeMenuLayout) convertView;
            layout.closeMenu();
            layout.setPosition(position);
            listAdapter.getView(position, layout.getContentView(), parent);
        }
        return layout;
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
