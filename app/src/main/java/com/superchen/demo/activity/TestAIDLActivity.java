package com.superchen.demo.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;

import com.superc.lib.ui.activity.SActivity;
import com.superchen.demo.IMyTestAidl;
import com.superchen.demo.R;
import com.superchen.demo.service.TestAIDLService;

import butterknife.OnClick;

public class TestAIDLActivity extends SActivity {

    private IMyTestAidl iMyTestAidl;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iMyTestAidl = IMyTestAidl.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iMyTestAidl = null;
        }
    };

    @Override
    protected int setLayoutResId() {
        return R.layout.activity_test_aidl;
    }

    @Override
    protected void initViews() {
        connectAIDL();
    }

    @OnClick({R.id.aidl_start_btn})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.aidl_start_btn:
                startPrintAIDL();
                break;
        }
    }

    private void connectAIDL() {
        Bundle b = new Bundle();
        Intent i = new Intent(this, TestAIDLService.class);
        i.putExtras(b);
        bindService(i, connection, Context.BIND_AUTO_CREATE);
    }

    private void startPrintAIDL() {
        if (iMyTestAidl == null) {
            return;
        }
        try {
            String msg = iMyTestAidl.testShow("Test AIDL");
            showToastShort("success to start multi process : " + msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

}
