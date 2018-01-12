package com.example.suriya.promptnom.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.suriya.promptnom.util.ReTransition;
import com.example.suriya.promptnom.view.ListAllTransition;

import java.util.ArrayList;

/**
 * Created by Suriya on 22/12/2560.
 */

public class AllTransitionAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<ReTransition> itemList;

    public AllTransitionAdapter(Context mContext, ArrayList<ReTransition> itemList) {
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
        ListAllTransition item;
        if (view == null){
            item = new ListAllTransition(mContext);
        }else {
            item = (ListAllTransition) view;
        }
        ReTransition tran = (ReTransition)getItem(i);
        item.setImgDevice(tran.getUrlDevice());
        item.setTvBrand(tran.getBrand()+ " "+tran.getName());
        item.setTvNumber(tran.getNumber());
        item.setTvDateLend(tran.getDateLend());
        return item;
    }
}
