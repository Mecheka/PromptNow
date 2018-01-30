package com.example.suriya.promptnom.util;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Suriya on 19/12/2560.
 */

public class Transition implements Parcelable {

    String tranID, itemID, deviceID, empID, dateLand, dateReturn;
    boolean lendState;

    public Transition() {

    }

    public Transition(String tranID, String itemID, String deviceID, String empID, String dateLand, boolean lendState) {
        this.tranID = tranID;
        this.itemID = itemID;
        this.deviceID = deviceID;
        this.empID = empID;
        this.dateLand = dateLand;
        this.lendState = lendState;
    }

    public Transition(String tranID, String itemID, String deviceID, String empID, String dateLand, String dateReturn, boolean lendState) {
        this.tranID = tranID;
        this.itemID = itemID;
        this.deviceID = deviceID;
        this.empID = empID;
        this.dateLand = dateLand;
        this.dateReturn = dateReturn;
        this.lendState = lendState;
    }

    public Transition(Parcel in) {
        tranID = in.readString();
        itemID = in.readString();
        deviceID = in.readString();
        empID = in.readString();
        dateLand = in.readString();
        dateReturn = in.readString();
    }

    public static final Creator<Transition> CREATOR = new Creator<Transition>() {
        @Override
        public Transition createFromParcel(Parcel in) {
            return new Transition(in);
        }

        @Override
        public Transition[] newArray(int size) {
            return new Transition[size];
        }
    };

    public String getTranID() {
        return tranID;
    }

    public void setTranID(String tranID) {
        this.tranID = tranID;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getEmpID() {
        return empID;
    }

    public void setEmpID(String empID) {
        this.empID = empID;
    }

    public String getDateLand() {
        return dateLand;
    }

    public void setDateLand(String dateLand) {
        this.dateLand = dateLand;
    }

    public String getDateReturn() {
        return dateReturn;
    }

    public void setDateReturn(String dateReturn) {
        this.dateReturn = dateReturn;
    }

    public boolean isLendState() {
        return lendState;
    }

    public void setLendState(boolean lendState) {
        this.lendState = lendState;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(tranID);
        parcel.writeString(itemID);
        parcel.writeString(deviceID);
        parcel.writeString(empID);
        parcel.writeString(dateLand);
        parcel.writeString(dateReturn);
    }
}
