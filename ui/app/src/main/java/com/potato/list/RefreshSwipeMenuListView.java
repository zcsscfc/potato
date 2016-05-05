package com.potato.list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import java.util.Date;

import com.android.potato.R;

public class RefreshSwipeMenuListView extends ListView {
    private static final int TOUCH_STATE_NONE = 0;
    private static final int TOUCH_STATE_X = 1; // x 轴触摸状态值
    private static final int TOUCH_STATE_Y = 2; // y 轴触摸状态值
    private int MAX_Y = 5;  // y 轴最大偏移量
    private int MAX_X = 3;  // x 轴最大偏移量
    private float touchX;   // 触摸 x
    private float touchY;   // 触摸 y
    private int touchState;    // 触摸状态
    private int touchPosition; // 触摸位置
    private SwipeMenuLayout swipeMenuLayout; // 滑动弹出布局
    private OnSwipeListener onSwipeListener;   // 弹出监听器
    private float firstTouchY;  // 第一次触摸 y 坐标
    private float lastTouchY;   // 最后一次触摸 y 坐标
    private SwipeMenuCreator swipeMenuCreator; // 创建左滑菜单接口
    private OnMenuItemClickListener onMenuItemClickListener; // 菜单点击事件
    private Interpolator interpolatorClose; // 关闭菜单动画修饰 Interpolator
    private Interpolator interpolatorOpen; // 开启菜单动画修饰 Interpolator
    private float lastY = -1;
    private Scroller scroller;
    private OnScrollListener onScrollListener; // 滑动监听
    private OnRefreshListener onRefreshListener; // 下拉上拉监听器
    private RefreshListHeader headerView; // 下拉头
    private RelativeLayout headerViewContent; // 头部视图内容，用来计算头部高度，不下拉时隐藏
    private TextView headerViewTime; // 下拉时间文本控件
    private int headerViewHeight; // 头部高度
    private boolean mEnablePullRefresh = true; // 能否下拉刷新
    private boolean refreshing = false; // 是否正在刷新
    private LinearLayout footerView; // 上拉尾部视图
    private boolean enablePullLoad;// 是否可以上拉加载
    private boolean loading;   // 是否正在上拉
    private boolean isFooterReady = false;
    private int totalItemCount;
    private int mScrollBack;
    private final static int SCROLLBACK_HEADER = 0;
    private final static int SCROLLBACK_FOOTER = 1;
    private final static int SCROLL_DURATION = 400;
    private final static int PULL_LOAD_MORE_DELTA = 50;
    private final static float OFFSET_RADIO = 1.8f;
    private boolean isFooterVisible = false;

    public static final int MODE_HEADER = 0; // 下拉
    public static final int MODE_FOOTER = 1; // 上拉
    public static final int MODE_BOTH = 2; // 上拉和下拉
    public static String tag; // ListView 的动作
    public static final String REFRESH = "refresh";
    public static final String LOAD = "load";

    public RefreshSwipeMenuListView(Context context) {
        super(context);
        Initial(context);
    }

    public RefreshSwipeMenuListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Initial(context);
    }

    public RefreshSwipeMenuListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Initial(context);
    }

    private void Initial(Context context) {
        scroller = new Scroller(context, new DecelerateInterpolator());
        SimpleOnScrollListener simpleOnScrollListener = new SimpleOnScrollListener(
                onScrollListener, totalItemCount, isFooterVisible
        );
        super.setOnScrollListener(simpleOnScrollListener);
        headerView = new RefreshListHeader(context); // 初始化头部视图
        headerViewContent = (RelativeLayout) headerView.findViewById(R.id.xlistview_header_content);
        headerViewTime = (TextView) headerView.findViewById(R.id.xlistview_header_time);
        addHeaderView(headerView);
        headerView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() { // 初始化头部高度
                headerViewHeight = headerViewContent.getHeight();
                // 向 ViewTreeObserver 注册方法，以获取控件尺寸
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
        MAX_X = Utility.dp2px(MAX_X, getContext());
        MAX_Y = Utility.dp2px(MAX_Y, getContext());
        touchState = TOUCH_STATE_NONE;

        footerView = (LinearLayout) LayoutInflater.from(context).inflate( // 初始化尾部视图
                R.layout.xlistview_footer, null, false);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        if (isFooterReady == false) {  // 添加尾部隐藏
            isFooterReady = true;
            addFooterView(footerView);
            footerView.setVisibility(GONE);
        }
        super.setAdapter(new SwipeMenuAdapter(getContext(), adapter) {
            @Override
            public void createMenu(SwipeMenu menu) { // 创建左滑菜单
                if (swipeMenuCreator != null) {
                    swipeMenuCreator.create(menu);
                }
            }
//            @Override
//            public void onItemClick(SwipeMenuView view, SwipeMenu menu, int index) {
//                if (onMenuItemClickListener != null) { // 左滑菜单点击事件
//                    onMenuItemClickListener.onMenuItemClick(view.getPosition(), menu, index);
//                }
//                if (swipeMenuLayout != null) {
//                    swipeMenuLayout.smoothCloseMenu();
//                }
//            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (lastY == -1) { // 获取上次 y 轴坐标
            lastY = ev.getRawY();
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: // 手势按下事件、获取坐标、设置上次下拉时间
                firstTouchY = ev.getRawY();
                lastY = ev.getRawY();
                setRefreshTime(RefreshTime.getRefreshTime(getContext()));
                int oldPos = touchPosition;
                touchX = ev.getX();
                touchY = ev.getY();
                touchState = TOUCH_STATE_NONE;
                touchPosition = pointToPosition((int) ev.getX(), (int) ev.getY());
                // 弹出左滑菜单
                if (touchPosition == oldPos && swipeMenuLayout != null && swipeMenuLayout.isOpen()) {
                    touchState = TOUCH_STATE_X;
                    swipeMenuLayout.onSwipe(ev); // 左滑菜单手势监听事件，根据滑动距离弹出菜单
                    return true;
                }
                // 获取 item view，此方法是因为 getChildAt() 传入 index 值导致 list view 不可见的 item 会报空指针
                // 防止 list view 不可见的 item 获取到的为空，使用下面方法
                View view = getChildAt(touchPosition - getFirstVisiblePosition());
                if (swipeMenuLayout != null && swipeMenuLayout.isOpen()) { // 如果滑动的 item 不为空并且已经开启，则关闭该菜单
                    swipeMenuLayout.smoothCloseMenu();
                    swipeMenuLayout = null;
                    return super.onTouchEvent(ev);
                }
                if (swipeMenuLayout != null) { // 否则打开左滑菜单
                    swipeMenuLayout.onSwipe(ev);
                }
                if (view instanceof SwipeMenuLayout) {
                    swipeMenuLayout = (SwipeMenuLayout) view;
                }
                break;
            case MotionEvent.ACTION_MOVE: // 手势滑动事件
                final float deltaY = ev.getRawY() - lastY;
                float dy = Math.abs((ev.getY() - touchY));
                float dx = Math.abs((ev.getX() - touchX));
                lastY = ev.getRawY();
                // 判断左滑菜单是否未激活、或者 x 轴偏移平方小于 y 轴偏移平方 3 倍的时候
                if ((swipeMenuLayout == null || !swipeMenuLayout.isActive()) && Math.pow(dx, 2) / Math.pow(dy, 2) <= 3) {
                    // 判断第一个可见位置并且头部布局可见高度大于 0 时或者 y 轴偏移量 > 0
                    if (getFirstVisiblePosition() == 0 && (headerView.getVisibleHeight() > 0 || deltaY > 0)) {
                        // 重新更新头部高度
                        updateHeaderHeight(deltaY / OFFSET_RADIO);
                        invokeOnScrolling();
                    }
                }
                if (touchState == TOUCH_STATE_X) { // 如果 x 轴偏移弹出左滑菜单
                    if (swipeMenuLayout != null) {
                        swipeMenuLayout.onSwipe(ev);
                    }
                    getSelector().setState(new int[]{0});
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                    super.onTouchEvent(ev);
                    return true;
                } else if (touchState == TOUCH_STATE_NONE) {
                    if (Math.abs(dy) > MAX_Y) { // 如果 y 轴偏移量 > 指定 y 轴偏移量，设置 y 轴偏移状态
                        touchState = TOUCH_STATE_Y;
                    } else if (dx > MAX_X) { // 如果 x 轴偏移量 > 指定 x 轴偏移量，设置 x 轴偏移状态，开始弹出左滑菜单
                        touchState = TOUCH_STATE_X;
                        if (onSwipeListener != null) {
                            onSwipeListener.onSwipeStart(touchPosition);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP: // 手势抬起事件
                lastY = -1; // reset
                if (getFirstVisiblePosition() == 0) {
                    // 设置下拉刷新状态值，开启下拉刷新状态
                    if (mEnablePullRefresh && headerView.getVisibleHeight() > headerViewHeight) {
                        refreshing = true;
                        headerView.SetState(RefreshListHeader.STATE_REFRESHING);
                        if (onRefreshListener != null) {
                            tag = REFRESH;
                            onRefreshListener.onRefresh();
                        }
                    }
                    resetHeaderHeight();
                }
                lastTouchY = ev.getRawY(); // 获取上次 y 轴偏移量
                if (CanLoadHistory()) { // 判断是否满足上拉
                    LoadMoreData();
                }
                if (touchState == TOUCH_STATE_X) { // 如果为 x 轴偏移状态，开启左滑
                    if (swipeMenuLayout != null) {
                        swipeMenuLayout.onSwipe(ev);
                        if (!swipeMenuLayout.isOpen()) {
                            touchPosition = -1;
                            swipeMenuLayout = null;
                        }
                    }
                    if (onSwipeListener != null) {
                        onSwipeListener.onSwipeEnd(touchPosition);
                    }
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                    super.onTouchEvent(ev);
                    return true;
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void setMenuCreator(SwipeMenuCreator menuCreator) {
        this.swipeMenuCreator = menuCreator;
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.onMenuItemClickListener = onMenuItemClickListener;
    }

    public void setOnSwipeListener(OnSwipeListener onSwipeListener) {
        this.onSwipeListener = onSwipeListener;
    }

    public static interface OnSwipeListener {
        void onSwipeStart(int position);

        void onSwipeEnd(int position);
    }

    /**
     * 设置刷新可用
     *
     * @param enable
     */
    private void SetPullRefreshEnable(boolean enable) {
        mEnablePullRefresh = enable;
        if (!mEnablePullRefresh) { // disable, hide the content
            headerViewContent.setVisibility(View.INVISIBLE);
        } else {
            headerViewContent.setVisibility(View.VISIBLE);
        }
    }

    private void SetPullLoadEnable(boolean enable) {
        enablePullLoad = enable;
        if (enablePullLoad) {
            loading = false;
            footerView.setVisibility(VISIBLE);
            footerView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    StartLoading();
                }
            });
        } else {
            footerView.setVisibility(GONE);
            footerView.setOnClickListener(null);
        }
    }

    private void StopRefresh() {
        if (refreshing == true) {
            refreshing = false;
            resetHeaderHeight();
        }
    }

    private void StopLoadMore() {
        if (loading == true) {
            loading = false;
            footerView.setVisibility(GONE);
        }
    }

    /**
     * set last refresh time
     *
     * @param time
     */
    public void setRefreshTime(String time) {
        headerViewTime.setText(time);
    }

    private void invokeOnScrolling() {
        if (onScrollListener instanceof OnXScrollListener) {
            OnXScrollListener l = (OnXScrollListener) onScrollListener;
            l.onXScrolling(this);
        }
    }

    /**
     * 更新头部高度，设置状态值
     *
     * @param delta
     */
    private void updateHeaderHeight(float delta) {
        headerView.setVisibleHeight((int) delta + headerView.getVisibleHeight());
        if (mEnablePullRefresh && !refreshing) {
            if (headerView.getVisibleHeight() > headerViewHeight) {
                headerView.SetState(RefreshListHeader.STATE_READY);
            } else {
                headerView.SetState(RefreshListHeader.STATE_NORMAL);
            }
        }
        setSelection(0); // scroll to top each time
    }

    /**
     * 重置头部视图高度
     */
    private void resetHeaderHeight() {
        int height = headerView.getVisibleHeight();
        if (height == 0) // 不可见
            return;
        // 如果正在刷新并且头部高度没有完全显示不做操作
        if (refreshing && height <= headerViewHeight) {
            return;
        }
        int finalHeight = 0; // 默认
        //如果正在刷新并且滑动高度大于头部高度
        if (refreshing && height > headerViewHeight) {
            finalHeight = headerViewHeight;
        }
        mScrollBack = SCROLLBACK_HEADER;
        scroller.startScroll(0, height, 0, finalHeight - height, SCROLL_DURATION);
        // 触发computescroll
        invalidate();
    }

    private void StartLoading() {
        loading = true;
        footerView.setVisibility(VISIBLE);
        if (onRefreshListener != null) {
            tag = LOAD;
            onRefreshListener.onLoadMore();
        }
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            if (mScrollBack == SCROLLBACK_HEADER) {
                headerView.setVisibleHeight(scroller.getCurrY());
            }
            postInvalidate();
            invokeOnScrolling();
        }
        super.computeScroll();
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        onScrollListener = l;
    }

    public void setOnRefreshListener(OnRefreshListener l) {
        onRefreshListener = l;
    }

    /**
     * you can listen ListView.OnScrollListener or this one. it will invoke
     * onXScrolling when header/footer scroll back.
     */
    public interface OnXScrollListener extends OnScrollListener {
        public void onXScrolling(View view);
    }

    /**
     * 上拉加载和下拉刷新请求完毕
     */
    public void Complete() {
        StopLoadMore();
        StopRefresh();
        if (REFRESH.equals(tag)) {
            RefreshTime.setRefreshTime(getContext(), new Date());
        }
    }

    public void SetListViewMode(int mode) {
        if (mode == MODE_BOTH) {
            SetPullRefreshEnable(true);
            SetPullLoadEnable(true);
        } else if (mode == MODE_FOOTER) {
            SetPullLoadEnable(true);
        } else if (mode == MODE_HEADER) {
            SetPullRefreshEnable(true);
        }
    }

    /**
     * 判断是否可以上蜡加载
     *
     * @return
     */
    private boolean CanLoadHistory() {
        return isBottom() && !loading && isPullingUp();
    }

    /**
     * 判断是否到达底部
     *
     * @return
     */
    private boolean isBottom() {
        if (getCount() > 0) {
            if (getLastVisiblePosition() == getAdapter().getCount() - 1 &&
                    getChildAt(getChildCount() - 1).getBottom() <= getHeight()) {
                return true;
            }
        }
        return false;
    }

    private boolean isPullingUp() {
        return (firstTouchY - lastTouchY) >= 200;
    }

    private void LoadMoreData() {
        if (onRefreshListener != null) {
            setLoading(true);
        } else {
            setLoading(false);
        }
    }

    public void setLoading(boolean loading) {
        if (this == null) return;
        this.loading = loading;
        if (loading) {
            footerView.setVisibility(VISIBLE);
            setSelection(getAdapter().getCount() - 1);
            onRefreshListener.onLoadMore();
        } else {
            footerView.setVisibility(GONE);
            firstTouchY = 0;
            lastTouchY = 0;
        }
    }

    public void setCloseInterpolator(Interpolator interpolator) {
        this.interpolatorClose = interpolator;
    }

    public void setOpenInterpolator(Interpolator interpolator) {
        this.interpolatorOpen = interpolator;
    }

    public Interpolator getOpenInterpolator() {
        return interpolatorOpen;
    }

    public Interpolator getCloseInterpolator() {
        return interpolatorClose;
    }
}