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
import android.view.Menu;
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
import com.google.gson.Gson;
import com.monstertechno.moderndashbord.Api.ApiService;
import com.monstertechno.moderndashbord.Data.DataManager;
import com.monstertechno.moderndashbord.Model.ChangePassModel;
import com.monstertechno.moderndashbord.Model.DefaultModel;
import com.monstertechno.moderndashbord.Model.ErrorModel;
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

public class ChangePassActivity extends AppCompatActivity {

    String REQUIRE = "Require";
    Toolbar toolbar;
    private TabLayout tabLayout;
    DataManager dataManager = DataManager.getInstance();
    TempInfor data = dataManager.getTempInfor();
    String Token = "Bearer "+ data.getToken();



    Button btnCreate;
    EditText etOld,etNew,etConfirm;
    TextView tvError;
    List<DefaultModel> defaultModel = new ArrayList<>();
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
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
                        startActivity(new Intent(ChangePassActivity.this, MainActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(ChangePassActivity.this, OvertimeActivity.class));
                        //startActivity(new Intent(MainActivity.this, Activity2.class));
                        break;
                    case 2:
                        startActivity(new Intent(ChangePassActivity.this, AttendanceActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(ChangePassActivity.this, InforActivity.class));
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

        etOld = findViewById(R.id.changepass_oldPass);
        etNew = findViewById(R.id.changepass_newPass);
        etConfirm = findViewById(R.id.changepass_confirmPass);
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

        ChangePassModel model = new ChangePassModel();
        String old = etOld.getText().toString();
        String newPass = etNew.getText().toString();
        String confirm = etConfirm.getText().toString();
        if(old.equals("")){
            etOld.setError("Vui lòng nhập thông tin");
            return;
        }
        if(newPass.equals("")){
            etNew.setError("Vui lòng nhập thông tin");
            return;
        }
        if(confirm.equals("")){
            etConfirm.setError("Vui lòng nhập thông tin");
            return;
        }

        model.oldPassword = old;
        model.newPassword = newPass;
        model.confirmPassword = confirm;

        btnCreate.setActivated(false);
        pd=  new ProgressDialog(ChangePassActivity.this);
        pd.setTitle("Đổi mật khẩu");
        pd.setMessage("Đang lưu...!!!");
        pd.show();

        ApiService.apiService.ChangePassword(Token,model).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code()==200){
                    pd.dismiss();
                    Toast.makeText(ChangePassActivity.this, "Đổi mật khẩu thành công!",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ChangePassActivity.this, MainActivity.class));
                }else if(response.code()==403 || response.code()==401){
                    Intent intent = new Intent(ChangePassActivity.this, LoginActivity.class);
                    intent.putExtra("Error", "Phiên đăng nhập đã hết hạn! Vui lòng đăng nhập lại!");
                    startActivity(intent);
                    pd.dismiss();
                }else{
                    String temp = "";
                    ErrorModel errorModel = new ErrorModel();
                    try {
                        Gson gson = new Gson();
                        errorModel = gson.fromJson(response.errorBody().string(), ErrorModel.class);
                    }catch (Exception ex){

                    }
                    for (String err:errorModel.result) {
                        temp= temp + err + ";";
                    }
                    tvError.setText(temp);
                    pd.dismiss();
                    btnCreate.setActivated(false);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pd.dismiss();
                Intent intent = new Intent(ChangePassActivity.this, LoginActivity.class);
                intent.putExtra("Error", "Vui lòng kiểm tra lại kết nối Internet!");
                startActivity(intent);
            }
        });
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
        AlertDialog.Builder builder = new AlertDialog.Builder(ChangePassActivity.this);
        builder.setTitle("Đăng xuất");
        builder.setMessage("Bạn có chắc chắn muốn đăng xuất?");
        builder.setPositiveButton("Đăng xuất", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ChangePassActivity.this, "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();
                dataManager.setTempInfor(null);
                startActivity(new Intent(ChangePassActivity.this,LoginActivity.class));
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