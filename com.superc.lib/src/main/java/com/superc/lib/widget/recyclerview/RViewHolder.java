package com.superc.lib.widget.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.superc.lib.presenter.SPresenter;

/**
 * Created by superchen on 2017/6/20.
 */

public class RViewHolder<V extends SPresenter> extends RecyclerView.ViewHolder {

    public RViewHolder(View itemView) {
        super(itemView);
    }
}
