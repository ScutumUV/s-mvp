package com.superc.lib.presenter;

import com.superc.lib.http.HttpExceptionEntity;
import com.superc.lib.http.SHttpService;
import com.superc.lib.http.SRXHttp;
import com.superc.lib.http.SimpleSubsCallBack;
import com.superc.lib.http.SubsCallBack;
import com.superc.lib.model.SBaseModel;
import com.superc.lib.ui.SView;
import com.superc.lib.util.SUtil;
import com.superc.lib.util.StringUtils;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by superchen on 2017/5/16.
 * <p>
 * dd
 */
public class SBasePresenterLmp<V extends SView> implements SPresenter {

    protected V mBaseView;

    protected SBaseModel mModel;

    protected SHttpService mHttpManager;


    public SBasePresenterLmp(V baseView) {
        SUtil.checkNull(baseView, "The BaseView not be null");
        mBaseView = baseView;
        mHttpManager = new SRXHttp.Builder().build();
    }

    protected <T> T create(Class<T> serviceInterface) {
        return mHttpManager.create(serviceInterface);
    }

    protected <T> Observable<T> applySchedulers(Observable<T> responseObservable) {
        return mHttpManager.applySchedulers(responseObservable);
    }

    protected <T> Observable<T> flatResponse(final T response) {
        return mHttpManager.flatResponse(response);
    }

    protected <T> Subscriber createNewSubscriber(final SimpleSubsCallBack callBack) {
        return mHttpManager.createNewSubscriber(new SubsCallBack<T>() {
            @Override
            public void onCompleted() {
                if (mBaseView != null) {
                    mBaseView.hideLoadingDialog();
                }
                callBack.onCompleted();
            }

            @Override
            public void onNext(T t) {
                callBack.onNext(t);
            }

            @Override
            public Boolean onError(Object e) {
                if (mBaseView != null) {
                    mBaseView.hideLoadingDialog();
                }
                if (callBack.onError(e) != null && callBack.onError(e)) {
                    if (e instanceof HttpExceptionEntity) {
                        HttpExceptionEntity t = (HttpExceptionEntity) e;
                        if (!StringUtils.isEmpty(t.getMessage())) {
                            mBaseView.showToastShort(t.getMessage());
                        }
                    } else {
                        callBack.onError(e);
                    }
                } else {
                    callBack.onError(e);
                }
                return callBack.onError(e);
            }
        });
    }

    @Override
    public void unSubscribe() {
        mHttpManager.unSubscribe();
    }

    public void back() {

    }


    public void back(String className) {

    }
}
