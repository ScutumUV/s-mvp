package com.superc.lib.presenter;

import com.google.gson.Gson;
import com.superc.lib.http.HttpExceptionEntity;
import com.superc.lib.http.SServiceManager;
import com.superc.lib.http.SimpleSubsCallBack;
import com.superc.lib.model.SBaseModel;
import com.superc.lib.ui.SView;
import com.superc.lib.util.SUtil;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by superchen on 2017/5/16.
 */
public class SBasePresenterLmp<T extends SView> implements SPresenter {

    protected T mBaseView;

    protected SBaseModel mModel;

    protected SServiceManager mHttpManager;
    /**
     * 使用CompositeSubscription来持有所有的Subscriptions
     */
    protected CompositeSubscription mCompositeSubscription;


    public SBasePresenterLmp(T baseView) {
        SUtil.checkNull(baseView, "The BaseView not be null");
        //创建 CompositeSubscription 对象 使用CompositeSubscription来持有所有的Subscriptions，
        // 然后在onDestroy()或者onDestroyView()里取消所有的订阅。
        mCompositeSubscription = new CompositeSubscription();
        mBaseView = baseView;
        mHttpManager = new SServiceManager.Builder(mBaseView.getContext()).build();
    }

    /**
     * 对 Observable<T> 做统一的处理，处理了线程调度、分割返回结果等操作组合了起来
     *
     * @param responseObservable
     * @param <T>
     * @return
     */
    protected <T> Observable<T> applySchedulers(Observable<T> responseObservable) {
        return responseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<T, Observable<T>>() {
                    @Override
                    public Observable<T> call(T tResponse) {
                        return flatResponse(tResponse);
                    }
                });
    }

    /**
     * 对网络接口返回的Response进行分割操作 对于jasn 解析错误以及返回的 响应实体为空的情况
     *
     * @param response
     * @return
     */
    public <T> Observable<T> flatResponse(final T response) {
        return Observable.create(new Observable.OnSubscribe<T>() {

            @Override
            public void call(Subscriber<? super T> subscriber) {
                if (response != null) {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onNext(response);
                    }
                } else {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onError(new HttpExceptionEntity().setCode("404").setMessage("网络错误，请检查网络设置"));
                    }
                    return;
                }
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onCompleted();
                }
            }
        });
    }

    protected <E> Subscriber createNewSubscriber(final SimpleSubsCallBack callBack) {
        return new Subscriber<E>() {
            @Override
            public void onCompleted() {
                if (mBaseView != null) {
                    mBaseView.hideLoadingDialog();
                }
                callBack.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof HttpException) {
                    ResponseBody body = ((HttpException) e).response().errorBody();
                    try {
                        String json = body.string();
                        Gson gson = new Gson();
                        HttpExceptionEntity he = gson.fromJson(json, HttpExceptionEntity.class);
                        if (he != null && he.getMessage() != null) {
                            mBaseView.showToastShort(he.getMessage());
                            callBack.onError(he);
                        }
                    } catch (IOException IOe) {
                        IOe.printStackTrace();
                    }
                } else {
                    callBack.onError("网络错误，请检查网络设置");
                }
                if (mBaseView != null) {
                    mBaseView.hideLoadingDialog();
                }
            }

            @Override
            public void onNext(E e) {
                SUtil.checkNull(mCompositeSubscription, "The CompositeSubscription is null");
                if (!mCompositeSubscription.isUnsubscribed()) {
                    callBack.onNext(e);
                }
            }
        };
    }


    public void back() {

    }


    public void back(String className) {

    }

    @Override
    public void unSubscribe() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }
}
