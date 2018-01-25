package com.example.suriya.promptnom.util;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Suriya on 20/12/2560.
 */


public class ReTransition implements Parcelable {

    String deviceID, urlDevice, brand, name, itemID, number, status, empID, empName,
            tranID, dateLend, dateReturn;
    boolean lendState;

    public ReTransition() {

    }

    public ReTransition(String deviceID, String urlDevice, String brand, String name, String itemID, String number, String status, String empID, String empName, String tranID, String dateLend, boolean lendState) {
        this.deviceID = deviceID;
        this.urlDevice = urlDevice;
        this.brand = brand;
        this.name = name;
        this.itemID = itemID;
        this.number = number;
        this.status = status;
        this.empID = empID;
        this.empName = empName;
        this.tranID = tranID;
        this.dateLend = dateLend;
        this.lendState = lendState;
    }

    public ReTransition(String deviceID, String urlDevice, String brand, String name, String itemID, String number, String status, String empID, String empName, String tranID, String dateLend, String dateReturn, boolean lendState) {
        this.deviceID = deviceID;
        this.urlDevice = urlDevice;
        this.brand = brand;
        this.name = name;
        this.itemID = itemID;
        this.number = number;
        this.status = status;
        this.empID = empID;
        this.empName = empName;
        this.tranID = tranID;
        this.dateLend = dateLend;
        this.dateReturn = dateReturn;
        this.lendState = lendState;
    }

    protected ReTransition(Parcel in) {
        deviceID = in.readString();
        urlDevice = in.readString();
        brand = in.readString();
        name = in.readString();
        itemID = in.readString();
        number = in.readString();
        status = in.readString();
        empID = in.readString();
        empName = in.readString();
        tranID = in.readString();
        dateLend = in.readString();
        dateReturn = in.readString();
        lendState = in.readByte() != 0;
    }

    public static final Creator<ReTransition> CREATOR = new Creator<ReTransition>() {
        @Override
        public ReTransition createFromParcel(Parcel in) {
            return new ReTransition(in);
        }

        @Override
        public ReTransition[] newArray(int size) {
            return new ReTransition[size];
        }
    };

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getUrlDevice() {
        return urlDevice;
    }

    public void setUrlDevice(String urlDevice) {
        this.urlDevice = urlDevice;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmpID() {
        return empID;
    }

    public void setEmpID(String empID) {
        this.empID = empID;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getTranID() {
        return tranID;
    }

    public void setTranID(String tranID) {
        this.tranID = tranID;
    }

    public String getDateLend() {
        return dateLend;
    }

    public void setDateLend(String dateLend) {
        this.dateLend = dateLend;
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
        parcel.writeString(deviceID);
        parcel.writeString(urlDevice);
        parcel.writeString(brand);
        parcel.writeString(name);
        parcel.writeString(itemID);
        parcel.writeString(number);
        parcel.writeString(status);
        parcel.writeString(empID);
        parcel.writeString(empName);
        parcel.writeString(tranID);
        parcel.writeString(dateLend);
        parcel.writeString(dateReturn);
        parcel.writeByte((byte) (lendState ? 1 : 0));
    }
}
