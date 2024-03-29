package com.monstertechno.moderndashbord.Api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.monstertechno.moderndashbord.Model.AddDependant;
import com.monstertechno.moderndashbord.Model.Attendance;
import com.monstertechno.moderndashbord.Model.ChangePassModel;
import com.monstertechno.moderndashbord.Model.Contract;
import com.monstertechno.moderndashbord.Model.DefaultModel;
import com.monstertechno.moderndashbord.Model.EditOvertime;
import com.monstertechno.moderndashbord.Model.Enum;
import com.monstertechno.moderndashbord.Model.ErrorModel;
import com.monstertechno.moderndashbord.Model.IpClass;
import com.monstertechno.moderndashbord.Model.Leave;
import com.monstertechno.moderndashbord.Model.LeaveAddModel;
import com.monstertechno.moderndashbord.Model.LoginModel;
import com.monstertechno.moderndashbord.Model.Overtime;
import com.monstertechno.moderndashbord.Model.PagingAttendance;
import com.monstertechno.moderndashbord.Model.PagingContract;
import com.monstertechno.moderndashbord.Model.PagingLeave;
import com.monstertechno.moderndashbord.Model.PagingOvertime;
import com.monstertechno.moderndashbord.Model.PagingPayslip;
import com.monstertechno.moderndashbord.Model.Payslip;
import com.monstertechno.moderndashbord.Model.Position;
import com.monstertechno.moderndashbord.Model.Regulation;
import com.monstertechno.moderndashbord.Model.ResponseModel;
import com.monstertechno.moderndashbord.Model.ResultDependent;
import com.monstertechno.moderndashbord.Model.TempInfor;
import com.monstertechno.moderndashbord.Model.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ApiService {
    //DateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    String baseUrl = "https://hrmanagerfpt.azurewebsites.net/";
    //String baseUrl = "https://api-bdc.net/";
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

    @PUT("Emp/UpdateStatusOvertimeLogRequest")
    Call<ResponseBody> UpdateStatusOvertimeLogRequest(@Header("Authorization") String token, @Body EditOvertime model);


    @GET("Emp/AttendanceEmployee")
    Call<PagingAttendance> AttendanceEmployee(@Header("Authorization") String token, @Query("pg") int pg);

    @GET("Emp/AttendanceEmployee/GetAttendanceCurrentDay")
    Call<List<Attendance>> GetCurrentAttendance(@Header("Authorization") String token);

    @GET("Emp/AttendanceEmployee/AttendantRegulations")
    Call<Regulation> AttendantRegulations(@Header("Authorization") String token);

    @GET("Emp/AttendanceEmployee/CreateAttendanceForMobileOnly")
    Call<ResponseBody> AttendanceEmployeeCreate(@Header("Authorization") String token,@Query("ip") String ip);

    @GET("Emp/GetPosition")
    Call<Position> GetPosition(@Header("Authorization") String token);

    /*@GET("data/client-info")
    Call<IpClass> Ip();*/



    @GET("Emp/GetListPayslip")
    Call<PagingPayslip> Payslip(@Header("Authorization") String token, @Query("pg") int pg);

    @GET("Emp/GetDetailPayslip")
    Call<Payslip> GetPayslipById(@Header("Authorization") String token, @Query("id")String id);


    @PUT("Account/ChangePassword")
    Call<ResponseBody> ChangePassword(@Header("Authorization") String token, @Body ChangePassModel model);

    @GET("Emp/ListDependent")
    Call<ResultDependent> ListDependent(@Header("Authorization") String token, @Query("page") int pg);

    @POST("Emp/DependentCreate")
    Call<ResponseBody> DependentCreate(@Header("Authorization") String token, @Body AddDependant model );

}
