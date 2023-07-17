package com.monstertechno.moderndashbord;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.tabs.TabLayout;
import com.monstertechno.moderndashbord.Api.ApiService;
import com.monstertechno.moderndashbord.Data.DataManager;
import com.monstertechno.moderndashbord.Model.Position;
import com.monstertechno.moderndashbord.Model.TempInfor;
import com.monstertechno.moderndashbord.Model.User;
import com.monstertechno.moderndashbord.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PositionActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    Toolbar toolbar;
    DataManager dataManager = DataManager.getInstance();
    TempInfor data = dataManager.getTempInfor();
    String Token = "Bearer "+ data.getToken();
    ProgressDialog pd ;
    private TextView tvPosition, tvDepartment, tvLevel;
    private ShapeableImageView ivAvatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_position);
        tabLayout = findViewById(R.id.infor_tabLayout);
        tabLayout.getTabAt(3).select();
        toolbar = findViewById(R.id.contract_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pd=  new ProgressDialog(PositionActivity.this);
        pd.setTitle("Vị trí - Phòng Ban");
        pd.setMessage("Đang mở...!!!");
        pd.show();
        tvPosition = findViewById(R.id.position_name_position);
        tvDepartment = findViewById(R.id.position_department_name);
        tvLevel = findViewById(R.id.position_Level);
        loadData();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                // Thực hiện chuyển đổi Activity dựa trên vị trí của TabItem
                switch (position) {
                    case 0:
                        startActivity(new Intent(PositionActivity.this, MainActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(PositionActivity.this, OvertimeActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(PositionActivity.this, AttendanceActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(PositionActivity.this, InforActivity.class));
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
        ApiService.apiService.GetPosition(Token).enqueue(new Callback<Position>() {

            @Override
            public void onResponse(Call<Position> call, Response<Position> response) {
                if(response.code()==200){
                    Position position = response.body();
                    if(position!=null){
                        tvDepartment.setText(position.department.name);
                        tvPosition.setText(position.name);
                        tvLevel.setText(position.level.name);
                        pd.dismiss();
                    }
                }else if(response.code()==403|| response.code()==401){
                    pd.dismiss();
                    Intent intent = new Intent(PositionActivity.this, LoginActivity.class);
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
                    Intent intent = new Intent(PositionActivity.this, MainActivity.class);
                    intent.putExtra("Error", errorDescription);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Position> call, Throwable t) {
                pd.dismiss();
                Intent intent = new Intent(PositionActivity.this, LoginActivity.class);
                intent.putExtra("Error", "Vui lòng kiểm tra lại kết nối Internet!");
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_contract, menu);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(PositionActivity.this);
        builder.setTitle("Đăng xuất");
        builder.setMessage("Bạn có chắc chắn muốn đăng xuất?");
        builder.setPositiveButton("Đăng xuất", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(PositionActivity.this, "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();
                dataManager.setTempInfor(null);
                startActivity(new Intent(PositionActivity.this,LoginActivity.class));
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