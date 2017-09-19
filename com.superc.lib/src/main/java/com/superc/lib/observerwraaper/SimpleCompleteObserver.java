package com.superc.lib.observerwraaper;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by owner on 2017/8/22.
 */
public abstract class SimpleCompleteObserver<T> implements Observer<T> {

    @Override
    public void onNext(T t) {
    }

    @Override
    public void onError(Throwable e) {
    }

    @Override
    public void onSubscribe(Disposable d) {
    }
}
