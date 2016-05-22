package com.potato.list;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Scroller;
import android.widget.TextView;

import java.util.Date;

import com.android.potato.R;

public class RefreshSwipeMenuListView extends ListView {
    private int touchState;    // 触摸状态
    private static final int TOUCH_STATE_NONE = 0;
    private static final int TOUCH_STATE_X = 1; // x 轴触摸状态值
    private static final int TOUCH_STATE_Y = 2; // y 轴触摸状态值
    private int MAX_Y = 5;  // y 轴最大偏移量
    private int MAX_X = 3;  // x 轴最大偏移量
    private int touchPosition; // 触摸位置
    private float touchX;   // 触摸 x
    private float touchY;   // 触摸 y
    private float firstTouchY;  // 第一次触摸 y 坐标
    private float lastTouchY;   // 最后一次触摸 y 坐标
    private SwipeMenuLayout swipeMenuLayout; // 滑动弹出布局
    private OnSwipeListener onSwipeListener;   // 弹出监听器
    private SwipeMenuCreator swipeMenuCreator; // 创建左滑菜单接口
    private OnMenuItemClickListener onMenuItemClickListener; // 菜单点击事件
    private Interpolator interpolatorClose; // 关闭菜单动画修饰 Interpolator
    private Interpolator interpolatorOpen; // 开启菜单动画修饰 Interpolator
    private float lastY = -1;
    private Scroller scroller;
    private OnScrollListener onScrollListener; // 滑动监听
    private OnRefreshListener onRefreshListener; // 下拉上拉监听器
    private RefreshHeader refreshHeader; // 下拉头
    private LinearLayout headerViewContent; // 头部视图内容，用来计算头部高度，不下拉时隐藏
    private TextView headerViewTime; // 下拉时间文本控件
    private int refreshHeaderHeight; // 头部高度
    private boolean enablePullRefresh = true; // 能否下拉刷新
    private boolean enablePullLoad = true;// 是否可以上拉加载
    private boolean refreshing = false; // 是否正在刷新
    private boolean loading = false;   // 是否正在上拉
    private LinearLayout loadFooter; // 上拉尾部视图
    private boolean isFooterReady = false;
    private int totalItemCount;
    private int scrollBack;
    private final static int SCROLL_BACK_HEADER = 0;
    private final static int SCROLL_BACK_FOOTER = 1;
    private final static int SCROLL_DURATION = 500;
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

        refreshHeader = new RefreshHeader(context);
        headerViewContent = (LinearLayout) refreshHeader.findViewById(R.id.headerViewContent);
        headerViewTime = (TextView) refreshHeader.findViewById(R.id.headerViewTime);
        addHeaderView(refreshHeader);

        refreshHeader.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                refreshHeaderHeight = headerViewContent.getHeight();
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

        MAX_X = Utility.dp2px(MAX_X, getContext());
        MAX_Y = Utility.dp2px(MAX_Y, getContext());
        touchState = TOUCH_STATE_NONE;

        loadFooter = (LinearLayout) LayoutInflater.from(context).inflate(
                R.layout.load_footer, null, false);
        addFooterView(loadFooter);
        loadFooter.setVisibility(GONE);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
//        if (isFooterReady == false) {  // 添加尾部隐藏
//            isFooterReady = true;
//            Log.e("lance_test", "addFooterView");
//            //addFooterView(loadFooter);
//            loadFooter.setVisibility(GONE);
//        }
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
            lastY = ev.getRawY(); // 相对于屏幕左上角
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: // 手势按下事件、获取坐标、设置上次下拉时间
                firstTouchY = ev.getRawY(); // 相对于屏幕左上角
                lastY = ev.getRawY();
                setRefreshTime(RefreshTime.getRefreshTime(getContext()));
                touchX = ev.getX(); // 相对于控件左上角
                touchY = ev.getY();
                touchState = TOUCH_STATE_NONE;
                int oldPosition = touchPosition;
                touchPosition = pointToPosition((int) ev.getX(), (int) ev.getY());
                if (touchPosition == oldPosition // 弹出左滑菜单
                        && swipeMenuLayout != null
                        && swipeMenuLayout.isOpen()) {
                    touchState = TOUCH_STATE_X;
                    //swipeMenuLayout.onSwipe(ev); // 左滑菜单手势监听事件，根据滑动距离弹出菜单
                    return true;
                }
                // 获取 item view，此方法是因为 getChildAt() 传入 index 值导致 list view 不可见的 item 会报空指针
                // 防止 list view 不可见的 item 获取到的为空，使用下面方法
                View view = getChildAt(touchPosition - getFirstVisiblePosition());
                if (swipeMenuLayout != null // 如果滑动的 item 不为空并且已经开启，则关闭该菜单
                        && swipeMenuLayout.isOpen()) {
                    swipeMenuLayout.smoothCloseMenu();
                    swipeMenuLayout = null;
                    return super.onTouchEvent(ev);
                }
                if (swipeMenuLayout != null) { // 否则打开左滑菜单
                    //swipeMenuLayout.onSwipe(ev);
                }
                if (view instanceof SwipeMenuLayout) {
                    swipeMenuLayout = (SwipeMenuLayout) view;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - lastY;
                float dy = Math.abs((ev.getY() - touchY));
                float dx = Math.abs((ev.getX() - touchX));
                lastY = ev.getRawY();
                if ((swipeMenuLayout == null || !swipeMenuLayout.isActive()) // 判断左滑菜单是否未激活
                        && Math.pow(dx, 2) / Math.pow(dy, 2) <= 3) { // 或者 x轴偏移平方 <=  y轴偏移平方 3 倍的时候
                    if (getFirstVisiblePosition() == 0 // 判断第一个可见位置
                            && (refreshHeader.GetVisibleHeight() > 0 || deltaY > 0)) { //  并且头部布局可见高度大于 0 时或者 y 轴偏移量 > 0
                        UpdateHeaderHeight(deltaY / OFFSET_RADIO); // 重新更新头部高度
                        invokeOnScrolling();
                    } else if (IsBottom()) {
                        loadFooter.setVisibility(VISIBLE);
                    }
                }
                if (touchState == TOUCH_STATE_X) { // 如果 x 轴偏移弹出左滑菜单
                    if (swipeMenuLayout != null) {
                        //swipeMenuLayout.onSwipe(ev);
                    }
                    getSelector().setState(new int[]{0});
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                    super.onTouchEvent(ev);
                    return true;
                } else if (touchState == TOUCH_STATE_NONE) {
                    if (Math.abs(dy) > MAX_Y) { // 如果 y轴偏移量 > 指定 y轴偏移量，设置 y轴偏移状态
                        touchState = TOUCH_STATE_Y;
                    } else if (dx > MAX_X) { // 如果 x轴偏移量 > 指定 x轴偏移量，设置 x轴偏移状态，开始弹出左滑菜单
                        touchState = TOUCH_STATE_X;
                        if (onSwipeListener != null) {
                            //onSwipeListener.onSwipeStart(touchPosition);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                lastY = -1;
                if (getFirstVisiblePosition() == 0) { // 处理下拉刷新
                    if (enablePullRefresh && refreshHeader.GetVisibleHeight() > refreshHeaderHeight) {
                        refreshing = true;
                        refreshHeader.SetState(RefreshHeader.STATE_REFRESHING);
                        if (onRefreshListener != null) {
                            tag = REFRESH;
                            onRefreshListener.onRefresh(1);
                        }
                    }
                    ResetRefreshHeaderHeight();
                }
                lastTouchY = ev.getRawY();
                if (IsBottom() && !loading && (firstTouchY - lastTouchY) >= 200) { // 处理上拉加载
                    LoadMoreData();
                    //loadFooter.setVisibility(VISIBLE);
                }
//                if (touchState == TOUCH_STATE_X) { // 处理左滑菜单
//                    if (swipeMenuLayout != null) {
//                        swipeMenuLayout.onSwipe(ev);
//                        if (!swipeMenuLayout.isOpen()) {
//                            touchPosition = -1;
//                            swipeMenuLayout = null;
//                        }
//                    }
//                    if (onSwipeListener != null) {
//                        onSwipeListener.onSwipeEnd(touchPosition);
//                    }
//                    ev.setAction(MotionEvent.ACTION_CANCEL);
//                    super.onTouchEvent(ev);
//                    return true;
//                }
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
        enablePullRefresh = enable;
        if (!enablePullRefresh) { // disable, hide the content
            headerViewContent.setVisibility(View.INVISIBLE);
        } else {
            headerViewContent.setVisibility(View.VISIBLE);
        }
    }

    private void SetPullLoadEnable(boolean enable) {
//        enablePullLoad = enable;
//        if (enablePullLoad) {
//            loading = false;
//            loadFooter.setVisibility(VISIBLE);
//            loadFooter.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //StartLoading();
//                }
//            });
//        } else {
//            loadFooter.setVisibility(GONE);
//            loadFooter.setOnClickListener(null);
//        }
    }

    private void StopRefresh() {
        if (refreshing == true) {
            refreshing = false;
            ResetRefreshHeaderHeight();
        }
    }

    private void StopLoadMore() {
        if (loading == true) {
            loading = false;
            loadFooter.setVisibility(GONE);
        }
    }

    public void setRefreshTime(String time) {
        headerViewTime.setText(time);
    }

    private void invokeOnScrolling() {
        if (onScrollListener instanceof OnXScrollListener) {
            OnXScrollListener onXScrollListener = (OnXScrollListener) onScrollListener;
            onXScrollListener.onXScrolling(this);
        }
    }

    private void UpdateHeaderHeight(float delta) {
        refreshHeader.SetVisibleHeight((int) delta + refreshHeader.GetVisibleHeight());
        if (enablePullRefresh && !refreshing) {
            if (refreshHeader.GetVisibleHeight() > refreshHeaderHeight) {
                refreshHeader.SetState(RefreshHeader.STATE_READY);
            } else {
                refreshHeader.SetState(RefreshHeader.STATE_NORMAL);
            }
        }
        setSelection(0);
    }

    private void ResetRefreshHeaderHeight() {
        int visibleHeight = refreshHeader.GetVisibleHeight();
        if (visibleHeight == 0)
            return;
        if (refreshing && visibleHeight <= refreshHeaderHeight) {
            return;
        }
        int finalHeight = 0;
        if (refreshing && visibleHeight > refreshHeaderHeight) {
            finalHeight = refreshHeaderHeight;
        }
        scrollBack = SCROLL_BACK_HEADER;
        scroller.startScroll(0, visibleHeight, 0, finalHeight - visibleHeight, SCROLL_DURATION);
        invalidate();
    }

    private void StartLoading() {
        loading = true;
        loadFooter.setVisibility(VISIBLE);
        if (onRefreshListener != null) {
            tag = LOAD;
            onRefreshListener.onLoadMore();
        }
    }

    public void setLoading(boolean loading) {
        if (this == null) return;
        this.loading = loading;
        if (loading) {
            //loadFooter.setVisibility(VISIBLE);
            setSelection(getAdapter().getCount() - 1);
            onRefreshListener.onLoadMore();
        } else {
            loadFooter.setVisibility(GONE);
            firstTouchY = 0;
            lastTouchY = 0;
        }
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            if (scrollBack == SCROLL_BACK_HEADER) {
                refreshHeader.SetVisibleHeight(scroller.getCurrY());
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

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
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
        } else if (mode == MODE_HEADER) {
            SetPullRefreshEnable(true);
        } else if (mode == MODE_FOOTER) {
            SetPullLoadEnable(true);
        }
    }

    private boolean IsBottom() {
        if (getCount() > 0) {
            if (getLastVisiblePosition() == getAdapter().getCount() - 1 &&
                    getChildAt(getChildCount() - 1).getBottom() <= getHeight()) {
                return true;
            }
        }
        return false;
    }

    private void LoadMoreData() {
        if (onRefreshListener != null) {
            setLoading(true);
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