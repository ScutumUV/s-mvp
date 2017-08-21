package com.superc.lib.http;

import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.CallBack;
import com.zhouyou.http.callback.CallBackProxy;
import com.zhouyou.http.callback.CallClazzProxy;
import com.zhouyou.http.model.ApiResult;

import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;


/**
 * RetrofitService管理类
 * <p>
 * create by SuperChen    2017/06/15
 * <p>
 * 暂无法实现在使用过程中更换BaseUrl
 */
public final class SRXHttp implements SApiService {


    @Override
    public String getBaseUrl() {
        return EasyHttp.getBaseUrl();
    }

    public <T> Observable<T> get(String url, Class<T> clazz) {
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

    public <T> Observable post(String url, Class<T> clazz) {
        return EasyHttp.post(url).execute(clazz);
    }

    public <T> Observable post(String url, Type type) {
        return EasyHttp.post(url).execute(type);
    }

    public <T> Observable post(String url, CallClazzProxy<? extends ApiResult<T>, T> proxy) {
        return EasyHttp.post(url).execute(proxy);
    }

    public <T> Disposable post(String url, CallBack<T> callBack) {
        return EasyHttp.post(url).execute(callBack);
    }

    public <T> Disposable post(String url, CallBackProxy<? extends ApiResult<T>, T> proxy) {
        return EasyHttp.post(url).execute(proxy);
    }

    public <T> Disposable delete(String url, CallBack<T> callBack) {
        return EasyHttp.delete(url).execute(callBack);
    }

    public <T> Disposable delete(String url, CallBackProxy<? extends ApiResult<T>, T> proxy) {
        return EasyHttp.delete(url).execute(proxy);
    }

    @Override
    public <T> Observable put(String url, Class<T> clazz) {
        return EasyHttp.put(url).execute(clazz);
    }

    @Override
    public <T> Observable put(String url, Type type) {
        return EasyHttp.put(url).execute(type);
    }

    @Override
    public <T> Observable put(String url, CallClazzProxy<? extends ApiResult<T>, T> proxy) {
        return EasyHttp.put(url).execute(proxy);
    }

    @Override
    public <T> Disposable put(String url, CallBack<T> callBack) {
        return EasyHttp.put(url).execute(callBack);
    }

    @Override
    public <T> Disposable put(String url, CallBackProxy<? extends ApiResult<T>, T> proxy) {
        return EasyHttp.put(url).execute(proxy);
    }
}
