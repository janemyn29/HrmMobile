package com.monstertechno.moderndashbord.Api;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.monstertechno.moderndashbord.Model.IpClass;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiServiceIp {
    String baseUrl = "https://api-bdc.net/";
    //String baseUrl = "https://api-bdc.net/";
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create();

    ApiServiceIp apiService = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build().create(ApiServiceIp.class);

    @GET("/data/client-info/")
    Call<ResponseBody> GetCurrentAttendance();
}
