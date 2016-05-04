package com.potato.list;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.android.potato.PotatoApplication;
import com.android.potato.R;

public class SimpleSwipeMenu implements SwipeMenuCreator {
    @Override
    public void create(SwipeMenu menu) {
        Context context = PotatoApplication.getInstance();
        SwipeMenuItem swipeMenuItemTop = new SwipeMenuItem(context);
        swipeMenuItemTop.setBackground(new ColorDrawable(context.getResources().getColor(R.color.top)));
        swipeMenuItemTop.setWidth(Utility.dp2px(80, context));
        swipeMenuItemTop.setTitle("置顶");
        swipeMenuItemTop.setTitleSize(16);
        swipeMenuItemTop.setTitleColor(Color.WHITE);
        menu.addMenuItem(swipeMenuItemTop);
        SwipeMenuItem swipeMenuItemDelete = new SwipeMenuItem(context);
        swipeMenuItemDelete.setBackground(new ColorDrawable(context.getResources().getColor(R.color.del)));
        swipeMenuItemDelete.setWidth(Utility.dp2px(80, context));
        swipeMenuItemDelete.setTitle("删除");
        swipeMenuItemDelete.setTitleSize(16);
        swipeMenuItemDelete.setTitleColor(Color.WHITE);
        menu.addMenuItem(swipeMenuItemDelete);
    }
}
