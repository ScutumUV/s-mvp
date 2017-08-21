package com.superc.lib.util;

import android.view.View;

/**
 * Created by owner on 2017/8/18.
 */

public class ViewUtil {

    /**
     * 获取控件宽度
     */
    public static int getViewWidth(final View v) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(w, h);
        int width = v.getMeasuredWidth();
        return width;
    }

    /**
     * 获取控件高度 get view heigth
     */
    public static int getViewHeight(final View v) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(w, h);
        int height = v.getMeasuredHeight();
        return height;
    }

    /**
     * 获取控件坐标 get the view location
     */
    public static int[] getViewXY(View v) {
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        return location;
    }
}
