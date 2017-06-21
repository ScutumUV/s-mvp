package com.superc.lib.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.superc.lib.manager.SFragmentManager;
import com.superc.lib.presenter.SPresenter;
import com.superc.lib.ui.activity.SActivity;
import com.superc.lib.util.SUtil;

import butterknife.ButterKnife;

/**
 * Created by superchen on 2017/5/16.
 */
public abstract class SFragment<T extends SPresenter> extends Fragment {

    protected String TAG;

    protected View rootView;

    private SFragmentManager mFragmentManager = null;

    public T presenter;


    @LayoutRes
    protected abstract int setLayoutId();

    protected abstract void initViews(View view);

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFragmentManager = SFragmentManager.getInstance((AppCompatActivity) getActivity());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (setLayoutId() == SActivity.NONE_VALUE) {
            throw new IllegalArgumentException("The fragment's layout id not be null");
        }
        rootView = inflater.inflate(setLayoutId(), container, false);
        ButterKnife.bind(this, rootView);
        TAG = getClass().getSimpleName();
        initViews(rootView);
        return rootView;
    }

    /**
     * 创建相应的 presenter
     */
    public void createPresenter(T presenter) {
        SUtil.checkNull(presenter, "The presenter not be null");
        this.presenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //解绑 presenter
        if (presenter != null) {
            presenter.unSubscribe();
        }
    }

    /**
     * popFragment onResume lifecycle when FragmentTransaction.add();
     */
    public void popOnResume(View view) {
    }

    /**
     * popFragment onResume lifecycle when FragmentTransaction.add();
     */
    public void popOnResume(Object object) {
    }

    /**
     * popFragment onPause lifecycle when FragmentTransaction.add();
     */
    public void popOnPause(View view) {
        SUtil.popDownKeybBoard(view);
    }

    @Nullable
    @Override
    public View getView() {
        return rootView;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    protected void setBackMethod() {
        mFragmentManager.goBack();
    }

    protected void finishActivity() {
        if (!getActivity().isFinishing()) {
            getActivity().finish();
        }
    }

    protected AppCompatActivity getHoldingActivity() {
        return mFragmentManager.getHoldingActivity();
    }

    protected void showLoadingDialog(String message) {
        checkHoldingActivity();
        ((SActivity) getHoldingActivity()).showLoadingDialog(message);
    }

    protected void hideLoadingDialog() {
        checkHoldingActivity();
        ((SActivity) getHoldingActivity()).hideLoadingDialog();
    }

    protected void showToastShort(String msg) {
        checkHoldingActivity();
        ((SActivity) getHoldingActivity()).showToastShort(msg);
    }

    protected void showToastLong(String msg) {
        checkHoldingActivity();
        ((SActivity) getHoldingActivity()).showToastLong(msg);
    }

    protected void showLog(String msg) {
        Log.i(TAG, msg);
    }

    /**
     * 在当前activity无动画 加载 一个fragment
     *
     * @param fragment    需要加载的fragment
     * @param containerId 加载framgent的ViewId
     */
    protected void addFragmentNoAnimation(@NonNull SFragment fragment, @IdRes int containerId) {
        mFragmentManager.addFragmentNoAnimation(fragment, containerId);
    }

    /**
     * 在当前activity默认动画(右进左出) 加载 一个fragment
     *
     * @param fragment    需要加载的fragment
     * @param containerId 加载fragment的ViewId
     */
    protected void addFragmentNormalAnimation(@NonNull SFragment fragment, @IdRes int containerId) {
        mFragmentManager.addFragmentNormalAnimation(fragment, containerId);
    }

    /**
     * 在当前activity自定义动画 加载 一个fragment，当API>=13时才有4个参数，API>=11时只有 enter 和 exit 2个参数
     *
     * @param fragment    需要加载的fragment
     * @param containerId 加载fragment的ViewId
     * @param enter       进入动画
     * @param exit        退出动画
     * @param popEnter    当有回退栈时的进入动画
     * @param popExit     当有回退栈时的退出动画
     */
    protected void addFragmentCustomAnimation(@NonNull SFragment fragment, @IdRes int containerId,
                                              @AnimRes int enter, @AnimRes int exit,
                                              @AnimRes int popEnter, @AnimRes int popExit) {
        mFragmentManager.addFragmentCustomAnimation(fragment, containerId, enter, exit, popEnter, popExit);
    }


    /**
     * 在当前activity无动画 替换 一个fragment
     *
     * @param fragment    需要加载的fragment
     * @param containerId 加载fragment的ViewId
     */
    protected void replaceFragmentNoAnimation(@NonNull SFragment fragment, @IdRes int containerId) {
        mFragmentManager.replaceFragmentNoAnimation(fragment, containerId);
    }

    /**
     * 在当前activity默认动画(右进左出) 替换 一个fragment
     *
     * @param fragment    需要加载的fragment
     * @param containerId 加载framgent的ViewId
     */
    protected void replaceFragmentNormalAnimation(@NonNull SFragment fragment, @IdRes int containerId) {
        mFragmentManager.replaceFragmentNormalAnimation(fragment, containerId);
    }

    /**
     * 在当前activity自定义动画 替换 一个fragment，当API>=13时才有4个参数，API>=11时只有 enter 和 exit 2个参数
     *
     * @param fragment    需要加载的fragment
     * @param containerId 加载fragment的ViewId
     * @param enter       进入动画
     * @param exit        退出动画
     * @param popEnter    当有回退栈时的进入动画
     * @param popExit     当有回退栈时的退出动画
     */
    protected void replaceFragmentCustomAnimation(@NonNull SFragment fragment, @IdRes int containerId,
                                                  @AnimRes int enter, @AnimRes int exit,
                                                  @AnimRes int popEnter, @AnimRes int popExit) {
        mFragmentManager.replaceFragmentCustomAnimation(fragment, containerId, enter, exit, popEnter, popExit);
    }

    /**
     * 根据需要返回的fragment的名称来对当前activity中的所有fragment进行回退，
     * 有，则回退到需要返回的fragment名称对应的fragment的实例
     * 无，则直接返回
     *
     * @param fragmentName 需要返回的fragment的名称
     */
    protected void backToFragment(@NonNull String fragmentName) {
        mFragmentManager.backToFragment(fragmentName);
    }

    /**
     * 直接对当前activity中的fragment进行回退，即返回上一个fragment
     */
    protected void backFragment() {
        mFragmentManager.goBack();
    }

    private void checkHoldingActivity() {
        AppCompatActivity ac = getHoldingActivity();
        if (!(ac instanceof SActivity)) {
            throw new IllegalArgumentException("The holding activity must be BaseActivity");
        }
    }

}