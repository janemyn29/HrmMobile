package com.monstertechno.moderndashbord;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.monstertechno.moderndashbord.Adapter.CustomSpinnerAdapter;
import com.monstertechno.moderndashbord.Api.ApiService;
import com.monstertechno.moderndashbord.Data.DataManager;
import com.monstertechno.moderndashbord.Model.DefaultModel;
import com.monstertechno.moderndashbord.Model.Enum;
import com.monstertechno.moderndashbord.Model.LeaveAddModel;
import com.monstertechno.moderndashbord.Model.TempInfor;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddLeaveActivity extends AppCompatActivity {

    String REQUIRE = "Require";
    Toolbar toolbar;
    private TabLayout tabLayout;
    DataManager dataManager = DataManager.getInstance();
    TempInfor data = dataManager.getTempInfor();
    String Token = "Bearer "+ data.getToken();


    Spinner spinner;
    Button btnDate,btnCreate;
    EditText etReason;
    TextView tvError;
    List<DefaultModel> defaultModel = new ArrayList<>();
    ProgressDialog pd;
    private int initialYear;
    private int initialMonth;
    private int initialDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_leave);
        toolbar = findViewById(R.id.contract_toolbar);
        setSupportActionBar(toolbar);

        tabLayout = findViewById(R.id.contract_tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                // Thực hiện chuyển đổi Activity dựa trên vị trí của TabItem
                switch (position) {
                    case 0:
                        startActivity(new Intent(AddLeaveActivity.this, MainActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(AddLeaveActivity.this, OvertimeActivity.class));
                        //startActivity(new Intent(MainActivity.this, Activity2.class));
                        break;
                    case 2:
                        //startActivity(new Intent(MainActivity.this, Activity2.class));
                        break;
                    case 3:
                        startActivity(new Intent(AddLeaveActivity.this, InforActivity.class));
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

        spinner = findViewById(R.id.leave_add_shift);
        btnDate = findViewById(R.id.leave_add_btnselect);
        etReason = findViewById(R.id.leave_add_reason);
        btnCreate = findViewById(R.id.leave_add_btnCreate);
        tvError = findViewById(R.id.leave_add_error);
        setValueForSnipper();

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createLeaveLog();
            }
        });
    }

    private void createLeaveLog() {
        btnCreate.setActivated(false);
        pd=  new ProgressDialog(AddLeaveActivity.this);
        pd.setTitle("Danh sách nghỉ phép");
        pd.setMessage("Đang mở...!!!");
        pd.show();

        LeaveAddModel model = new LeaveAddModel();
        String dateString = btnDate.getText().toString();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date date = format.parse(dateString);
            // Sử dụng đối tượng date
            model.leaveDate = date;

        } catch (ParseException e) {
            pd.dismiss();
            btnCreate.setActivated(true);
            tvError.setText("Vui lòng chọn ngày nghỉ!");
            return;
        }
        int shift = 0;
        for (DefaultModel temp: defaultModel
             ) {
            if(temp.display.equals(spinner.getSelectedItem())){
                model.leaveShift = temp.value;
            }
        }
        String reason = etReason.getText().toString();
        if(reason.equals("")){
            etReason.setError(REQUIRE);
            btnCreate.setActivated(true);
            pd.dismiss();
            return;
        }
        model.reason = reason;

        ApiService.apiService.CreateLeaveLog(Token,model).enqueue(new Callback<LeaveAddModel>() {
            @Override
            public void onResponse(Call<LeaveAddModel> call, Response<LeaveAddModel> response) {
                if(response.code()==200){
                    pd.dismiss();
                    Toast.makeText(AddLeaveActivity.this, "Tạo yêu cầu nghỉ phép thành công",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(AddLeaveActivity.this, LeaveActivity.class));
                }else if(response.code()==403 || response.code()==401){
                    Intent intent = new Intent(AddLeaveActivity.this, LoginActivity.class);
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
                    btnCreate.setActivated(false);
                    tvError.setText(errorDescription);
                }
            }

            @Override
            public void onFailure(Call<LeaveAddModel> call, Throwable t) {
                pd.dismiss();
                Intent intent = new Intent(AddLeaveActivity.this, LoginActivity.class);
                intent.putExtra("Error", "Vui lòng kiểm tra lại kết nối Internet!");
                startActivity(intent);
            }
        });
    }

    public void btn_dateSelect_click(View view){
        Calendar calendar = Calendar.getInstance(); // Lấy thời gian hiện tại
        initialYear = calendar.get(Calendar.YEAR);
        initialMonth = calendar.get(Calendar.MONTH);
        initialDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(AddLeaveActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        String text = String.format("%d/%d/%d",i2, (i1+1), i);
                        btnDate.setText(text);
                    }
                },initialYear,initialMonth-1,initialDay);
        datePickerDialog.show();
    }
    private void setValueForSnipper() {
        ApiService.apiService.ShiftLeave().enqueue(new Callback<List<DefaultModel>>() {
            @Override
            public void onResponse(Call<List<DefaultModel>> call, Response<List<DefaultModel>> response) {
                if(response.code()==200){
                    defaultModel = response.body();
                    List<String> itemList = new ArrayList<>();
                    for (int i=0;i<defaultModel.size();i++){
                        itemList.add(defaultModel.get(i).display);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(AddLeaveActivity.this, android.R.layout.simple_spinner_item, itemList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                }else if(response.code()==403|| response.code()==401){
                    Intent intent = new Intent(AddLeaveActivity.this, LoginActivity.class);
                    intent.putExtra("Error", "Phiên đăng nhập đã hết hạn! Vui lòng đăng nhập lại!");
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(AddLeaveActivity.this, LoginActivity.class);
                    intent.putExtra("Error", "Đã xảy ra lỗi trong quá trình xử lý!");
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<List<DefaultModel>> call, Throwable t) {
                Intent intent = new Intent(AddLeaveActivity.this, LoginActivity.class);
                intent.putExtra("Error", "Vui lòng kiểm tra lại kết nối Internet!");
                startActivity(intent);
            }
        });
    }

}