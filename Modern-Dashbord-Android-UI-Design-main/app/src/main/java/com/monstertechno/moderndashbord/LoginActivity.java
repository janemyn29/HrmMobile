package com.monstertechno.moderndashbord;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;
import com.monstertechno.moderndashbord.Api.ApiService;
import com.monstertechno.moderndashbord.Data.DataManager;
import com.monstertechno.moderndashbord.Model.LoginModel;
import com.monstertechno.moderndashbord.Model.TempInfor;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    String REQUIRE = "Require";
    EditText edtUsername , edtPass;
    ShapeableImageView imgAvatar;
    TextView tvError;
    Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtUsername = findViewById(R.id.login_Edt_username);
        edtPass = findViewById(R.id.login_Edt_pass);
        btnLogin = findViewById(R.id.btnLogin);
        tvError = findViewById(R.id.login_tv_error);

        Intent intent = getIntent();
        String data = intent.getStringExtra("Error");
        tvError.setText(data);



        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean check = true;
                if(TextUtils.isEmpty(edtPass.getText().toString())){
                    edtPass.setError("Vui lòng nhập thông tin");
                    check = false;
                }
                if (TextUtils.isEmpty(edtUsername.getText().toString())){
                    edtUsername.setError("Vui lòng nhập thông tin");
                    check= false;
                }
                if(check){
                    clickCallAPILogin();
                }
            }
        });
    }

    private void clickCallAPILogin(){
        String username = String.valueOf(edtUsername.getText());
        String pass = String.valueOf(edtPass.getText());
        LoginModel model = new LoginModel(username,pass);
        ApiService.apiService.login(model).enqueue(new Callback<TempInfor>() {
            @Override
            public void onResponse(Call<TempInfor> call, Response<TempInfor> response) {
                if(response.isSuccessful()){
                    TempInfor tempInfor = response.body();
                    if(tempInfor.getListRoles().get(0).equals("Manager")){
                        tvError.setText("Tài khoản Quản lý không thể đăng nhập vào ứng dụng này!");
                    }else{
                        DataManager dataManager = DataManager.getInstance();
                        dataManager.setTempInfor(tempInfor);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

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
                    tvError.setText(errorDescription);
                }

            }

            @Override
            public void onFailure(Call<TempInfor> call, Throwable t) {

                tvError.setText("Vui lòng kiểm tra lại kết nối Internet!");
            }
        });
    }
}