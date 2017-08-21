package com.superchen.demo.fragment.libTest1;

import android.content.Context;

import com.superc.lib.widget.recyclerview.wrapper.RAdapter;

import java.util.List;

/**
 * Created by superchen on 2017/7/27.
 */
public class TestRRecyclerViewAdapter1 extends RAdapter<String> {

    public TestRRecyclerViewAdapter1(Context context, List<String> list) {
        super(context, list);
        addItemViewDelegate(0, new TestRRecyclerViewDelegate1());
        addItemViewDelegate(1, new TestRRecyclerViewDelegate2());
        addItemViewDelegate(2, new TestRRecyclerViewDelegate3());
    }
}
