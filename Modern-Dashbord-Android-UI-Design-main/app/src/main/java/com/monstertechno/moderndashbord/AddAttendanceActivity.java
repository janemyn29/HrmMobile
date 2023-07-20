package com.monstertechno.moderndashbord;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.monstertechno.moderndashbord.Adapter.AttendanceAdapter;
import com.monstertechno.moderndashbord.Adapter.PagingScrollListener;
import com.monstertechno.moderndashbord.Api.ApiService;
import com.monstertechno.moderndashbord.Api.ApiServiceIp;
import com.monstertechno.moderndashbord.Data.DataManager;
import com.monstertechno.moderndashbord.Model.Attendance;
import com.monstertechno.moderndashbord.Model.IpClass;
import com.monstertechno.moderndashbord.Model.PagingAttendance;
import com.monstertechno.moderndashbord.Model.Regulation;
import com.monstertechno.moderndashbord.Model.SelectListener;
import com.monstertechno.moderndashbord.Model.TempInfor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAttendanceActivity extends AppCompatActivity implements SelectListener {

    Toolbar toolbar;
    ProgressDialog pd ;
    private TabLayout tabLayout;
    DataManager dataManager = DataManager.getInstance();
    TempInfor data = dataManager.getTempInfor();
    String Token = "Bearer "+ data.getToken();
    RecyclerView recyclerView ;
    AttendanceAdapter adapter;
    String ip = "";
    private boolean isLoading;
    private  boolean isLastPage;
    private List<Attendance> mlist;
    private int totalPage ;
    private  int currentPage = 1;
    TextView tvError,tvTitle, tvQd1, tvQd2;
    Button btnAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_attendance);
        toolbar = findViewById(R.id.contract_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvError = findViewById(R.id.attendance_add_error);
        tvTitle = findViewById(R.id.attendance_add_title);
        tvQd1 = findViewById(R.id.attendance_add_quydinh1);
        tvQd2 = findViewById(R.id.attendance_add_quydinh2);

        btnAdd = findViewById(R.id.attendance_add_buttonAdd);

        pd=  new ProgressDialog(AddAttendanceActivity.this);
        pd.setTitle("Chấm công");
        pd.setMessage("Đang mở...!!!");
        pd.show();

        recyclerView = findViewById(R.id.leave_rv);
        adapter = new AttendanceAdapter(this, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        tabLayout = findViewById(R.id.contract_tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                // Thực hiện chuyển đổi Activity dựa trên vị trí của TabItem
                switch (position) {
                    case 0:
                        startActivity(new Intent(AddAttendanceActivity.this, MainActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(AddAttendanceActivity.this, OvertimeActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(AddAttendanceActivity.this, AttendanceActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(AddAttendanceActivity.this, InforActivity.class));
                        break;
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        setFirstData();
        loadRegulation();

        recyclerView.addOnScrollListener(new PagingScrollListener(linearLayoutManager) {
            @Override
            public void loadMoreItem() {
                isLoading=true;
                currentPage+=1;
                loadNextPage();

            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }
        });
        GetIp();
        Intent intent = getIntent();
        String data = intent.getStringExtra("Error");
        if(data!=null && data.isEmpty()&& data.equals("") ){
            Toast.makeText(this, data, Toast.LENGTH_LONG).show();

        }
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        chamcong();
                    }
        });

    }

    private void GetIp(){

        ApiServiceIp.apiService.GetCurrentAttendance().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Gson gson = new Gson();
                    IpClass ipInfo = gson.fromJson(response.body().string(), IpClass.class);
                    ip= ipInfo.ipString;
                }catch (Exception exception){

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void chamcong() {
        pd=  new ProgressDialog(AddAttendanceActivity.this);
        pd.setTitle("Chấm công");
        pd.setMessage("Đang chấm công...!!!");
        pd.show();
        if(ip.equals("")){
            tvError.setText("Vui lòng kiểm tra kết nối Internet");
            pd.dismiss();
            return;
        }
        ApiService.apiService.AttendanceEmployeeCreate(Token,ip).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200){
                    ResponseBody responseBody= response.body();
                    if(responseBody==null){
                        String Description = "";
                        try {
                            ResponseBody errorBody = response.body();
                            if (errorBody != null) {
                                Description = errorBody.string();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        pd.dismiss();
                        mlist.clear();
                        setFirstData();
                        return;
                    }


                }else if(response.code()==403 || response.code()==401){
                    pd.dismiss();

                    Intent intent = new Intent(AddAttendanceActivity.this, LoginActivity.class);
                        intent.putExtra("Error", "Phiên đăng nập đã hết hạn! Vui lòng đăng nhập lại!");
                        startActivity(intent);
                    }else{
                    String errorDescription = "";
                    try {
                        ResponseBody errorBody = response.errorBody();
                        if (errorBody != null) {
                            errorDescription = errorBody.string();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    pd.dismiss();

                    tvError.setText(errorDescription);
                    }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Intent intent = new Intent(AddAttendanceActivity.this, LoginActivity.class);
                intent.putExtra("Error", "Vui lòng kiểm tra kết nối Internet");
                startActivity(intent);
            }
        });
    }

    private void loadNextPage() {
        List<Attendance> list = load();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                adapter.removeFooterLoading();
                mlist.addAll(list);

                adapter.notifyDataSetChanged();
                isLoading= false;
                if(currentPage<totalPage){
                    adapter.addFooterLoading();
                }else{
                    isLastPage = true;
                }
            }
        }, 7000);

    }

    private void setFirstData(){

        mlist = load();
        new Handler().postDelayed( new Runnable(){

            @Override
            public void run() {
                adapter.setData(mlist);
                recyclerView.setAdapter(adapter);

                if(currentPage<totalPage){
                    adapter.addFooterLoading();
                }else{
                    isLastPage = true;
                }
                pd.dismiss();
            }
        }, 7000);

    }

    private void loadRegulation(){
        ApiService.apiService.AttendantRegulations(Token).enqueue(new Callback<Regulation>() {
            @Override
            public void onResponse(Call<Regulation> call, Response<Regulation> response) {
                if(response.code() == 200){
                    Regulation regulation= response.body();
                    if(regulation==null){
                        return;
                    }
                    tvTitle.setText(regulation.title);
                    tvQd1.setText(regulation.morning);
                    tvQd2.setText(regulation.afternoon);
                }else{
                    if(response.code()==403 || response.code()==401){
                        Intent intent = new Intent(AddAttendanceActivity.this, LoginActivity.class);
                        intent.putExtra("Error", "Phiên đăng nập đã hết hạn! Vui lòng đăng nhập lại!");
                        startActivity(intent);
                    }else{
                        tvError.setText("Hôm nay bạn chưa thực hiện chấm công!");
                    }
                }
            }

            @Override
            public void onFailure(Call<Regulation> call, Throwable t) {
                Intent intent = new Intent(AddAttendanceActivity.this, LoginActivity.class);
                intent.putExtra("Error", "Vui lòng kiểm tra kết nối Internet");
                startActivity(intent);

            }
        });
    }


    private ArrayList<Attendance> load(){
        ArrayList<Attendance> list = new ArrayList<>();
        Toast.makeText(this, "Tải thêm thông tin trang "+ currentPage,Toast.LENGTH_SHORT ).show();
        ApiService.apiService.GetCurrentAttendance(Token).enqueue(new Callback<List<Attendance>>() {
            @Override
            public void onResponse(Call<List<Attendance>> call, Response<List<Attendance>> response) {
                if(response.code() == 200){
                    List<Attendance> listAttendance= response.body();
                    if(listAttendance==null){
                        return;
                    }
                    list.addAll(listAttendance);
                    totalPage = 1;
                }else{
                    if(response.code()==403 || response.code()==401){
                        Intent intent = new Intent(AddAttendanceActivity.this, LoginActivity.class);
                        intent.putExtra("Error", "Phiên đăng nập đã hết hạn! Vui lòng đăng nhập lại!");
                        startActivity(intent);
                    }else{
                        tvError.setText("Hôm nay bạn chưa thực hiện chấm công!");
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Attendance>> call, Throwable t) {
                Intent intent = new Intent(AddAttendanceActivity.this, LoginActivity.class);
                intent.putExtra("Error", "Vui lòng kiểm tra kết nối Internet");
                startActivity(intent);

            }
        });
        return list;
    }


    @Override
    public void onItemClicked(Object attendance) {
        Attendance newattendance= (Attendance) attendance;
        Intent intent = new Intent(AddAttendanceActivity.this, LeaveDetailActivity.class);
        intent.putExtra("id", newattendance.id);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_leave, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.toolbar_logout){

                logout();
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddAttendanceActivity.this);
        builder.setTitle("Đăng xuất");
        builder.setMessage("Bạn có chắc chắn muốn đăng xuất?");
        builder.setPositiveButton("Đăng xuất", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(AddAttendanceActivity.this, "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();
                dataManager.setTempInfor(null);
                startActivity(new Intent(AddAttendanceActivity.this,LoginActivity.class));
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // Đóng dialog nếu người dùng chọn Hủy
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}