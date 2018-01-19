package com.example.suriya.promptnom;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Suriya on 16/1/2561.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService{

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        sendRegistrationToServer(refreshedToken);
        
    }

    private void sendRegistrationToServer(String refreshedToken) {
    }
}
