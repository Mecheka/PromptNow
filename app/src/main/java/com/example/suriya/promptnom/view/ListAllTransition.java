package com.example.suriya.promptnom.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.suriya.promptnom.R;
import com.inthecheesefactory.thecheeselibrary.view.BaseCustomViewGroup;
import com.inthecheesefactory.thecheeselibrary.view.state.BundleSavedState;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class ListAllTransition extends BaseCustomViewGroup {

    private ImageView imgDevice;
    private TextView tvBrand, tvNumber, tvDateLend;

    public ListAllTransition(Context context) {
        super(context);
        initInflate();
        initInstances();
    }

    public ListAllTransition(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstances();
        initWithAttrs(attrs, 0, 0);
    }

    public ListAllTransition(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public ListAllTransition(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, defStyleRes);
    }

    private void initInflate() {
        inflate(getContext(), R.layout.list_all_transition, this);
    }

    private void initInstances() {
        // findViewById here
        imgDevice = (ImageView)findViewById(R.id.imgDevice);
        tvBrand = (TextView)findViewById(R.id.tvBrandName);
        tvNumber = (TextView)findViewById(R.id.tvNumber);
        tvDateLend = (TextView)findViewById(R.id.tvDateLend);
    }

    public void setImgDevice(String URL){
        Glide.with(getContext()).load(URL).into(imgDevice);
    }

    public void setTvBrand(String text){
        tvBrand.setText(text);
    }

    public void setTvNumber(String text){
        tvNumber.setText(text);
    }

    public void setTvDateLend(String text){
        tvDateLend.setText(text);
    }

    private void initWithAttrs(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        /*
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.StyleableName,
                defStyleAttr, defStyleRes);

        try {

        } finally {
            a.recycle();
        }
        */
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        BundleSavedState savedState = new BundleSavedState(superState);
        // Save Instance State(s) here to the 'savedState.getBundle()'
        // for example,
        // savedState.getBundle().putString("key", value);

        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        BundleSavedState ss = (BundleSavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        Bundle bundle = ss.getBundle();
        // Restore State from bundle here
    }

}
