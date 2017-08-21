package com.superchen.demo.activity;

import android.view.View;

import com.superc.lib.ui.activity.SActivity;
import com.superchen.demo.R;
import com.superchen.demo.fragment.libTest1.LibTestFragment1;
import com.superchen.demo.fragment.libTest2.LibTestFragment2;

import butterknife.OnClick;

public class TestLibActivity extends SActivity {

//    @BindView(R.id.lib_test_container)
//    FrameLayout containerFL;

    private int i = 0;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_test_lib;
    }

    @Override
    protected void initViews() {

    }

    @OnClick({R.id.lib_test_btn_add, R.id.lib_test_btn_back})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.lib_test_btn_add:
                if (i == 0) {
                    addFragmentNoAnimation(LibTestFragment1.newInstance(), R.id.lib_test_container);
                } else if (i == 1) {
                    addFragmentNormalAnimation(LibTestFragment2.newInstance(), R.id.lib_test_container);
                }
                if (i > 1) {
                    i = 1;
                }
                i++;
                break;
            case R.id.lib_test_btn_back:
                i--;
                backFragment();
                break;
        }
    }

}
