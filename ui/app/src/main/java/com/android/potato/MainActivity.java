package com.android.potato;

import android.app.ActionBar;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager = null;
    PagerTabStrip pagerTabStrip = null;
    ArrayList<View> pageViewList = new ArrayList<View>();
    ArrayList<String> tabNameList = new ArrayList<String>();
    public String TAG = "tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 创建状态栏的管理实例
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // 激活状态栏设置
        tintManager.setStatusBarTintEnabled(true);
        // 激活导航栏设置
        tintManager.setNavigationBarTintEnabled(true);
        // 设置颜色
        tintManager.setStatusBarTintResource(R.color.colorPrimary);
        tintManager.setNavigationBarTintResource(R.color.colorPrimary);
        // 设置边距，保证对齐
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        //TextView textViewHello = (TextView)findViewById(R.id.textViewHello);
        //textViewHello.setPadding(0, config.getPixelInsetTop(true), 0, config.getPixelInsetBottom());

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        pagerTabStrip = (PagerTabStrip) this.findViewById(R.id.pagerTabStrip);
        pagerTabStrip.setDrawFullUnderline(false);
        pagerTabStrip.setBackgroundColor(Color.parseColor("#29B295"));
        pagerTabStrip.setTabIndicatorColor(Color.parseColor("#ffffff"));
        pagerTabStrip.setTextColor(Color.parseColor("#ffffff"));
        pagerTabStrip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        View view1 = LayoutInflater.from(this).inflate(R.layout.tab1, null);
        View view2 = LayoutInflater.from(this).inflate(R.layout.tab1, null);
        View view3 = LayoutInflater.from(this).inflate(R.layout.tab1, null);
        View view4 = LayoutInflater.from(this).inflate(R.layout.tab1, null);
        View view5 = LayoutInflater.from(this).inflate(R.layout.tab1, null);
        View view6 = LayoutInflater.from(this).inflate(R.layout.tab1, null);
        pageViewList.add(view1);
        pageViewList.add(view2);
        pageViewList.add(view3);
        pageViewList.add(view4);
        pageViewList.add(view5);
        pageViewList.add(view6);
        tabNameList.add("推荐");
        tabNameList.add("热门");
        tabNameList.add("种植");
        tabNameList.add("养殖");
        tabNameList.add("水产");
        tabNameList.add("市场");

        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return pageViewList.size();
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ((ViewPager) container).addView(pageViewList.get(position));
                return pageViewList.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                ((ViewPager) container).removeView(pageViewList.get(position));
            }

            @Override
            public int getItemPosition(Object object) {
                return super.getItemPosition(object);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return tabNameList.get(position);
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            /*
            * 这个方法会在屏幕滚动过程中不断被调用。
            有三个参数，第一个position，这个参数要特别注意一下。
            当用手指滑动时，如果手指按在页面上不动，position和当前页面index是一致的；
            如果手指向左拖动（相应页面向右翻动），这时候position大部分时间和当前页面是一致的，
            只有翻页成功的情况下最后一次调用才会变为目标页面；如果手指向右拖动（相应页面向左翻动），
            这时候position大部分时间和目标页面是一致的，只有翻页不成功的情况下最后一次调用才会变为原页面。
            当直接设置setCurrentItem翻页时，
            如果是相邻的情况（比如现在是第二个页面，跳到第一或者第三个页面），
            如果页面向右翻动，大部分时间是和当前页面是一致的，只有最后才变成目标页面；
            如果向左翻动，position和目标页面是一致的。这和用手指拖动页面翻动是基本一致的。
            如果不是相邻的情况，比如我从第一个页面跳到第三个页面，position先是0，然后逐步变成1，
            然后逐步变成2；我从第三个页面跳到第一个页面，position先是1，然后逐步变成0，
            并没有出现为2的情况。
            positionOffset是当前页面滑动比例，如果页面向右翻动，这个值不断变大，
            最后在趋近1的情况后突变为0。如果页面向左翻动，这个值不断变小，最后变为0。
            positionOffsetPixels是当前页面滑动像素，变化情况和positionOffset一致。
            * */

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d(TAG, "------scrolled positon:" + position);
                Log.d(TAG, "------scrolled positionOffset:" + positionOffset);
                Log.d(TAG, "------scrolled positionOffsetPixels:" + positionOffsetPixels);
            }

            /*
            *
            * 这个方法有一个参数position，代表哪个页面被选中。
            * 当用手指滑动翻页的时候，如果翻动成功了（滑动的距离够长），
            * 手指抬起来就会立即执行这个方法，position就是当前滑动到的页面。
            * 如果直接setCurrentItem翻页，那position就和setCurrentItem的参数一致，
            * 这种情况在onPageScrolled执行方法前就会立即执行。
            * */
            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "-----Selected:" + position);
            }


            /*
           * 这个方法在手指操作屏幕的时候发生变化。
           * 有三个值：0（END）,1(PRESS) , 2(UP) 。
           当用手指滑动翻页时，手指按下去的时候会触发这个方法，state值为1，
           手指抬起时，如果发生了滑动（即使很小），这个值会变为2，然后最后变为0 。
           总共执行这个方法三次。一种特殊情况是手指按下去以后一点滑动也没有发生，
           这个时候只会调用这个方法两次，state值分别是1,0 。
           当setCurrentItem翻页时，会执行这个方法两次，state值分别为2 , 0
           * */
            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d(TAG, "-----changed:" + state);
            }
        });

    }


}
