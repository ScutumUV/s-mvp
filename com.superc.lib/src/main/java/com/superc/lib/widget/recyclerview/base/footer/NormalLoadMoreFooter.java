package com.superc.lib.widget.recyclerview.base.footer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.superc.lib.R;
import com.superc.lib.widget.recyclerview.progressindicator.ProgressStyle;
import com.superc.lib.widget.recyclerview.SimpleViewSwitcher;
import com.superc.lib.widget.recyclerview.progressindicator.AVLoadingIndicatorView;

/**
 * Created by superchen on 2017/7/25.
 */
public class NormalLoadMoreFooter extends RBaseFooter {

    private TextView mText;
    private SimpleViewSwitcher progressCon;


    public NormalLoadMoreFooter(Context context) {
        super(context);
    }

    public NormalLoadMoreFooter(Context context, AttributeSet attrs) {
        super(context);
    }

    @Override
    protected void initViews() {
        super.initViews();
        progressCon = new SimpleViewSwitcher(getContext());
        progressCon.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        AVLoadingIndicatorView progressView = new AVLoadingIndicatorView(this.getContext());
        progressView.setIndicatorColor(0xffB5B5B5);
        progressView.setIndicatorId(ProgressStyle.BallSpinFadeLoader);
        progressCon.setView(progressView);

        addView(progressCon);
        mText = new TextView(getContext());
        mText.setText(getContext().getText(R.string.footer_state_loading));

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins((int) getResources().getDimension(R.dimen.footer_margin), 0, 0, 0);

        mText.setLayoutParams(layoutParams);
        addView(mText);
    }

    public void setProgressStyle(int style) {
        if (style == ProgressStyle.SysProgress) {
            progressCon.setView(new ProgressBar(getContext(), null, android.R.attr.progressBarStyle));
        } else {
            AVLoadingIndicatorView progressView = new AVLoadingIndicatorView(this.getContext());
            progressView.setIndicatorColor(0xffB5B5B5);
            progressView.setIndicatorId(style);
            progressCon.setView(progressView);
        }
    }

    @Override
    public void setStateComplete() {
        super.setStateComplete();
        mText.setText(getContext().getText(R.string.footer_state_success));
    }

    @Override
    public void setStateLoading() {
        super.setStateLoading();
        mText.setText(getContext().getText(R.string.footer_state_loading));
    }

    @Override
    public void setStateNoMore() {
        super.setStateNoMore();
        mText.setText(getContext().getText(R.string.footer_state_no_more_data));
    }
}
