package com.superchen.demo.util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.util.Log;

import com.superc.lib.util.ToastUtil;
import com.superchen.demo.interface_.SearchBlueToothListener;
import com.superchen.demo.receiver.BlueToothReceiver;

import java.util.Set;

/**
 * Created by superchen on 2017/8/3.
 */
public class BlueToothUtil {

    public static boolean checkPhoneBLE(@NonNull Context context) {
        // 检查设备是否支持蓝牙
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) {
            // 设备不支持蓝牙
            ToastUtil.show(context, "设备不支持蓝牙BLE");
            return false;
        }
        // 打开蓝牙
        if (!adapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            // 设置蓝牙可见性，最多300秒
            intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            context.startActivity(intent);
        }
        return true;
    }

    public static void getDevices(@NonNull Context context, SearchBlueToothListener listener) {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> devices = adapter.getBondedDevices();
        for (int i = 0; i < devices.size(); i++) {
            BluetoothDevice device = (BluetoothDevice) devices.iterator().next();
            System.out.println(device.getName());
        }
        BlueToothReceiver r = new BlueToothReceiver();
        r.setSearchListener(listener);
        // 设置广播信息过滤
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        // 注册广播接收器，接收并处理搜索结果
        context.registerReceiver(r, intentFilter);
        // 寻找蓝牙设备，android会将查找到的设备以广播形式发出去
        adapter.startDiscovery();
    }

    public static void search(Context context) {
    }
}
