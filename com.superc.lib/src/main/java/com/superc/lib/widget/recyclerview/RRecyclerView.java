package com.superc.lib.widget.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.superc.lib.presenter.SPresenter;
import com.superc.lib.util.SUtil;

/**
 * Created by superchen on 2017/6/20.
 */
public class RRecyclerView<T extends SPresenter> extends RecyclerView {

    private T presenter;

    protected RAdapter rAdapter;


    public RRecyclerView(Context context) {
        super(context, null);
    }

    public RRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public RRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 创建相应的 presenter
     */
    public void createPresenter(T presenter) {
        SUtil.checkNull(presenter, "The RRecyclerView presenter not be null");
        this.presenter = presenter;
    }

    public <T extends RAdapter> void setAdapter(T adapter) {
        this.rAdapter = adapter;
    }
}
