package com.example.suriya.promptnom.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.suriya.promptnom.util.Transition;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class CountItem extends Service {

    private DatabaseReference mDataRefUserTran;
    private DatabaseReference mDataRegTran;
    private DatabaseReference mDataRegRoot;
    private FirebaseAuth mAuth;
    private int count = 0;
    private List<Transition> tranList;

    public CountItem() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mDataRegRoot = FirebaseDatabase.getInstance().getReference();
        mDataRefUserTran = mDataRegRoot.child("User-Transition");
        mAuth = FirebaseAuth.getInstance();
        countData();
    }

    private void countData() {

        String userID = mAuth.getCurrentUser().getUid();

        mDataRefUserTran.child(userID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Transition tran = dataSnapshot.getValue(Transition.class);
                if (tran.isLendState() == true){
                    count++;
                    tranList.add(tran);
                    Log.d("User Item count ", String.valueOf(count));
                    return;
                }
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
        });

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
