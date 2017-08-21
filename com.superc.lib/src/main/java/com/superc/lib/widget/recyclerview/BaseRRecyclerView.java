package com.superc.lib.widget.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;


import java.util.List;

/**
 * Created by superchen on 2017/7/26.
 */
public class BaseRRecyclerView<T> extends RRecyclerView {

    private List<T> mDatas;


    public BaseRRecyclerView(Context context) {
        super(context, null);
    }

    public BaseRRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public BaseRRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
