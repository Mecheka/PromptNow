package com.example.suriya.promptnom.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.suriya.promptnom.R;

/**
 * Created by Suriya on 12/12/2560.
 */

public class ListDeviceItem extends FrameLayout {

    ImageView imgDevice;
    TextView tvDeviceName, tvDeviceDetail, tvDeviceItem;

    public ListDeviceItem(@NonNull Context context) {
        super(context);
        initInflate();
    }

    public ListDeviceItem(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initInflate();
    }

    public ListDeviceItem(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
    }

    @TargetApi(21)
    public ListDeviceItem(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInflate();
    }

    private void initInflate() {
        inflate(getContext(), R.layout.list_device, this);
        imgDevice = (ImageView) findViewById(R.id.imgDevice);
        tvDeviceName = (TextView) findViewById(R.id.tvDeviceName);
    }

    public void setImage(String urlImage) {
        // TODO: Load Image
        Glide.with(getContext())
                .load(urlImage)
                .into(imgDevice);
    }

    public void setNameText(String text) {
        tvDeviceName.setText(text);
    }

}
