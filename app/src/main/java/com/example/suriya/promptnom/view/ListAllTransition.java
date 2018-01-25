package com.example.suriya.promptnom.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.suriya.promptnom.R;
import com.inthecheesefactory.thecheeselibrary.view.BaseCustomViewGroup;
import com.inthecheesefactory.thecheeselibrary.view.state.BundleSavedState;

import org.w3c.dom.Text;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class ListAllTransition extends BaseCustomViewGroup {

    private TextView tvBrand, tvNumber, tvDateLend, tvEmpName;

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
        tvBrand = (TextView) findViewById(R.id.tvBrandName);
        tvNumber = (TextView) findViewById(R.id.tvNumber);
        tvDateLend = (TextView) findViewById(R.id.tvDateLend);
        tvEmpName = (TextView) findViewById(R.id.tvEmpName);

    }

    public void setTvBrand(String text) {
        tvBrand.setText(getResources().getString(R.string.brand) + " " + text);
    }

    public void setTvNumber(String text) {
        tvNumber.setText(getResources().getString(R.string.number) + " " + text);
    }

    public void setTvDateLend(String text) {
        tvDateLend.setText(getResources().getString(R.string.datelend) + " " + text);
    }

    public void setTvEmpName(String text) {
        tvEmpName.setText(getResources().getString(R.string.usertran) + " " + text);
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
