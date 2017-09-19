package com.superc.lib.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.AnimRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;

import com.superc.lib.dialog.WaitDialog;
import com.superc.lib.http.HttpManager;
import com.superc.lib.manager.SActivityManager;
import com.superc.lib.manager.SFragmentManager;
import com.superc.lib.presenter.SPresenter;
import com.superc.lib.ui.fragment.SFragment;
import com.superc.lib.util.DialogUtil;
import com.superc.lib.util.LogUtils;
import com.superc.lib.util.SUtil;
import com.superc.lib.util.StatusBarCompatUtil;
import com.superc.lib.util.StringUtils;
import com.superc.lib.util.ToastUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * BaseActivity create by SuperChen
 */
public abstract class SActivity<P extends SPresenter> extends AppCompatActivity {

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
    protected WaitDialog progressDialog;

    protected Unbinder mUnBinder;

    protected P presenter;
    /**
     * 根布局
     */
    protected View mRootView;
    /**
     * 是否使用沉浸式状态栏
     */
    protected boolean mUseImmersionStatusBar = true;

    protected Object signObj;

    protected HttpManager mHttp = null;


    /**
     * @return 设置继承 BaseActivity的子Activity的布局Id
     */
    protected abstract @LayoutRes
    int setLayoutResId();

    /**
     * 初始化
     */
    protected abstract void initViews();

    protected void initTitleBar() {
    }

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (setLayoutResId() == NONE_VALUE || setLayoutResId() == 0) {
            throw new IllegalArgumentException("The activity's layout id not be null");
        }
        if (mUseImmersionStatusBar) {
            StatusBarCompatUtil.compat(this);
        }
        setContentView(setLayoutResId());
        init(savedInstanceState);
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

    protected void onOutInstanceState(Bundle savedInstanceState) {

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
            mActivityManager.finishActivity(this);
            SUtil.unbindReferences(mRootView);
        }
        if (mFragmentManager != null) {
            mFragmentManager = null;
        }
        destroyAllNetTask();
        mUnBinder.unbind();
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

    public void setPresenter(P presenter) {
        SUtil.checkNull(presenter, "The Activity presenter not be null");
        this.presenter = presenter;
    }

    public void showToastShort(@NonNull String msg) {
        if (StringUtils.isEmpty(msg)) {
            return;
        }
        ToastUtil.show(this, msg);
    }

    public void showToastLong(@NonNull String msg) {
        if (StringUtils.isEmpty(msg)) {
            return;
        }
        ToastUtil.show(this, msg);
    }

    public void showLoadingDialog(String loadingReminderText) {
        initLoadingDialog();
        progressDialog.setMessage(loadingReminderText);
        progressDialog.show();
    }

    public void hideLoadingDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public void showEmptyView() {
    }

    public void showError() {
    }

    public void showLog(String msg) {
        LogUtils.i(TAG, msg);
    }

    public Context getContext() {
        return this;
    }

    public void destroyAllNetTask() {
        mRootView = null;
        if (presenter != null) {

        }
        if (mHttp != null) {
            mHttp.destroyAllTask();
        }
    }

    public HttpManager getHttpManager() {
        if (mHttp != null) {
            return mHttp;
        }
        return initHttpManager();
    }

    /**
     * 开始一个新的Activity
     *
     * @param cls Activity的Class
     */
    public <T extends Activity> void startActivity(@NonNull Class<T> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }


    /**
     * 开始一个新的Activity
     *
     * @param cls Activity的Class
     */
    public <T extends Activity> void startActivity(@NonNull Class<T> cls, Bundle b) {
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
    public <T extends Activity> void startActivityForResult(@NonNull Class<T> cls, int resultCode) {
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
    public <T extends Activity> void startActivityForResult(@NonNull Class<T> cls, Bundle b, int resultCode) {
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
     * 结束某个Activity，并通知ActivityManager进行改变
     */
    public void finishActivity(@NonNull Activity activity) {
        if (mActivityManager != null) {
            mActivityManager.finishActivity(activity);
        }
    }

    /**
     * 结束当前Application所有的activity
     */
    public void finishAllActivity() {
        if (mActivityManager != null) {
            mActivityManager.clearAllActivities();
        }
    }

    /**
     * 在当前activity无动画 加载 一个fragment
     *
     * @param fragment    需要加载的fragment
     * @param containerId 加载fragment的ViewId
     */
    protected void addFragmentNoAnimation(@NonNull Fragment fragment, @IdRes int containerId) {
        initFragmentManager();
        mFragmentManager.addFragmentNoAnimation(fragment, containerId);
    }

    /**
     * 在当前activity默认动画(右进左出) 加载 一个fragment
     *
     * @param fragment    需要加载的fragment
     * @param containerId 加载fragment的ViewId
     */
    protected void addFragmentNormalAnimation(@NonNull Fragment fragment, @IdRes int containerId) {
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
    protected void addFragmentCustomAnimation(@NonNull Fragment fragment, @IdRes int containerId,
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
    protected void replaceFragmentNoAnimation(@NonNull Fragment fragment, @IdRes int containerId) {
        initFragmentManager();
        mFragmentManager.replaceFragmentNoAnimation(fragment, containerId);
    }

    /**
     * 在当前activity默认动画(右进左出) 替换 一个fragment
     *
     * @param fragment    需要加载的fragment
     * @param containerId 加载fragment的ViewId
     */
    protected void replaceFragmentNormalAnimation(@NonNull Fragment fragment, @IdRes int containerId) {
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
    protected void replaceFragmentCustomAnimation(@NonNull Fragment fragment, @IdRes int containerId,
                                                  @AnimRes int enter, @AnimRes int exit,
                                                  @AnimRes int popEnter, @AnimRes int popExit) {
        initFragmentManager();
        mFragmentManager.replaceFragmentCustomAnimation(fragment, containerId, enter, exit, popEnter, popExit);
    }

    /**
     * 在当前activity无动画 加载 一个fragment
     *
     * @param fragment 需要加载的fragment
     */
    protected void showAndHideFragmentNoAnimation(@NonNull Fragment fragment, SFragment... hideFragments) {
        initFragmentManager();
        mFragmentManager.showAndHideFragmentNoAnimation(fragment, hideFragments);
    }

    protected void hideFragment(Fragment... hideFragments) {
        initFragmentManager();
        mFragmentManager.hideFragment(hideFragments);
    }

    protected void showFragment(@NonNull Fragment fragment) {
        initFragmentManager();
        mFragmentManager.showFragment(fragment);
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


    /***********************************************************************************************
     //**********************************************************************************************
     //*********************************************************************************************/

    private void init(Bundle saveInstanceState) {
        initActivityManager();
        mActivityManager.addActivity(this);
        mRootView = getWindow().getDecorView();
        //创建 CompositeSubscription 对象 使用CompositeSubscription来持有所有的Subscriptions，
        // 然后在onDestroy()或者onDestroyView()里取消所有的订阅。
//        mCompositeSubscription = new CompositeSubscription();
        TAG = getClass().getSimpleName();
        signObj = TAG;
        mUnBinder = ButterKnife.bind(this);
        if (saveInstanceState != null) {
            onOutInstanceState(saveInstanceState);
        }
        initHttpManager();
        initTitleBar();
        initViews();
    }

    private HttpManager initHttpManager() {
        if (mHttp == null) {
            mHttp = new HttpManager(this);
        }
        return mHttp;
    }

    private void initActivityManager() {
        if (mActivityManager == null) {
            mActivityManager = SActivityManager.getInstance();
        }
    }

    private void initFragmentManager() {
        if (mFragmentManager == null) {
            mFragmentManager = new SFragmentManager(this);
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

//    public CompositeSubscription getCompositeSubscription() {
//        if (mCompositeSubscription == null) {
//            mCompositeSubscription = new CompositeSubscription();
//        }
//        return mCompositeSubscription;
//    }

}
