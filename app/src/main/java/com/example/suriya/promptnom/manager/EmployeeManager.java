package com.example.suriya.promptnom.manager;

import android.content.Context;

import com.inthecheesefactory.thecheeselibrary.manager.Contextor;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class EmployeeManager {

    private static EmployeeManager instance;

    public static EmployeeManager getInstance() {
        if (instance == null)
            instance = new EmployeeManager();
        return instance;
    }

    private String ruleID, userID;

    private Context mContext;

    private EmployeeManager() {
        mContext = Contextor.getInstance().getContext();
    }

    public String getRuleID() {
        return ruleID;
    }

    public void setRuleID(String ruleID) {
        this.ruleID = ruleID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
