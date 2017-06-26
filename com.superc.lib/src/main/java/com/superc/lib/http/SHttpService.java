package com.superc.lib.http;

import android.support.annotation.NonNull;

import com.superc.lib.presenter.SPresenter;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by superchen on 2017/6/23.
 * <p>
 * dd
 */
public interface SHttpService extends SPresenter {

    /**
     * 创建的访问网络的接口通过Retrofit
     * {@link retrofit2.Retrofit}
     * Create an implementation of the API endpoints defined by the interface
     *
     * @param serviceInterface
     * @param <T>
     * @return
     */
    <T> T create(@NonNull Class<T> serviceInterface);

    <T> Subscriber createNewSubscriber(SubsCallBack callBack);

    /**
     * 对 Observable<T> 做统一的处理，处理了线程调度、分割返回结果等操作组合了起来
     *
     * @param responseObservable
     * @param <T>
     * @return
     */
    <T> Observable<T> applySchedulers(@NonNull Observable<T> responseObservable);

    /**
     * 对网络接口返回的Response进行分割操作 对于jasn 解析错误以及返回的 响应实体为空的情况
     *
     * @param response
     * @return
     */
    <T> Observable<T> flatResponse(@NonNull final T response);


}
