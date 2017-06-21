package com.superchen.demo.fragment.libTest1;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;

import com.superc.lib.ui.fragment.SFragment;
import com.superchen.demo.R;
import com.superchen.demo.fragment.libTest2.LibTestFragment2;

import java.util.List;

import butterknife.BindViews;
import butterknife.OnClick;

public class LibTestFragment1 extends SFragment<ILibContract1.ILibContractPresenter1> implements ILibContract1.ILibContractView1 {

    @BindViews({R.id.lib_test_et_name, R.id.lib_test_et_pd})
    List<TextInputEditText> etLists;

    public LibTestFragment1() {
    }

    public static LibTestFragment1 newInstance() {
        LibTestFragment1 fragment = new LibTestFragment1();
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_lib_test_fragment1;
    }

    @Override
    public void popOnResume(View view) {
        Log.i("msgg", "LibTestFragment1 popOnResume");
    }

    @Override
    public void popOnPause(View view) {
        Log.i("msgg", "LibTestFragment1 popOnPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("msgg", "LibTestFragment1 onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("msgg", "LibTestFragment1 onPause");
    }

    @Override
    protected void initViews(View view) {
        createPresenter(new LibPresenter1(this));
    }

    @Override
    public void setPresenter(ILibContract1.ILibContractPresenter1 presenter) {

    }

    @Override
    public void showLoadingDialog(String message) {
        super.showLoadingDialog(message);
    }

    @Override
    public void hideLoadingDialog() {
        super.hideLoadingDialog();
    }

    @Override
    public void showError() {

    }

    @Override
    public void showEmptyView() {
    }

    @Override
    public void showToastShort(String msg) {
        super.showToastShort(msg);
    }

    @Override
    public void showToastLong(String msg) {
        super.showToastLong(msg);
    }

    @Override
    public void showLog(String msg) {
        super.showLog(msg);
    }

    @Override
    public void success() {
        showToastShort("登录成功");
        addFragmentNoAnimation(LibTestFragment2.newInstance(), R.id.lib_test_container);
    }

    @Override
    public Context getContext() {
        return super.getContext();
    }

    @OnClick({R.id.lib_test_btn_login})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.lib_test_btn_login:
                presenter.getPublicKey();
                break;
        }
    }
}
