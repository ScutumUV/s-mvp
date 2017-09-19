package com.superc.lib.widget.recyclerview.holder;

import android.support.v4.util.SparseArrayCompat;

/**
 * Created by zhy on 16/6/22.
 */
public class ItemViewDelegateManager<T> {
    SparseArrayCompat<ItemViewDelegate> delegates = new SparseArrayCompat();
    SparseArrayCompat<ItemStickerViewDelegate> stickerDelegates = new SparseArrayCompat();

    public int getItemViewDelegateCount() {
        return delegates.size();
    }

    public ItemViewDelegateManager addDelegate(ItemViewDelegate delegate) {
        int viewType = delegates.size();
        if (delegate != null) {
            delegates.put(viewType, delegate);
        }
        return this;
    }

    public ItemViewDelegateManager addDelegate(int viewType, ItemViewDelegate delegate) {
        if (delegates.get(viewType) != null) {
            throw new IllegalArgumentException(
                    "An ItemViewDelegate is already registered for the viewType = "
                            + viewType
                            + ". Already registered ItemViewDelegate is "
                            + delegates.get(viewType));
        }
        delegates.put(viewType, delegate);
        return this;
    }

    public ItemViewDelegateManager addStickerDelegate(ItemStickerViewDelegate delegate) {
        if (delegate != null) {
            stickerDelegates.put(stickerDelegates.size(), delegate);
        }
        return this;
    }

    public ItemViewDelegateManager addStickerDelegate(int viewType, ItemStickerViewDelegate delegate) {
        if (stickerDelegates.get(viewType) != null) {
            throw new IllegalArgumentException(
                    "An ItemStickerViewDelegate is already registered for the viewType = "
                            + viewType
                            + ". Already registered ItemViewDelegate is "
                            + stickerDelegates.get(viewType));
        }
        stickerDelegates.put(viewType, delegate);
        return this;
    }

    public ItemViewDelegateManager<T> removeDelegate(ItemViewDelegate delegate) {
        if (delegate == null) {
            throw new NullPointerException("ItemViewDelegate is null");
        }
        int indexToRemove = delegates.indexOfValue(delegate);

        if (indexToRemove >= 0) {
            delegates.removeAt(indexToRemove);
        }
        return this;
    }

    public ItemViewDelegateManager<T> removeDelegate(int itemType) {
        int indexToRemove = delegates.indexOfKey(itemType);

        if (indexToRemove >= 0) {
            delegates.removeAt(indexToRemove);
        }
        return this;
    }

    public ItemViewDelegateManager<T> removeStickerDelegate(ItemStickerViewDelegate delegate) {
        if (delegate == null) {
            throw new NullPointerException("ItemStickerViewDelegate is null");
        }
        int indexToRemove = stickerDelegates.indexOfValue(delegate);

        if (indexToRemove >= 0) {
            stickerDelegates.removeAt(indexToRemove);
        }
        return this;
    }

    public ItemViewDelegateManager<T> removeStickerDelegate(int itemType) {
        int indexToRemove = stickerDelegates.indexOfKey(itemType);

        if (indexToRemove >= 0) {
            stickerDelegates.removeAt(indexToRemove);
        }
        return this;
    }

    public SparseArrayCompat<ItemViewDelegate> getDelegates() {
        return delegates;
    }

    public SparseArrayCompat<ItemStickerViewDelegate> getStickerDelegates() {
        return stickerDelegates;
    }

    public int getItemViewType(T item, int position) {
//        LogUtils.d("ItemViewDelegateManager getItemViewType position = " + position);
        int delegatesCount = delegates.size();
        for (int i = delegatesCount - 1; i >= 0; i--) {
            ItemViewDelegate delegate = delegates.valueAt(i);
            if (delegate.isForViewType(item, position)) {
                return delegates.keyAt(i);
            }
        }
        throw new IllegalArgumentException(
                "No ItemViewDelegate added that matches position=" + position + " in data source");
    }

    public int getStickerItemViewType(T item, int position) {
//        LogUtils.d("ItemViewDelegateManager getItemViewType position = " + position);
        int delegatesCount = stickerDelegates.size();
        for (int i = delegatesCount - 1; i >= 0; i--) {
            ItemStickerViewDelegate delegate = stickerDelegates.valueAt(i);
            if (delegate.isStickerHeaderForViewType(item, position)) {
                return stickerDelegates.keyAt(i);
            }
        }
        throw new IllegalArgumentException(
                "No ItemStickerViewDelegate added that matches position=" + position + " in data source");
    }

    public void onBindViewHolder(RViewHolder holder, T item, int position) {
        int delegatesCount = delegates.size();
        for (int i = 0; i < delegatesCount; i++) {
            ItemViewDelegate delegate = delegates.valueAt(i);

            if (delegate.isForViewType(item, position)) {
                delegate.onBindViewHolder(holder, item, position);
                return;
            }
        }
        throw new IllegalArgumentException(
                "No ItemViewDelegateManager added that matches position=" + position + " in data source");
    }

    public void onBindStickerHeaderViewHolder(RViewHolder holder, T item, int position) {
        int delegatesCount = stickerDelegates.size();
        for (int i = 0; i < delegatesCount; i++) {
            ItemStickerViewDelegate delegate = stickerDelegates.valueAt(i);

            if (delegate.isStickerHeaderForViewType(item, position)) {
                delegate.onBindStickerHeaderViewHolder(holder, item, position);
                return;
            }
        }
        throw new IllegalArgumentException(
                "No ItemViewDelegateManager added that matches position=" + position + " in data source");
    }


    public ItemViewDelegate getItemViewDelegate(int viewType) {
        return delegates.get(viewType);
    }

    public ItemStickerViewDelegate getSickerItemViewDelegate(int viewType) {
        return stickerDelegates.get(viewType);
    }

    public int getItemViewLayoutId(int viewType) {
        return getItemViewDelegate(viewType).setLayoutResId();
    }

    public int getStickerItemViewLayoutId(int viewType) {
        return getSickerItemViewDelegate(viewType).setStickerHeaderLayoutResId();
    }

    public int getItemViewType(ItemViewDelegate itemViewDelegate) {
        return delegates.indexOfValue(itemViewDelegate);
    }

    public int getItemViewType(ItemStickerViewDelegate itemViewDelegate) {
        return stickerDelegates.indexOfValue(itemViewDelegate);
    }
}
