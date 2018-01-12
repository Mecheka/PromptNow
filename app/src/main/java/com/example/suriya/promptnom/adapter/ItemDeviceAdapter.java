package com.example.suriya.promptnom.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.suriya.promptnom.util.ItemDevice;
import com.example.suriya.promptnom.view.ListItemDevice;

import java.util.ArrayList;

/**
 * Created by Suriya on 17/12/2560.
 */

public class ItemDeviceAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<ItemDevice> itemList;

    public ItemDeviceAdapter(Context mContext, ArrayList<ItemDevice> itemList) {
        this.mContext = mContext;
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int i) {
        return itemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ListItemDevice item;
        if (view != null){
            item = (ListItemDevice) view;
        }else {
            item = new ListItemDevice(mContext);
        }
        ItemDevice itemDevice = (ItemDevice)getItem(i);
        item.setTextNumber(itemDevice.getItemNumber());
        item.setStatusText(itemDevice.getItemStatus());
        return item;
    }
}
