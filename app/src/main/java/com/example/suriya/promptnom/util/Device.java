package com.example.suriya.promptnom.util;

/**
 * Created by Suriya on 15/12/2560.
 */

public class Device {
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

    public Device(){

    }

    public Device(String deviceID, String urlPhotoDevice, String brand, String deviceName, String deviceCpu, String deviceRam, String deviceRom, String displaySize, String deviceOS, boolean deleteState) {
        this.deviceID = deviceID;
        this.urlPhotoDevice = urlPhotoDevice;
        this.brand = brand;
        this.deviceName = deviceName;
        this.deviceCpu = deviceCpu;
        this.deviceRam = deviceRam;
        this.deviceRom = deviceRom;
        this.displaySize = displaySize;
        this.deviceOS = deviceOS;
        this.deleteState = deleteState;
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
