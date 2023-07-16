package com.monstertechno.moderndashbord;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.monstertechno.moderndashbord.Api.ApiService;
import com.monstertechno.moderndashbord.Data.DataManager;
import com.monstertechno.moderndashbord.Model.DefaultModel;
import com.monstertechno.moderndashbord.Model.Leave;
import com.monstertechno.moderndashbord.Model.Payslip;
import com.monstertechno.moderndashbord.Model.TempInfor;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayslipDetailActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    Toolbar toolbar;
    DataManager dataManager = DataManager.getInstance();
    TempInfor data = dataManager.getTempInfor();
    String Token = "Bearer "+ data.getToken();
    TextView tvDateCal, tvFrom, tvTo, tvSalaryPerHour, tvDefaultHour, tvTotalHour, tvOTHour, tvOTAmount, tvLeave,
            tvLeaveAmount,tvNote, tvBankname, tvBankAccount, tvBankaccuntName, tvFinalSalary,
            tvInsType, tvInsAmount, tvMinimum, tvXHPer,tvYtPer, tvTNPer, tvRegionName,tvRegionSalary, tvPersional, tvDepenanceAmount,tvNumDependance,
            tvGross,tvBHXH, tvBHYT, tvBHTN, tvTNTT,tvFinalPersional, tvFinalDependance,tvleaveMinus, tvTNCT, tvTTNCN, tvTNST,tvAllowanceContract,tvAllowanceDepartment, tvOTPlus, tvNet,
            tvGrossComp, tvXHComp,tvYtComp,tvTNComp, tvToatlComp , tvxhCompPer, tvytCompPer, tvtnCompPer ;
    List<DefaultModel> defaultModel = new ArrayList<>();
    String id = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payslip_detail);

        toolbar = findViewById(R.id.contract_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabLayout = findViewById(R.id.detail_contact_tablayout);
        Intent intent = getIntent();
        id= intent.getStringExtra("id");

        tvInsType = findViewById(R.id.detail_payslip_insType);
        tvInsAmount = findViewById(R.id.detail_payslip_insAmount);
        tvMinimum = findViewById(R.id.detail_payslip_minimumSalary);
        tvXHPer = findViewById(R.id.detail_payslip_BHXHpercent);
        tvYtPer = findViewById(R.id.detail_payslip_BHytpercent);
        tvTNPer = findViewById(R.id.detail_payslip_BHtnpercent);
        tvRegionName = findViewById(R.id.detail_payslip_regionNum);
        tvRegionSalary = findViewById(R.id.detail_payslip_regionSalary);
        tvPersional = findViewById(R.id.detail_payslip_personal);
        tvDepenanceAmount = findViewById(R.id.detail_payslip_dependant);
        tvNumDependance = findViewById(R.id.detail_payslip_numDependance);

        tvGross = findViewById(R.id.detail_payslip_gross);
        tvBHXH = findViewById(R.id.detail_payslip_BHXH);
        tvBHYT = findViewById(R.id.detail_payslip_BHYT);
        tvBHTN = findViewById(R.id.detail_payslip_BHTN);
        tvTNTT = findViewById(R.id.detail_payslip_TNTT);
        tvFinalPersional = findViewById(R.id.detail_payslip_personalFinal);
        tvFinalDependance = findViewById(R.id.detail_payslip_finalDependance);
        tvleaveMinus = findViewById(R.id.detail_payslip_leaveMinus);
        tvTNCT = findViewById(R.id.detail_payslip_TNCT);
        tvTTNCN = findViewById(R.id.detail_payslip_TTNCN);
        tvTNST = findViewById(R.id.detail_payslip_TNST);
        tvAllowanceContract = findViewById(R.id.detail_payslip_AllowanceContract);
        tvAllowanceDepartment = findViewById(R.id.detail_payslip_AllowanceDepartment);
        tvOTPlus = findViewById(R.id.detail_payslip_OTwagrPlus);
        tvNet = findViewById(R.id.detail_payslip_Net);

        tvGrossComp = findViewById(R.id.detail_payslip_grossComp);
        tvXHComp = findViewById(R.id.detail_payslip_BHXHComp);
        tvYtComp = findViewById(R.id.detail_payslip_BHYTComp);
        tvTNComp = findViewById(R.id.detail_payslip_BHTNComp);
        tvxhCompPer = findViewById(R.id.detail_payslip_xhCompPer);
        tvytCompPer = findViewById(R.id.detail_payslip_ytCompPer);
        tvtnCompPer = findViewById(R.id.detail_payslip_tnCompPer);
        tvToatlComp = findViewById(R.id.detail_payslip_totalBHComp);

        tvDateCal= findViewById(R.id.detail_payslip_dateCal);
        tvFrom= findViewById(R.id.detail_payslip_fromDate);
        tvTo= findViewById(R.id.detail_payslip_toDate);
        tvSalaryPerHour = findViewById(R.id.detail_payslip_perHour);
        tvDefaultHour= findViewById(R.id.detail_payslip_hourDefault);
        tvTotalHour= findViewById(R.id.detail_payslip_hourTotal);
        tvOTHour= findViewById(R.id.detail_payslip_OTHour);
        tvOTAmount = findViewById(R.id.detail_payslip_OTAmount);
        tvLeave= findViewById(R.id.detail_payslip_leaveH);
        tvLeaveAmount= findViewById(R.id.detail_payslip_leaveAmount);
        tvNote= findViewById(R.id.detail_payslip_Note);
        tvBankname = findViewById(R.id.detail_payslip_Bankname);
        tvBankAccount= findViewById(R.id.detail_payslip_Bankaccount);
        tvBankaccuntName= findViewById(R.id.detail_payslip_BankaccountName);
        tvFinalSalary= findViewById(R.id.detail_payslip_finalSalary);



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                // Thực hiện chuyển đổi Activity dựa trên vị trí của TabItem
                switch (position) {
                    case 0:
                        startActivity(new Intent(PayslipDetailActivity.this, MainActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(PayslipDetailActivity.this, OvertimeActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(PayslipDetailActivity.this, AttendanceActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(PayslipDetailActivity.this, InforActivity.class));
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
        getMenuInflater().inflate(R.menu.menu_toolbar_contract, menu);
        return true;
    }

    /*private void loadShift() {
        ApiService.apiService.ShiftLeave().enqueue(new Callback<List<DefaultModel>>() {
            @Override
            public void onResponse(Call<List<DefaultModel>> call, Response<List<DefaultModel>> response) {
                if(response.code()==200){
                    defaultModel = response.body();
                }else if(response.code()==403|| response.code()==401){
                    Intent intent = new Intent(PayslipDetailActivity.this, LoginActivity.class);
                    intent.putExtra("Error", "Phiên đăng nhập đã hết hạn! Vui lòng đăng nhập lại!");
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(PayslipDetailActivity.this, LoginActivity.class);
                    intent.putExtra("Error", "Đã xảy ra lỗi trong quá trình xử lý!");
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<List<DefaultModel>> call, Throwable t) {
                Intent intent = new Intent(PayslipDetailActivity.this, LoginActivity.class);
                intent.putExtra("Error", "Vui lòng kiểm tra lại kết nối Internet!");
                startActivity(intent);
            }
        });
    }
*/
    private void loadData() {
        if(id==null){
            Intent intent = new Intent(PayslipDetailActivity.this, ContractActivity.class);
            intent.putExtra("Error", "Đã xảy ra lỗi trong quá trình lấy chi tiết hợp đồng!");
            startActivity(intent);
        }else{
            ApiService.apiService.GetPayslipById(Token, id).enqueue(new Callback<Payslip>() {
                @Override
                public void onResponse(Call<Payslip> call, Response<Payslip> response) {
                    if(response.code()==200) {
                        Payslip payslip = response.body();
                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        String DateCal = dateFormat.format(payslip.paydayCal);
                        String fromDate = dateFormat.format(payslip.fromTime);
                        String toDate = dateFormat.format(payslip.toTime);

                        //tvDateCal, tvFrom, tvTo, tvSalaryPerHour, tvDefaultHour, tvTotalHour, tvOTHour, tvOTAmount, tvLeave,
                        //tvLeaveAmount,tvNote, tvBankname, tvBankAccount, tvBankaccuntName, tvFinalSalary,
                        tvDateCal.setText(DateCal);
                        tvFrom.setText(fromDate);
                        tvTo.setText(toDate);
                        tvSalaryPerHour.setText(String.valueOf(payslip.salaryPerHour));
                        tvDefaultHour.setText(String.valueOf(payslip.standard_Work_Hours));
                        tvTotalHour.setText(String.valueOf(payslip.actual_Work_Hours));
                        tvOTHour.setText(String.valueOf(payslip.ot_Hours));
                        tvOTAmount.setText("+"+convertToFormattedStringINT(payslip.otWage));
                        tvLeave.setText(String.valueOf(payslip.leave_Hours));
                        tvLeaveAmount.setText("-"+convertToFormattedStringLONG(payslip.leaveWageDeduction));
                        tvNote.setText(payslip.note);
                        tvBankname.setText(payslip.bankName);
                        tvBankAccount.setText(payslip.bankAcountNumber);
                        tvBankaccuntName.setText(payslip.bankAcountName);
                        tvFinalSalary.setText(convertToFormattedStringLONG(payslip.finalSalary) + " VNĐ");

                        //tvInsType, tvInsAmount, tvMinimum, tvXHPer,tvYtPer, tvTNPer, tvRegionName,tvRegionSalary, tvPersional, tvDepenanceAmount,tvNumDependance,
                        tvInsType.setText(String.valueOf(payslip.insuranceType));
                        tvInsAmount.setText(convertToFormattedStringLONG(payslip.insuranceAmount));
                        //tvMinimum.setText(convertToFormattedStringLONG(payslip.sa));
                        tvXHPer.setText(payslip.bhxH_Emp_Percent + "%");
                        tvYtPer.setText(payslip.bhyT_Emp_Percent + "%");
                        tvTNPer.setText(payslip.bhtN_Emp_Percent + "%");
                        tvRegionName.setText("Vùng ("+payslip.regionType+"):");
                        tvRegionSalary.setText(convertToFormattedStringLONG(payslip.regionMinimumWage));
                        tvPersional.setText(convertToFormattedStringLONG(payslip.personalTaxDeductionAmount));
                        tvDepenanceAmount.setText(convertToFormattedStringLONG(payslip.dependentTaxDeductionAmount));
                        tvNumDependance.setText(convertToFormattedStringLONG(payslip.numberOfDependent));

                        //tvGross,tvBHXH, tvBHYT, tvBHTN, tvTNTT,tvFinalPersional, tvFinalDependance,tvleaveMinus, tvTNCT, tvTTNCN,tvAllowance, tvOTPlus, tvNet,

                        tvGross.setText(convertToFormattedStringLONG(payslip.defaultSalary) + " VNĐ");
                        tvBHXH.setText("-"+convertToFormattedStringLONG(payslip.bhxH_Emp_Amount));
                        tvBHYT.setText("-"+convertToFormattedStringLONG(payslip.bhyT_Emp_Amount));
                        tvBHTN.setText("-"+convertToFormattedStringLONG(payslip.bhtN_Emp_Amount));
                        tvTNTT.setText(convertToFormattedStringLONG(payslip.tntt));
                        tvFinalPersional.setText("-"+convertToFormattedStringLONG(payslip.personalTaxDeductionAmount));
                        tvFinalDependance.setText("-"+convertToFormattedStringLONG(payslip.totalDependentAmount));
                        tvleaveMinus.setText("-"+convertToFormattedStringLONG(payslip.leaveWageDeduction));
                        tvTNCT.setText(convertToFormattedStringLONG(payslip.taxableSalary));
                        tvTTNCN.setText("-"+convertToFormattedStringLONG(payslip.totalTaxIncome));
                        tvTNST.setText(convertToFormattedStringLONG(payslip.afterTaxSalary));
                        tvAllowanceContract.setText("+"+convertToFormattedStringLONG(payslip.totalContractAllowance));
                        tvAllowanceDepartment.setText("+"+convertToFormattedStringLONG(payslip.totalDepartmentAllowance));
                        tvOTPlus.setText("+"+convertToFormattedStringLONG(payslip.otWage));
                        tvNet.setText("+"+convertToFormattedStringLONG(payslip.finalSalary) + " VNĐ");

                        //tvGrossComp, tvXHComp,tvYtComp,tvTNComp, tvToatlComp
                        tvGrossComp.setText(convertToFormattedStringLONG(payslip.defaultSalary));
                        tvXHComp.setText(convertToFormattedStringLONG(payslip.bhxH_Comp_Amount));
                        tvYtComp.setText(convertToFormattedStringLONG(payslip.bhyT_Comp_Amount));
                        tvTNComp.setText(convertToFormattedStringLONG(payslip.bhtN_Comp_Amount));
                        tvToatlComp.setText(convertToFormattedStringLONG(payslip.totalInsuranceComp) + " VNĐ");
                        tvxhCompPer.setText("BHXH ("+ payslip.bhxH_Comp_Percent+"%):");
                        tvytCompPer.setText("BHYT ("+ payslip.bhyT_Comp_Percent+"%):");
                        tvtnCompPer.setText("BHTN ("+ payslip.bhtN_Comp_Percent+"%):");

                    }else if(response.code()==403 || response.code()==401){
                        Intent intent = new Intent(PayslipDetailActivity.this, LoginActivity.class);
                        intent.putExtra("Error", "Phiên đăng nập đã hết hạn! Vui lòng đăng nhập lại!");
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(PayslipDetailActivity.this, PayslipActivity.class);
                        Toast.makeText(PayslipDetailActivity.this ,"Đã xảy ra lỗi trong quá trình xử lý!" ,Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }
                }

                @Override
                public void onFailure(Call<Payslip> call, Throwable t) {
                    Intent intent = new Intent(PayslipDetailActivity.this, LoginActivity.class);
                    intent.putExtra("Error", "Vui lòng kiểm tra kết nối Internet");
                    startActivity(intent);
                }
            });

        }
    }

    public String convertToFormattedStringINT(int number) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setGroupingSeparator('.');

        DecimalFormat decimalFormat = new DecimalFormat("#,###,###", symbols);
        String formattedString = decimalFormat.format(number);

        return formattedString;
    }
    public String convertToFormattedStringLONG(long number) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setGroupingSeparator('.');

        DecimalFormat decimalFormat = new DecimalFormat("#,###,###", symbols);
        String formattedString = decimalFormat.format(number);

        return formattedString;
    }
}