package com.superchen.demo.service;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.superchen.demo.IMyTestAidl;

/**
 * Created by superchen on 2017/5/25.
 */
public class TestAIDLService extends Service {

    private final IMyTestAidl.Stub mIBinder = new IMyTestAidl.Stub() {
        @Override
        public String testShow(String msg) throws RemoteException {
            Log.i("AIDLT", "testShow msg :" + msg);
            return msg + " Success";
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mIBinder;
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
    }
}
