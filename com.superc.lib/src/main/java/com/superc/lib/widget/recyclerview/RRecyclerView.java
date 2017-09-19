package com.superc.lib.widget.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

import com.superc.lib.widget.recyclerview.adapter.RAdapter;
import com.superc.lib.widget.recyclerview.adapter.WrapperAdapter;
import com.superc.lib.widget.recyclerview.callback.AppBarStateChangeListener;
import com.superc.lib.widget.recyclerview.callback.OnItemClickListener;
import com.superc.lib.widget.recyclerview.callback.OnRecyclerItemClickListener;
import com.superc.lib.widget.recyclerview.callback.RRecycleViewLoadListener;
import com.superc.lib.widget.recyclerview.util.RRecyclerViewHelper;

/**
 * 自定义RecyclerView
 * <p>
 * Created by superchen on  2017/9/11
 * The last edit time       2017/9/11
 */
public class RRecyclerView<T> extends RecyclerView {

    protected RAdapter rAdapter;

    protected RRecyclerViewHelper rHelper;

    protected AdapterDataObserver rDataObserver = null;


    public RRecyclerView(Context context) {
        super(context, null);
        init();
    }

    public RRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
        init();
    }

    public RRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        rHelper = new RRecyclerViewHelper(getContext(), this);
        rDataObserver = new RDataObserver();
        rHelper.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    /**
     * 设置不滚动
     */
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        LogUtils.d("RRecyclerView onMeasure");
        int expandSpec;
        if (rHelper.isFullLayoutManager()) {
            expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        } else {
            expandSpec = heightMeasureSpec;
        }
        super.onMeasure(widthMeasureSpec, expandSpec);
//        LogUtils.d("RRecyclerView onMeasure completed");
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
//        LogUtils.d("RRecyclerView onAttachedToWindow");
        //解决和CollapsingToolbarLayout冲突的问题
        AppBarLayout appBarLayout = null;
        ViewParent p = getParent();
        while (p != null) {
            if (p instanceof CoordinatorLayout) {
                break;
            }
            p = p.getParent();
        }
        if (p != null && p instanceof CoordinatorLayout) {
            CoordinatorLayout coordinatorLayout = (CoordinatorLayout) p;
            final int childCount = coordinatorLayout.getChildCount();
            for (int i = childCount - 1; i >= 0; i--) {
                final View child = coordinatorLayout.getChildAt(i);
                if (child instanceof AppBarLayout) {
                    appBarLayout = (AppBarLayout) child;
                    break;
                }
            }
            if (appBarLayout != null) {
                appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
                    @Override
                    public void onStateChanged(AppBarLayout appBarLayout, State state) {
                        rHelper.setAppbarState(state);
                    }
                });
            }
        }
//        LogUtils.d("RRecyclerView onAttachedToWindow completed");
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return rHelper.onTouchEvent(ev);
    }

    public boolean returnSuperOnTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }

    public void setRefreshing(boolean refreshing) {
        rHelper.setRefreshing(refreshing);
    }

    public <T extends WrapperAdapter> void setAdapter(@NonNull T adapter) {
//        LogUtils.d("RRecyclerView setAdapter adapter = " + adapter);
        if (rAdapter != null) {
            rAdapter.unregisterAdapterDataObserver(rDataObserver);
            rDataObserver.onChanged();
        }
        rAdapter = new RAdapter(getContext(), adapter, rHelper);
        if (rHelper.getDividerItemDecoration() != null) {
            addItemDecoration(rHelper.getDividerItemDecoration());
        }
        super.setAdapter(rAdapter);
        adapter.registerAdapterDataObserver(rDataObserver);
        rDataObserver.onChanged();
    }

    public void addHeaderView(View view) {
        rHelper.addHeaderView(view);
    }

    public void addHeaderView(int index, View view) {
        rHelper.addHeaderView(index, view);
    }

    public void addFooterView(View view) {
        rHelper.addFooterView(view);
    }

    public void addFooterView(int index, View view) {
        rHelper.addFooterView(index, view);
    }

    public void setRRecyclerViewLoadListener(@NonNull RRecycleViewLoadListener listener) {
        rHelper.setRRecyclerViewLoadListener(listener);
    }

    public SparseArrayCompat<View> getHeaderViews() {
        return rHelper.getHeaderViews();
    }

    public SparseArrayCompat<View> getFooterViews() {
        return rHelper.getFooterViews();
    }

    @Override
    public final void setLayoutManager(LayoutManager layout) {
        rHelper.setLayoutManager(layout);
        super.setLayoutManager(layout);
    }

    public <T, VH extends ViewHolder> void setOnItemClickListener(final OnItemClickListener<T, VH> onClickListener) {
        if (onClickListener == null) return;
        addOnItemTouchListener(new OnRecyclerItemClickListener<T, VH>(this) {

            @Override
            public void onItemClick(View view, VH holder, T model, int position) {
                onClickListener.onItemClick(view, holder, model, position);
            }

            @Override
            public void onItemLongClick(View view, VH holder, T model, int position) {
                onClickListener.onItemLongClick(view, holder, model, position);
            }
        });
    }

    public boolean isRefreshingState() {
        return rHelper.isLoadingState();
    }

    public boolean canPullRefreshEnable() {
        return rHelper.canPullRefreshEnable();
    }

    public void setPullRefreshEnable(boolean enable) {
        rHelper.setPullRefreshEnable(enable);
    }

    public boolean canLoadMoreEnable() {
        return rHelper.canLoadMoreEnable();
    }

    public void setLoadMoreEnable(boolean enable) {
        rHelper.setLoadMoreEnable(enable);
    }

    public boolean getHeaderEnable() {
        return rHelper.getHeaderEnable();
    }

    public void setHeaderEnable(boolean enable) {
        rHelper.setHeaderEnable(enable);
    }

    public boolean getFooterEnable() {
        return rHelper.getFooterEnable();
    }

    public void setFooterEnable(boolean enable) {
        rHelper.setFooterEnable(enable);
    }

    public boolean isLoadingState() {
        return rHelper.isLoadingState();
    }

    public void setLoadingState(boolean isLoading) {
        rHelper.setLoadingState(isLoading);
    }

    public boolean isNoMoreData() {
        return rHelper.isNoMoreData();
    }

    public void setNoMoreData(boolean noMoreData) {
        rHelper.setNoMoreData(noMoreData);
    }

    public void loadMoreComplete() {
        rHelper.loadMoreComplete();
    }

    public void setFullLayoutManager(boolean fullEnable) {
        rHelper.setFullLayoutManager(fullEnable);
    }

    public void setEmptyView(@NonNull View emptyView) {
        rHelper.setEmptyView(emptyView);
    }

    public View getEmptyView() {
        return rHelper.getEmptyView();
    }

    public AppBarStateChangeListener.State getAppbarState() {
        return rHelper.getAppbarState();
    }

    public RRecycleViewLoadListener getRRecyclerViewLoadListener() {
        return rHelper.getRRecyclerViewLoadListener();
    }

    public void setDividerItemDecoration(DividerItemDecoration dividerItemDecoration) {
        rHelper.setDividerItemDecoration(dividerItemDecoration);
    }

    public DividerItemDecoration getDividerItemDecoration() {
        return rHelper.getDividerItemDecoration();
    }

    public static float getDragRate() {
        return RRecyclerViewHelper.getDragRate();
    }

    public static void setDragRate(float dragRate) {
        RRecyclerViewHelper.setDragRate(dragRate);
    }

    public AdapterDataObserver getDataObserver() {
        return rDataObserver;
    }

    public class RDataObserver extends AdapterDataObserver {

        @Override
        public void onChanged() {
            if (rAdapter != null) {
                rAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            if (rAdapter != null) {
                rAdapter.notifyItemRangeInserted(positionStart, itemCount);
            }
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            if (rAdapter != null) {
                rAdapter.notifyItemRangeChanged(positionStart, itemCount);
            }
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            if (rAdapter != null) {
                rAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
            }
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            if (rAdapter != null) {
                rAdapter.notifyItemRangeRemoved(positionStart, itemCount);
            }
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            if (rAdapter != null) {
                rAdapter.notifyItemMoved(fromPosition, toPosition);
            }
        }
    }
}
