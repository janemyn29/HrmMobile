package com.monstertechno.moderndashbord;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.monstertechno.moderndashbord.Api.ApiService;
import com.monstertechno.moderndashbord.Data.DataManager;
import com.monstertechno.moderndashbord.Model.Contract;
import com.monstertechno.moderndashbord.Model.DefaultModel;
import com.monstertechno.moderndashbord.Model.Enum;
import com.monstertechno.moderndashbord.Model.Leave;
import com.monstertechno.moderndashbord.Model.TempInfor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaveDetailActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    Toolbar toolbar;
    DataManager dataManager = DataManager.getInstance();
    TempInfor data = dataManager.getTempInfor();
    String Token = "Bearer "+ data.getToken();
    TextView tvId, tvDate,tvReason,tvShift,tvCancel,status;
    List<DefaultModel> defaultModel = new ArrayList<>();
    String id = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_detail);

        toolbar = findViewById(R.id.contract_toolbar);
        setSupportActionBar(toolbar);

        tabLayout = findViewById(R.id.detail_contact_tablayout);
        Intent intent = getIntent();
        id= intent.getStringExtra("id");

        tvId = findViewById(R.id.detail_leave_id);
        tvDate = findViewById(R.id.detail_leave_date);
        tvShift = findViewById(R.id.detail_leave_shift);
        tvReason = findViewById(R.id.detail_leave_reason);
        tvCancel = findViewById(R.id.detail_leave_cancel);
        status = (TextView) findViewById(R.id.detail_leave_tvstatus);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                // Thực hiện chuyển đổi Activity dựa trên vị trí của TabItem
                switch (position) {
                    case 0:
                        startActivity(new Intent(LeaveDetailActivity.this, MainActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(LeaveDetailActivity.this, OvertimeActivity.class));
                        break;
                    case 2:
                        //startActivity(new Intent(MainActivity.this, Activity2.class));
                        break;
                    case 3:
                        startActivity(new Intent(LeaveDetailActivity.this, InforActivity.class));
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
        loadShift();
        loadData();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_contract, menu);
        return true;
    }

    private void loadShift() {
        ApiService.apiService.ShiftLeave().enqueue(new Callback<List<DefaultModel>>() {
            @Override
            public void onResponse(Call<List<DefaultModel>> call, Response<List<DefaultModel>> response) {
                if(response.code()==200){
                    defaultModel = response.body();
                }else if(response.code()==403|| response.code()==401){
                    Intent intent = new Intent(LeaveDetailActivity.this, LoginActivity.class);
                    intent.putExtra("Error", "Phiên đăng nhập đã hết hạn! Vui lòng đăng nhập lại!");
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(LeaveDetailActivity.this, LoginActivity.class);
                    intent.putExtra("Error", "Đã xảy ra lỗi trong quá trình xử lý!");
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<List<DefaultModel>> call, Throwable t) {
                Intent intent = new Intent(LeaveDetailActivity.this, LoginActivity.class);
                intent.putExtra("Error", "Vui lòng kiểm tra lại kết nối Internet!");
                startActivity(intent);
            }
        });
    }

    private void loadData() {
        if(id==null){
            Intent intent = new Intent(LeaveDetailActivity.this, ContractActivity.class);
            intent.putExtra("Error", "Đã xảy ra lỗi trong quá trình lấy chi tiết hợp đồng!");
            startActivity(intent);
        }else{
            ApiService.apiService.GetLeaveLogById(Token, id).enqueue(new Callback<Leave>() {
                @Override
                public void onResponse(Call<Leave> call, Response<Leave> response) {
                    if(response.code()==200) {
                        Leave leave = response.body();
                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        String end = dateFormat.format(leave.leaveDate);

                        tvId.setText(leave.id);
                        tvDate.setText(end);
                        for (DefaultModel model: defaultModel) {
                            if(model.value == leave.leaveShift){
                                tvShift.setText(model.display);
                            }
                        }
                        tvCancel.setText(leave.cancelReason);
                        tvReason.setText(leave.reason);
                        status.setText(leave.status);
                        if(leave.status.equals("Request")){
                            int color = ContextCompat.getColor(LeaveDetailActivity.this, R.color.yellow);
                            status.setTextColor(color);
                        }else if(leave.status.equals("Approved")){
                            int color = ContextCompat.getColor(LeaveDetailActivity.this, R.color.Green);
                            status.setTextColor(color);
                        }else if(leave.status.equals("Cancel")){
                            int color = ContextCompat.getColor(LeaveDetailActivity.this, R.color.error_200);
                            status.setTextColor(color);
                        }



                    } if(response.code()==403 || response.code()==401){
                        Intent intent = new Intent(LeaveDetailActivity.this, LoginActivity.class);
                        intent.putExtra("Error", "Phiên đăng nập đã hết hạn! Vui lòng đăng nhập lại!");
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(LeaveDetailActivity.this, LeaveActivity.class);
                        Toast.makeText(LeaveDetailActivity.this ,"Đã xảy ra lỗi trong quá trình xử lý!" ,Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(Call<Leave> call, Throwable t) {
                    Intent intent = new Intent(LeaveDetailActivity.this, LoginActivity.class);
                    intent.putExtra("Error", "Vui lòng kiểm tra kết nối Internet");
                    startActivity(intent);
                }
            });
        }
    }
}