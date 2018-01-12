package com.example.suriya.promptnom.util;

/**
 * Created by Suriya on 6/12/2560.
 */

public class Employee {

    String empID;
    String empName;
    String empLastname;
    String ruleID;
    String posID;
    String phone;
    String email;

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
}
