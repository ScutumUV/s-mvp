package com.superc.lib.widget.recyclerview.callback;

/**
 * RRecyclerView 下拉刷新和加载更多监听
 * <p>
 * Created by superchen on 2017/7/19.
 */
public interface RRecycleViewLoadListener {

    /**
     * 刷新回调
     */
    void onRefresh();

    /**
     * 加载更多回调
     */
    void onLoadMore();
}
