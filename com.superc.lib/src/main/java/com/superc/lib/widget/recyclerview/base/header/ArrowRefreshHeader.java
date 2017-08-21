package com.superc.lib.widget.recyclerview.base.header;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StyleRes;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.superc.lib.R;
import com.superc.lib.widget.recyclerview.progressindicator.ProgressStyle;
import com.superc.lib.widget.recyclerview.SimpleViewSwitcher;
import com.superc.lib.widget.recyclerview.progressindicator.AVLoadingIndicatorView;

public class ArrowRefreshHeader extends RBaseHeader {

    private ImageView mArrowImageView;
    private TextView mStatusTextView;
    private SimpleViewSwitcher mProgressBar;

    private TextView mHeaderTimeView;

    private Animation mRotateUpAnim;
    private Animation mRotateDownAnim;

    private ArrowRefreshHeaderRefreshCompletedListener refreshCompletedListener;


    public ArrowRefreshHeader(Context context) {
        super(context);
    }

    protected void initView() {
        super.initView();
        mArrowImageView = (ImageView) findViewById(R.id.listview_header_arrow);
        mStatusTextView = (TextView) findViewById(R.id.refresh_status_textview);

        mProgressBar = (SimpleViewSwitcher) findViewById(R.id.listview_header_progressbar);
        AVLoadingIndicatorView progressView = new AVLoadingIndicatorView(getContext());
        progressView.setIndicatorColor(0xffB5B5B5);
        progressView.setIndicatorId(ProgressStyle.BallSpinFadeLoader);
        mProgressBar.setView(progressView);


        mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);
        mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);

        mHeaderTimeView = (TextView) findViewById(R.id.last_refresh_time);
    }

    public void setProgressStyle(@StyleRes int style) {
        if (style == ProgressStyle.SysProgress) {
            mProgressBar.setView(new ProgressBar(getContext(), null, android.R.attr.progressBarStyle));
        } else {
            AVLoadingIndicatorView progressView = new AVLoadingIndicatorView(this.getContext());
            progressView.setIndicatorColor(0xffB5B5B5);
            progressView.setIndicatorId(style);
            mProgressBar.setView(progressView);
        }
    }

    public void setArrowResId(@DrawableRes int resId) {
        mArrowImageView.setImageResource(resId);
    }

    public void setArrowRefreshHeaderRefreshCompletedListener(
            ArrowRefreshHeaderRefreshCompletedListener arrowRefreshHeaderRefreshCompletedListener) {
        this.refreshCompletedListener = arrowRefreshHeaderRefreshCompletedListener;
    }

    @Override
    public void setNormalState() {
        super.setNormalState();
        mArrowImageView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setRefreshingState() {
        super.setRefreshingState();
        mArrowImageView.clearAnimation();
        mArrowImageView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        mStatusTextView.setText(R.string.header_state_refreshing);
    }

    @Override
    public void setReleaseToRefreshState() {
        super.setReleaseToRefreshState();
        mArrowImageView.clearAnimation();
        mArrowImageView.startAnimation(mRotateDownAnim);
        mStatusTextView.setText(R.string.header_state_ready);
    }

    @Override
    public void setDoneState() {
        super.setDoneState();
        mArrowImageView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        mStatusTextView.setText(R.string.header_state_refresh_done);
    }

    public interface ArrowRefreshHeaderRefreshCompletedListener {
        void onHeaderRefreshCompleted(boolean tag);
    }
}
