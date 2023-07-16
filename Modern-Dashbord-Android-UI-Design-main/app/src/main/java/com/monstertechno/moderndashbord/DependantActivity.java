package com.monstertechno.moderndashbord;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.monstertechno.moderndashbord.Adapter.DependentAdapter;
import com.monstertechno.moderndashbord.Adapter.LeaveAdapter;
import com.monstertechno.moderndashbord.Adapter.PagingScrollListener;
import com.monstertechno.moderndashbord.Api.ApiService;
import com.monstertechno.moderndashbord.Data.DataManager;
import com.monstertechno.moderndashbord.Model.Dependent;
import com.monstertechno.moderndashbord.Model.Leave;
import com.monstertechno.moderndashbord.Model.PagingLeave;
import com.monstertechno.moderndashbord.Model.ResultDependent;
import com.monstertechno.moderndashbord.Model.SelectListener;
import com.monstertechno.moderndashbord.Model.TempInfor;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DependantActivity extends AppCompatActivity implements SelectListener {
    Toolbar toolbar;
    ProgressDialog pd ;
    private TabLayout tabLayout;
    DataManager dataManager = DataManager.getInstance();
    TempInfor data = dataManager.getTempInfor();
    String Token = "Bearer "+ data.getToken();
    RecyclerView recyclerView ;
    DependentAdapter adapter;
    private boolean isLoading;
    private  boolean isLastPage;
    private List<Dependent> mlist;
    private int totalPage ;
    private  int currentPage = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dependant);
        toolbar = findViewById(R.id.leave_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pd=  new ProgressDialog(DependantActivity.this);
        pd.setTitle("Danh sách người phụ thuộc");
        pd.setMessage("Đang mở...!!!");
        pd.show();

        recyclerView = findViewById(R.id.leave_rv);
        adapter = new DependentAdapter(this, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        tabLayout = findViewById(R.id.contract_tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                // Thực hiện chuyển đổi Activity dựa trên vị trí của TabItem
                switch (position) {
                    case 0:
                        startActivity(new Intent(DependantActivity.this, MainActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(DependantActivity.this, OvertimeActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(DependantActivity.this, AttendanceActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(DependantActivity.this, InforActivity.class));
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

        setFirstData();

        recyclerView.addOnScrollListener(new PagingScrollListener(linearLayoutManager) {
            @Override
            public void loadMoreItem() {
                isLoading=true;
                currentPage+=1;
                loadNextPage();

            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }
        });

        Intent intent = getIntent();
        String data = intent.getStringExtra("Error");
        if(data!=null && data.isEmpty()&& data.equals("") ){
            Toast.makeText(this, data, Toast.LENGTH_LONG).show();

        }

    }

    private void loadNextPage() {
        List<Dependent> list = load();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                adapter.removeFooterLoading();
                mlist.addAll(list);

                adapter.notifyDataSetChanged();
                isLoading= false;
                if(currentPage<totalPage){
                    adapter.addFooterLoading();
                }else{
                    isLastPage = true;
                }
            }
        }, 7000);

    }

    private void setFirstData(){

        mlist = load();
        new Handler().postDelayed( new Runnable(){

            @Override
            public void run() {
                adapter.setData(mlist);
                recyclerView.setAdapter(adapter);

                if(currentPage<totalPage){
                    adapter.addFooterLoading();
                }else{
                    isLastPage = true;
                }
                pd.dismiss();
            }
        }, 7000);

    }

    private ArrayList<Dependent> load(){
        ArrayList<Dependent> list = new ArrayList<>();
        Toast.makeText(this, "Tải thêm thông tin trang "+ currentPage,Toast.LENGTH_SHORT ).show();
        ApiService.apiService.ListDependent(Token,currentPage).enqueue(new Callback<ResultDependent>() {
            @Override
            public void onResponse(Call<ResultDependent> call, Response<ResultDependent> response) {
                if(response.code() == 200){
                    ResultDependent pagingLeave = response.body();
                    if(pagingLeave==null){
                        return;
                    }
                    for (Dependent leave: pagingLeave.result.items) {
                        list.add(leave);
                    }
                    totalPage = pagingLeave.result.totalPages;
                }else{
                    if(response.code()==403 || response.code()==401){
                        Intent intent = new Intent(DependantActivity.this, LoginActivity.class);
                        intent.putExtra("Error", "Phiên đăng nập đã hết hạn! Vui lòng đăng nhập lại!");
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(DependantActivity.this, LoginActivity.class);
                        intent.putExtra("Error", "Đã xảy ra lỗi trong quá trình xử lý!");
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultDependent> call, Throwable t) {
                Intent intent = new Intent(DependantActivity.this, LoginActivity.class);
                intent.putExtra("Error", "Vui lòng kiểm tra kết nối Internet");
                startActivity(intent);

            }
        });
        return list;
    }


    @Override
    public void onItemClicked(Object leave) {
        /*Leave newLeave= (Leave) leave;
        Intent intent = new Intent(DependantActivity.this, LeaveDetailActivity.class);
        intent.putExtra("id", newLeave.id);
        startActivity(intent);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_leave, menu);
        return true;
    }

}