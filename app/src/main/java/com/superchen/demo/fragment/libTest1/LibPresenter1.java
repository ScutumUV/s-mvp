package com.superchen.demo.fragment.libTest1;

import android.support.annotation.NonNull;

import com.superc.lib.presenter.SBasePresenterLmp;
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

    @Override
    public void getPublicKey() {

    }

    @Override
    public void login() {

    }
}
