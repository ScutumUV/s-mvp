package com.superchen.demo.activity;

import android.util.Log;

import com.superc.lib.ui.activity.SActivity;
import com.superchen.demo.R;

import rx.Observer;
import rx.Subscriber;

public class TestRXJavaActivity extends SActivity {

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_test_rxjava;
    }

    @Override
    protected void initViews() {
        init();
    }

    private void init() {
        Observer<String> o = new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {

            }
        };

        Subscriber<String> s = new Subscriber<String>() {

            @Override
            public void onStart() {
                super.onStart();
            }


            @Override
            public void onCompleted() {
                Log.i("msgg", "onCompleted");
                showToastShort("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                showToastShort("onError :" + e.toString());
            }

            @Override
            public void onNext(String s) {
                Log.i("msgg", "s :" + s);
                showToastShort("onNext :" + s);
            }

        };

        rx.Observable ov = rx.Observable.create(new rx.Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello RXJava");
                subscriber.onNext("Hello Alpha");
                subscriber.onCompleted();
            }
        });
        ov.subscribe(s);
    }

}
