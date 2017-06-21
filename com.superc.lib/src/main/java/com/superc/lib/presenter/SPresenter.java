package com.superc.lib.presenter;

import com.superc.lib.ui.SView;

/**
 * Created by superchen on 2017/5/10.
 */
public interface SPresenter<V extends SView> {

    void  unSubscribe();
}
