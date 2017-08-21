package com.superc.lib.widget.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.superc.lib.constants.Constants;
import com.superc.lib.util.LogUtils;
import com.superc.lib.widget.recyclerview.callback.AppBarStateChangeListener;
import com.superc.lib.widget.recyclerview.callback.OnItemClickListener;
import com.superc.lib.widget.recyclerview.callback.RRecycleViewLoadListener;
import com.superc.lib.widget.recyclerview.util.RRecyclerViewHelper;
import com.superc.lib.widget.recyclerview.wrapper.RAdapter;

/**
 * Created by superchen on 2017/6/20.
 */
public class RRecyclerView<T> extends RecyclerView {

    protected WrapperAdapter rAdapter;

    private RRecyclerViewHelper rHelper;

    private RecyclerView.AdapterDataObserver rDataObserver = null;


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
    }

    /**
     * 设置不滚动
     */
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        LogUtils.d("RRecyclerView onMeasure");
        int expandSpec;
        if (rHelper.isFullLayoutManager()) {
            expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        } else {
            expandSpec = heightMeasureSpec;
        }
        super.onMeasure(widthMeasureSpec, expandSpec);
        LogUtils.d("RRecyclerView onMeasure completed");
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        LogUtils.d("RRecyclerView onAttachedToWindow");
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
        LogUtils.d("RRecyclerView onAttachedToWindow completed");
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

    public <T extends RAdapter> void setAdapter(@NonNull T adapter) {
        LogUtils.d("RRecyclerView setAdapter adapter = " + adapter);
        if (rAdapter != null) {
            rAdapter.registerAdapterDataObserver(rDataObserver);
            rDataObserver.onChanged();
        }
        rAdapter = new WrapperAdapter(adapter);
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

    public RecyclerView.AdapterDataObserver getDataObserver() {
        return rDataObserver;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        if (rAdapter == null || rAdapter.getInnerAdapter() == null) return;
        rAdapter.getInnerAdapter().setOnItemClickListener(listener);
    }

    public
    @NonNull
    WrapperAdapter getAdapter() {
        return rAdapter;
    }

    public class WrapperAdapter<T> extends RecyclerView.Adapter<RViewHolder> {


        private RAdapter<T> rAdapter;

        public WrapperAdapter(@NonNull RAdapter rAdapter) {
            this.rAdapter = rAdapter;
        }

        @Override
        public RViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LogUtils.d("WrapperAdapter onCreateViewHolder start ");
            LogUtils.d("WrapperAdapter onCreateViewHolder parent = " + parent.toString() + " , viewType = " + viewType);
            if (viewType == RRecyclerViewHelper.TYPE_REFRESH_HEADER) {
                return new RViewHolder(getContext(), rHelper.getRefreshHeader());
            } else if (viewType == RRecyclerViewHelper.TYPE_LOAD_FOOTER) {
                return new RViewHolder(getContext(), rHelper.getLoadFooter());
            } else if (rHelper.isHeaderType(viewType)) {
                return new RViewHolder(getContext(), rHelper.getHeaderViewByType(viewType));
            } else if (rHelper.isFooterType(viewType)) {
                return new RViewHolder(getContext(), rHelper.getFooterViewByType(viewType));
            }
            return rAdapter.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(RViewHolder holder, int position) {
            LogUtils.d("WrapperAdapter onBindViewHolder start ");
            LogUtils.d("WrapperAdapter onBindViewHolder holder = " + holder.toString() + " , position = " + position);
            if (rHelper.isHeader(position) || rHelper.isRefreshHeader(position)) {
                return;
            }
            if (rHelper.isFooter(position) || rHelper.isLoadMoreFooter(position)) {
                return;
            }
            int realPosition = rHelper.getHeaderPosition(position);
            if (realPosition < rAdapter.getItemCount()) {
                rAdapter.onBindViewHolder(holder, realPosition);
            }
        }

        @Override
        public int getItemViewType(int position) {
            LogUtils.d("WrapperAdapter getItemViewType start ");
            LogUtils.d("WrapperAdapter getItemViewType position = " + position);
            int realPosition = rHelper.getHeaderPosition(position);
            if (rHelper.isReservedItemViewType(rAdapter.getItemViewType(realPosition))) {
                throw new IllegalStateException("RRecyclerView require itemViewType in adapter should be less than 10000 ");
            }
            if (rHelper.isRefreshHeader(position)) {
                return RRecyclerViewHelper.TYPE_REFRESH_HEADER;
            }
            if (rHelper.isHeader(position)) {
                position = position - 1;
                return rHelper.getHeaderTypes().get(position);
            }
            if (rHelper.isLoadMoreFooter(position)) {
                return RRecyclerViewHelper.TYPE_LOAD_FOOTER;
            }
            if (rHelper.isFooter(position)) {
                position = rHelper.getFooterPosition(position);
                return rHelper.getFooterTypes().get(position);
            }

            int adapterCount;
            adapterCount = rAdapter.getItemCount();
            if (realPosition < adapterCount) {
                return rAdapter.getItemViewType(realPosition);
            }
            return Constants.NO_VALUE;
        }

        @Override
        public int getItemCount() {
            LogUtils.d("WrapperAdapter getItemCount start ");
            int count = rAdapter.getItemCount();
            int adCount = 0;
            if (rHelper.canLoadMoreEnable()) {
                adCount += 1;
                if (rHelper.canPullRefreshEnable()) {
                    adCount += 1;
                }
                if (rHelper.getHeaderEnable()) {
                    adCount += rHelper.getHeaderViewCounts();
                }
                if (rHelper.getFooterEnable()) {
                    adCount += rHelper.getFooterViewCounts();
                }
            } else {
                if (rHelper.canPullRefreshEnable()) {
                    adCount += 1;
                }
                if (rHelper.getHeaderEnable()) {
                    adCount += rHelper.getHeaderViewCounts();
                }
                if (rHelper.getFooterEnable()) {
                    adCount += rHelper.getFooterViewCounts();
                }
            }
            int realCount = count + adCount;
            LogUtils.d("WrapperAdapter getItemCount = " + realCount);
            return realCount;
        }

        @Override
        public long getItemId(int position) {
            LogUtils.d("WrapperAdapter getItemId start ");
            long itemId = Constants.NO_VALUE;
            if (rHelper.canPullRefreshEnable()) {
                if (rAdapter != null && position >= rHelper.getHeaderViewCounts() + 1) {
                    int adjPosition = position - (rHelper.getHeaderViewCounts() + 1);
                    if (adjPosition < rAdapter.getItemCount()) {
                        itemId = rAdapter.getItemId(adjPosition);
                    }
                }
            } else {
                if (rAdapter != null && position >= rHelper.getHeaderViewCounts()) {
                    int adjPosition = position - rHelper.getHeaderViewCounts();
                    if (adjPosition < rAdapter.getItemCount()) {
                        itemId = rAdapter.getItemId(adjPosition);
                    }
                }
            }
            LogUtils.d("WrapperAdapter getItemId = " + itemId);
            return itemId;
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            LogUtils.d("WrapperAdapter onAttachedToRecyclerView start ");
            super.onAttachedToRecyclerView(recyclerView);
            RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
            if (manager instanceof GridLayoutManager) {
                final GridLayoutManager gridManager = ((GridLayoutManager) manager);
                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return (rHelper.isRefreshHeader(position) || rHelper.isLoadMoreFooter(position) ||
                                rHelper.isHeader(position) || rHelper.isFooter(position))
                                ? gridManager.getSpanCount() : 1;
                    }
                });
            }
            rAdapter.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
            LogUtils.d("WrapperAdapter onDetachedFromRecyclerView start ");
            rAdapter.onDetachedFromRecyclerView(recyclerView);
        }

        @Override
        public void onViewAttachedToWindow(RViewHolder holder) {
            LogUtils.d("WrapperAdapter onViewAttachedToWindow start ");
            LogUtils.d("WrapperAdapter onViewAttachedToWindow holder = " + holder.toString());
            super.onViewAttachedToWindow(holder);
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams
                    && (rHelper.isRefreshHeader(holder.getLayoutPosition()) || rHelper.isFooter(holder.getLayoutPosition()) ||
                    rHelper.isHeader(holder.getLayoutPosition()) || rHelper.isLoadMoreFooter(holder.getLayoutPosition()))) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
            rAdapter.onViewAttachedToWindow(holder);
        }

        @Override
        public void onViewDetachedFromWindow(RViewHolder holder) {
            LogUtils.d("WrapperAdapter onViewDetachedFromWindow start ");
            LogUtils.d("WrapperAdapter onViewDetachedFromWindow holder = " + holder.toString());
            super.onViewDetachedFromWindow(holder);
            rAdapter.onViewDetachedFromWindow(holder);
        }

        @Override
        public void onViewRecycled(RViewHolder holder) {
            LogUtils.d("WrapperAdapter onViewRecycled start ");
            LogUtils.d("WrapperAdapter onViewRecycled holder = " + holder.toString());
            rAdapter.onViewRecycled(holder);
        }

        @Override
        public boolean onFailedToRecycleView(RViewHolder holder) {
            LogUtils.d("WrapperAdapter onFailedToRecycleView start ");
            LogUtils.d("WrapperAdapter onFailedToRecycleView holder = " + holder.toString());
            return rAdapter.onFailedToRecycleView(holder);
        }

        @Override
        public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
            LogUtils.d("WrapperAdapter unregisterAdapterDataObserver start ");
            LogUtils.d("WrapperAdapter unregisterAdapterDataObserver observer = " + observer.toString());
            rAdapter.unregisterAdapterDataObserver(observer);
        }

        @Override
        public void registerAdapterDataObserver(AdapterDataObserver observer) {
            LogUtils.d("WrapperAdapter registerAdapterDataObserver start ");
            LogUtils.d("WrapperAdapter registerAdapterDataObserver observer = " + observer.toString());
            rAdapter.registerAdapterDataObserver(observer);
        }

        public RAdapter<T> getInnerAdapter() {
            return rAdapter;
        }
    }

    public class RDataObserver extends RecyclerView.AdapterDataObserver {

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
