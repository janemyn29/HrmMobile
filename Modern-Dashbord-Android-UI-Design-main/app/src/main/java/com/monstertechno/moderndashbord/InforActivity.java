package com.monstertechno.moderndashbord;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.tabs.TabLayout;
import com.monstertechno.moderndashbord.Api.ApiService;
import com.monstertechno.moderndashbord.Data.DataManager;
import com.monstertechno.moderndashbord.Model.Contract;
import com.monstertechno.moderndashbord.Model.PagingContract;
import com.monstertechno.moderndashbord.Model.TempInfor;
import com.monstertechno.moderndashbord.Model.User;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InforActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    DataManager dataManager = DataManager.getInstance();
    TempInfor data = dataManager.getTempInfor();
    String Token = "Bearer "+ data.getToken();
    private TextView tvUsername, tvFullname, tvGenderType, tvAddress,tvIdentityNumber, tvBirthDay,
            tvbankAccountNumber, tvbankAccountName, tvbankName, tvisMaternity, tvuserName, tvemail, tvPhone;
    private ShapeableImageView ivAvatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor);
        tabLayout = findViewById(R.id.infor_tabLayout);
        tabLayout.getTabAt(3).select();

        tvUsername = findViewById(R.id.infor_username);
        tvFullname = findViewById(R.id.infor_fullname);
        tvGenderType = findViewById(R.id.info_gender);
        tvAddress = findViewById(R.id.infor_address);
        tvIdentityNumber = findViewById(R.id.infor_identityNum);
        tvBirthDay = findViewById(R.id.info_birth);
        tvbankAccountNumber = findViewById(R.id.item_accountbanknumber);
        tvbankAccountName = findViewById(R.id.info_backaccountName);
        tvbankName = findViewById(R.id.infor_bankname);
        tvisMaternity = findViewById(R.id.infor_isMaternity);
        tvemail = findViewById(R.id.info_email);
        tvPhone = findViewById(R.id.info_phone);
        ivAvatar = findViewById(R.id.infor_avartar);

        loadData();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                // Thực hiện chuyển đổi Activity dựa trên vị trí của TabItem
                switch (position) {
                    case 0:
                        startActivity(new Intent(InforActivity.this, MainActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(InforActivity.this, OvertimeActivity.class));
                        break;
                    case 2:
                        //startActivity(new Intent(MainActivity.this, Activity2.class));
                        break;
                    case 3:

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
    }

    private void loadData() {
        ApiService.apiService.Infor(Token).enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.code()==200){
                    User user = response.body();
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    String end = dateFormat.format(user.birthDay);
                    String gender = "";
                    tvUsername.setText(user.userName);
                    tvFullname.setText(user.fullname);
                    if(user.genderType==1){
                        gender = "Nữ";
                    }else if(user.genderType==2){
                        gender = "Nam";
                    }else{
                        gender = "Khác";
                    }
                    tvGenderType.setText(gender);
                    tvAddress.setText(user.address);
                    tvIdentityNumber.setText(user.identityNumber);
                    tvBirthDay.setText(end);
                    tvbankAccountNumber.setText(user.bankAccountNumber);
                    tvbankAccountName.setText(user.bankAccountName);
                    tvbankName.setText(user.bankName);
                    tvisMaternity.setText(String.valueOf(user.isMaternity));
                    tvemail.setText(user.email);
                    tvPhone.setText(user.phoneNumber);
                    String avartar = user.image;
                    Picasso.get().load(avartar).into(ivAvatar);
                }else if(response.code()==403|| response.code()==401){
                    Intent intent = new Intent(InforActivity.this, LoginActivity.class);
                    intent.putExtra("Error", "Phiên đăng nhập đã hết hạn! Vui lòng đăng nhập lại!");
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
                    Intent intent = new Intent(InforActivity.this, MainActivity.class);
                    intent.putExtra("Error", errorDescription);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
}