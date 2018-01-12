package com.example.suriya.promptnom.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.example.suriya.promptnom.R;
import com.example.suriya.promptnom.activity.MainActivity;
import com.example.suriya.promptnom.manager.EmployeeManager;
import com.example.suriya.promptnom.util.Transition;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class ListenTransition extends Service implements ChildEventListener {

    DatabaseReference mDataRefRoot;
    DatabaseReference mDataRefTran;
    DatabaseReference mDataRefUserTran;
    FirebaseAuth mAuth;

    public ListenTransition() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mDataRefRoot = FirebaseDatabase.getInstance().getReference();
        mDataRefTran = mDataRefRoot.child("Transition");
        mDataRefUserTran = mDataRefRoot.child("User-Transition");
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String ruleID = EmployeeManager.getInstance().getRuleID();
        FirebaseUser user = mAuth.getCurrentUser();
        if (ruleID != null) {
            if (ruleID.equals("Admin")) {
                mDataRefTran.addChildEventListener(this);
            } else {
                mDataRefUserTran.child(user.getUid()).addChildEventListener(this);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

        Transition tran = dataSnapshot.getValue(Transition.class);
        if (tran.isLendState() == true) {
            showNotification(tran);
        }

    }

    private void showNotification(Transition tran) {

        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getBaseContext(), 0, intent, 0);

        NotificationCompat.Builder builderNoti = new NotificationCompat.Builder(getBaseContext());

        builderNoti.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setTicker("PromptNow")
                .setContentInfo("New Transition")
                .setContentText("You have new transition")
                .setSmallIcon(R.mipmap.ic_launcher);

        NotificationManager manager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);

        int randomInt = new Random().nextInt(9999 - 1) + 1;
        manager.notify(randomInt, builderNoti.build());

    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
