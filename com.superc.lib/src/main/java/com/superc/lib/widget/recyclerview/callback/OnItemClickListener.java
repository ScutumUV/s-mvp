package com.superc.lib.widget.recyclerview.callback;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by superchen on 2017/7/26.
 */
public abstract class OnItemClickListener<T> implements OnClickListener<T> {

    @Override
    public void onClick(View view, T t) {
    }

    public void onItemClick(View view, RecyclerView.ViewHolder holder, T t, int position) {

    }

    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, T t, int position) {
        return true;
    }
}
