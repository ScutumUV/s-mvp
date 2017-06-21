package com.superc.lib.manager;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ListView;

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

    private static SActivityManager manager = null;

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
        int index = 0;
        for (int i = activityStack.size() - 1; i >= 0; i--) {
            if (activityStack.get(i) == a) {
                index = i;
                break;
            }
        }
        for (int i = activityStack.size() - 1; i >= index; i--) {
            Activity t = activityStack.get(i);
            if (t != null && !t.isFinishing()) {
                t.finish();
                activityStack.remove(t);
            }
        }
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
            }
        }
    }

    /**
     * 从自定义的ActivityManager中移除a
     *
     * @param a 需要销毁的activity
     */
    public void removeActivity(@NonNull Activity a) {
        SUtil.checkNull(activityStack, "The ActivityManager's activityStack is null, please check it");
        activityStack.remove(a);
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

    public void unbindReferences(View view) {
        try {
            if (view != null) {
                view.destroyDrawingCache();
                unbindViewReferences(view);
                if (view instanceof ViewGroup) {
                    unbindViewGroupReferences((ViewGroup) view);
                }
            }
        } catch (Throwable e) {
        }
    }

    @SuppressWarnings("deprecation")
    public void unbindViewReferences(View view) {
        // set all listeners to null (not every view and not every API level
        // supports the methods)
        try {
            view.setOnClickListener(null);
            view.setOnCreateContextMenuListener(null);
            view.setOnFocusChangeListener(null);
            view.setOnKeyListener(null);
            view.setOnLongClickListener(null);
            view.setOnClickListener(null);
        } catch (Throwable mayHappen) {
            //todo
        }

        // set background to null
        Drawable d = view.getBackground();
        if (d != null) {
            d.setCallback(null);
        }

        if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;
            d = imageView.getDrawable();
            if (d != null) {
                d.setCallback(null);
            }
            imageView.setImageDrawable(null);
            imageView.setBackgroundDrawable(null);
        }

        // destroy WebView
        if (view instanceof WebView) {
            WebView webview = (WebView) view;
            webview.stopLoading();
            webview.clearFormData();
            webview.clearDisappearingChildren();
            webview.setWebChromeClient(null);
            webview.setWebViewClient(null);
            webview.destroyDrawingCache();
            webview.destroy();
            webview = null;
        }

        if (view instanceof ListView) {
            ListView listView = (ListView) view;
            try {
                listView.removeAllViewsInLayout();
            } catch (Throwable mayHappen) {
            }
            ((ListView) view).destroyDrawingCache();
        }

        if (view instanceof RecyclerView) {
            RecyclerView r = (RecyclerView) view;
            try {
                r.removeAllViewsInLayout();
            } catch (Throwable mayHappen) {
            }
            r.destroyDrawingCache();
        }
    }

    public void unbindViewGroupReferences(ViewGroup viewGroup) {
        int nrOfChildren = viewGroup.getChildCount();
        for (int i = 0; i < nrOfChildren; i++) {
            View view = viewGroup.getChildAt(i);
            unbindViewReferences(view);
            if (view instanceof ViewGroup)
                unbindViewGroupReferences((ViewGroup) view);
        }
        try {
            viewGroup.removeAllViews();
        } catch (Throwable mayHappen) {
            // AdapterViews, ListViews and potentially other ViewGroups don't
            // support the removeAllViews operation
        }
    }
}
