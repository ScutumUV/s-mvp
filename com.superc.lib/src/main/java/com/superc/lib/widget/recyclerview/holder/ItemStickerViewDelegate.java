package com.superc.lib.widget.recyclerview.holder;

import android.support.annotation.LayoutRes;

import com.superc.lib.presenter.SPresenter;
import com.superc.lib.ui.SView;
import com.superc.lib.widget.recyclerview.adapter.WrapperStickerAdapter;

/**
 * Created by superchen on 2017/7/26.
 */
public abstract class ItemStickerViewDelegate<T, V extends SView, P extends SPresenter> implements SPresenter<V> {

    protected V sV;
    protected P sP;
    protected WrapperStickerAdapter<T> sAdapter;

    public void setAdapter(WrapperStickerAdapter<T> mAdapter) {
        this.sAdapter = mAdapter;
    }

    public ItemStickerViewDelegate(V v, P p) {
        this.sV = v;
        this.sP = p;
    }

    public abstract boolean isStickerHeaderForViewType(T item, int position);

    public abstract void onBindStickerHeaderViewHolder(RViewHolder holder, T t, int position);

    @LayoutRes
    public abstract int setStickerHeaderLayoutResId();

    public abstract long getStickerHeaderId(T t, int position);

    @Override
    public void destroyAllNetTask(V v) {
        sV.destroyAllNetTask();
    }
}
