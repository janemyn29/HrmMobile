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
import com.monstertechno.moderndashbord.Data.DataManager;
import com.monstertechno.moderndashbord.Model.TempInfor;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

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

    }

}