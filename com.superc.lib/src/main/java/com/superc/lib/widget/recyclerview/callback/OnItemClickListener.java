package com.superc.lib.widget.recyclerview.callback;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 列表View的点击事件
 * <p>
 * Created by superchen on  2017/9/11
 * The last edit time       2017/9/11
 */
public abstract class OnItemClickListener<T, VH extends RecyclerView.ViewHolder> implements OnClickListener<T> {

    @Override
    public void onClick(View view, T model) {
    }

    public void onItemClick(View view, VH holder, T model, int position) {
    }

    public boolean onItemLongClick(View view, VH holder, T model, int position) {
        return true;
    }
}
