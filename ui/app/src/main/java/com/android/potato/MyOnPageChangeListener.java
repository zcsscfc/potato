package com.android.potato;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by S1011001 on 2016/4/27.
 */
public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
    private ViewGroup viewGroup;
    private int position = 0;
    private int colorDefault;
    private int colorSelected;
    private int offset = 0;
    private HorizontalScrollView horizontalScrollView;

    public MyOnPageChangeListener(ViewGroup viewGroup, int position, int colorDefault,
                                  int colorSelected, int offset,
                                  HorizontalScrollView horizontalScrollView) {
        this.viewGroup = viewGroup;
        this.position = position;
        this.colorDefault = colorDefault;
        this.colorSelected = colorSelected;
        this.offset = offset;
        this.horizontalScrollView = horizontalScrollView;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        View preTab = viewGroup.getChildAt(this.position);
        ((TextView) (preTab.findViewById(R.id.textViewName))).setTextColor(colorDefault);
        ((ImageView) (preTab.findViewById(R.id.imageViewLine))).setImageResource(R.drawable.line_default);
        this.position = position;
        View currTab = viewGroup.getChildAt(this.position);
        ((ImageView) (currTab.findViewById(R.id.imageViewLine))).setImageResource(R.drawable.line_selected);
        ((TextView) (currTab.findViewById(R.id.textViewName))).setTextColor(colorSelected);
        offset = currTab.getLeft() - 300;
        horizontalScrollView.post(new Runnable() {
            @Override
            public void run() {
                horizontalScrollView.scrollTo(offset, 0);
            }
        });
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
