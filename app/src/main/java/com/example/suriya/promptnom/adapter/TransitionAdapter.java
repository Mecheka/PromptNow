package com.example.suriya.promptnom.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.suriya.promptnom.util.ReTransition;
import com.example.suriya.promptnom.view.ListTransition;

import java.util.ArrayList;

/**
 * Created by Suriya on 20/12/2560.
 */

public class TransitionAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<ReTransition> itemList;

    public TransitionAdapter(Context mContext, ArrayList<ReTransition> itemList) {
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
        ListTransition item;
        if (view == null){
            item = new ListTransition(mContext);
        }else {
            item = (ListTransition) view;
        }
        ReTransition itemTransition = (ReTransition)getItem(i);
        item.setImgDevice(itemTransition.getUrlDevice());
        item.setTvBrand(itemTransition.getBrand() +" "+ itemTransition.getName());
        item.setTvNumber(itemTransition.getNumber(), itemTransition.getStatus());
        item.setTvEmpName(itemTransition.getEmpName());
        return item;
    }
}
