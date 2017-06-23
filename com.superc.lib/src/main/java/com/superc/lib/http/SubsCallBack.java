package com.superc.lib.http;


import retrofit2.HttpException;

/**
 * Created by superchen on 2017/6/19.
 */
public interface SubsCallBack<C> {

    void onCompleted();

    void onNext(C t);

    Boolean onError(Object e);
}
