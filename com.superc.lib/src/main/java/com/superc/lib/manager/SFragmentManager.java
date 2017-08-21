package com.superc.lib.manager;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.AnimRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;

import com.superc.lib.R;
import com.superc.lib.ui.fragment.SFragment;
import com.superc.lib.util.SUtil;
import com.superc.lib.util.StringUtils;

import java.util.Stack;

import static com.superc.lib.ui.activity.SActivity.NONE_VALUE;

/**
 * 自定义的FragmentManager create by SuperChen
 * <p>
 * 当前值针对于一个App中进行只在一个activity中加载多个fragment，
 * 暂不支持，在activity A中加载多个fragment后，从A跳入activity B后B再加载多个fragment，
 * 会抛出异常，若需要如此，可以考虑不使用单例模式，同时不适用static字段
 */
public final class SFragmentManager<T extends AppCompatActivity> {

    /**
     * Fragment的栈
     */
    private Stack<SFragment> mFragmentStacks = null;

    private static SFragmentManager manager = null;

    public static SFragmentManager getInstance(@NonNull AppCompatActivity activity) {
        if (manager == null) {
            synchronized (SFragmentManager.class) {
                if (manager == null) {
                    manager = new SFragmentManager(activity);
                }
            }
        }
        return manager;
    }

    /**
     * support.v4.app.FragmentManager
     */
    private android.support.v4.app.FragmentManager fm;
    /**
     * 位于栈顶的fragment
     */
    private SFragment mLastFragment;
    /**
     * fragment的宿主activity
     */
    private AppCompatActivity mHoldingActivity;
    /**
     * 需要返回的fragmentName
     */
    private String backToFragmentClassName;
    /**
     * 返回到需要的fragment后需要给改fragment传递的数据
     */
    private Object backObj = null;


    private SFragmentManager(@NonNull AppCompatActivity activity) {
        mHoldingActivity = activity;
        mFragmentStacks = new Stack<>();
    }

    /**
     * 对该FragmentManager进行初始化
     */
    private void initFragmentManager() {
        SUtil.checkNull(mHoldingActivity, "The fragment holding activity is null");
        if (fm == null) {
            fm = mHoldingActivity.getSupportFragmentManager();
        }
    }

    /**
     * 加入自定义回退栈
     */
    private void addFragmentToStack(@NonNull Fragment fragment) {
        mFragmentStacks.add((SFragment) fragment);
    }

    /**
     * 通过add方法，进行自定义生命周期的回调
     *
     * @param isBack 是否返回
     */
    private void startPopFragmentLifeCycle(boolean isBack) {
        /**  设置最上层Fragment的生命周期 **/
        if (mFragmentStacks.size() >= 1) {
            final SFragment mLastPopFragment = mFragmentStacks.get(mFragmentStacks.size() - 1);
            if (mLastPopFragment != null) {
                if (isBack) {
                    final View view = mLastPopFragment.getView();
                    mLastPopFragment.popOnPause(view);
                }
            }
        }
        /**  设置最上层Fragment之前的Fragment的生命周期 **/
        if (mFragmentStacks.size() > 1) {
            SFragment mBeforePopFragment = mFragmentStacks.get(mFragmentStacks.size() - 2);
            if (mBeforePopFragment != null) {
                View view = mBeforePopFragment.getView();
                if (isBack) {
                    mBeforePopFragment.popOnResume(view);
                    if (backObj != null && mBeforePopFragment.getClass().getName().equals(backToFragmentClassName)) {
                        mBeforePopFragment.popOnResume(backObj);
                        backObj = null;
                    }
                } else {
                    mBeforePopFragment.popOnPause(view);
                }
            }
        }
        /** 移除最上层的Fragment **/
        if (isBack && mFragmentStacks.size() > 0) {
            SFragment mLastPopFragment = mFragmentStacks.get(mFragmentStacks.size() - 1);
            if (mFragmentStacks.contains(mLastPopFragment)) {
                mFragmentStacks.remove(mLastPopFragment);
            }
        }
        if (isBack) {
            if (mFragmentStacks.size() > 0) {
                mLastFragment = mFragmentStacks.get(mFragmentStacks.size() - 1);
            } else {
                mLastFragment = null;
            }
        }
    }

    /**
     * 设置fragment的加载动画
     *
     * @param t        fragmentTransaction
     * @param enter    进入动画
     * @param exit     退出动画
     * @param popEnter 当有回退栈时的进入动画
     * @param popExit  当有回退栈时的退出动画
     */
    @SuppressLint("ObsoleteSdkInt")
    private void setCustomAnimations(@NonNull FragmentTransaction t, @AnimRes int enter, @AnimRes int exit,
                                     @AnimRes int popEnter, @AnimRes int popExit) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
            t.setCustomAnimations(enter, exit);
        } else {
            t.setCustomAnimations(enter, exit, popEnter, popExit);
        }
    }

    /**
     * 在当前activity无动画 加载 一个fragment
     *
     * @param fragment    需要加载的fragment
     * @param containerId 加载fragment的ViewId
     */
    public void addFragmentNoAnimation(@NonNull SFragment fragment, @IdRes int containerId) {
        if (containerId == NONE_VALUE) {
            return;
        }
        mLastFragment = fragment;
        initFragmentManager();
        FragmentTransaction t = fm.beginTransaction();
        addFragmentToStack(fragment);
        t.add(containerId, fragment, fragment.getClass().getName());
        t.addToBackStack(fragment.getClass().getName());
        t.commitAllowingStateLoss();
        startPopFragmentLifeCycle(false);
    }

    /**
     * 在当前activity默认动画(右进左出) 加载 一个fragment
     *
     * @param fragment    需要加载的fragment
     * @param containerId 加载fragment的ViewId
     */
    public void addFragmentNormalAnimation(@NonNull SFragment fragment, @IdRes int containerId) {
        if (containerId == NONE_VALUE) {
            return;
        }
        mLastFragment = fragment;
        initFragmentManager();
        FragmentTransaction t = fm.beginTransaction();
        setCustomAnimations(t, R.anim.right_in, R.anim.left_out,
                R.anim.left_in, R.anim.right_out);
        t.add(containerId, fragment, fragment.getClass().getName());
        t.addToBackStack(fragment.getClass().getName());
        t.commitAllowingStateLoss();
        startPopFragmentLifeCycle(false);
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
    @SuppressLint("ObsoleteSdkInt")
    public void addFragmentCustomAnimation(@NonNull SFragment fragment, @IdRes int containerId,
                                           @AnimRes int enter, @AnimRes int exit,
                                           @AnimRes int popEnter, @AnimRes int popExit) {
        if (containerId == NONE_VALUE) {
            return;
        }
        mLastFragment = fragment;
        initFragmentManager();
        FragmentTransaction t = fm.beginTransaction();
        setCustomAnimations(t, enter, exit, popEnter, popExit);
        addFragmentToStack(fragment);
        t.add(containerId, fragment, fragment.getClass().getName());
        t.addToBackStack(fragment.getClass().getName());
        t.commitAllowingStateLoss();
        startPopFragmentLifeCycle(false);
    }

    /**
     * 在当前activity无动画 替换 一个fragment
     *
     * @param fragment    需要加载的fragment
     * @param containerId 加载fragment的ViewId
     */
    public void replaceFragmentNoAnimation(@NonNull SFragment fragment, @IdRes int containerId) {
        if (containerId == NONE_VALUE) {
            return;
        }
        mLastFragment = fragment;
        initFragmentManager();
        FragmentTransaction t = fm.beginTransaction();
        addFragmentToStack(fragment);
        t.replace(containerId, fragment, fragment.getClass().getName());
        t.addToBackStack(fragment.getClass().getName());
        t.commitAllowingStateLoss();
        startPopFragmentLifeCycle(false);
    }

    /**
     * 在当前activity默认动画(右进左出) 替换 一个fragment
     *
     * @param fragment    需要加载的fragment
     * @param containerId 加载fragment的ViewId
     */
    public void replaceFragmentNormalAnimation(@NonNull SFragment fragment, @IdRes int containerId) {
        if (containerId == NONE_VALUE) {
            return;
        }
        mLastFragment = fragment;
        initFragmentManager();
        FragmentTransaction t = fm.beginTransaction();
        setCustomAnimations(t, R.anim.right_in, R.anim.left_out,
                R.anim.left_in, R.anim.right_out);
        addFragmentToStack(fragment);
        t.replace(containerId, fragment, fragment.getClass().getName());
        t.addToBackStack(fragment.getClass().getName());
        t.commitAllowingStateLoss();
        startPopFragmentLifeCycle(false);
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
    public void replaceFragmentCustomAnimation(@NonNull SFragment fragment, @IdRes int containerId,
                                               @AnimRes int enter, @AnimRes int exit,
                                               @AnimRes int popEnter, @AnimRes int popExit) {
        if (containerId == NONE_VALUE) {
            return;
        }
        mLastFragment = fragment;
        initFragmentManager();
        FragmentTransaction t = fm.beginTransaction();
        setCustomAnimations(t, enter, exit, popEnter, popExit);
        addFragmentToStack(fragment);
        t.replace(containerId, fragment, fragment.getClass().getName());
        t.addToBackStack(fragment.getClass().getName());
        t.commitAllowingStateLoss();
        startPopFragmentLifeCycle(false);
    }

    /**
     * 在当前activity无动画 加载 一个fragment
     *
     * @param fragment 需要加载的fragment
     */
    public void showAndHideFragmentNoAnimation(@NonNull SFragment fragment, SFragment... hideFragments) {
        mLastFragment = fragment;
        initFragmentManager();
        FragmentTransaction t = fm.beginTransaction();
        t.show(fragment);
        if (hideFragments != null) {
            for (SFragment f : hideFragments) {
                if (f != null) {
                    t.hide(f);
                }
            }
        }
        t.commitAllowingStateLoss();
        startPopFragmentLifeCycle(false);
    }

    /**
     * 在当前activity无动画 加载 一个fragment
     */
    public void hideFragment(SFragment... hideFragments) {
        initFragmentManager();
        FragmentTransaction t = fm.beginTransaction();
        if (hideFragments != null) {
            for (SFragment f : hideFragments) {
                if (f != null) {
                    t.hide(f);
                }
            }
        }
        t.commitAllowingStateLoss();
        startPopFragmentLifeCycle(false);
    }

    /**
     * 在当前activity无动画 加载 一个fragment
     */
    public void showFragment(@NonNull SFragment fragment) {
        mLastFragment = fragment;
        initFragmentManager();
        FragmentTransaction t = fm.beginTransaction();
        addFragmentToStack(fragment);
        t.show(fragment);
        t.commitAllowingStateLoss();
        startPopFragmentLifeCycle(false);
    }


    /**
     * 直接对当前activity中的fragment进行回退，即返回上一个fragment
     */
    public void goBack() {
        SUtil.checkNull(mHoldingActivity, "The fragment holding activity is null");
        if (fm.getBackStackEntryCount() <= 1) {
            mHoldingActivity.finish();
        } else {
            startPopFragmentLifeCycle(true);
            fm.popBackStack();
        }
    }

    /**
     * 根据需要返回的fragment的名称来对当前activity中的所有fragment进行回退，
     * 有，则回退到需要返回的fragment名称对应的fragment的实例
     * 无，则直接返回
     *
     * @param fragmentName 需要返回的fragment的名称
     */
    public void backToFragment(@NonNull String fragmentName) {
        if (StringUtils.isEmpty(fragmentName)) {
            return;
        }
        if (mFragmentStacks.size() == 0) {
            return;
        }
        backToFragmentClassName = fragmentName;
        int a = 0;
        for (int i = 0; i < mFragmentStacks.size(); i++) {
            if (mFragmentStacks.get(i).getClass().getName().equals(fragmentName)) {
                a = i;
                break;
            }
        }
        for (int b = mFragmentStacks.size() - 1; b > a; b--) {
            goBack();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mLastFragment != null) {
            boolean tag = mLastFragment.onKeyDown(keyCode, event);
            startPopFragmentLifeCycle(true);
            if (tag) {
                return tag;
            }
        }
        if (fm != null && fm.getBackStackEntryCount() <= 1) {
            mHoldingActivity.finish();
            return true;
        }
        return false;
    }

    /**
     * 获取宿主Activity
     *
     * @return AppCompatActivity
     */
    public AppCompatActivity getHoldingActivity() {
        return mHoldingActivity;
    }

    public android.support.v4.app.FragmentManager getFragmentManager() {
        return fm;
    }

    /**
     * 当activity摧毁时调用该方法
     */
    public void destroy() {
        manager = null;
    }
}
