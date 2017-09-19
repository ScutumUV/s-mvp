package com.superc.lib.widget.recyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.superc.lib.widget.recyclerview.callback.OnItemClickListener;
import com.superc.lib.widget.recyclerview.holder.ItemViewDelegate;
import com.superc.lib.widget.recyclerview.holder.ItemViewDelegateManager;
import com.superc.lib.widget.recyclerview.holder.RViewHolder;

import java.util.List;

/**
 * Created by superchen on 2017/6/20.
 */
public abstract class WrapperAdapter<T> extends RecyclerView.Adapter<RViewHolder> {

    protected List<T> mDatas;
    protected Context mContext;
    protected ItemViewDelegateManager<T> mItemViewDelegateManager;
    protected OnItemClickListener mItemClickListener;

    public WrapperAdapter(Context context, List<T> list) {
        this.mContext = context;
        mDatas = list;
        mItemViewDelegateManager = new ItemViewDelegateManager<T>();
    }

    @Override
    public RViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewDelegate itemViewDelegate = mItemViewDelegateManager.getItemViewDelegate(viewType);
        itemViewDelegate.setAdapter(WrapperAdapter.this);
        int layoutId = itemViewDelegate.setLayoutResId();
        RViewHolder holder = RViewHolder.createViewHolder(mContext, parent, layoutId);
        onViewHolderCreated(holder, holder.getConvertView());
        return holder;
    }

    @Override
    public void onBindViewHolder(RViewHolder holder, int position) {
        if (checkPositionAvailable(position)) {
            mItemViewDelegateManager.onBindViewHolder(holder, mDatas.get(position), position);
            setListener(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
//        LogUtils.d("RAdapter getItemViewType position = " + position);
        if (checkPositionAvailable(position)) {
            if (!useItemViewDelegateManager()) {
                return super.getItemViewType(position);
            }
            int type = mItemViewDelegateManager.getItemViewType(mDatas.get(position), position);
            return type;
        } else {
            return 0;
        }
    }

    public void onViewHolderCreated(RViewHolder holder, View itemView) {

    }

    protected void setListener(final RViewHolder viewHolder, final int position) {
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(v, viewHolder, mDatas.get(position), position);
                }
            }
        });

        viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mItemClickListener != null) {
                    return mItemClickListener.onItemLongClick(v, viewHolder, mDatas.get(position), position);
                }
                return false;
            }
        });
    }

    protected boolean checkPositionAvailable(int position) {
        if (!mDatas.isEmpty() && position >= 0 && position <= ((!mDatas.isEmpty()) ? mDatas.size() - 1 : 0)) {
            return true;
        } else {
            return false;
        }
    }

    protected boolean useItemViewDelegateManager() {
        return mItemViewDelegateManager.getItemViewDelegateCount() > 0;
    }

    public WrapperAdapter<T> addItemViewDelegate(ItemViewDelegate itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(itemViewDelegate);
        return this;
    }

    public WrapperAdapter<T> addItemViewDelegate(int viewType, ItemViewDelegate itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(viewType, itemViewDelegate);
        return this;
    }

    public List<T> getDatas() {
        return mDatas;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }
}
