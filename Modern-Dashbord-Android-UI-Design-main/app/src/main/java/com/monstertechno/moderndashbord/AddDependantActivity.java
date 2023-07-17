package com.monstertechno.moderndashbord;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.monstertechno.moderndashbord.Api.ApiService;
import com.monstertechno.moderndashbord.Data.DataManager;
import com.monstertechno.moderndashbord.Model.AddDependant;
import com.monstertechno.moderndashbord.Model.DefaultModel;
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

public class AddDependantActivity extends AppCompatActivity {

    String REQUIRE = "Require";
    Toolbar toolbar;
    private TabLayout tabLayout;
    DataManager dataManager = DataManager.getInstance();
    TempInfor data = dataManager.getTempInfor();
    String Token = "Bearer "+ data.getToken();


    Button btnDate,btnCreate;
    EditText etName, etRela , etDes;
    TextView tvError;
    List<DefaultModel> defaultModel = new ArrayList<>();
    ProgressDialog pd;
    private int initialYear;
    private int initialMonth;
    private int initialDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dependant);
        toolbar = findViewById(R.id.contract_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabLayout = findViewById(R.id.contract_tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                // Thực hiện chuyển đổi Activity dựa trên vị trí của TabItem
                switch (position) {
                    case 0:
                        startActivity(new Intent(AddDependantActivity.this, MainActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(AddDependantActivity.this, OvertimeActivity.class));
                        //startActivity(new Intent(MainActivity.this, Activity2.class));
                        break;
                    case 2:
                        startActivity(new Intent(AddDependantActivity.this, AttendanceActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(AddDependantActivity.this, InforActivity.class));
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

        etName = findViewById(R.id.dependant_add_name);
        btnDate = findViewById(R.id.dependant_add_date);
        etRela = findViewById(R.id.dependant_add_rela);
        etDes = findViewById(R.id.dependant_add_des);
        btnCreate = findViewById(R.id.leave_add_btnCreate);
        tvError = findViewById(R.id.leave_add_error);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createLeaveLog();
            }
        });
    }

    private void createLeaveLog() {
        btnCreate.setActivated(false);
        pd=  new ProgressDialog(AddDependantActivity.this);
        pd.setTitle("Người phụ thuộc");
        pd.setMessage("Đang thêm...!!!");
        pd.show();

        AddDependant model = new AddDependant();
        String dateString = btnDate.getText().toString();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date date = format.parse(dateString);
            // Sử dụng đối tượng date
            model.birthDate = date;

        } catch (ParseException e) {
            pd.dismiss();
            btnCreate.setActivated(true);
            tvError.setText("Vui lòng chọn ngày sinh!");
            return;
        }
        String name = etName.getText().toString();
        String rela = etRela.getText().toString();
        String des = etDes.getText().toString();
        if(name.equals("")){
            etName.setError(REQUIRE);
            btnCreate.setActivated(true);
            pd.dismiss();
            return;
        }
        if(rela.equals("")){
            etRela.setError(REQUIRE);
            btnCreate.setActivated(true);
            pd.dismiss();
            return;
        }
        if(des.equals("")){
            etDes.setError(REQUIRE);
            btnCreate.setActivated(true);
            pd.dismiss();
            return;
        }

        model.relationship = rela;
        model.name = name;
        model.desciption = des;

        ApiService.apiService.DependentCreate(Token,model).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code()==200){
                    pd.dismiss();
                    Toast.makeText(AddDependantActivity.this, "Thêm người phụ thuộc thành công!",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(AddDependantActivity.this, DependantActivity.class));
                }else if(response.code()==403 || response.code()==401){
                    Intent intent = new Intent(AddDependantActivity.this, LoginActivity.class);
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
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
                Intent intent = new Intent(AddDependantActivity.this, LoginActivity.class);
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

        DatePickerDialog datePickerDialog = new DatePickerDialog(AddDependantActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        String text = String.format("%d/%d/%d",i2, (i1+1), i);
                        btnDate.setText(text);
                    }
                },initialYear,initialMonth-1,initialDay);
        datePickerDialog.show();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(AddDependantActivity.this);
        builder.setTitle("Đăng xuất");
        builder.setMessage("Bạn có chắc chắn muốn đăng xuất?");
        builder.setPositiveButton("Đăng xuất", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(AddDependantActivity.this, "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();
                dataManager.setTempInfor(null);
                startActivity(new Intent(AddDependantActivity.this,LoginActivity.class));
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