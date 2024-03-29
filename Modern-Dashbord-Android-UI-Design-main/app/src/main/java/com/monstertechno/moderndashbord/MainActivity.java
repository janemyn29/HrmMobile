package com.monstertechno.moderndashbord;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.tabs.TabLayout;
import com.monstertechno.moderndashbord.Data.DataManager;
import com.monstertechno.moderndashbord.Model.TempInfor;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    CardView cvContract,cvLeave, cvPosition, cvPayslip, cvDependent;
    TextView tvFullname, tvRole;
    ShapeableImageView imgAvatar;
    ImageView logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        int color = ContextCompat.getColor(this, R.color.pastel);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(color);
        setContentView(R.layout.activity_main);

        DataManager dataManager = DataManager.getInstance();
        TempInfor data = dataManager.getTempInfor();

        tvFullname = findViewById(R.id.main_tv_FullName);
        tvRole = findViewById(R.id.main_tv_Role);
        imgAvatar = findViewById(R.id.main_img_avatar);
        cvContract = findViewById(R.id.main_cv_contract);
        cvLeave = findViewById(R.id.main_leave);
        tabLayout = findViewById(R.id.main_tabLayout);
        cvPosition = findViewById(R.id.main_cv_position);
        cvPayslip = findViewById(R.id.main_cv_payslip);
        cvDependent = findViewById(R.id.main_cv_dependent);
        logout = findViewById(R.id.main_logout);

        tvRole.setText(data.getListRoles().get(0));
        tvFullname.setText(data.getFullName());
        String avartar = data.getImage();
        Picasso.get().load(avartar).into(imgAvatar);

        cvContract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ContractActivity.class));
            }
        });

        cvLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LeaveActivity.class));
            }
        });

        cvPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PositionActivity.class));
            }
        });

        cvPayslip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PayslipActivity.class));
            }
        });

        cvDependent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, DependantActivity.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Đăng xuất");
                builder.setMessage("Bạn có chắc chắn muốn đăng xuất?");
                builder.setPositiveButton("Đăng xuất", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();
                        dataManager.setTempInfor(null);
                        startActivity(new Intent(MainActivity.this,LoginActivity.class));
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
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                // Thực hiện chuyển đổi Activity dựa trên vị trí của TabItem
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this, OvertimeActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this, AttendanceActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(MainActivity.this, InforActivity.class));
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
}