package com.superchen.demo;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import com.superc.lib.ui.activity.SActivity;
import com.superchen.demo.activity.TestAIDLActivity;
import com.superchen.demo.activity.TestBlueToothActivity;
import com.superchen.demo.activity.TestFrescoActivity;
import com.superchen.demo.activity.TestLibActivity;
import com.superchen.demo.activity.TestRXJavaActivity;
import com.superchen.demo.activity.TestSelectPhotoActivity;
import com.superchen.demo.adapter.MainAdapter;

import butterknife.BindView;


public class MainActivity extends SActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.main_recycler_view)
    RecyclerView recyclerView;

    private MainAdapter mAdapter;
    private String[] mDatas;

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        mDatas = getResources().getStringArray(R.array.MainListDatas);
        mAdapter = new MainAdapter(this, mDatas);
        mAdapter.setOnClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager
                (this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Class o = null;
        switch (position) {
            case 0:
                o = TestAIDLActivity.class;
                break;
            case 1:
                o = TestRXJavaActivity.class;
                break;
            case 2:
                o = TestFrescoActivity.class;
                break;
            case 3:
                o = TestLibActivity.class;
                break;
            case 4:
                o = TestBlueToothActivity.class;
                break;
            case 5:
                o = TestSelectPhotoActivity.class;
                break;
        }
        if (o != null) {
            startActivity(o);
        }
    }
}
