package com.potato.list;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

import com.android.potato.PotatoApplication;

public class Utility {
    public static int dp2px(int dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    private static int navigationBarHeight = -1;

    public static int getNavigationBarHeight() {
        if (navigationBarHeight == -1) {
            Resources resources = PotatoApplication.getInstance().getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            navigationBarHeight = resources.getDimensionPixelSize(resourceId);
        }
        return navigationBarHeight;
    }

    private static int statusBarHeight = -1;

    public static int getStatusBarHeight() {
        if (statusBarHeight == -1) {
            Resources resources = PotatoApplication.getInstance().getResources();
            int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
            statusBarHeight = resources.getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }
}
