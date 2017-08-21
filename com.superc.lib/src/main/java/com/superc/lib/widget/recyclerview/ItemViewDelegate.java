package com.superc.lib.widget.recyclerview;

import android.support.annotation.LayoutRes;

/**
 * Created by superchen on 2017/7/26.
 */

public interface ItemViewDelegate<T> {

    @LayoutRes
    int setLayoutResId();

    boolean isForViewType(T item, int position);

    void onBindViewHolder(RViewHolder holder, T t, int position);
}
