package com.android.potato;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by S1011001 on 2016/4/26.
 */
public class InfoFragment extends Fragment implements ViewPager.OnPageChangeListener {

    private View view;
    ViewPager viewPager;
    private List<Fragment> fragmentList;
    private MyFragmentStatePagerAdapter mPagerAdater;
    private String[] titles = new String[]{"推荐", "热门", "种植", "养殖", "水产", "市场"};

    /**
     * 当前选择的分类
     */
    private int mCurClassIndex = 0;
    /**
     * 选择的分类字体颜色
     */
    private int mColorSelected;
    /**
     * 非选择的分类字体颜色
     */
    private int mColorUnSelected;
    /**
     * 水平滚动的Tab容器
     */
    private HorizontalScrollView horizontalScrollView;
    /**
     * 分类导航的容器
     */
    private ViewGroup viewGroup;
    /**
     * 水平滚动X
     */
    private int mScrollX = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.info_fragment_layout, container, false);
            viewPager = (ViewPager) view.findViewById(R.id.viewPager);
            horizontalScrollView = (HorizontalScrollView) view.findViewById(R.id.horizontalScrollView);
            viewGroup = (ViewGroup) view.findViewById(R.id.linearLayoutTabContainer);
            initValidata();
        }
        return view;
    }

    private void initValidata() {
        mColorSelected = Color.RED;
        mColorUnSelected = Color.parseColor("#ffffff");
        AddTabTitle(titles);
        horizontalScrollView.post(new Runnable() {
            @Override
            public void run() {
                horizontalScrollView.scrollTo(mScrollX, 0);
            }
        });
        fragmentList = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            OneFragment oneFragment = new OneFragment();
            Bundle bundle = new Bundle();
            bundle.putString("extra", titles[i]);
            oneFragment.setArguments(bundle);
            fragmentList.add(oneFragment);
        }

        mPagerAdater = new MyFragmentStatePagerAdapter(getChildFragmentManager());
        mPagerAdater.setTitles(titles);
        mPagerAdater.setFragmentList(fragmentList);
        viewPager.setAdapter(mPagerAdater);
        viewPager.addOnPageChangeListener(this);
    }

    private void AddTabTitle(String[] titles) {
        LayoutInflater layoutInflater = LayoutInflater.from(PotatoApplication.getInstance());
        final int len = titles.length;
        for (int i = 0; i < len; i++) {
            final String title = titles[i];
            final View view = layoutInflater.inflate(R.layout.horizontal_item_layout, null);
            final LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.horizontal_linearlayout_type);
            final ImageView img_type = (ImageView) view.findViewById(R.id.horizontal_img_type);
            final TextView type_name = (TextView) view.findViewById(R.id.horizontal_tv_type);
            type_name.setText(title);
            if (i == mCurClassIndex) {
                //已经选中
                type_name.setTextColor(mColorSelected);
                img_type.setImageResource(R.drawable.bottom_line_blue);
            } else {
                //未选中
                type_name.setTextColor(mColorUnSelected);
                img_type.setImageResource(R.drawable.bottom_line_gray);
            }
            final int index = i;
            //点击顶部Tab标签，动态设置下面的ViewPager页面
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //首先设置当前的Item为正常状态
                    View currentItem = viewGroup.getChildAt(mCurClassIndex);
                    ((TextView) (currentItem.findViewById(R.id.horizontal_tv_type))).setTextColor(mColorUnSelected);
                    ((ImageView) (currentItem.findViewById(R.id.horizontal_img_type))).setImageResource(R.drawable.bottom_line_gray);
                    mCurClassIndex = index;
                    //设置点击状态
                    img_type.setImageResource(R.drawable.bottom_line_blue);
                    type_name.setTextColor(mColorSelected);
                    //跳转到指定的ViewPager
                    viewPager.setCurrentItem(mCurClassIndex);
                }
            });
            viewGroup.addView(view);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //首先设置当前的Item为正常状态
        View preView = viewGroup.getChildAt(mCurClassIndex);
        ((TextView) (preView.findViewById(R.id.horizontal_tv_type))).setTextColor(mColorUnSelected);
        ((ImageView) (preView.findViewById(R.id.horizontal_img_type))).setImageResource(R.drawable.bottom_line_gray);
        mCurClassIndex = position;
        //设置当前为选中状态
        View currentItem = viewGroup.getChildAt(mCurClassIndex);
        ((ImageView) (currentItem.findViewById(R.id.horizontal_img_type))).setImageResource(R.drawable.bottom_line_blue);
        ((TextView) (currentItem.findViewById(R.id.horizontal_tv_type))).setTextColor(mColorSelected);
        //这边移动的距离 是经过计算粗略得出来的
        mScrollX = currentItem.getLeft() - 300;
        Log.d("zttjiangqq", "mScrollX:" + mScrollX);
        horizontalScrollView.post(new Runnable() {
            @Override
            public void run() {
                horizontalScrollView.scrollTo(mScrollX, 0);
            }
        });
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
