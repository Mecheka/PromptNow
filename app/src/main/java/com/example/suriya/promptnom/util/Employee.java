package com.example.suriya.promptnom.util;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Suriya on 6/12/2560.
 */

public class Employee implements Parcelable{

    String empID;
    String empName;
    String empLastname;
    String ruleID;
    String posID;
    String phone;
    String email;
    String imageUrl;

    public Employee(){

    }

    public Employee(String empID, String empName, String empLastname, String ruleID, String posID, String phone, String email) {
        this.empID = empID;
        this.empName = empName;
        this.empLastname = empLastname;
        this.ruleID = ruleID;
        this.posID = posID;
        this.phone = phone;
        this.email = email;
    }

    public Employee(String empID, String empName, String empLastname, String ruleID, String posID, String phone, String email, String imageUrl) {
        this.empID = empID;
        this.empName = empName;
        this.empLastname = empLastname;
        this.ruleID = ruleID;
        this.posID = posID;
        this.phone = phone;
        this.email = email;
        this.imageUrl = imageUrl;
    }

    protected Employee(Parcel in) {
        empID = in.readString();
        empName = in.readString();
        empLastname = in.readString();
        ruleID = in.readString();
        posID = in.readString();
        phone = in.readString();
        email = in.readString();
        imageUrl = in.readString();
    }

    public static final Creator<Employee> CREATOR = new Creator<Employee>() {
        @Override
        public Employee createFromParcel(Parcel in) {
            return new Employee(in);
        }

        @Override
        public Employee[] newArray(int size) {
            return new Employee[size];
        }
    };

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

    public String getEmpLastname() {
        return empLastname;
    }

    public void setEmpLastname(String empLastname) {
        this.empLastname = empLastname;
    }

    public String getRuleID() {
        return ruleID;
    }

    public void setRuleID(String ruleID) {
        this.ruleID = ruleID;
    }

    public String getPosID() {
        return posID;
    }

    public void setPosID(String posID) {
        this.posID = posID;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(empID);
        parcel.writeString(empName);
        parcel.writeString(empLastname);
        parcel.writeString(ruleID);
        parcel.writeString(posID);
        parcel.writeString(phone);
        parcel.writeString(email);
        parcel.writeString(imageUrl);
    }
}
