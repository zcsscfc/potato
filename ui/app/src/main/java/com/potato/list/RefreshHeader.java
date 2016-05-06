package com.potato.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.potato.R;

public class RefreshHeader extends LinearLayout {
    private LinearLayout linearLayoutRoot; // 根布局
    private ImageView imageViewArrow; // 下拉箭头图片
    private ProgressBar progressBar; // 下拉进度条
    private TextView textViewHint; // 下拉文本
    private Animation animationUp; // 抬起动画
    private Animation animationDown; // 下拉动画
    private final int ROTATE_ANIM_DURATION = 180;
    private int state = STATE_NORMAL; // 状态值 0 - 正常 1 - 准备刷新  2 - 正在刷新
    public final static int STATE_NORMAL = 0;
    public final static int STATE_READY = 1;
    public final static int STATE_REFRESHING = 2;

    public RefreshHeader(Context context) {
        super(context);
        Initial(context);
    }

    public RefreshHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        Initial(context);
    }

    private void Initial(Context context) {
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
        linearLayoutRoot = (LinearLayout) LayoutInflater.from(context)
                .inflate(R.layout.refresh_header, null);
        addView(linearLayoutRoot, layoutParams);
        //setGravity(Gravity.BOTTOM);

        imageViewArrow = (ImageView) findViewById(R.id.imageViewArrow);
        textViewHint = (TextView) findViewById(R.id.textViewHint);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        animationUp = new RotateAnimation(0.0f, -180.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f); // 设置抬起动画
        animationUp.setDuration(ROTATE_ANIM_DURATION);
        animationUp.setFillAfter(true);

        animationDown = new RotateAnimation(-180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f); // 设置下拉动画
        animationDown.setDuration(ROTATE_ANIM_DURATION);
        animationDown.setFillAfter(true);
    }

    public void SetState(int state) {
        if (state == this.state)
            return;
        switch (state) {
            case STATE_NORMAL:
                imageViewArrow.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                if (this.state == STATE_READY) {
                    imageViewArrow.startAnimation(animationDown);
                }
                if (this.state == STATE_REFRESHING) {
                    imageViewArrow.clearAnimation();
                }
                textViewHint.setText("下拉刷新");
                break;
            case STATE_READY:
                imageViewArrow.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                imageViewArrow.clearAnimation();
                imageViewArrow.startAnimation(animationUp);
                textViewHint.setText("松开刷新数据");
                break;
            case STATE_REFRESHING:
                imageViewArrow.clearAnimation();
                imageViewArrow.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                textViewHint.setText("正在加载...");
                break;
            default:
        }
        this.state = state;
    }

    public void SetVisibleHeight(int height) {
        if (height < 0)
            height = 0;
        LayoutParams layoutParams = (LayoutParams) linearLayoutRoot.getLayoutParams();
        layoutParams.height = height;
        linearLayoutRoot.setLayoutParams(layoutParams);
    }

    public int GetVisibleHeight() {
        return linearLayoutRoot.getHeight();
    }
}