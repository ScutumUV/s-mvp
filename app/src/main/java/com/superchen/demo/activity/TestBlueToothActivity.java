package com.superchen.demo.activity;


import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.superc.lib.ui.activity.SActivity;
import com.superc.lib.util.PermissionUtil;
import com.superchen.demo.R;
import com.superchen.demo.interface_.SearchBlueToothListener;
import com.superchen.demo.util.BlueToothUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by superchen on 2017/8/2.
 */
public class TestBlueToothActivity extends SActivity {

    @BindViews({R.id.this_phone_blue_tooth_ip})
    List<TextView> tvLists;
    @BindViews(R.id.start_search_blut_tooth)
    List<Button> btnLists;
    @BindView(R.id.blue_tooth_list)
    ListView mLV;

    private boolean grantP = false;
    private List<BlueTooth> datas = new ArrayList<>();

    private List<BluetoothGattService> mGattServices = new ArrayList<>();
    List<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics;
    ArrayList<BluetoothGattCharacteristic> charas;
    String uuid;
    String LIST_UUID = "LIST_UUID";


    @Override
    protected int setLayoutResId() {
        return R.layout.activity_test_blue_tooth;
    }

    @Override
    protected void initViews() {
        getPermission();
    }

    @OnClick({R.id.start_search_blut_tooth})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_search_blut_tooth:
                if (grantP) {
//                    searchBlueTooth(this);
                    if (BlueToothUtil.checkPhoneBLE(this)) {
                        BlueToothUtil.getDevices(this, listener);
                    }
                } else {
                    getPermission();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (PermissionUtil.checkPermissionsGroupStatus(grantResults)) {
                grantP = true;
            }
        }
    }

    private void getPermission() {
        if (!PermissionUtil.checkGroupPermissionsGroupStatus(this, Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN)) {
            PermissionUtil.checkPermissionsAndAutoRequestPermissions(this, 101,
                    Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN);
        } else {
            grantP = true;
        }
    }


    private SearchBlueToothListener listener = new SearchBlueToothListener() {
        @Override
        public void startSearch() {
            Log.i("msgg", "startSearch");
        }

        @Override
        public void whileSearch(BluetoothDevice device) {
            Log.i("msgg", "whileSearch  device = " + device.toString());

        }

        @Override
        public void finishSearch(Map<String, List<BlueTooth>> blueToothMap) {
            datas.clear();
            Log.i("msgg", "finishSearch  blueToothMap = " + blueToothMap.toString());
            for (Map.Entry<String, List<BlueTooth>> m : blueToothMap.entrySet()) {
                for (BlueTooth b : m.getValue()) {
                    b.setDeviceType(m.getKey());
                }
                datas.addAll(m.getValue());
            }
            mLV.setAdapter(new A());
        }
    };

    class A extends BaseAdapter {

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return datas.get(position).hashCode();
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(TestBlueToothActivity.this).inflate(R.layout.item_blue_tooth_layout, null);
                holder = new Holder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            holder.tvName.setText(datas.get(position).getName());
            holder.tvType.setText(datas.get(position).getType() + "");
            holder.tvAddress.setText(datas.get(position).getAddress());
            holder.tvUUID.setText(datas.get(position).getUuid() + "");
            holder.tvDeviceType.setText(datas.get(position).getDeviceType());
            return convertView;
        }
    }

    class Holder {
        TextView tvName;
        TextView tvType;
        TextView tvAddress;
        TextView tvUUID;
        TextView tvDeviceType;

        public Holder(View view) {
            tvName = (TextView) view.findViewById(R.id.blue_tooth_device_name);
            tvType = (TextView) view.findViewById(R.id.blue_tooth_type);
            tvAddress = (TextView) view.findViewById(R.id.blue_tooth_device_address);
            tvUUID = (TextView) view.findViewById(R.id.blue_tooth_device_uuid);
            tvDeviceType = (TextView) view.findViewById(R.id.blue_tooth_device_type);
        }
    }


    @SuppressLint("NewApi")
    private void getUUID(List<BluetoothGattService> gattServices) {
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData = new ArrayList<ArrayList<HashMap<String, String>>>();
        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();
            if (gattService.getType() == BluetoothGattService.SERVICE_TYPE_PRIMARY) {
                uuid = gattService.getUuid().toString();
            }
            if (gattService.getType() == BluetoothGattService.SERVICE_TYPE_SECONDARY) {
                uuid = gattService.getUuid().toString();
            }
            currentServiceData.put(LIST_UUID, uuid);
            gattServiceData.add(currentServiceData);
            ArrayList<HashMap<String, String>> gattCharacteristicGroupData = new ArrayList<HashMap<String, String>>();
            List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
            charas = new ArrayList<BluetoothGattCharacteristic>();
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                HashMap<String, String> currentCharaData = new HashMap<String, String>();
                uuid = gattCharacteristic.getUuid().toString();
                if ((BluetoothGattCharacteristic.PROPERTY_READ | gattCharacteristic.getProperties()) > 0) {
                    // READ set one
                    uuid = gattCharacteristic.getUuid().toString();
                    ;
                }
                if ((BluetoothGattCharacteristic.PROPERTY_WRITE & gattCharacteristic.getProperties()) > 0) {
                    // write set one
                    uuid = gattCharacteristic.getUuid().toString();
                }
                currentCharaData.put(LIST_UUID, uuid);
                gattCharacteristicGroupData.add(currentCharaData);
            }
            mGattCharacteristics.add(charas);
            gattCharacteristicData.add(gattCharacteristicGroupData);
//            setCharacteristicNotification();
        }
    }


    @SuppressLint("NewApi")
    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {


        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                gatt.discoverServices();
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            mGattServices = gatt.getServices();
            getUUID(mGattServices);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
        }


        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
        }


        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);

        }


        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
        }
    };
}
