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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.superc.lib.http.HttpManager;
import com.superc.lib.manager.SFragmentManager;
import com.superc.lib.presenter.SPresenter;
import com.superc.lib.ui.FragmentListener;
import com.superc.lib.ui.activity.SActivity;
import com.superc.lib.util.SUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by superchen on 2017/5/16.
 */
public abstract class SFragment<P extends SPresenter> extends Fragment {

    private SFragmentManager mFragmentManager = null;

    protected String TAG;

    protected View mRootView;

    protected Unbinder mUnBinder;

    protected P presenter;

    protected HttpManager mHttp;

    protected boolean isActive = false;

    protected FragmentListener tFragmentListener;


    @LayoutRes
    protected abstract int setLayoutId();

    protected abstract void initViews(View view);

    protected void initTitleBar(View view) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentListener) {
            tFragmentListener = (FragmentListener) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isActive = true;
        mFragmentManager = new SFragmentManager((AppCompatActivity) getActivity());
    }

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (setLayoutId() == SActivity.NONE_VALUE || setLayoutId() == 0) {
            throw new IllegalArgumentException("The fragment's layout id not be null");
        }
        mRootView = inflater.inflate(setLayoutId(), container, false);
        mUnBinder = ButterKnife.bind(this, mRootView);
        TAG = getClass().getSimpleName();
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initHttpManager();
        initTitleBar(mRootView);
        initViews(mRootView);
    }

    @Override
    public void onStart() {
        super.onStart();
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
    public void onStop() {
        super.onStop();
        isActive = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFragmentManager != null) {
            mFragmentManager = null;
            SUtil.unbindReferences(mRootView);
        }
        if (mHttp != null) {
            mHttp.destroyAllTask();
        }
        mUnBinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
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
        return mRootView;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    protected void setBackMethod() {
        mFragmentManager.goBack();
    }

    protected void finishActivity() {
        if (!getHoldingActivity().isFinishing()) {
            getHoldingActivity().finish();
        }
    }

    protected AppCompatActivity getHoldingActivity() {
        return mFragmentManager.getHoldingActivity();
    }

    /**
     * 跳转另外的Activity
     *
     * @param cls Activity的Class
     */
    public <T extends Activity> void startActivity(Class<T> cls) {
        if (getHoldingActivity() != null && (getHoldingActivity() instanceof SActivity)) {
            SActivity s = (SActivity) getHoldingActivity();
            s.startActivity(cls);
        }
    }

    /**
     * 带数据跳转另外的Activity
     *
     * @param cls Activity的Class
     * @param b   数据
     */
    public <T extends Activity> void startActivity(Class<T> cls, Bundle b) {
        if (getHoldingActivity() != null && (getHoldingActivity() instanceof SActivity)) {
            SActivity s = (SActivity) getHoldingActivity();
            s.startActivity(cls, b);
        }
    }

    /**
     * ActivityForResult
     *
     * @param cls        Activity的Class
     * @param resultCode 返回码
     */
    public <T extends Activity> void startActivityForResult(@NonNull Class<T> cls, int resultCode) {
        if (getHoldingActivity() != null && (getHoldingActivity() instanceof SActivity)) {
            SActivity s = (SActivity) getHoldingActivity();
            Intent intent = new Intent(s, cls);
            startActivityForResult(intent, resultCode);
        }
    }

    /**
     * 带数据传递的ActivityForResult
     *
     * @param cls        Activity的Class
     * @param b          数据
     * @param resultCode 返回码
     */
    public <T extends Activity> void startActivityForResult(@NonNull Class<T> cls, Bundle b, int resultCode) {
        if (getHoldingActivity() != null && (getHoldingActivity() instanceof SActivity)) {
            SActivity s = (SActivity) getHoldingActivity();
            Intent intent = new Intent(s, cls);
            if (b != null) {
                intent.putExtras(b);
            }
            startActivityForResult(intent, resultCode);
        }
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

    private void initHttpManager() {
        if (mHttp == null) {
            mHttp = new HttpManager(getHoldingActivity());
        }
    }

    public void setPresenter(P presenter) {
        SUtil.checkNull(presenter, "The presenter not be null");
        this.presenter = presenter;
    }

    public void showLoadingDialog(String message) {
        checkHoldingActivity();
        ((SActivity) getHoldingActivity()).showLoadingDialog(message);
    }

    public void hideLoadingDialog() {
        checkHoldingActivity();
        ((SActivity) getHoldingActivity()).hideLoadingDialog();
    }

    public void showToastShort(String msg) {
        checkHoldingActivity();
        ((SActivity) getHoldingActivity()).showToastShort(msg);
    }

    public void showToastLong(String msg) {
        checkHoldingActivity();
        ((SActivity) getHoldingActivity()).showToastLong(msg);
    }

    public void showLog(String msg) {
        Log.i(TAG, msg);
    }

    public void showError() {

    }

    public void showEmptyView() {

    }

    public Context getContext() {
        return getHoldingActivity();
    }

    public HttpManager getHttpManager() {
        if (mHttp != null) {
            return mHttp;
        }
        if (getHoldingActivity() instanceof SActivity) {
            return ((SActivity) getHoldingActivity()).getHttpManager();
        }
        throw new IllegalArgumentException("The SFragment must use in SActivity");
    }

    public void destroyAllNetTask() {
        mRootView = null;
        if (mHttp != null) {
            mHttp.destroyAllTask();
        }
    }

}
