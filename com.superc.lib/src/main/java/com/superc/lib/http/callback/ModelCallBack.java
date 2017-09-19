package com.superc.lib.http.callback;

import com.superc.lib.http.ApiModel;
import com.yanzhenjie.nohttp.rest.Response;

/**
 * Created by owner on 2017/8/29.
 */

public abstract class ModelCallBack<T, M extends ApiModel<M>> extends CallBack<T> {
    Class<M> clazz;

    public ModelCallBack(Class<M> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void onSucceed(int what, Response<T> response) {
    }

    @Override
    public void onFailed(int what, Response<T> response, Exception e) {

    }

    @Override
    public boolean onFailed(int what, String message) {
        return false;
    }

    public Class<M> getClazz() {
        return clazz;
    }

    public abstract void onModelSucceed(int what, Response<T> response, M m);
}
