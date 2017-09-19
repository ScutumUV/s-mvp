package com.superc.lib.widget.popupwindow;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.superc.lib.R;
import com.superc.lib.util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 列表PopupWindow
 * <p>
 * Created by superchen on  2017/8/21
 * The last edit time       2017/8/21
 */
public class SListPopupWindow<T> {

    public static int DEFAULT_ANIM_STYLE = R.style.Default_PopupWindow_Animation;

    private PopupWindow mPop;

    private Activity mContext;

    private LinearLayout mContainer;

    private List<SMenuItem<T>> mDatas;

    private ListView mLv;

    private LAdapter mAdapter;
    /**
     * 列表宽高
     */
    private LinearLayout.LayoutParams params;
    /**
     * 是否允许背景变暗，默认为true
     */
    private boolean mDimBackground = true;
    /**
     * 背景变暗透明度
     */
    private float mBAlpha = 0.9f;
    /**
     * 默认背景变暗动画时长
     */
    private int mDuration = 250;
    /**
     * 是否显示，显示消失动画
     */
    private boolean mShowAnimation = true;
    /**
     * PopupWindow显示动画
     */
    private int mAnimationStyle;

    private @DrawableRes
    int mBackgroundDrawableResId = 0;

    public SListPopupWindow(@NonNull Activity context, @NonNull List<SMenuItem<T>> list) {
        mContext = context;
        mDatas = list;
        create();
    }

    public SListPopupWindow(@NonNull Activity context) {
        mContext = context;
        initItemList();
        create();
    }

    private void create() {
        mContainer = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.list_popup_window_layout, null);
        mLv = (ListView) mContainer.findViewById(R.id.list_popup_window_lv);
        mAdapter = new LAdapter();
    }

    private void initDefaultPopupWindow() {
        if (mBackgroundDrawableResId > 0) {
            mContainer.setBackgroundResource(mBackgroundDrawableResId);
        }
        if (params == null) {
            params = new LinearLayout.LayoutParams(ScreenUtil.getScreenWidth(mContext) / 3, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        mLv.setLayoutParams(params);
        mPop = new PopupWindow(mContainer, params.width, params.height);
        mPop.setFocusable(true);
        mPop.setOutsideTouchable(true);
        if (mShowAnimation) {
            this.mPop.setAnimationStyle(mAnimationStyle <= 0 ? DEFAULT_ANIM_STYLE : this.mAnimationStyle);
        }
        mPop.setBackgroundDrawable(new ColorDrawable());
        mPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                if (mDimBackground) {
                    setBackgroundAlpha(mBAlpha, 1.0F, 300);
                }
            }
        });
        mLv.setAdapter(mAdapter);
    }

    private void setBackgroundAlpha(float from, float to, int duration) {
        final WindowManager.LayoutParams lp = this.mContext.getWindow().getAttributes();
        ValueAnimator animator = ValueAnimator.ofFloat(new float[]{from, to});
        animator.setDuration((long) duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                lp.alpha = ((Float) animation.getAnimatedValue()).floatValue();
                mContext.getWindow().setAttributes(lp);
            }
        });
        animator.start();
    }

    private void dimBackground() {
        if (mDimBackground) {
            setBackgroundAlpha(1f, mBAlpha, mDuration);
        }
    }

    private void initItemList() {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
    }

    public void addItem(SMenuItem<T> item) {
        mDatas.add(item);
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener itemClickListener) {
        mLv.setOnItemClickListener(itemClickListener);
    }

    public void showAtLocation(View parent, int gravity, int x, int y) {
        initDefaultPopupWindow();
        mPop.showAtLocation(parent, gravity, x, y);
        dimBackground();
    }

    public void showAsDropDown(View anchor) {
        initDefaultPopupWindow();
        mPop.showAsDropDown(anchor);
        dimBackground();
    }

    public void showAsDropDown(View anchor, int xoff, int yoff) {
        initDefaultPopupWindow();
        mPop.showAsDropDown(anchor, xoff, yoff);
        dimBackground();
    }

    @SuppressLint("NewApi")
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        initDefaultPopupWindow();
        mPop.showAsDropDown(anchor, xoff, yoff, gravity);
        dimBackground();
    }

    public boolean isShowing() {
        initDefaultPopupWindow();
        return mPop.isShowing();
    }

    public boolean isDimBackground() {
        return mDimBackground;
    }

    public void setDimBackground(boolean mDimBackground) {
        this.mDimBackground = mDimBackground;
    }

    public float getBAlpha() {
        return mBAlpha;
    }

    public void setBAlpha(float mBAlpha) {
        this.mBAlpha = mBAlpha;
    }

    public int getDuration() {
        return mDuration;
    }

    public void setDuration(int mDuration) {
        this.mDuration = mDuration;
    }

    public boolean isShowAnimation() {
        return mShowAnimation;
    }

    public void setShowAnimation(boolean mShowAnimation) {
        this.mShowAnimation = mShowAnimation;
    }

    public int getAnimationStyle() {
        return mAnimationStyle;
    }

    public void setAnimationStyle(int mAnimationStyle) {
        this.mAnimationStyle = mAnimationStyle;
    }

    public List<SMenuItem<T>> getmDatas() {
        return mDatas;
    }

    public void setDatas(List<SMenuItem<T>> mDatas) {
        this.mDatas = mDatas;
    }

    public void setBackground(@DrawableRes int resId) {
        if (resId >= 0) {
            mContainer.setBackgroundResource(resId);
        }
    }

    public void setBackground(@NonNull Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mContainer.setBackground(drawable);
        } else {
            mContainer.setBackgroundDrawable(drawable);
        }
    }

    public LinearLayout.LayoutParams getParams() {
        return params;
    }

    public void setParams(LinearLayout.LayoutParams params) {
        this.params = params;
    }

    public void setWidthAndHeight(int width, int height) {
        if (params == null) {
            params = new LinearLayout.LayoutParams(width, height);
        } else {
            params.width = width;
            params.height = height;
        }
    }

    public int getWidth() {
        if (params != null) {
            return params.width;
        }
        return 0;
    }

    public int getHeight() {
        if (params != null) {
            return params.height;
        }
        return 0;
    }

    class LAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mDatas == null ? 0 : mDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mDatas.get(position).getItemId();
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.list_popup_window_item_layout, null);
                holder = new Holder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            if (mDatas != null && mDatas.size() > 0) {
                holder.iv.setImageResource(mDatas.get(position).getIcon());
                holder.tv.setText(mDatas.get(position).getTitle() + "");
            }
            return convertView;
        }
    }

    class Holder {

        ImageView iv;
        TextView tv;

        Holder(View v) {
            iv = (ImageView) v.findViewById(R.id.list_popup_window_item_iv);
            tv = (TextView) v.findViewById(R.id.list_popup_window_item_tv);
        }
    }
}
