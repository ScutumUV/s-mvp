package com.superc.lib.presenter;

import com.superc.lib.http.HttpManager;
import com.superc.lib.model.SBaseModel;
import com.superc.lib.ui.SView;
import com.superc.lib.util.SUtil;


/**
 * Created by superchen on 2017/5/16.
 * <p>
 * dd
 */
public class SBasePresenterLmp<V extends SView> implements SPresenter {

    protected V mView;

    protected SBaseModel mModel;

    protected HttpManager mHttp;


    public SBasePresenterLmp(V mView) {
        SUtil.checkNull(mView, "The PresenterLmpView not be null");
        this.mView = mView;
        mHttp = mView.getHttpManager();
    }

    @Override
    public void destroyAllNetTask(SView sView) {
        mView.destroyAllNetTask();
    }
}
