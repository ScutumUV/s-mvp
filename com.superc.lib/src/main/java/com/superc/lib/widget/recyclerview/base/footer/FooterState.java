package com.superc.lib.widget.recyclerview.base.footer;

/**
 * Created by superchen on 2017/7/25.
 */
public interface FooterState {

    int STATE_LOADING = 0;
    int STATE_COMPLETE = 1;
    int STATE_NO_MORE = 2;

    void setStateLoading();

    void setStateComplete();

    void setStateNoMore();
}
