package com.monstertechno.moderndashbord.Api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.monstertechno.moderndashbord.Model.Contract;
import com.monstertechno.moderndashbord.Model.ContractList;
import com.monstertechno.moderndashbord.Model.LoginModel;
import com.monstertechno.moderndashbord.Model.PagingContract;
import com.monstertechno.moderndashbord.Model.TempInfor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    //DateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    String baseUrl = "https://hrmanagerfpt.azurewebsites.net/";
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();

    ApiService apiService = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build().create(ApiService.class);

    @POST("Login")
    Call<TempInfor> login(@Body LoginModel model);

    @GET("Emp/GetListContract2")
    Call<PagingContract> ListContract(@Header("Authorization") String token, @Query("pg") int pg);
}
