package com.superc.lib.http;


import retrofit2.HttpException;

/**
 * Created by superchen on 2017/6/19.
 */
public abstract class SimpleSubsCallBack<C> implements SubsCallBack<C> {

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Object e) {

    }
}
