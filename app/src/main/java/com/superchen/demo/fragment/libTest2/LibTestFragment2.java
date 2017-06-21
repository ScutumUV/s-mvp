package com.superchen.demo.fragment.libTest2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.superc.lib.ui.fragment.SFragment;
import com.superchen.demo.R;

public class LibTestFragment2 extends SFragment {

    public LibTestFragment2() {
    }

    public static LibTestFragment2 newInstance() {
        LibTestFragment2 fragment = new LibTestFragment2();
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_lib_test_fragment2;
    }

    @Override
    protected void initViews(View view) {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void popOnResume(View view) {
        Log.i("msgg", "LibTestFragment2 popOnResume");
    }

    @Override
    public void popOnPause(View view) {
        Log.i("msgg", "LibTestFragment2 popOnPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("msgg", "LibTestFragment2 onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("msgg", "LibTestFragment2 onPause");
    }
}
