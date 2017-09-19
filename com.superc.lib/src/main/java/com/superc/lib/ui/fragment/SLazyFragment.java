package com.superc.lib.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.superc.lib.presenter.SPresenter;

/**
 * Created by owner on 2017/8/16.
 */
public abstract class SLazyFragment<T extends SPresenter> extends SFragment<T> {

    /**
     * Fragment的View加载完毕的标记
     */
    private boolean isPrepared;
    /**
     * Fragment对用户可见的标记
     */
    private boolean isVisible;
    private boolean isFirst = true;

    //--------------------system method callback------------------------//

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isPrepared = true;
        initPrepare();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            lazyLoad();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            setUserVisibleHint(true);
        }
    }

    //--------------------------------method---------------------------//

    /**
     * 懒加载
     */
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || !isFirst) {
            return;
        }
        lazyLoadData();
        isFirst = false;
    }

    //--------------------------abstract method------------------------//

    /**
     * 在onActivityCreated中调用的方法，可以用来进行初始化操作。
     */
    protected void initPrepare() {
    }

    /**
     * fragment被设置为不可见时调用
     */
    protected abstract void onInvisible();

    /**
     * 这里获取数据，刷新界面
     */
    protected abstract void lazyLoadData();

}
