package com.superc.lib.ui;

import com.superc.lib.presenter.SPresenter;

/**
 * 对于想让Fragment的View接口获取宿主Activity的Presenter和View，可以使用该接口，用法如下注释部分
 * <p>
 * Created by owner on  2017/8/30
 * The last edit time   2017/8/30
 */
public interface SConnectionActivityFragmentView<P extends SPresenter, HP extends SPresenter, V extends SView> extends SView<P> {

    V getHoldingActivityView();

    HP getHoldingActivityPresenter();


}

/*
abstract class A extends SActivity implements FragmentListener<ActivityWrapper.P, ActivityWrapper.V>, ActivityWrapper.V {
    @Override
    public ActivityWrapper.P getHoldingActivityPresenter() {
        return null;
    }

    @Override
    public ActivityWrapper.V getHoldingActivityView() {
        return null;
    }
}

interface ActivityWrapper {
    interface V extends SView<P> {
    }

    interface P extends SPresenter {
    }
}

abstract class B extends SFragment<FragmentWrapper.P> implements FragmentWrapper.V {

    @Override
    public SView getHoldingActivityView() {
        return tFragmentListener.getHoldingActivityView();
    }

    @Override
    public SPresenter getHoldingActivityPresenter() {
        return tFragmentListener.getHoldingActivityPresenter();
    }
}

interface FragmentWrapper {
    interface V extends SConnectionActivityFragmentView<P> {
    }

    interface P extends SPresenter {
    }
}
*/
