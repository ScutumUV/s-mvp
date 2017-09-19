package com.superc.lib.util;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

/**
 * Created by owner on 2017/8/22.
 */

public class AnimationUtil {

    /**
     * 以View中心为旋转中心点进行旋转180°动画
     *
     * @param view        需要展示动画的View
     * @param duration    动画时长
     * @param fillAfter   动画完成后是否应用
     * @param repeatCount 重复次数
     * @param open        是否为打开状态，由用户自己去定义
     */
    public static void startRotate180BackAndForth(@NonNull View view, long duration, boolean fillAfter, int repeatCount, boolean open) {
        RotateAnimation a;
        if (open) {
            a = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        } else {
            a = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        }
        a.setFillAfter(fillAfter);
        a.setRepeatCount(repeatCount);
        a.setDuration(duration);
        view.startAnimation(a);
    }
}
