package com.superc.lib.widget.recyclerview.base.header;

/**
 * Created by superchen on 2017/7/24.
 */
public interface HeaderState {

    int STATE_NORMAL = 0;
    int STATE_RELEASE_TO_REFRESH = 1;
    int STATE_REFRESHING = 2;
    int STATE_DONE = 3;
    int STATE_NET_ERROR = 4;
    int STATE_NO_NET = 5;

    int ROTATE_ANIM_DURATION = 180;

    void setNormalState();

    void setRefreshingState();

    void setDoneState();

    void setReleaseToRefreshState();

    void onMove(float delta);

    boolean releaseAction();

    void refreshComplete();

    void reset();
}
