package com.example.suriya.promptnom.util;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Suriya on 15/12/2560.
 */

public class ItemDevice implements Parcelable{

    String itemId;
    String itemNumber;
    String itemStatus;

    public ItemDevice() {

    }

    public ItemDevice(String itemId, String itemNumber, String itemStatus) {
        this.itemId = itemId;
        this.itemNumber = itemNumber;
        this.itemStatus = itemStatus;
    }

    protected ItemDevice(Parcel in) {
        itemId = in.readString();
        itemNumber = in.readString();
        itemStatus = in.readString();
    }

    public static final Creator<ItemDevice> CREATOR = new Creator<ItemDevice>() {
        @Override
        public ItemDevice createFromParcel(Parcel in) {
            return new ItemDevice(in);
        }

        @Override
        public ItemDevice[] newArray(int size) {
            return new ItemDevice[size];
        }
    };

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(itemId);
        parcel.writeString(itemNumber);
        parcel.writeString(itemStatus);
    }
}
