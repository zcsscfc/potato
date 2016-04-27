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

public class InfoFragment extends Fragment {
    private int position = 1;
    private int colorSelected = Color.RED;
    private int colorDefault = Color.parseColor("#ffffff");
    private int offset = 0;
    private String[] titles = new String[]{"推荐", "热门", "种植", "养殖", "水产", "市场"};
    private View view;
    private ViewPager viewPager;
    private ViewGroup viewGroup;
    private List<Fragment> fragmentList;
    private MyFragmentStatePagerAdapter myFragmentStatePagerAdapter;
    private HorizontalScrollView horizontalScrollView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.info_fragment_layout, container, false);
            viewPager = (ViewPager) view.findViewById(R.id.viewPager);
            horizontalScrollView = (HorizontalScrollView) view.findViewById(R.id.horizontalScrollView);
            viewGroup = (ViewGroup) view.findViewById(R.id.linearLayoutTabContainer);
            BuildTabTitles();
            BuildTabContent();
        }
        return view;
    }

    private void BuildTabContent() {
        horizontalScrollView.post(new Runnable() {
            @Override
            public void run() {
                horizontalScrollView.scrollTo(offset, 0);
            }
        });

        fragmentList = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            HotFragment hotFragment = new HotFragment();
            Bundle bundle = new Bundle();
            bundle.putString("extra", titles[i]);
            hotFragment.setArguments(bundle);
            fragmentList.add(hotFragment);
        }

        myFragmentStatePagerAdapter = new MyFragmentStatePagerAdapter(getChildFragmentManager());
        myFragmentStatePagerAdapter.setTitles(titles);
        myFragmentStatePagerAdapter.setFragmentList(fragmentList);
        viewPager.setAdapter(myFragmentStatePagerAdapter);
        MyOnPageChangeListener myOnPageChangeListener = new MyOnPageChangeListener(
                viewGroup, position, colorDefault,
                colorSelected, offset, horizontalScrollView
        );
        viewPager.addOnPageChangeListener(myOnPageChangeListener);
        viewPager.setCurrentItem(position);
    }

    private void BuildTabTitles() {
        LayoutInflater layoutInflater = LayoutInflater.from(PotatoApplication.getInstance());
        final int len = titles.length;
        for (int i = 0; i < len; i++) {
            final String title = titles[i];
            final View tabItem = layoutInflater.inflate(R.layout.tab_item_layout, null);
            final LinearLayout linearLayoutItemContainer = (LinearLayout) tabItem.findViewById(R.id.linearLayoutItemContainer);
            final TextView textViewTabName = (TextView) tabItem.findViewById(R.id.textViewName);
            final ImageView imageViewTabImage = (ImageView) tabItem.findViewById(R.id.imageViewLine);
            textViewTabName.setText(title);
            if (i == position) {
                textViewTabName.setTextColor(colorSelected);
                imageViewTabImage.setImageResource(R.drawable.line_selected);
            } else {
                textViewTabName.setTextColor(colorDefault);
                imageViewTabImage.setImageResource(R.drawable.line_default);
            }
            final int index = i;
            tabItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View currentItem = viewGroup.getChildAt(position);
                    ((TextView) (currentItem.findViewById(R.id.textViewName))).setTextColor(colorDefault);
                    ((ImageView) (currentItem.findViewById(R.id.imageViewLine))).setImageResource(R.drawable.line_default);
                    position = index;
                    imageViewTabImage.setImageResource(R.drawable.line_selected);
                    textViewTabName.setTextColor(colorSelected);
                    viewPager.setCurrentItem(position);
                }
            });
            viewGroup.addView(tabItem);
        }
    }
}
