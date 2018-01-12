package com.example.suriya.promptnom.util;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Suriya on 15/12/2560.
 */

public class ReDevice implements Parcelable{

    String deviceID;
    String urlPhotoDevice;
    String brand;
    String deviceName;
    String deviceCpu;
    String deviceRam;
    String deviceRom;
    String displaySize;
    String deviceOS;
    boolean deleteState;

    public ReDevice() {
    }

    protected ReDevice(Parcel in) {
        deviceID = in.readString();
        urlPhotoDevice = in.readString();
        brand = in.readString();
        deviceName = in.readString();
        deviceCpu = in.readString();
        deviceRam = in.readString();
        deviceRom = in.readString();
        displaySize = in.readString();
        deviceOS = in.readString();
        deleteState = in.readByte() != 0;
    }

    public static final Creator<ReDevice> CREATOR = new Creator<ReDevice>() {
        @Override
        public ReDevice createFromParcel(Parcel in) {
            return new ReDevice(in);
        }

        @Override
        public ReDevice[] newArray(int size) {
            return new ReDevice[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(deviceID);
        parcel.writeString(urlPhotoDevice);
        parcel.writeString(brand);
        parcel.writeString(deviceName);
        parcel.writeString(deviceCpu);
        parcel.writeString(deviceRam);
        parcel.writeString(deviceRom);
        parcel.writeString(displaySize);
        parcel.writeString(deviceOS);
        parcel.writeByte((byte) (deleteState ? 1 : 0));
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getUrlPhotoDevice() {
        return urlPhotoDevice;
    }

    public void setUrlPhotoDevice(String urlPhotoDevice) {
        this.urlPhotoDevice = urlPhotoDevice;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceCpu() {
        return deviceCpu;
    }

    public void setDeviceCpu(String deviceCpu) {
        this.deviceCpu = deviceCpu;
    }

    public String getDeviceRam() {
        return deviceRam;
    }

    public void setDeviceRam(String deviceRam) {
        this.deviceRam = deviceRam;
    }

    public String getDeviceRom() {
        return deviceRom;
    }

    public void setDeviceRom(String deviceRom) {
        this.deviceRom = deviceRom;
    }

    public String getDisplaySize() {
        return displaySize;
    }

    public void setDisplaySize(String displaySize) {
        this.displaySize = displaySize;
    }

    public String getDeviceOS() {
        return deviceOS;
    }

    public void setDeviceOS(String deviceOS) {
        this.deviceOS = deviceOS;
    }

    public boolean isDeleteState() {
        return deleteState;
    }

    public void setDeleteState(boolean deleteState) {
        this.deleteState = deleteState;
    }
}
