package com.superc.lib.widget.recyclerview.holder;

import android.support.annotation.LayoutRes;

import com.superc.lib.presenter.SPresenter;
import com.superc.lib.ui.SView;
import com.superc.lib.widget.recyclerview.adapter.WrapperAdapter;

/**
 * Created by superchen on 2017/7/26.
 */
public abstract class ItemViewDelegate<T, V extends SView, P extends SPresenter> implements SPresenter<V> {

    protected V sV;
    protected P sP;
    protected WrapperAdapter<T> sAdapter;

    public void setAdapter(WrapperAdapter<T> mAdapter) {
        this.sAdapter = mAdapter;
    }

    public ItemViewDelegate(V v, P p) {
        this.sV = v;
        this.sP = p;
    }


    @LayoutRes
    public abstract int setLayoutResId();

    public abstract boolean isForViewType(T item, int position);

    public abstract void onBindViewHolder(RViewHolder holder, T t, int position);

    @Override
    public void destroyAllNetTask(V v) {
        sV.destroyAllNetTask();
    }
}
