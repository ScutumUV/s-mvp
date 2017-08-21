package com.superchen.demo.activity;

import android.os.ParcelUuid;

import java.util.Arrays;

/**
 * Created by superchen on 2017/8/3.
 */

public class BlueTooth {
    private String name;
    private String address;
    private int type;
    private ParcelUuid[] uuid;
    private String deviceType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ParcelUuid[] getUuid() {
        return uuid;
    }

    public void setUuid(ParcelUuid[] uuid) {
        this.uuid = uuid;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    @Override
    public String toString() {
        return "BlueTooth{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", type=" + type +
                ", uuid=" + Arrays.toString(uuid) +
                ", deviceType='" + deviceType + '\'' +
                '}';
    }
}
