package com.superchen.demo.fragment.libTest1;

import com.superc.lib.util.LogUtils;
import com.superc.lib.widget.recyclerview.ItemViewDelegate;
import com.superc.lib.widget.recyclerview.RViewHolder;
import com.superchen.demo.R;

/**
 * Created by superchen on 2017/7/27.
 */
public class TestRRecyclerViewDelegate3 implements ItemViewDelegate<String> {

    @Override
    public int setLayoutResId() {
        return R.layout.item_blue_tooth_layout;
    }

    @Override
    public boolean isForViewType(String item, int position) {
        if (position == 3) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onBindViewHolder(RViewHolder holder, String s, int position) {
        LogUtils.d("TestRRecyclerViewDelegate1 position = " + position);
        holder.setText(R.id.blue_tooth_device_name, "蓝牙名称" + s);
    }
}
