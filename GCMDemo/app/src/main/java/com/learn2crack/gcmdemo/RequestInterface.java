package com.learn2crack.gcmdemo;

import com.learn2crack.gcmdemo.models.RequestBody;
import com.learn2crack.gcmdemo.models.ResponseBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RequestInterface {

    @POST("devices")
    Call<ResponseBody> registerDevice(@Body RequestBody body);
}
