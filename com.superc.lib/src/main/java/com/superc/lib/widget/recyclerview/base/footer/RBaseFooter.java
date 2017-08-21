package com.superc.lib.widget.recyclerview.base.footer;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.superc.lib.util.LogUtils;
import com.superc.lib.util.ScreenUtil;

/**
 * Created by superchen on 2017/7/25.
 */
public abstract class RBaseFooter extends LinearLayout implements FooterState {

    public static int Base_Footer_Height = 60;

    protected int mState = FooterState.STATE_COMPLETE;

    public RBaseFooter(Context context) {
        super(context, null);
        initViews();
    }

    public RBaseFooter(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        initViews();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RBaseFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr, 0);
        initViews();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RBaseFooter(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews();
    }

    protected void initViews() {
        setGravity(Gravity.CENTER);
        setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtil.dp2px(getContext(), Base_Footer_Height)));
    }

    @Override
    public void setStateLoading() {
        LogUtils.d("RBaseFooter setStateLoading();");
        mState = FooterState.STATE_LOADING;
        setVisibility(View.VISIBLE);
    }

    @Override
    public void setStateComplete() {
        LogUtils.d("RBaseFooter setStateComplete();");
        mState = FooterState.STATE_COMPLETE;
        setVisibility(View.GONE);
    }

    @Override
    public void setStateNoMore() {
        LogUtils.d("RBaseFooter setStateNoMore();");
        mState = FooterState.STATE_NO_MORE;
        setVisibility(View.VISIBLE);
    }
}
