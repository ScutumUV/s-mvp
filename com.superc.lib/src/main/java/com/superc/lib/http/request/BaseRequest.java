package com.superc.lib.http.request;

import android.support.annotation.NonNull;

import com.superc.lib.http.SHttpService;
import com.superc.lib.http.SubsCallBack;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by superchen on 2017/6/26.
 */
public class BaseRequest<R> implements SHttpService {

    @Override
    public void unSubscribe() {

    }

    @Override
    public <T> T create(@NonNull Class<T> serviceInterface) {
        return null;
    }

    @Override
    public <T> Subscriber createNewSubscriber(SubsCallBack callBack) {
        return null;
    }

    @Override
    public <T> Observable<T> applySchedulers(@NonNull Observable<T> responseObservable) {
        return null;
    }

    @Override
    public <T> Observable<T> flatResponse(@NonNull T response) {
        return null;
    }
}
