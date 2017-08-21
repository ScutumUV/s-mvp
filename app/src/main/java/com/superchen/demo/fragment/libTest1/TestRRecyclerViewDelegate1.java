package com.superchen.demo.fragment.libTest1;

import com.superc.lib.util.LogUtils;
import com.superc.lib.widget.recyclerview.ItemViewDelegate;
import com.superc.lib.widget.recyclerview.RViewHolder;
import com.superchen.demo.R;

/**
 * Created by superchen on 2017/7/27.
 */
public class TestRRecyclerViewDelegate1 implements ItemViewDelegate<String> {

    @Override
    public int setLayoutResId() {
        return R.layout.item_main;
    }

    @Override
    public boolean isForViewType(String item, int position) {
        if (position % 2 == 0 || position == 3) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onBindViewHolder(RViewHolder holder, String s, int position) {
        LogUtils.d("TestRRecyclerViewDelegate1 position = " + position);
        holder.setText(R.id.tv_main_item, s);
    }
}
