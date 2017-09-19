package com.superc.lib.manager;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.superc.lib.util.SUtil;
import com.superc.lib.util.StringUtils;

import java.util.Stack;

/**
 * Created by superchen on 2017/5/15.
 */
public final class SActivityManager {

    /**
     * Activity的栈
     */
    private Stack<Activity> activityStack = null;

    private volatile static SActivityManager manager = null;

    public static SActivityManager getInstance() {
        if (manager == null) {
            synchronized (SActivityManager.class) {
                if (manager == null) {
                    manager = new SActivityManager();
                }
            }
        }
        return manager;
    }

    private SActivityManager() {
        activityStack = new Stack<>();
    }

    /**
     * get current activity from Stack
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 添加一个activity对象到Stack中
     *
     * @param a activity对象
     */
    public void addActivity(@NonNull Activity a) {
        SUtil.checkNull(activityStack, "The ActivityManager's activityStack is null, please check it");
        activityStack.add(a);
    }

    /**
     * 销毁某个activity实例
     *
     * @param a activity对象
     */
    public void finishActivity(@NonNull Activity a) {
        SUtil.checkNull(activityStack, "The ActivityManager's activityStack is null, please check it");
        a.finish();
        activityStack.remove(a);
    }

    /**
     * 根据需要回退到得activity的名称，在回退栈中推出所有在它之上的所有activity
     *
     * @param activityName 需要回退到得activity的名称
     */
    public void finishActivity(@NonNull String activityName) {
        SUtil.checkNull(activityStack, "The ActivityManager's activityStack is null, please check it");
        if (activityStack.size() == 0 || StringUtils.isEmpty(activityName)) {
            return;
        }
        int index = 0;
        for (int i = activityStack.size() - 1; i >= 0; i--) {
            if (activityStack.get(i).getClass().getName().equals(activityName)) {
                index = i;
                break;
            }
        }
        for (int i = activityStack.size() - 1; i >= index; i--) {
            Activity a = activityStack.get(i);
            if (a != null && !a.isFinishing()) {
                a.finish();
                activityStack.remove(a);
            }
        }
    }

    /**
     * 销毁所有activity
     */
    public void clearAllActivities() {
        SUtil.checkNull(activityStack, "The ActivityManager's activityStack is null, please check it");
        if (activityStack.size() > 0) {
            for (Activity a : activityStack) {
                if (!a.isFinishing()) {
                    a.finish();
                }
            }
        }
        activityStack.clear();
    }
}
