package com.learn2crack.gcmdemo;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;


public class GCMInstanceIDListenerService extends InstanceIDListenerService {

    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}
