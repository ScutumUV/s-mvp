package com.superc.lib.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.AnimRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import com.superc.lib.manager.SActivityManager;
import com.superc.lib.manager.SFragmentManager;
import com.superc.lib.presenter.SPresenter;
import com.superc.lib.ui.SView;
import com.superc.lib.ui.fragment.SFragment;
import com.superc.lib.util.SUtil;
import com.superc.lib.util.DialogUtil;
import com.superc.lib.util.StringUtils;
import com.superc.lib.util.ToastUtil;

import butterknife.ButterKnife;
import rx.subscriptions.CompositeSubscription;

/**
 * BaseActivity create by SuperChen
 */
public abstract class SActivity<P extends SPresenter> extends AppCompatActivity implements SView {

    protected String TAG;
    /**
     * 没有值作为单独定义的值进行比对
     */
    public static final int NONE_VALUE = -1;
    /**
     * 自定义的ActivityManager
     */
    private SActivityManager mActivityManager = null;
    /**
     * 自定义的FragmentManager
     */
    private SFragmentManager mFragmentManager = null;
    /**
     * 正在加载对话框
     */
    protected ProgressDialog progressDialog;
    /**
     * 使用CompositeSubscription来持有所有的Subscriptions
     */
    protected CompositeSubscription mCompositeSubscription;

    protected P presenter;

    protected View mRootView;

    /**
     * @return 设置继承 BaseActivity的子Activity的布局Id
     */
    @LayoutRes
    protected abstract int setLayoutResId();

    /**
     * 初始化
     */
    protected abstract void initViews();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (setLayoutResId() == NONE_VALUE) {
            throw new IllegalArgumentException("The activity's layout id not be null");
        }
        setContentView(setLayoutResId());
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        View view = LayoutInflater.from(this).inflate(layoutResID, null);
        setContentView(view);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        mRootView = view;
        init();
        initViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onStateNotSaved() {
        super.onStateNotSaved();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mActivityManager != null) {
            mActivityManager.removeActivity(this);
            mActivityManager.unbindReferences(mRootView);
        }
        if (mFragmentManager != null) {
            mFragmentManager.destroy();
            mFragmentManager = null;
        }
        mRootView = null;
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
        //解绑 presenter
        if (presenter != null) {
            presenter.unSubscribe();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (checkFragmentManager()) {
                initFragmentManager();
                mFragmentManager.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void showToastShort(@NonNull String msg) {
        if (StringUtils.isEmpty(msg)) {
            return;
        }
        ToastUtil.show(this, msg);
    }

    @Override
    public void showToastLong(@NonNull String msg) {
        if (StringUtils.isEmpty(msg)) {
            return;
        }
        ToastUtil.show(this, msg);
    }

    @Override
    public void showLoadingDialog(String loadingReminderText) {
        initLoadingDialog();
        progressDialog.setMessage(loadingReminderText);
        progressDialog.show();
    }

    @Override
    public void hideLoadingDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void showEmptyView() {
    }

    @Override
    public void showError() {
    }

    @Override
    public void showLog(String msg) {
        Log.i(TAG, msg);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void setPresenter(SPresenter presenter) {
        createNewPresenter((P) presenter);
    }

    protected void createNewPresenter(P presenter) {
        SUtil.checkNull(presenter, "The Activity presenter not be null");
        this.presenter = presenter;
    }

    /**
     * 开始一个新的Activity
     *
     * @param cls Activity的Class
     */
    protected void startActivity(@NonNull Class<Activity> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }


    /**
     * 开始一个新的Activity
     *
     * @param cls Activity的Class
     */
    protected void startActivity(@NonNull Class<Activity> cls, Bundle b) {
        Intent intent = new Intent(this, cls);
        if (b != null) {
            intent.putExtras(b);
        }
        startActivity(intent);
    }

    /**
     * ActivityForResult
     *
     * @param cls        Activity的Class
     * @param resultCode 返回码
     */
    protected void startActivityForResult(@NonNull Class<Activity> cls, int resultCode) {
        Intent intent = new Intent(this, cls);
        startActivityForResult(intent, resultCode);
    }

    /**
     * 带数据传递的ActivityForResult
     *
     * @param cls        Activity的Class
     * @param b          数据
     * @param resultCode 返回码
     */
    protected void startActivityForResult(@NonNull Class<Activity> cls, Bundle b, int resultCode) {
        Intent intent = new Intent(this, cls);
        if (b != null) {
            intent.putExtras(b);
        }
        startActivityForResult(intent, resultCode);
    }

    /**
     * 结束当前Activity，并通知ActivityManager进行改变和销毁当前activity
     */
    public void finishByActivityName(@NonNull String activityName) {
        if (mActivityManager != null) {
            mActivityManager.finishActivity(activityName);
        }
    }

    /**
     * 结束当前Application所有的activity
     */
    protected void finishAllActivity() {
        if (mActivityManager != null) {
            mActivityManager.clearAllActivities();
        }
    }

    /**
     * 在当前activity无动画 加载 一个fragment
     *
     * @param fragment    需要加载的fragment
     * @param containerId 加载framgent的ViewId
     */
    protected void addFragmentNoAnimation(@NonNull SFragment fragment, @IdRes int containerId) {
        initFragmentManager();
        mFragmentManager.addFragmentNoAnimation(fragment, containerId);
    }

    /**
     * 在当前activity默认动画(右进左出) 加载 一个fragment
     *
     * @param fragment    需要加载的fragment
     * @param containerId 加载fragment的ViewId
     */
    protected void addFragmentNormalAnimation(@NonNull SFragment fragment, @IdRes int containerId) {
        initFragmentManager();
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
        initFragmentManager();
        mFragmentManager.addFragmentCustomAnimation(fragment, containerId, enter, exit, popEnter, popExit);
    }


    /**
     * 在当前activity无动画 替换 一个fragment
     *
     * @param fragment    需要加载的fragment
     * @param containerId 加载fragment的ViewId
     */
    protected void replaceFragmentNoAnimation(@NonNull SFragment fragment, @IdRes int containerId) {
        initFragmentManager();
        mFragmentManager.replaceFragmentNoAnimation(fragment, containerId);
    }

    /**
     * 在当前activity默认动画(右进左出) 替换 一个fragment
     *
     * @param fragment    需要加载的fragment
     * @param containerId 加载framgent的ViewId
     */
    protected void replaceFragmentNormalAnimation(@NonNull SFragment fragment, @IdRes int containerId) {
        initFragmentManager();
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
        initFragmentManager();
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
        initFragmentManager();
        mFragmentManager.backToFragment(fragmentName);
    }

    /**
     * 直接对当前activity中的fragment进行回退，即返回上一个fragment
     */
    protected void backFragment() {
        initFragmentManager();
        mFragmentManager.goBack();
    }

    private void init() {
        initActivityManager();
        mActivityManager.addActivity(this);
        //创建 CompositeSubscription 对象 使用CompositeSubscription来持有所有的Subscriptions，
        // 然后在onDestroy()或者onDestroyView()里取消所有的订阅。
        mCompositeSubscription = new CompositeSubscription();
        ButterKnife.bind(this);
        TAG = getClass().getSimpleName();
    }

    private void initActivityManager() {
        if (mActivityManager == null) {
            mActivityManager = SActivityManager.getInstance();
        }
    }

    private void initFragmentManager() {
        if (mFragmentManager == null) {
            mFragmentManager = SFragmentManager.getInstance(this);
        }
    }

    private void initLoadingDialog() {
        if (progressDialog == null) {
            progressDialog = DialogUtil.createProgressDialog(this);
        }
    }

    private boolean checkFragmentManager() {
        if (mFragmentManager == null) {
            return false;
        }
        return true;
    }

    public android.support.v4.app.FragmentManager getFm() {
        return mFragmentManager.getFragmentManager();
    }

    public CompositeSubscription getCompositeSubscription() {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        return mCompositeSubscription;
    }

}
