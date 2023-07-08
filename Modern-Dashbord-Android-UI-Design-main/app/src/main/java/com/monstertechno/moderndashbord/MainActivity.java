package com.monstertechno.moderndashbord;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.tabs.TabLayout;
import com.monstertechno.moderndashbord.Data.DataManager;
import com.monstertechno.moderndashbord.Model.TempInfor;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    CardView cvContract;
    TextView tvFullname, tvRole;
    ShapeableImageView imgAvatar;
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
        tabLayout = findViewById(R.id.main_tabLayout);

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

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                // Thực hiện chuyển đổi Activity dựa trên vị trí của TabItem
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        //startActivity(new Intent(MainActivity.this, Activity2.class));
                        break;
                    case 2:
                        //startActivity(new Intent(MainActivity.this, Activity2.class));
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