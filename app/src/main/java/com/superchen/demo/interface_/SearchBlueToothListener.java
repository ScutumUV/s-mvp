package com.superchen.demo.interface_;

import android.bluetooth.BluetoothDevice;

import com.superchen.demo.activity.BlueTooth;

import java.util.List;
import java.util.Map;

/**
 * Created by superchen on 2017/8/3.
 */
public interface SearchBlueToothListener {

    void startSearch();

    void whileSearch(BluetoothDevice device);

    void finishSearch(Map<String, List<BlueTooth>> blueToothMap);

}
