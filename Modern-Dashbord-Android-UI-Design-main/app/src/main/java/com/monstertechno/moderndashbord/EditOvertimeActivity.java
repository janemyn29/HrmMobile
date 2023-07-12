package com.monstertechno.moderndashbord;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.style.UpdateAppearance;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.monstertechno.moderndashbord.Adapter.OvertimeAdapter;
import com.monstertechno.moderndashbord.Adapter.PagingScrollListener;
import com.monstertechno.moderndashbord.Api.ApiService;
import com.monstertechno.moderndashbord.Data.DataManager;
import com.monstertechno.moderndashbord.Model.DefaultModel;
import com.monstertechno.moderndashbord.Model.EditOvertime;
import com.monstertechno.moderndashbord.Model.Overtime;
import com.monstertechno.moderndashbord.Model.PagingOvertime;
import com.monstertechno.moderndashbord.Model.TempInfor;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditOvertimeActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    Toolbar toolbar;
    DataManager dataManager = DataManager.getInstance();
    TempInfor data = dataManager.getTempInfor();
    String Token = "Bearer "+ data.getToken();
    private Menu myMenu;
    MenuItem menuItem;
    TextView tvId, tvDate,tvShift,status ,  tvError;
    EditText reason;
    Button btnAccept, btnCancel;
    List<DefaultModel> defaultModel = new ArrayList<>();
    Overtime overtime;
    ProgressDialog pd;
    String id = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_overtime);

        tabLayout = findViewById(R.id.detail_contact_tablayout);
        Intent intent = getIntent();
        id= intent.getStringExtra("id");
        toolbar = findViewById(R.id.contract_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvId = findViewById(R.id.detail_leave_id);
        tvDate = findViewById(R.id.detail_leave_date);
        tvShift = findViewById(R.id.detail_leave_shift);
        reason = findViewById(R.id.overtime_detail_reason);
        status = (TextView) findViewById(R.id.detail_leave_tvstatus);
        btnAccept= findViewById(R.id.overtime_btnAccept);
        btnCancel= findViewById(R.id.overtime_btnCancel);
        tvError = findViewById(R.id.overtime_edit_error);
        loadData();

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnAccept.setActivated(false);
                btnCancel.setActivated(false);

                setAccept();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnAccept.setActivated(false);
                btnCancel.setActivated(false);
                setBtnCancel();
            }
        });



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                // Thực hiện chuyển đổi Activity dựa trên vị trí của TabItem
                switch (position) {
                    case 0:
                        startActivity(new Intent(EditOvertimeActivity.this, MainActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(EditOvertimeActivity.this, OvertimeActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(EditOvertimeActivity.this, AttendanceActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(EditOvertimeActivity.this, InforActivity.class));
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
        //loadData();

    }

    private void setAccept() {
        pd=  new ProgressDialog(EditOvertimeActivity.this);
        pd.setTitle("Phản hồi yêu cầu");
        pd.setMessage("Đang chấp nhận...!!!");
        pd.show();
        EditOvertime model = new EditOvertime();
        model.id = id;
        model.status = 2;
        ApiService.apiService.UpdateStatusOvertimeLogRequest(Token,model).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code()==200){
                    pd.dismiss();

                    Toast.makeText(EditOvertimeActivity.this,"Chấp nhận yêu cầu tăng ca thành công!",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(EditOvertimeActivity.this, OvertimeDetailActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }else if(response.code()==403 || response.code() == 401){
                    Intent intent = new Intent(EditOvertimeActivity.this, LoginActivity.class);
                    intent.putExtra("Error", "Phiên đăng nhập đã hết hạn! Vui lòng đăng nhập lại!");
                    startActivity(intent);
                    pd.dismiss();
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
                    btnAccept.setActivated(true);
                    btnCancel.setActivated(true);
                    tvError.setText(errorDescription);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
                Intent intent = new Intent(EditOvertimeActivity.this, LoginActivity.class);
                intent.putExtra("Error", "Vui lòng kiểm tra lại kết nối Internet!");
                startActivity(intent);
            }
        });
    }


    private void setBtnCancel() {

        if(reason.getText().toString().equals("")){
            reason.setError("Vui lòng điền lý do từ chối");
            return;
        }
        pd=  new ProgressDialog(EditOvertimeActivity.this);
        pd.setTitle("Phản hồi yêu cầu");
        pd.setMessage("Đang từ chối...!!!");
        pd.show();
        EditOvertime model = new EditOvertime();
        model.id = id;
        model.status = 3;
        model.cancelReason = reason.getText().toString();
        ApiService.apiService.UpdateStatusOvertimeLogRequest(Token,model).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code()==200){
                    pd.dismiss();

                    Toast.makeText(EditOvertimeActivity.this,"Từ chối yêu cầu tăng ca thành công!",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(EditOvertimeActivity.this, OvertimeDetailActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }else if(response.code()==403 || response.code() == 401){
                    Intent intent = new Intent(EditOvertimeActivity.this, LoginActivity.class);
                    intent.putExtra("Error", "Phiên đăng nhập đã hết hạn! Vui lòng đăng nhập lại!");
                    startActivity(intent);
                    pd.dismiss();
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
                    btnAccept.setActivated(true);
                    btnCancel.setActivated(true);
                    tvError.setText(errorDescription);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
                Intent intent = new Intent(EditOvertimeActivity.this, LoginActivity.class);
                intent.putExtra("Error", "Vui lòng kiểm tra lại kết nối Internet!");
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_contract, menu);

        myMenu = menu;

        return true;
    }

    private void loadData() {
        if(id==null){
            Intent intent = new Intent(EditOvertimeActivity.this, OvertimeActivity.class);
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

                        status.setText(overtime.status);
                        if(overtime.status.equals("Request")){
                            int color = ContextCompat.getColor(EditOvertimeActivity.this, R.color.yellow);
                            status.setTextColor(color);
                        }else if(overtime.status.equals("Approved")){
                            int color = ContextCompat.getColor(EditOvertimeActivity.this, R.color.Green);
                            status.setTextColor(color);
                        }else if(overtime.status.equals("Cancel")){
                            int color = ContextCompat.getColor(EditOvertimeActivity.this, R.color.error_200);
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
                        Intent intent = new Intent(EditOvertimeActivity.this, LoginActivity.class);
                        intent.putExtra("Error", "Phiên đăng nập đã hết hạn! Vui lòng đăng nhập lại!");
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(EditOvertimeActivity.this, OvertimeActivity.class);
                        Toast.makeText(EditOvertimeActivity.this ,"Đã xảy ra lỗi trong quá trình xử lý!" ,Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(Call<Overtime> call, Throwable t) {
                    Intent intent = new Intent(EditOvertimeActivity.this, LoginActivity.class);
                    intent.putExtra("Error", "Vui lòng kiểm tra kết nối Internet");
                    startActivity(intent);
                }
            });
        }
    }

    /*@Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.menu_edit:
                Intent intent = new Intent(OvertimeDetailActivity.this,EditOvertimeActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }*/
}