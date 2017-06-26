package com.superc.lib.presenter;

import com.superc.lib.S;
import com.superc.lib.ui.SView;

/**
 * Created by superchen on 2017/5/10.
 */
public interface SPresenter<V extends SView> extends S{

    /**
     * 创建 CompositeSubscription 对象 使用CompositeSubscription来持有所有的Subscriptions，
     * 然后在onDestroy()或者onDestroyView()里取消所有的订阅。
     */
    void unSubscribe();
}
