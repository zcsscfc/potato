package com.potato.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.android.potato.R;
import com.potato.application.PotatoApplication;


import java.util.ArrayList;
import java.util.List;

public class HotFragment extends Fragment {
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.hot_fragment_layout, container, false);
            TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
            Bundle bundle = getArguments();
            if (bundle != null) {
                tv_content.setText(bundle.getString("extra"));
            }
        }
        return view;
    }

}
