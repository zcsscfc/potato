package com.potato.list;

public class SimpleOnSwipeItemClickListener implements OnSwipeItemClickListener {
    private OnMenuItemClickListener onMenuItemClickListener;

    public SimpleOnSwipeItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.onMenuItemClickListener = onMenuItemClickListener;
    }

    @Override
    public void onItemClick(SwipeMenuView view, SwipeMenu menu, int index) {
        if (onMenuItemClickListener != null) {
            onMenuItemClickListener.onMenuItemClick(view.getPosition(), menu, index);
        }
    }
}
