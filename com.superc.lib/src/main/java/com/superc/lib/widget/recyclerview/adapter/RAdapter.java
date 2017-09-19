package com.superc.lib.widget.recyclerview.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ViewGroup;

import com.superc.lib.constants.Constants;
import com.superc.lib.widget.recyclerview.holder.RViewHolder;
import com.superc.lib.widget.recyclerview.util.RRecyclerViewHelper;

/**
 * Created by superchen on 2017/6/20.
 */
public class RAdapter<T extends WrapperAdapter<T>> extends RecyclerView.Adapter<RViewHolder> {


    private T cAdapter;
    protected Context mContext;
    protected RRecyclerViewHelper rHelper;

    public RAdapter(@NonNull Context context, @NonNull T cAdapter) {
        this.mContext = context;
        this.cAdapter = cAdapter;
    }

    public RAdapter(@NonNull Context context, @NonNull T cAdapter, @NonNull RRecyclerViewHelper rHelper) {
        this.mContext = context;
        this.cAdapter = cAdapter;
        this.rHelper = rHelper;
    }

    @Override
    public RViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            LogUtils.d("WrapperAdapter onCreateViewHolder start ");
//            LogUtils.d("WrapperAdapter onCreateViewHolder parent = " + parent.toString() + " , viewType = " + viewType);
        if (viewType == RRecyclerViewHelper.TYPE_REFRESH_HEADER) {
            return new RViewHolder(mContext, rHelper.getRefreshHeader());
        } else if (viewType == RRecyclerViewHelper.TYPE_LOAD_FOOTER) {
            return new RViewHolder(mContext, rHelper.getLoadFooter());
        } else if (rHelper.isHeaderType(viewType)) {
            return new RViewHolder(mContext, rHelper.getHeaderViewByType(viewType));
        } else if (rHelper.isFooterType(viewType)) {
            return new RViewHolder(mContext, rHelper.getFooterViewByType(viewType));
        }
        return cAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RViewHolder holder, int position) {
//            LogUtils.d("WrapperAdapter onBindViewHolder start ");
//            LogUtils.d("WrapperAdapter onBindViewHolder holder = " + holder.toString() + " , position = " + position);
        if (rHelper.isHeader(position) || rHelper.isRefreshHeader(position)) {
            return;
        }
        if (rHelper.isFooter(position) || rHelper.isLoadMoreFooter(position)) {
            return;
        }
        int realPosition = rHelper.getHeaderPosition(position);
        if (realPosition < cAdapter.getItemCount()) {
            cAdapter.onBindViewHolder(holder, realPosition);
        }
    }

    @Override
    public int getItemViewType(int position) {
//            LogUtils.d("WrapperAdapter getItemViewType start ");
//            LogUtils.d("WrapperAdapter getItemViewType position = " + position);
        int realPosition = rHelper.getHeaderPosition(position);
        if (rHelper.isReservedItemViewType(cAdapter.getItemViewType(realPosition))) {
            throw new IllegalStateException("RRecyclerView require itemViewType in adapter should be less than 10000 ");
        }
        if (rHelper.isRefreshHeader(position)) {
            return RRecyclerViewHelper.TYPE_REFRESH_HEADER;
        }
        if (rHelper.isHeader(position)) {
            position = position - 1;
            return (int) rHelper.getHeaderTypes().get(position);
        }
        if (rHelper.isLoadMoreFooter(position)) {
            return RRecyclerViewHelper.TYPE_LOAD_FOOTER;
        }
        if (rHelper.isFooter(position)) {
            position = rHelper.getFooterPosition(position);
            return (int) rHelper.getFooterTypes().get(position);
        }

        int adapterCount;
        adapterCount = cAdapter.getItemCount();
        if (realPosition < adapterCount) {
            return cAdapter.getItemViewType(realPosition);
        }
        return Constants.NO_VALUE;
    }

    @Override
    public int getItemCount() {
//            LogUtils.d("WrapperAdapter getItemCount start ");
        int count = cAdapter.getItemCount();
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
//            LogUtils.d("WrapperAdapter getItemCount = " + realCount);
        return realCount;
    }

    @Override
    public long getItemId(int position) {
//            LogUtils.d("WrapperAdapter getItemId start ");
        long itemId = Constants.NO_VALUE;
        if (rHelper.canPullRefreshEnable()) {
            if (cAdapter != null && position >= rHelper.getHeaderViewCounts() + 1) {
                int adjPosition = position - (rHelper.getHeaderViewCounts() + 1);
                if (adjPosition < cAdapter.getItemCount()) {
                    itemId = cAdapter.getItemId(adjPosition);
                }
            }
        } else {
            if (cAdapter != null && position >= rHelper.getHeaderViewCounts()) {
                int adjPosition = position - rHelper.getHeaderViewCounts();
                if (adjPosition < cAdapter.getItemCount()) {
                    itemId = cAdapter.getItemId(adjPosition);
                }
            }
        }
//            LogUtils.d("WrapperAdapter getItemId = " + itemId);
        return itemId;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
//            LogUtils.d("WrapperAdapter onAttachedToRecyclerView start ");
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
        cAdapter.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
//            LogUtils.d("WrapperAdapter onDetachedFromRecyclerView start ");
        cAdapter.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public void onViewAttachedToWindow(RViewHolder holder) {
//            LogUtils.d("WrapperAdapter onViewAttachedToWindow start ");
//            LogUtils.d("WrapperAdapter onViewAttachedToWindow holder = " + holder.toString());
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams
                && (rHelper.isRefreshHeader(holder.getLayoutPosition()) || rHelper.isFooter(holder.getLayoutPosition()) ||
                rHelper.isHeader(holder.getLayoutPosition()) || rHelper.isLoadMoreFooter(holder.getLayoutPosition()))) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
        cAdapter.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(RViewHolder holder) {
//            LogUtils.d("WrapperAdapter onViewDetachedFromWindow start ");
//            LogUtils.d("WrapperAdapter onViewDetachedFromWindow holder = " + holder.toString());
        super.onViewDetachedFromWindow(holder);
        cAdapter.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onViewRecycled(RViewHolder holder) {
//            LogUtils.d("WrapperAdapter onViewRecycled start ");
//            LogUtils.d("WrapperAdapter onViewRecycled holder = " + holder.toString());
        cAdapter.onViewRecycled(holder);
    }

    @Override
    public boolean onFailedToRecycleView(RViewHolder holder) {
//            LogUtils.d("WrapperAdapter onFailedToRecycleView start ");
//            LogUtils.d("WrapperAdapter onFailedToRecycleView holder = " + holder.toString());
        return cAdapter.onFailedToRecycleView(holder);
    }

    @Override
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
//            LogUtils.d("WrapperAdapter unregisterAdapterDataObserver start ");
//            LogUtils.d("WrapperAdapter unregisterAdapterDataObserver observer = " + observer.toString());
        cAdapter.unregisterAdapterDataObserver(observer);
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
//            LogUtils.d("WrapperAdapter registerAdapterDataObserver start ");
//            LogUtils.d("WrapperAdapter registerAdapterDataObserver observer = " + observer.toString());
        cAdapter.registerAdapterDataObserver(observer);
    }

    public WrapperAdapter<T> getInnerAdapter() {
        return cAdapter;
    }
}
