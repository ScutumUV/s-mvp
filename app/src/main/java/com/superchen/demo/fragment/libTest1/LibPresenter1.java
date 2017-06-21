package com.superchen.demo.fragment.libTest1;

import android.support.annotation.NonNull;
import android.util.Log;

import com.superc.lib.http.SimpleSubsCallBack;
import com.superc.lib.presenter.SBasePresenterLmp;
import com.superchen.demo.url.ApiService;
import com.superchen.demo.util.Base64Utils;
import com.superchen.demo.util.RSAUtils;

import java.security.PublicKey;

/**
 * Created by superchen on 2017/6/13.
 */

public class LibPresenter1 extends SBasePresenterLmp<ILibContract1.ILibContractView1>
        implements ILibContract1.ILibContractPresenter1 {

    @NonNull
    private String pwd = "123456";

    private String un = "15223698225";

    public LibPresenter1(ILibContract1.ILibContractView1 baseView) {
        super(baseView);
    }

    @Override
    public void getPublicKey() {
        mBaseView.showLoadingDialog("登录中...");
        applySchedulers(mHttpManager.create(ApiService.GetPublicKeyService.class).getPublicKeyMethod())
                .subscribe(createNewSubscriber(new SimpleSubsCallBack<PublicKeyEntity>() {
                    @Override
                    public void onNext(PublicKeyEntity o) {
                        pwd = RSAJiaMi(o.getData().getPublicKey(), pwd);//RSA加密
                        login();
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();

                    }

                    @Override
                    public void onError(Object e) {
                        super.onError(e);
                    }
                }));
//        ApiService.GetPublicKeyService get = mHttpManager.create(ApiService.GetPublicKeyService.class);
//        get
//                .getPublicKeyMethod()
//                .subscribeOn(Schedulers.io())
//                .subscribe(createNewSubscriber(new SimpleSubsCallBack<PublicKeyEntity>() {
//                    @Override
//                    public void onNext(PublicKeyEntity o) {
//                        mBaseView.showToastShort(o.toString());
//                    }
//
//                    @Override
//                    public void onCompleted() {
//                        super.onCompleted();
//                        mBaseView.showToastShort("获取数据获取数据成功");
//                    }
//
//                    @Override
//                    public void onError(Object e) {
//                        super.onError(e);
//                    }
//                }));
//        get
//                .getPublicKeyMethod()
//                .subscribeOn(Schedulers.io())
//                .subscribe(new Subscriber<PublicKeyEntity>() {
//                    @Override
//                    public void onCompleted() {
//                        mBaseView.showLog("获取数据获取数据成功");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        mBaseView.showLog("获取数据失败 + e :" + e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(PublicKeyEntity model) {
//                        mBaseView.showLog(model.toString());
//                    }
//                });
    }

    @Override
    public void login() {
        applySchedulers(mHttpManager.create(ApiService.Login.class).login(un, pwd))
                .subscribe(createNewSubscriber(new SimpleSubsCallBack<Object>() {
                    @Override
                    public void onNext(Object o) {
                        Log.i("msgg", "login =======> s :" + o.toString());
                        mBaseView.success();
                    }
                }));
    }

    /**
     * RSA加密
     */
    private String RSAJiaMi(String pk, String data) {
        // 加密
        byte[] encryptByte;
        try {
            PublicKey publicK = RSAUtils.loadPublicKey(pk);
            encryptByte = RSAUtils.encryptData(data.getBytes(), publicK);
            // 把加密后的数据用base64加密转一下，要不然看起来是乱码,所以解密是也是要用Base64先转换
            String afterencrypt = Base64Utils.encode(encryptByte);
            return afterencrypt;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}
