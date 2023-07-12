package com.monstertechno.moderndashbord;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.monstertechno.moderndashbord.Api.ApiService;
import com.monstertechno.moderndashbord.Data.DataManager;
import com.monstertechno.moderndashbord.Model.DefaultModel;
import com.monstertechno.moderndashbord.Model.Leave;
import com.monstertechno.moderndashbord.Model.Overtime;
import com.monstertechno.moderndashbord.Model.TempInfor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OvertimeDetailActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    Toolbar toolbar;
    DataManager dataManager = DataManager.getInstance();
    TempInfor data = dataManager.getTempInfor();
    String Token = "Bearer "+ data.getToken();
    private Menu myMenu;
    MenuItem menuItem;
    TextView tvId, tvDate,tvReason,tvShift,tvCancel,status;
    List<DefaultModel> defaultModel = new ArrayList<>();
    Overtime overtime;
    String id = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overtime_detail);

        tabLayout = findViewById(R.id.detail_contact_tablayout);
        Intent intent = getIntent();
        id= intent.getStringExtra("id");
        toolbar = findViewById(R.id.contract_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvId = findViewById(R.id.detail_leave_id);
        tvDate = findViewById(R.id.detail_leave_date);
        tvShift = findViewById(R.id.detail_leave_shift);
        tvCancel = findViewById(R.id.detail_leave_cancel);
        status = (TextView) findViewById(R.id.detail_leave_tvstatus);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                // Thực hiện chuyển đổi Activity dựa trên vị trí của TabItem
                switch (position) {
                    case 0:
                        startActivity(new Intent(OvertimeDetailActivity.this, MainActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(OvertimeDetailActivity.this, OvertimeActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(OvertimeDetailActivity.this, AttendanceActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(OvertimeDetailActivity.this, InforActivity.class));
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
        //loadShift();
        loadData();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail_overtime, menu);

        myMenu = menu;

        return true;
    }

    private void loadData() {
        if(id==null){
            Intent intent = new Intent(OvertimeDetailActivity.this, OvertimeActivity.class);
            intent.putExtra("Error", "Đã xảy ra lỗi trong quá trình lấy chi tiết hợp đồng!");
            startActivity(intent);
        }else{
            ApiService.apiService.GetOvertimeLogById(Token, id).enqueue(new Callback<Overtime>() {
                @Override
                public void onResponse(Call<Overtime> call, Response<Overtime> response) {
                    if(response.code()==200) {
                        overtime = response.body();
                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        String end = dateFormat.format(overtime.date);

                        tvId.setText(overtime.id);
                        tvDate.setText(end);
                        tvShift.setText(String.valueOf(overtime.hours));
                        tvCancel.setText(overtime.cancelReason);
                        status.setText(overtime.status);
                        if(overtime.status.equals("Request")){
                            int color = ContextCompat.getColor(OvertimeDetailActivity.this, R.color.yellow);
                            status.setTextColor(color);
                        }else if(overtime.status.equals("Approved")){
                            int color = ContextCompat.getColor(OvertimeDetailActivity.this, R.color.Green);
                            status.setTextColor(color);
                        }else if(overtime.status.equals("Cancel")){
                            int color = ContextCompat.getColor(OvertimeDetailActivity.this, R.color.error_200);
                            status.setTextColor(color);
                        }
                        menuItem = myMenu.findItem(R.id.menu_edit);
                        Date current = new Date();
                        if(!(current.compareTo(overtime.date) > 0)){
                            if(!overtime.status.equals("Request")){
                                menuItem.setVisible(false);
                            }
                        }
                    }else if(response.code()==403 || response.code()==401){
                        Intent intent = new Intent(OvertimeDetailActivity.this, LoginActivity.class);
                        intent.putExtra("Error", "Phiên đăng nập đã hết hạn! Vui lòng đăng nhập lại!");
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(OvertimeDetailActivity.this, OvertimeActivity.class);
                        Toast.makeText(OvertimeDetailActivity.this ,"Đã xảy ra lỗi trong quá trình xử lý!" ,Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(Call<Overtime> call, Throwable t) {
                    Intent intent = new Intent(OvertimeDetailActivity.this, LoginActivity.class);
                    intent.putExtra("Error", "Vui lòng kiểm tra kết nối Internet");
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.menu_edit:
                Intent intent = new Intent(OvertimeDetailActivity.this,EditOvertimeActivity.class);
                intent.putExtra("id",this.id);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}