package com.superc.lib.presenter;

import com.superc.lib.model.SBaseModel;
import com.superc.lib.ui.SView;
import com.superc.lib.util.SUtil;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.CallBack;
import com.zhouyou.http.callback.CallBackProxy;
import com.zhouyou.http.callback.CallClazzProxy;
import com.zhouyou.http.model.ApiResult;

import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;


/**
 * Created by superchen on 2017/5/16.
 * <p>
 * dd
 */
public class SBasePresenterLmp<V extends SView> implements SPresenter {

    protected V mView;

    protected SBaseModel mModel;

    public SBasePresenterLmp(V mView) {
        SUtil.checkNull(mView, "The PresenterLmpView not be null");
        this.mView = mView;
    }

    protected <T> Observable get(String url, Class<T> clazz) {
        return EasyHttp.get(url).execute(clazz);
    }

    public <T> Disposable get(String url, CallBack<T> callBack) {
        return EasyHttp.get(url).execute(new CallBackProxy<ApiResult<T>, T>(callBack) {
        });
    }

    public <T> Disposable get(String url, CallBackProxy<? extends ApiResult<T>, T> proxy) {
        return EasyHttp.get(url).execute(proxy);
    }

    public <T> Observable get(String url, Type type) {
        return EasyHttp.get(url).execute(new CallClazzProxy<ApiResult<T>, T>(type) {
        });
    }

    protected <T> Observable post(String url, Class<T> clazz) {
        return EasyHttp.post(url).execute(clazz);
    }

    protected <T> Observable post(String url, Type type) {
        return EasyHttp.post(url).execute(type);
    }

    protected <T> Observable post(String url, CallClazzProxy<? extends ApiResult<T>, T> proxy) {
        return EasyHttp.post(url).execute(proxy);
    }

    protected <T> Disposable post(String url, CallBack<T> callBack) {
        return EasyHttp.post(url).execute(callBack);
    }

    protected <T> Disposable post(String url, CallBackProxy<? extends ApiResult<T>, T> proxy) {
        return EasyHttp.post(url).execute(proxy);
    }

    protected <T> Disposable delete(String url, CallBack<T> callBack) {
        return EasyHttp.delete(url).execute(callBack);
    }

    protected <T> Disposable delete(String url, CallBackProxy<? extends ApiResult<T>, T> proxy) {
        return EasyHttp.delete(url).execute(proxy);
    }

    @Override
    public void unSubscribe(Disposable subscriber) {
//        EasyHttp.cancelDisposable(subscriber);
    }

}
