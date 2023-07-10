package com.monstertechno.moderndashbord;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.monstertechno.moderndashbord.Api.ApiService;
import com.monstertechno.moderndashbord.Data.DataManager;
import com.monstertechno.moderndashbord.Model.Contract;
import com.monstertechno.moderndashbord.Model.Enum;
import com.monstertechno.moderndashbord.Model.TempInfor;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContractDetailActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    Toolbar toolbar;
    DataManager dataManager = DataManager.getInstance();
    TempInfor data = dataManager.getTempInfor();
    String Token = "Bearer "+ data.getToken();
    TextView tvCode, tvStart , tvEnd,tvJob, tvBasicSalary, tvSalryType,
            tvContractType, tvInsType, tvInsAmount, tvIsPersional, tvStatus;
    ImageView ivFile;
    List<Enum> SalaryType = new ArrayList<>();
    List<Enum> ContractType = new ArrayList<>();
    List<Enum> StatusType = new ArrayList<>();
    String id = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_detail);
        toolbar = findViewById(R.id.contract_toolbar);
        setSupportActionBar(toolbar);

        tvCode = findViewById(R.id.detail_contact_code);
        tvStart = findViewById(R.id.detail_contact_start);
        tvEnd = findViewById(R.id.detail_contact_end);
        tvJob = findViewById(R.id.detail_contact_job);
        tvBasicSalary = findViewById(R.id.detail_contact_basicSalary);
        tvSalryType = findViewById(R.id.detail_contact_salaryType);
        tvContractType = findViewById(R.id.detail_contact_contractType);
        tvInsType = findViewById(R.id.detail_contact_InsType);
        tvInsAmount = findViewById(R.id.detail_contact_InsAmount);
        tvIsPersional = findViewById(R.id.detail_contact_isPersional);
        tvStatus = findViewById(R.id.detail_contact_status);
        ivFile = findViewById(R.id.detail_contact_file);

        tabLayout = findViewById(R.id.detail_contact_tablayout);
       Intent intent = getIntent();
       id= intent.getStringExtra("code");
       tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
           @Override
           public void onTabSelected(TabLayout.Tab tab) {
               int position = tab.getPosition();
               // Thực hiện chuyển đổi Activity dựa trên vị trí của TabItem
               switch (position) {
                   case 0:
                       startActivity(new Intent(ContractDetailActivity.this, MainActivity.class));
                       break;
                   case 1:
                       startActivity(new Intent(ContractDetailActivity.this, OvertimeActivity.class));

                       break;
                   case 2:
                       startActivity(new Intent(ContractDetailActivity.this, AttendanceActivity.class));
                       break;
                   case 3:
                       startActivity(new Intent(ContractDetailActivity.this, InforActivity.class));
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

       loadData();



    }
/*

    private void loadStatus() {
        ApiService.apiService.EmployeeContractStatus().enqueue(new Callback<List<Enum>>() {
            @Override
            public void onResponse(Call<List<Enum>> call, Response<List<Enum>> response) {
                if(response.code()==200){
                    StatusType = response.body();

                }else if(response.code()==403|| response.code()==401){
                    Intent intent = new Intent(ContractDetailActivity.this, LoginActivity.class);
                    intent.putExtra("Error", "Phiên đăng nhập đã hết hạn! Vui lòng đăng nhập lại!");
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(ContractDetailActivity.this, LoginActivity.class);
                    intent.putExtra("Error", "Đã xảy ra lỗi trong quá trình xử lý!");
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<List<Enum>> call, Throwable t) {
                Intent intent = new Intent(ContractDetailActivity.this, LoginActivity.class);
                intent.putExtra("Error", "Vui lòng kiểm tra lại kết nối Internet!");
                startActivity(intent);
            }
        });
    }

    private void loadContractType() {
        ApiService.apiService.ContractType().enqueue(new Callback<List<Enum>>() {
            @Override
            public void onResponse(Call<List<Enum>> call, Response<List<Enum>> response) {
                if(response.code()==200){
                    ContractType = response.body();

                }else if(response.code()==403|| response.code()==401){
                    Intent intent = new Intent(ContractDetailActivity.this, LoginActivity.class);
                    intent.putExtra("Error", "Phiên đăng nhập đã hết hạn! Vui lòng đăng nhập lại!");
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(ContractDetailActivity.this, LoginActivity.class);
                    intent.putExtra("Error", "Đã xảy ra lỗi trong quá trình xử lý!");
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<List<Enum>> call, Throwable t) {
                Intent intent = new Intent(ContractDetailActivity.this, LoginActivity.class);
                intent.putExtra("Error", "Vui lòng kiểm tra lại kết nối Internet!");
                startActivity(intent);
            }
        });
    }

    private void loadSalaryType() {
        ApiService.apiService.SalaryType().enqueue(new Callback<List<Enum>>() {
            @Override
            public void onResponse(Call<List<Enum>> call, Response<List<Enum>> response) {
                if(response.code()==200){
                    SalaryType = response.body();

                }else if(response.code()==403|| response.code()==401){
                    Intent intent = new Intent(ContractDetailActivity.this, LoginActivity.class);
                    intent.putExtra("Error", "Phiên đăng nhập đã hết hạn! Vui lòng đăng nhập lại!");
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(ContractDetailActivity.this, LoginActivity.class);
                    intent.putExtra("Error", "Đã xảy ra lỗi trong quá trình xử lý!");
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<List<Enum>> call, Throwable t) {
                Intent intent = new Intent(ContractDetailActivity.this, LoginActivity.class);
                intent.putExtra("Error", "Vui lòng kiểm tra lại kết nối Internet!");
                startActivity(intent);
            }
        });
    }
*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_contract, menu);
        return true;
    }

    private void loadData() {
        if(id==null){
            Intent intent = new Intent(ContractDetailActivity.this, ContractActivity.class);
            intent.putExtra("Error", "Đã xảy ra lỗi trong quá trình lấy chi tiết hợp đồng!");
            startActivity(intent);
        }else{
            ApiService.apiService.ContractDetail(Token,id)
                    .enqueue(new Callback<Contract>() {
                        @Override
                        public void onResponse(Call<Contract> call, Response<Contract> response) {
                            if(response.code()==200){
                                Contract contract = response.body();
                                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                                String end = dateFormat.format(contract.endDate);
                                String start = dateFormat.format(contract.startDate);
                                String salary = convertToFormattedString(contract.basicSalary) + " VNĐ";
                                String Amount = "0 VNĐ";
                                if(contract.insuranceAmount>0){
                                    Amount = convertToFormattedString(contract.insuranceAmount) + " VNĐ";
                                }
                                tvCode.setText(contract.contractCode);

                                tvStart.setText(start);
                                tvEnd.setText(end);
                                tvJob.setText(contract.job);
                                tvBasicSalary.setText(salary);

                                tvSalryType.setText(contract.salaryType);
                                tvContractType.setText(contract.contractType);
                                tvInsType.setText(contract.insuranceType);
                                tvInsAmount.setText(Amount);
                                tvIsPersional.setText(String.valueOf(contract.isPersonalTaxDeduction));
                                tvStatus.setText(contract.status);

                                ivFile.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent1 = new Intent(ContractDetailActivity.this, ViewPdfActivity.class);
                                        intent1.putExtra("fileUrl", contract.file );
                                        startActivity(intent1);
                                    }
                                });


                            }else if(response.code()==403|| response.code()==401){
                                Intent intent = new Intent(ContractDetailActivity.this, LoginActivity.class);
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
                                Intent intent = new Intent(ContractDetailActivity.this, ContractDetailActivity.class);
                                intent.putExtra("Error", errorDescription);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<Contract> call, Throwable t) {
                            Intent intent = new Intent(ContractDetailActivity.this, LoginActivity.class);
                            intent.putExtra("Error", "Vui lòng kiểm tra lại kết nối Internet!");
                            startActivity(intent);
                        }
                    });
        }
    }

    public String convertToFormattedString(int number) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setGroupingSeparator('.');

        DecimalFormat decimalFormat = new DecimalFormat("#,###,###", symbols);
        String formattedString = decimalFormat.format(number);

        return formattedString;
    }
}