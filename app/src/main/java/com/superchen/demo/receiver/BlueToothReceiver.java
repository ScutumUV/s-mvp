package com.superchen.demo.receiver;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.superchen.demo.activity.BlueTooth;
import com.superchen.demo.interface_.SearchBlueToothListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by superchen on 2017/8/3.
 */
public class BlueToothReceiver extends BroadcastReceiver {

    public final static String CONNECTED_BLUETOOTHS = "connectedBlueTooths";
    public final static String NEW_BLUETOOTHS = "newBlueTooths";

    private List<BlueTooth> connectedBlueTooths = new ArrayList<>();
    private List<BlueTooth> newBlueTooths = new ArrayList<>();
    private SearchBlueToothListener mSearchListener;

    @SuppressLint("NewApi")
    @Override
    public void onReceive(Context arg0, Intent arg1) {
        String action = arg1.getAction();
        switch (action) {
            case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                connectedBlueTooths.clear();
                newBlueTooths.clear();
                mSearchListener.startSearch();
                break;
            case BluetoothDevice.ACTION_FOUND:
                BluetoothDevice device = arg1.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                BlueTooth blueTooth = new BlueTooth();
                blueTooth.setName(device.getName());
                blueTooth.setAddress(device.getAddress());
                blueTooth.setType(device.getType());
                blueTooth.setUuid(device.getUuids());
                short rssi = arg1.getExtras().getShort(
                        BluetoothDevice.EXTRA_RSSI);
                Log.i("info", "蓝牙信号强度     " + rssi);
                if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    for (BlueTooth blueToothPul : connectedBlueTooths) {
                        if (blueToothPul.getAddress().equals(
                                blueTooth.getAddress()))
                            return;
                    }
                    connectedBlueTooths.add(blueTooth);
                } else {
                    for (BlueTooth blueToothPul : newBlueTooths) {
                        if (blueToothPul.getAddress().equals(
                                blueTooth.getAddress())) {
                            return;
                        }
                    }
                    newBlueTooths.add(blueTooth);
                }
                mSearchListener.whileSearch(device);
                break;
            case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                Map<String, List<BlueTooth>> blueToothMap = new HashMap<String, List<BlueTooth>>();
                // if (connectedBlueTooths != null
                // && connectedBlueTooths.size() > 0)
                blueToothMap.put(CONNECTED_BLUETOOTHS, connectedBlueTooths);
                // else
                // blueToothMap.put(CONNECTED_BLUETOOTHS, null);
                // if (newBlueTooths != null && newBlueTooths.size() > 0)
                blueToothMap.put(NEW_BLUETOOTHS, newBlueTooths);
                // else
                // blueToothMap.put(NEW_BLUETOOTHS, null);
                mSearchListener.finishSearch(blueToothMap);
                break;
        }
    }

    public void setSearchListener(SearchBlueToothListener mSearchListener) {
        this.mSearchListener = mSearchListener;
    }
}
