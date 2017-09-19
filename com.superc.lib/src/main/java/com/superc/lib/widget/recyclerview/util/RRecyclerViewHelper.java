package com.superc.lib.widget.recyclerview.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import com.superc.lib.widget.recyclerview.RRecyclerView;
import com.superc.lib.widget.recyclerview.StickyVIew.StickyRecyclerHeadersTouchListener;
import com.superc.lib.widget.recyclerview.base.footer.NormalLoadMoreFooter;
import com.superc.lib.widget.recyclerview.base.footer.RBaseFooter;
import com.superc.lib.widget.recyclerview.base.header.ArrowRefreshHeader;
import com.superc.lib.widget.recyclerview.base.header.HeaderState;
import com.superc.lib.widget.recyclerview.base.header.RBaseHeader;
import com.superc.lib.widget.recyclerview.callback.AppBarStateChangeListener;
import com.superc.lib.widget.recyclerview.callback.RRecycleViewLoadListener;
import com.superc.lib.widget.recyclerview.progressindicator.ProgressStyle;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by superchen on 2017/6/20.
 */
public class RRecyclerViewHelper<R extends RRecyclerView> {


    public static float DRAG_RATE = 2.5f;

    /**
     * 设置一个很大的数字,尽可能避免和用户的adapter冲突
     */
    public static final int TYPE_REFRESH_HEADER = 10000;
    public static final int TYPE_LOAD_FOOTER = 11000;
    public static final int HEADER_INIT_INDEX = 12000;
    public static final int FOOTER_INIT_INDEX = 13000;

    private R r;

    private Context mContext;

    private float mLastY = -1;
    /**
     * 所有的HeaderViews
     */
    private SparseArrayCompat<View> mHeaderViews = null;
    /**
     * 所有的FooterViews
     */
    private SparseArrayCompat<View> mFooterViews = null;
    /**
     * 每个header必须有不同的type,不然滚动的时候顺序会变化
     */
    private List<Integer> sHeaderTypes = null;
    /**
     * 每个footer必须有不同的type,不然滚动的时候顺序会变化
     */
    private List<Integer> sFooterTypes = null;
    /**
     * 空视图
     */
    private View mEmptyView = null;

    private RBaseFooter mFooterView = null;

    private RBaseHeader mHeaderView = null;
    private RecyclerView.LayoutManager mLayoutManager;
    /**
     * 下啦刷新开关，默认为开
     */
    private boolean mPullRefreshEnable = true;
    /**
     * 是否允许添加HeaderView开关，默认为开，即使是mPullRefreshEnable = false时，同样也可以添加其它HeaderView
     */
    private boolean mHeaderEnable = true;
    /**
     * 底部加载更多开关，默认为开
     */
    private boolean mLoadMoreEnable = true;
    /**
     * 是否允许添加FooterView开光，默认为开，即使是mLoadMoreEnable = false时，同样也可以添加其它FooterView
     */
    private boolean mFooterEnable = true;
    /**
     * 是否正在获取数据状态
     */
    private boolean mIsLoading = false;
    /**
     * 有无更多数据，默认为有
     */
    private boolean mNoMoreData = false;
    /**
     * ScrollView中嵌套RecyclerView时使用
     */
    private boolean mFullLayoutManager = false;
    /**
     * 是否显示分组组头信息
     */
    private boolean showStickerHeader = false;
    /**
     * 分组组头是否可以点击
     */
    private boolean stickerHeaderClickEnable = false;
    /**
     * 分割线
     */
    private DividerItemDecoration dividerItemDecoration;

    private RRecycleViewLoadListener mLoadListener;
    /**
     * 分组组头点击监听
     */
    private StickyRecyclerHeadersTouchListener.OnHeaderClickListener onHeaderClickListener;

    private AppBarStateChangeListener.State appbarState = AppBarStateChangeListener.State.EXPANDED;


    public RRecyclerViewHelper(Context context, R r) {
        mContext = context;
        this.r = r;
        initHeader();
        initFooter();
    }

    private void initHeader() {
        if (mHeaderViews == null) {
            mHeaderViews = new SparseArrayCompat<>();
        }
        if (sHeaderTypes == null) {
            sHeaderTypes = new LinkedList<>();
        }
        if (mPullRefreshEnable) {
            mHeaderView = new ArrowRefreshHeader(mContext);
            ((ArrowRefreshHeader) mHeaderView).setProgressStyle(ProgressStyle.SysProgress);
        }
    }

    private void initFooter() {
        if (mFooterViews == null) {
            mFooterViews = new SparseArrayCompat<>();
        }
        if (sFooterTypes == null) {
            sFooterTypes = new LinkedList<>();
        }
        if (mLoadMoreEnable) {
            mFooterView = new NormalLoadMoreFooter(mContext);
            ((NormalLoadMoreFooter) mFooterView).setProgressStyle(ProgressStyle.SysProgress);
            mFooterView.setVisibility(View.GONE);
        }
    }

    public void addHeaderView(@NonNull View view) {
        sHeaderTypes.add(HEADER_INIT_INDEX + mHeaderViews.size());
        mHeaderViews.put(HEADER_INIT_INDEX + mHeaderViews.size(), view);
        invalidate();
    }

    public void addHeaderView(int index, @NonNull View view) {
        if (!checkReboundsConform(mHeaderViews, index)) return;
        sHeaderTypes.add(HEADER_INIT_INDEX + mHeaderViews.size());
        mHeaderViews.put(index, view);
        invalidate();
    }

    public void addFooterView(@NonNull View view) {
        sFooterTypes.add(FOOTER_INIT_INDEX + mFooterViews.size());
        mFooterViews.put(FOOTER_INIT_INDEX + mFooterViews.size(), view);
        invalidate();
    }

    public void addFooterView(int index, @NonNull View view) {
        if (!checkReboundsConform(mFooterViews, index)) return;
        sFooterTypes.add(FOOTER_INIT_INDEX + mFooterViews.size());
        mFooterViews.put(index, view);
        invalidate();
    }

    public SparseArrayCompat<View> getHeaderViews() {
        return mHeaderViews;
    }

    public SparseArrayCompat<View> getFooterViews() {
        return mFooterViews;
    }

    public int getHeaderViewCounts() {
        return mHeaderViews.size();
    }

    public int getFooterViewCounts() {
        return mFooterViews.size();
    }

    /**
     * 根据header的ViewType判断是哪个header
     *
     * @param itemType
     * @return
     */
    public View getHeaderViewByType(int itemType) {
        if (!isHeaderType(itemType)) {
            return null;
        }
        return mHeaderViews.get(itemType - HEADER_INIT_INDEX);
    }

    /**
     * 判断一个type是否为HeaderType
     *
     * @param itemViewType
     * @return
     */
    public boolean isHeaderType(int itemViewType) {
        return mHeaderViews.size() > 0 && sHeaderTypes.contains(itemViewType);
    }

    /**
     * 根据footer的ViewType判断是哪个footer
     *
     * @param itemType
     * @return
     */
    public View getFooterViewByType(int itemType) {
        if (!isFooterType(itemType)) {
            return null;
        }
        return mFooterViews.get(itemType - FOOTER_INIT_INDEX);
    }

    /**
     * 判断一个type是否为FooterType
     *
     * @param itemViewType
     * @return
     */
    public boolean isFooterType(int itemViewType) {
        initFooter();
        return mFooterViews.size() > 0 && sFooterTypes.contains(itemViewType);
    }

    /**
     * 判断是否是XRecyclerView保留的itemViewType
     *
     * @param itemViewType
     * @return
     */
    public boolean isReservedItemViewType(int itemViewType) {
        if (itemViewType == TYPE_REFRESH_HEADER || itemViewType == FOOTER_INIT_INDEX || sHeaderTypes.contains(itemViewType) ||
                sFooterTypes.contains(itemViewType)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isHeader(int position) {
        if (mPullRefreshEnable) {
            position += 1;
        }
        if (mHeaderEnable) {
            return position >= 1 && position < mHeaderViews.size();
        }
        return false;
    }

    public boolean isRefreshHeader(int position) {
        if (mPullRefreshEnable) {
            return position == 0;
        }
        return false;
    }

    public boolean isFooter(int position) {
        int c = r.getAdapter().getItemCount();
        if (mLoadMoreEnable) {
            c -= 1;
        }
        if (mFooterEnable) {
            int b = c - mFooterViews.size();
            return position >= b && position < c;
        }
        return false;
    }

    public boolean isLoadMoreFooter(int position) {
        if (mLoadMoreEnable) {
            return position == r.getAdapter().getItemCount() - 1;
        }
        return false;
    }

    public int getHeaderPosition(int position) {
        int adjPosition = position - getHeaderViewCounts();
        if (mHeaderEnable) {
            if (mPullRefreshEnable) {
                adjPosition -= 1;
            }
        }
        return adjPosition;
    }

    public int getFooterPosition(int position) {
        int adjPosition = position - r.getAdapter().getItemCount() - getHeaderViewCounts();
        if (mFooterEnable) {
            if (mLoadMoreEnable) {
                adjPosition = adjPosition - getFooterViewCounts() - 1;
            } else {
                adjPosition = adjPosition - getFooterViewCounts();
            }
        }
        return adjPosition;
    }

    public void loadMoreComplete() {
        setLoadingState(false);
        mFooterView.setStateComplete();
    }

    public void setNoMoreData(boolean noMore) {
        setLoadingState(false);
        mNoMoreData = noMore;
        if (mNoMoreData) {
            mFooterView.setStateNoMore();
        } else {
            mFooterView.setStateComplete();
        }
    }

    public void setRefreshing(boolean refreshing) {
        if (refreshing && !mIsLoading && mPullRefreshEnable && mLoadListener != null) {
            mHeaderView.setRefreshingState();
            mHeaderView.onMove(mHeaderView.getThisMeasuredHeight());
            mFooterView.setVisibility(View.GONE);
            mLoadListener.onRefresh();
            if (mEmptyView != null) {
                mEmptyView.setVisibility(View.GONE);
                r.setVisibility(View.VISIBLE);
            }
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();
                if (mPullRefreshEnable && appbarState == AppBarStateChangeListener.State.EXPANDED) {
                    String sy = deltaY + "";
                    mHeaderView.onMove(deltaY / DRAG_RATE);
                    if (mHeaderView.getVisibleHeight() > 0 && mHeaderView.getState() < HeaderState.STATE_REFRESHING) {
                        return false;
                    }
                }
                break;
            default:
                /** reset **/
                mLastY = -1;
                if (mPullRefreshEnable && appbarState == AppBarStateChangeListener.State.EXPANDED) {
                    if (mHeaderView.releaseAction()) {
                        if (mLoadListener != null) {
                            mLoadListener.onRefresh();
                            mFooterView.setVisibility(View.GONE);
                        }
                    }
                }
                break;
        }
        return r.returnSuperOnTouchEvent(ev);
    }

    public void invalidate() {
        if (r != null) {
            r.invalidate();
        }
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return mLayoutManager;
    }

    public void setLayoutManager(RecyclerView.LayoutManager mLayoutManager) {
        this.mLayoutManager = mLayoutManager;
    }

    private boolean checkReboundsConform(SparseArrayCompat l, int position) {
        if (l == null) return false;
        if (position >= 0 && position < l.size()) {
            return true;
        }
        return false;
    }

    public boolean canPullRefreshEnable() {
        return mPullRefreshEnable;
    }

    public void setPullRefreshEnable(boolean enable) {
        mPullRefreshEnable = enable;
    }

    public boolean canLoadMoreEnable() {
        return mLoadMoreEnable;
    }

    public void setLoadMoreEnable(boolean enable) {
        mLoadMoreEnable = enable;
        if (!enable) {
            mFooterView.setStateComplete();
        }
    }

    public boolean isShowStickerHeader() {
        return showStickerHeader;
    }

    public void setShowStickerHeader(boolean showStickerHeader) {
        this.showStickerHeader = showStickerHeader;
    }

    public boolean isStickerHeaderClickEnable() {
        return stickerHeaderClickEnable;
    }

    public void setStickerHeaderClickEnable(boolean stickerHeaderClickEnable) {
        this.stickerHeaderClickEnable = stickerHeaderClickEnable;
    }

    public DividerItemDecoration getDividerItemDecoration() {
        return dividerItemDecoration;
    }

    public void setDividerItemDecoration(DividerItemDecoration dividerItemDecoration) {
        this.dividerItemDecoration = dividerItemDecoration;
    }

    public List<Integer> getHeaderTypes() {
        return sHeaderTypes;
    }

    public List<Integer> getFooterTypes() {
        return sFooterTypes;
    }

    public boolean getHeaderEnable() {
        return mHeaderEnable;
    }

    public void setHeaderEnable(boolean enable) {
        mHeaderEnable = enable;
    }

    public boolean getFooterEnable() {
        return mFooterEnable;
    }

    public void setFooterEnable(boolean enable) {
        mFooterEnable = enable;
    }

    public boolean isLoadingState() {
        return mIsLoading;
    }

    public void setLoadingState(boolean isLoading) {
        mIsLoading = isLoading;
    }

    public boolean isNoMoreData() {
        return mNoMoreData;
    }

    public RBaseHeader getRefreshHeader() {
        return mHeaderView;
    }

    public RBaseFooter getLoadFooter() {
        return mFooterView;
    }

    public void setEmptyView(@NonNull View emptyView) {
        this.mEmptyView = emptyView;
        r.getDataObserver().onChanged();
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    public void setFullLayoutManager(boolean fullEnable) {
        this.mFullLayoutManager = fullEnable;
        invalidate();
    }

    public boolean isFullLayoutManager() {
        return mFullLayoutManager;
    }

    public void setAppbarState(AppBarStateChangeListener.State state) {
        appbarState = state;
    }

    public AppBarStateChangeListener.State getAppbarState() {
        return appbarState;
    }

    public void setRRecyclerViewLoadListener(@NonNull RRecycleViewLoadListener listener) {
        mLoadListener = listener;
    }

    public StickyRecyclerHeadersTouchListener.OnHeaderClickListener getOnHeaderClickListener() {
        return onHeaderClickListener;
    }

    public void setOnHeaderClickListener(StickyRecyclerHeadersTouchListener.OnHeaderClickListener onHeaderClickListener) {
        this.onHeaderClickListener = onHeaderClickListener;
    }

    public RRecycleViewLoadListener getRRecyclerViewLoadListener() {
        return mLoadListener;
    }

    public float getLastY() {
        return mLastY;
    }

    public void setLastY(float y) {
        mLastY = y;
    }

    public static float getDragRate() {
        return DRAG_RATE;
    }

    public static void setDragRate(float dragRate) {
        DRAG_RATE = dragRate;
    }

}
