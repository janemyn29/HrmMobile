package com.monstertechno.moderndashbord.Api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.monstertechno.moderndashbord.Model.Contract;
import com.monstertechno.moderndashbord.Model.DefaultModel;
import com.monstertechno.moderndashbord.Model.EditOvertime;
import com.monstertechno.moderndashbord.Model.Enum;
import com.monstertechno.moderndashbord.Model.Leave;
import com.monstertechno.moderndashbord.Model.LeaveAddModel;
import com.monstertechno.moderndashbord.Model.LoginModel;
import com.monstertechno.moderndashbord.Model.Overtime;
import com.monstertechno.moderndashbord.Model.PagingContract;
import com.monstertechno.moderndashbord.Model.PagingLeave;
import com.monstertechno.moderndashbord.Model.PagingOvertime;
import com.monstertechno.moderndashbord.Model.TempInfor;
import com.monstertechno.moderndashbord.Model.User;

import java.util.List;

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
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create();

    ApiService apiService = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build().create(ApiService.class);

    @POST("Login")
    Call<TempInfor> login(@Body LoginModel model);

    @GET("Emp/GetListContract2")
    Call<PagingContract> ListContract(@Header("Authorization") String token, @Query("pg") int pg);

    @GET("Emp/ContractDetail")
    Call<Contract> ContractDetail(@Header("Authorization") String token, @Query("code")String code);

    @GET("api/Enum/SalaryType")
    Call<List<Enum>> SalaryType();

    @GET("/api/Enum/LeaveShift")
    Call<List<DefaultModel>> ShiftLeave();

    @GET("api/Enum/ContractType")
    Call<List<Enum>> ContractType();

    @GET("api/Enum/EmployeeContractStatus")
    Call<List<Enum>> EmployeeContractStatus();

    @GET("Emp/Infor")
    Call<User> Infor(@Header("Authorization") String token);

    @GET("Emp/LeaveLog")
    Call<PagingLeave> LeaveLog(@Header("Authorization") String token, @Query("pg") int pg);

    @POST("Emp/CreateLeaveLog")
    Call<LeaveAddModel> CreateLeaveLog(@Header("Authorization") String token, @Body LeaveAddModel model );

    @GET("Emp/GetLeaveLogById")
    Call<Leave> GetLeaveLogById(@Header("Authorization") String token, @Query("id")String id);

    @GET("Emp/GetOvertimeLog")
    Call<PagingOvertime> GetOvertimeLog(@Header("Authorization") String token, @Query("pg") int pg);

    @GET("Emp/GetOvertimeLogById")
    Call<Overtime> GetOvertimeLogById(@Header("Authorization") String token, @Query("id")String id);

    @GET("Emp/UpdateStatusOvertimeLogRequest")
    Call<String> UpdateStatusOvertimeLogRequest(@Header("Authorization") String token, @Body EditOvertime model);

}
