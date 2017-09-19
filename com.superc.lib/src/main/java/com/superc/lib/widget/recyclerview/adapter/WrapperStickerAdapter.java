package com.superc.lib.widget.recyclerview.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.superc.lib.constants.Constants;
import com.superc.lib.widget.recyclerview.StickyVIew.StickyRecyclerHeadersAdapter;
import com.superc.lib.widget.recyclerview.holder.ItemStickerViewDelegate;
import com.superc.lib.widget.recyclerview.holder.RViewHolder;

import java.util.List;

/**
 * Created by superchen on 2017/6/20.
 */
public abstract class WrapperStickerAdapter<T> extends WrapperAdapter<T> implements StickyRecyclerHeadersAdapter<RViewHolder<T>> {

    public WrapperStickerAdapter(Context context, List<T> list) {
        super(context, list);
    }

    @Override
    public long getHeaderId(int position) {
        ItemStickerViewDelegate delegate = mItemViewDelegateManager.getSickerItemViewDelegate(0);
        if (delegate != null) {
            return delegate.getStickerHeaderId(mDatas.get(position), position);
        }
        return Constants.NO_VALUE;
    }

    @Override
    public RViewHolder<T> onCreateHeaderViewHolder(ViewGroup parent) {
        if (mItemViewDelegateManager.getStickerDelegates().size() == 0) return null;
        ItemStickerViewDelegate delegate = mItemViewDelegateManager.getSickerItemViewDelegate(0);
        if (delegate == null) return null;
        delegate.setAdapter(WrapperStickerAdapter.this);
        int layoutId = delegate.setStickerHeaderLayoutResId();
        RViewHolder holder = RViewHolder.createViewHolder(mContext, parent, layoutId);
        return holder;
    }

    @Override
    public void onBindHeaderViewHolder(RViewHolder<T> holder, int position) {
        if (checkPositionAvailable(position)) {
            mItemViewDelegateManager.onBindStickerHeaderViewHolder(holder, mDatas.get(position), position);
        }
    }

    @Override
    public int getHeaderItemCount() {
        return getItemCount();
    }

    public WrapperStickerAdapter<T> addStickerItemViewDelegate(ItemStickerViewDelegate itemViewDelegate) {
        mItemViewDelegateManager.addStickerDelegate(itemViewDelegate);
        return this;
    }
}
