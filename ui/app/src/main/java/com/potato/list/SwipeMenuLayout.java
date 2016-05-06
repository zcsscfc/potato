package com.potato.list;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.FrameLayout;

@SuppressWarnings("all")
public class SwipeMenuLayout extends FrameLayout {
    private static final int CONTENT_VIEW_ID = 1;
    private static final int MENU_VIEW_ID = 2;
    private static final int STATE_CLOSE = 0;
    private static final int STATE_OPEN = 1;

    private View contentView;
    private SwipeMenuView swipeMenuView;
    private int downX;
    private int state = STATE_CLOSE;
    private OnGestureListener onGestureListener;
    private GestureDetectorCompat gestureDetectorCompat;
    private boolean isFling;
    private int MIN_FLING = Utility.dp2px(15, getContext());
    private int MAX_VELOCITYX = -Utility.dp2px(500, getContext());
    private ScrollerCompat scrollerCompatOpen;
    private ScrollerCompat scrollerCompatClose;
    private int baseX;
    private int position;
    private Interpolator interpolatorOpen;
    private Interpolator interpolatorClose;

    private SwipeMenuLayout(Context context) {
        super(context);
    }

    private SwipeMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwipeMenuLayout(View contentView, SwipeMenuView swipeMenuView) {
        this(contentView, swipeMenuView, null, null);
    }

    public SwipeMenuLayout(View contentView, SwipeMenuView swipeMenuView,
                           Interpolator interpolatorClose,
                           Interpolator interpolatorOpen) {
        super(contentView.getContext());
        this.interpolatorClose = interpolatorClose;
        this.interpolatorOpen = interpolatorOpen;
        this.contentView = contentView;
        this.swipeMenuView = swipeMenuView;
        this.swipeMenuView.setLayout(this);
        Initial();
    }

    private void Initial() {
        setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        onGestureListener = new SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                isFling = false;
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1 != null && e2 != null) {
                    if ((e1.getX() - e2.getX()) > MIN_FLING && velocityX < MAX_VELOCITYX) {
                        isFling = true;
                    }
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        };
        gestureDetectorCompat = new GestureDetectorCompat(getContext(), onGestureListener);
        if (interpolatorClose != null) {
            scrollerCompatClose = ScrollerCompat.create(getContext(), interpolatorClose);
        } else {
            scrollerCompatClose = ScrollerCompat.create(getContext());
        }
        if (interpolatorOpen != null) {
            scrollerCompatOpen = ScrollerCompat.create(getContext(), interpolatorOpen);
        } else {
            scrollerCompatOpen = ScrollerCompat.create(getContext());
        }
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        contentView.setLayoutParams(layoutParams);
        if (contentView.getId() < 1) {
            contentView.setId(CONTENT_VIEW_ID);
        }
        swipeMenuView.setId(MENU_VIEW_ID);
        swipeMenuView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        addView(contentView);
        addView(swipeMenuView);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public boolean onSwipe(MotionEvent event) {
        gestureDetectorCompat.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                isFling = false;
                break;
            case MotionEvent.ACTION_MOVE:
                int distance = (int) (downX - event.getX());
                if (state == STATE_OPEN) {
                    distance += swipeMenuView.getWidth();
                }
                swipe(distance);
                break;
            case MotionEvent.ACTION_UP:
                if (isFling || (downX - event.getX()) > (swipeMenuView.getWidth() / 2)) {
                    smoothOpenMenu();
                } else {
                    smoothCloseMenu();
                    return false;
                }
                break;
        }
        return true;
    }

    public boolean isOpen() {
        return state == STATE_OPEN;
    }

    public boolean isActive() {
        if (contentView != null) {
            return contentView.getLeft() != 0;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    private void swipe(int dis) {
        if (dis > swipeMenuView.getWidth()) {
            dis = swipeMenuView.getWidth();
        }
        if (dis < 0) {
            dis = 0;
        }
        contentView.layout(-dis, contentView.getTop(), contentView.getWidth() - dis, getMeasuredHeight());
        swipeMenuView.layout(contentView.getWidth() - dis, swipeMenuView.getTop(),
                contentView.getWidth() + swipeMenuView.getWidth() - dis, swipeMenuView.getBottom());
    }

    @Override
    public void computeScroll() {
        if (state == STATE_OPEN) {
            if (scrollerCompatOpen.computeScrollOffset()) {
                swipe(scrollerCompatOpen.getCurrX());
                postInvalidate();
            }
        } else {
            if (scrollerCompatClose.computeScrollOffset()) {
                swipe(baseX - scrollerCompatClose.getCurrX());
                postInvalidate();
            }
        }
    }

    public void smoothCloseMenu() {
        state = STATE_CLOSE;
        baseX = -contentView.getLeft();
        scrollerCompatClose.startScroll(0, 0, baseX, 0, 350);
        postInvalidate();
    }

    public void smoothOpenMenu() {
        state = STATE_OPEN;
        scrollerCompatOpen.startScroll(-contentView.getLeft(), 0, swipeMenuView.getWidth(), 0, 350);
        postInvalidate();
    }

    public void closeMenu() {
        if (scrollerCompatClose.computeScrollOffset()) {
            scrollerCompatClose.abortAnimation();
        }
        if (state == STATE_OPEN) {
            state = STATE_CLOSE;
            swipe(0);
        }
    }

    public void openMenu() {
        if (state == STATE_CLOSE) {
            state = STATE_OPEN;
            swipe(swipeMenuView.getWidth());
        }
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
        this.swipeMenuView.setPosition(position);
    }

    public View getContentView() {
        return contentView;
    }

    public SwipeMenuView getMenuView() {
        return swipeMenuView;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        swipeMenuView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        contentView.layout(0, 0, getMeasuredWidth(), contentView.getMeasuredHeight());
        swipeMenuView.layout(getMeasuredWidth(), 0, getMeasuredWidth() + swipeMenuView.getMeasuredWidth(),
                contentView.getMeasuredHeight());
    }

    public void setMenuHeight(int measuredHeight) {
        LayoutParams layoutParams = (LayoutParams) swipeMenuView.getLayoutParams();
        if (layoutParams.height != measuredHeight) {
            layoutParams.height = measuredHeight;
            swipeMenuView.setLayoutParams(swipeMenuView.getLayoutParams());
        }
    }
}
