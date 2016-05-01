package com.learn2crack.gcmdemo;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.learn2crack.gcmdemo.models.RequestBody;
import com.learn2crack.gcmdemo.models.ResponseBody;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistrationIntentService extends IntentService{


    public RegistrationIntentService() {
        super("RegistrationIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String deviceId = intent.getStringExtra("DEVICE_ID");
        String deviceName = intent.getStringExtra("DEVICE_NAME");

        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            String registrationId = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            registerDeviceProcess(deviceName,deviceId,registrationId);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void registerDeviceProcess(String deviceName, String deviceId, String registrationId){

        RequestBody requestBody = new RequestBody();
        requestBody.setDeviceId(deviceId);
        requestBody.setDeviceName(deviceName);
        requestBody.setRegistrationId(registrationId);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<ResponseBody> call = request.registerDevice(requestBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody responseBody = response.body();
                Intent intent = new Intent(MainActivity.REGISTRATION_PROCESS);
                intent.putExtra("result", responseBody.getResult());
                intent.putExtra("message",responseBody.getMessage());
                LocalBroadcastManager.getInstance(RegistrationIntentService.this).sendBroadcast(intent);

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }
}
