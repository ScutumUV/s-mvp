package com.superc.lib.widget.recyclerview.base.header;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.superc.lib.R;
import com.superc.lib.util.LogUtils;

public abstract class RBaseHeader extends LinearLayout implements HeaderState {

    protected LinearLayout mContainer;
    protected int mMeasuredHeight;
    protected int mState = HeaderState.STATE_NORMAL;


    public RBaseHeader(Context context) {
        super(context, null);
        initView();
    }

    public RBaseHeader(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RBaseHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr, 0);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RBaseHeader(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    protected void initView() {
        initContainer();
        mMeasuredHeight = getMeasuredHeight();
    }

    protected void initContainer() {
        // 初始情况，设置下拉刷新view高度为0
        mContainer = (LinearLayout) LayoutInflater.from(getContext()).inflate(
                R.layout.listview_header, null);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 0);
        this.setLayoutParams(lp);
        this.setPadding(0, 0, 0, 0);

        addView(mContainer, new LayoutParams(LayoutParams.MATCH_PARENT, 0));
        setGravity(Gravity.BOTTOM);

        measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public int getState() {
        return mState;
    }

    @Override
    public void setNormalState() {
        LogUtils.d("RBaseHeader", "RBaseHeader setNormalState();");
        mState = HeaderState.STATE_NORMAL;
    }

    @Override
    public void setRefreshingState() {
        LogUtils.d("RBaseHeader", "RBaseHeader setRefreshingState();");
        mState = HeaderState.STATE_REFRESHING;
    }

    @Override
    public void setDoneState() {
        LogUtils.d("RBaseHeader", "RBaseHeader setDoneState();");
        mState = HeaderState.STATE_DONE;
    }

    @Override
    public void setReleaseToRefreshState() {
        LogUtils.d("RBaseHeader", "RBaseHeader setReleaseToRefreshState();");
        mState = HeaderState.STATE_RELEASE_TO_REFRESH;
    }

    @Override
    public void refreshComplete() {
        LogUtils.d("RBaseHeader", "RBaseHeader refreshComplete();");
        setDoneState();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                reset();
            }
        }, 200);
    }

    @Override
    public void onMove(float delta) {
        LogUtils.d("RBaseHeader", "RBaseHeader onMove delta = " + delta);
        if (getVisibleHeight() > 0 || delta > 0) {
            setVisibleHeight((int) delta + getVisibleHeight());
            if (mState <= STATE_RELEASE_TO_REFRESH) // 未处于刷新状态，更新箭头
            {
                if (getVisibleHeight() > mMeasuredHeight) {
                    setRefreshingState();
                } else {
                    setNormalState();
                }
            }
        }
    }

    @Override
    public boolean releaseAction() {
        LogUtils.d("RBaseHeader", "RBaseHeader releaseAction();");
        boolean isOnRefresh = false;
        int height = getVisibleHeight();
        if (height == 0) // not visible.
            isOnRefresh = false;

        if (getVisibleHeight() > mMeasuredHeight && mState < STATE_REFRESHING) {
            setRefreshingState();
            isOnRefresh = true;
        }
        // refreshing and header isn't shown fully. do nothing.
        if (mState == STATE_REFRESHING && height <= mMeasuredHeight) {
            //return;
        }
        int destHeight = 0; // default: scroll back to dismiss header.
        // is refreshing, just scroll back to show all the header.
        if (mState == STATE_REFRESHING) {
            destHeight = mMeasuredHeight;
        }
        smoothScrollTo(destHeight);

        return isOnRefresh;
    }

    @Override
    public void reset() {
        LogUtils.d("RBaseHeader", "RBaseHeader setNormalState();");
        smoothScrollTo(0);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                setNormalState();
            }
        }, 500);
    }

    private void smoothScrollTo(int destHeight) {
        ValueAnimator animator = ValueAnimator.ofInt(getVisibleHeight(), destHeight);
        animator.setDuration(300).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setVisibleHeight((int) animation.getAnimatedValue());
            }
        });
        animator.start();
    }

    public void setVisibleHeight(int height) {
        if (height < 0) height = 0;
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }

    public int getVisibleHeight() {
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        return lp.height;
    }

    public int getThisMeasuredHeight() {
        return mMeasuredHeight;
    }
}
