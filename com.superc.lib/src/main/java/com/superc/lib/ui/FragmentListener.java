package com.superc.lib.ui;

import com.superc.lib.presenter.SPresenter;

/**
 * 对于Fragment想要取得宿主Activity的Presenter和View的可以让宿主Activity实现该接口
 * <p>
 * Created by superChen on  2017/8/30
 * The last edit time       2017/8/30
 */
public interface FragmentListener<P extends SPresenter, V extends SView> {

    P getHoldingActivityPresenter();

    V getHoldingActivityView();
}
