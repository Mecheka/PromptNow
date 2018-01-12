package com.example.suriya.promptnom.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.suriya.promptnom.util.ReDevice;
import com.example.suriya.promptnom.view.ListDeviceItem;

import java.util.ArrayList;

/**
 * Created by Suriya on 15/12/2560.
 */

public class DeviceAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<ReDevice> devices;

    public DeviceAdapter(Context mContext, ArrayList<ReDevice> devices) {
        this.mContext = mContext;
        this.devices = devices;
    }

    @Override
    public int getCount() {
        return devices.size();
    }

    @Override
    public Object getItem(int i) {
        return devices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ListDeviceItem item;
        if (view != null){
            item = (ListDeviceItem) view;
        }else {
            item = new ListDeviceItem(mContext);
        }
        ReDevice device = (ReDevice) getItem(i);
        item.setImage(device.getUrlPhotoDevice().toString());
        item.setNameText(device.getBrand()+"\n"+device.getDeviceName());
        return item;
    }
}
