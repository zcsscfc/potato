package com.potato.list;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.TypedValue;
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
    private ISwipeMenuCreator swipeMenuCreator; // 创建左滑菜单接口
    private IOnMenuItemClickListener onMenuItemClickListener; // 菜单点击事件
    private Interpolator mCloseInterpolator; // 关闭菜单动画修饰 Interpolator
    private Interpolator mOpenInterpolator; // 开启菜单动画修饰 Interpolator
    private float mLastY = -1;
    private Scroller mScroller;
    private OnScrollListener mScrollListener; // 滑动监听
    private IOnRefreshListener onRefreshListener; // 下拉上拉监听器
    private RefreshListHeader mHeaderView; // 下拉头
    private RelativeLayout mHeaderViewContent; // 头部视图内容，用来计算头部高度，不下拉时隐藏
    private TextView mHeaderTimeView; // 下拉时间文本控件
    private int mHeaderViewHeight; // 头部高度
    private boolean mEnablePullRefresh = true; // 能否下拉刷新
    private boolean mPullRefreshing = false; // 是否正在刷新
    private LinearLayout mFooterView; // 上拉尾部视图
    private boolean mEnablePullLoad;//是否可以上拉加载
    private boolean mPullLoading;   //是否正在上拉
    private boolean mIsFooterReady = false;
    private int mTotalItemCount;
    private int mScrollBack;
    private final static int SCROLLBACK_HEADER = 0;
    private final static int SCROLLBACK_FOOTER = 1;
    private final static int SCROLL_DURATION = 400;
    private final static int PULL_LOAD_MORE_DELTA = 50;
    private final static float OFFSET_RADIO = 1.8f;
    private boolean isFooterVisible = false;


    public static final int HEADER = 0; // 下拉
    public static final int FOOTER = 1; // 上拉
    public static final int BOTH = 2; // 上拉和下拉
    public static String tag; // ListView 的动作
    public static final String REFRESH = "refresh";
    public static final String LOAD = "load";

    public RefreshSwipeMenuListView(Context context) {
        super(context);
        init(context);
    }

    public RefreshSwipeMenuListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public RefreshSwipeMenuListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * 初始化组件
     *
     * @param context
     */
    private void init(Context context) {
        mScroller = new Scroller(context, new DecelerateInterpolator());
        SimpleOnScrollListener simpleOnScrollListener = new SimpleOnScrollListener(
                mScrollListener, mTotalItemCount, isFooterVisible
        );
        super.setOnScrollListener(simpleOnScrollListener);
        // 初始化头部视图
        mHeaderView = new RefreshListHeader(context);
        mHeaderViewContent = (RelativeLayout) mHeaderView.findViewById(R.id.xlistview_header_content);
        mHeaderTimeView = (TextView) mHeaderView.findViewById(R.id.xlistview_header_time);
        addHeaderView(mHeaderView);

        // 初始化尾部视图
        mFooterView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.xlistview_footer, null, false);

        // 初始化头部高度
        mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mHeaderViewHeight = mHeaderViewContent.getHeight();
                //向 ViewTreeObserver 注册方法，以获取控件尺寸
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
        MAX_X = dp2px(MAX_X);
        MAX_Y = dp2px(MAX_Y);
        touchState = TOUCH_STATE_NONE;
    }

    /**
     * 添加适配器
     *
     * @param adapter
     */
    @Override
    public void setAdapter(ListAdapter adapter) {
        if (mIsFooterReady == false) {  //添加尾部隐藏
            mIsFooterReady = true;
            addFooterView(mFooterView);
            mFooterView.setVisibility(GONE);
        }
        super.setAdapter(new SwipeMenuAdapter(getContext(), adapter) {
            @Override
            public void createMenu(SwipeMenu menu) {//创建左滑菜单
                if (swipeMenuCreator != null) {
                    swipeMenuCreator.create(menu);
                }
            }

            @Override
            public void onItemClick(SwipeMenuView view, SwipeMenu menu, int index) {
                if (onMenuItemClickListener != null) {//左滑菜单点击事件
                    onMenuItemClickListener.onMenuItemClick(view.getPosition(), menu, index);
                }
                if (swipeMenuLayout != null) {
                    swipeMenuLayout.smoothCloseMenu();
                }
            }
        });
    }

    public void setCloseInterpolator(Interpolator interpolator) {
        mCloseInterpolator = interpolator;
    }

    public void setOpenInterpolator(Interpolator interpolator) {
        mOpenInterpolator = interpolator;
    }

    public Interpolator getOpenInterpolator() {
        return mOpenInterpolator;
    }

    public Interpolator getCloseInterpolator() {
        return mCloseInterpolator;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1) { //获取上次y轴坐标
            mLastY = ev.getRawY();
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:   //手势按下事件、获取坐标、设置上次下拉时间
                firstTouchY = ev.getRawY();
                mLastY = ev.getRawY();
                setRefreshTime(RefreshTime.getRefreshTime(getContext()));
                int oldPos = touchPosition;
                touchX = ev.getX();
                touchY = ev.getY();
                touchState = TOUCH_STATE_NONE;

                touchPosition = pointToPosition((int) ev.getX(), (int) ev.getY());

                //弹出左滑菜单
                if (touchPosition == oldPos && swipeMenuLayout != null && swipeMenuLayout.isOpen()) {
                    touchState = TOUCH_STATE_X;
                    swipeMenuLayout.onSwipe(ev);//左滑菜单手势监听事件，根据滑动距离弹出菜单
                    return true;
                }


                //获取item view，此方法是因为getChildAt()传入index值导致listview不可见的item会报空指针
                // 防止listview不可见的item获取到的为空，使用下面方法
                View view = getChildAt(touchPosition - getFirstVisiblePosition());

                if (swipeMenuLayout != null && swipeMenuLayout.isOpen()) {//如果滑动的item不为空并且已经开启，则关闭该菜单
                    swipeMenuLayout.smoothCloseMenu();
                    swipeMenuLayout = null;
                    return super.onTouchEvent(ev);
                }

                if (swipeMenuLayout != null) {//否则打开左滑菜单
                    swipeMenuLayout.onSwipe(ev);
                }
                if (view instanceof SwipeMenuLayout) {
                    swipeMenuLayout = (SwipeMenuLayout) view;
                }

                break;
            case MotionEvent.ACTION_MOVE://手势滑动事件
                final float deltaY = ev.getRawY() - mLastY;
                float dy = Math.abs((ev.getY() - touchY));
                float dx = Math.abs((ev.getX() - touchX));
                mLastY = ev.getRawY();
                //判断左滑菜单是否未激活、或者x轴偏移平方小于y轴偏移平方3倍的时候
                if ((swipeMenuLayout == null || !swipeMenuLayout.isActive()) && Math.pow(dx, 2) / Math.pow(dy, 2) <= 3) {
                    //判断第一个可见位置并且头部布局可见高度大于0时或者y轴偏移量>0
                    if (getFirstVisiblePosition() == 0 && (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)) {
                        // 重新更新头部高度
                        updateHeaderHeight(deltaY / OFFSET_RADIO);
                        invokeOnScrolling();
                    }
                }

                if (touchState == TOUCH_STATE_X) {//如果x轴偏移弹出左滑菜单
                    if (swipeMenuLayout != null) {
                        swipeMenuLayout.onSwipe(ev);
                    }
                    getSelector().setState(new int[]{0});
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                    super.onTouchEvent(ev);
                    return true;
                } else if (touchState == TOUCH_STATE_NONE) {
                    if (Math.abs(dy) > MAX_Y) { //如果y轴偏移量>指定y轴偏移量，设置y轴偏移状态
                        touchState = TOUCH_STATE_Y;
                    } else if (dx > MAX_X) {//如果x轴偏移量>指定x轴偏移量，设置x轴偏移状态，开始弹出左滑菜单
                        touchState = TOUCH_STATE_X;
                        if (onSwipeListener != null) {
                            onSwipeListener.onSwipeStart(touchPosition);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP://手势抬起事件
                mLastY = -1; // reset
                if (getFirstVisiblePosition() == 0) {
                    // 设置下拉刷新状态值，开启下拉刷新状态
                    if (mEnablePullRefresh && mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
                        mPullRefreshing = true;
                        mHeaderView.setState(RefreshListHeader.STATE_REFRESHING);
                        if (onRefreshListener != null) {
                            tag = REFRESH;
                            onRefreshListener.onRefresh();
                        }
                    }
                    resetHeaderHeight();
                }

                lastTouchY = ev.getRawY();//获取上次y轴偏移量
                if (canLoadMore()) {//判断是否满足上拉
                    loadData();
                }

                if (touchState == TOUCH_STATE_X) {//如果为x轴偏移状态，开启左滑
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

    class ResetHeaderHeightTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            mPullRefreshing = false;
            mHeaderView.setState(RefreshListHeader.STATE_NORMAL);
            resetHeaderHeight();

        }
    }

    public void smoothOpenMenu(int position) {
        if (position >= getFirstVisiblePosition() && position <= getLastVisiblePosition()) {
            View view = getChildAt(position - getFirstVisiblePosition());
            if (view instanceof SwipeMenuLayout) {
                touchPosition = position;
                if (swipeMenuLayout != null && swipeMenuLayout.isOpen()) {
                    swipeMenuLayout.smoothCloseMenu();
                }
                swipeMenuLayout = (SwipeMenuLayout) view;
                swipeMenuLayout.smoothOpenMenu();
            }
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getContext().getResources().getDisplayMetrics());
    }

    public void setMenuCreator(ISwipeMenuCreator menuCreator) {
        this.swipeMenuCreator = menuCreator;
    }

    public void setOnMenuItemClickListener(IOnMenuItemClickListener onMenuItemClickListener) {
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
    private void setPullRefreshEnable(boolean enable) {
        mEnablePullRefresh = enable;
        if (!mEnablePullRefresh) { // disable, hide the content
            mHeaderViewContent.setVisibility(View.INVISIBLE);
        } else {
            mHeaderViewContent.setVisibility(View.VISIBLE);
        }
    }

    /**
     * enable or disable pull up load more feature.
     * 设置加载可用
     *
     * @param enable
     */
    private void setPullLoadEnable(boolean enable) {
        mEnablePullLoad = enable;
        if (!mEnablePullLoad) {
            mFooterView.setVisibility(GONE);
            mFooterView.setOnClickListener(null);
        } else {
            mPullLoading = false;
            mFooterView.setVisibility(VISIBLE);
            // both "pull up" and "click" will invoke load more.
            mFooterView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startLoadMore();
                }
            });
        }
    }

    /**
     * stop refresh, reset header view.
     * 停止刷新,重置头部控件
     */
    private void stopRefresh() {
        if (mPullRefreshing == true) {
            mPullRefreshing = false;
            resetHeaderHeight();
        }
    }

    /**
     * stop load more, reset footer view.
     * 停止加载更多,重置尾部控件
     */
    private void stopLoadMore() {
        if (mPullLoading == true) {
            mPullLoading = false;
            mFooterView.setVisibility(GONE);
        }
    }

    /**
     * set last refresh time
     *
     * @param time
     */
    public void setRefreshTime(String time) {
        mHeaderTimeView.setText(time);
    }

    private void invokeOnScrolling() {
        if (mScrollListener instanceof OnXScrollListener) {
            OnXScrollListener l = (OnXScrollListener) mScrollListener;
            l.onXScrolling(this);
        }
    }

    /**
     * 更新头部高度，设置状态值
     *
     * @param delta
     */
    private void updateHeaderHeight(float delta) {
        mHeaderView.setVisiableHeight((int) delta + mHeaderView.getVisiableHeight());
        if (mEnablePullRefresh && !mPullRefreshing) {
            if (mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
                mHeaderView.setState(RefreshListHeader.STATE_READY);
            } else {
                mHeaderView.setState(RefreshListHeader.STATE_NORMAL);
            }
        }
        setSelection(0); // scroll to top each time
    }

    /**
     * 重置头部视图高度
     */
    private void resetHeaderHeight() {
        int height = mHeaderView.getVisiableHeight();
        if (height == 0) // 不可见
            return;
        // 如果正在刷新并且头部高度没有完全显示不做操作
        if (mPullRefreshing && height <= mHeaderViewHeight) {
            return;
        }
        int finalHeight = 0; // 默认
        //如果正在刷新并且滑动高度大于头部高度
        if (mPullRefreshing && height > mHeaderViewHeight) {
            finalHeight = mHeaderViewHeight;
        }
        mScrollBack = SCROLLBACK_HEADER;
        mScroller.startScroll(0, height, 0, finalHeight - height, SCROLL_DURATION);
        // 触发computescroll
        invalidate();
    }

    /**
     * 开启上啦
     */
    private void startLoadMore() {
        mPullLoading = true;
        mFooterView.setVisibility(VISIBLE);
        if (onRefreshListener != null) {
            tag = LOAD;
            onRefreshListener.onLoadMore();
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (mScrollBack == SCROLLBACK_HEADER) {
                mHeaderView.setVisiableHeight(mScroller.getCurrY());
            }
            postInvalidate();
            invokeOnScrolling();
        }
        super.computeScroll();
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        mScrollListener = l;
    }

    public void setOnRefreshListener(IOnRefreshListener l) {
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
    public void complete() {
        stopLoadMore();
        stopRefresh();
        if (REFRESH.equals(tag)) {
            RefreshTime.setRefreshTime(getContext(), new Date());
        }
    }

    /**
     * 设置ListView的模式,上拉和下拉
     *
     * @param mode
     */
    public void setListViewMode(int mode) {
        if (mode == BOTH) {
            setPullRefreshEnable(true);
            setPullLoadEnable(true);
        } else if (mode == FOOTER) {
            setPullLoadEnable(true);
        } else if (mode == HEADER) {
            setPullRefreshEnable(true);
        }
    }

    /**
     * 判断是否可以上蜡加载
     *
     * @return
     */
    private boolean canLoadMore() {
        return isBottom() && !mPullLoading && isPullingUp();
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


    private void loadData() {
        if (onRefreshListener != null) {
            setLoading(true);
        }
    }

    /**
     * 设置是否上拉
     *
     * @param loading
     */
    public void setLoading(boolean loading) {
        if (this == null) return;
        mPullLoading = loading;
        if (loading) {
            mFooterView.setVisibility(VISIBLE);

            setSelection(getAdapter().getCount() - 1);
            onRefreshListener.onLoadMore();
        } else {
            mFooterView.setVisibility(GONE);
            firstTouchY = 0;
            lastTouchY = 0;
        }
    }
}