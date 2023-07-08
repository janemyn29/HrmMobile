package com.monstertechno.moderndashbord;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.monstertechno.moderndashbord.Adapter.ContractAdapter;
import com.monstertechno.moderndashbord.Adapter.PagingScrollListener;
import com.monstertechno.moderndashbord.Api.ApiService;
import com.monstertechno.moderndashbord.Data.DataManager;
import com.monstertechno.moderndashbord.Model.Contract;
import com.monstertechno.moderndashbord.Model.ContractList;
import com.monstertechno.moderndashbord.Model.PagingContract;
import com.monstertechno.moderndashbord.Model.SelectListener;
import com.monstertechno.moderndashbord.Model.TempInfor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContractActivity extends AppCompatActivity implements SelectListener {
    Toolbar toolbar;
    DataManager dataManager = DataManager.getInstance();
    TempInfor data = dataManager.getTempInfor();
    String Token = "Bearer "+ data.getToken();
    RecyclerView recyclerView ;
    ContractAdapter adapter;
    private boolean isLoading;
    private  boolean isLastPage;
    private List<Contract> mlist;

    private int TotalPage ;
    private  int currentPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract);
        toolbar = findViewById(R.id.contract_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Danh Sách Hợp Đồng");
        recyclerView = findViewById(R.id.contract_rv);

        adapter = new ContractAdapter(this, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        loadList();
        Intent intent = getIntent();
        String data = intent.getStringExtra("Error");
        if(data!=null && data.isEmpty()&& data.equals("") ){
            Toast.makeText(this, data, Toast.LENGTH_LONG).show();

        }
    }


    private void loadList() {
        ApiService.apiService.ListContract(Token,1)
                .enqueue(new Callback<PagingContract>() {
                    @Override
                    public void onResponse(Call<PagingContract> call, Response<PagingContract> response) {
                        if(response.code() == 200){
                            PagingContract pagingContract = response.body();
                            if(pagingContract==null){
                                return;
                            }
                            ArrayList<Contract> list = new ArrayList<>();
                            for (Contract contract: pagingContract.items) {
                                list.add(contract);
                            }
                            mlist = list;
                            adapter.setData(list);
                            recyclerView.setAdapter(adapter);
                            TotalPage = pagingContract.totalPages;
                            currentPage = pagingContract.pageNumber;
                        }else{
                            if(response.code()==403 || response.code()==401){
                                Intent intent = new Intent(ContractActivity.this, LoginActivity.class);
                                intent.putExtra("Error", "Phiên đăng nập đã hết hạn! Vui lòng đăng nhập lại!");
                                startActivity(intent);
                            }else{
                                Intent intent = new Intent(ContractActivity.this, LoginActivity.class);
                                intent.putExtra("Error", "Đã xảy ra lỗi trong quá trình xử lý!");
                                startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<PagingContract> call, Throwable t) {
                        Intent intent = new Intent(ContractActivity.this, LoginActivity.class);
                        intent.putExtra("Error", "Vui lòng kiểm tra lại kết nối Internet!");
                        startActivity(intent);
                    }
                });
    }

    @Override
    public void onItemClicked(Contract contract) {
        Intent intent = new Intent(ContractActivity.this, ContractDetailActivity.class);
        intent.putExtra("code", contract.contractCode);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_contract, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.toolbar_account:
        }
        return super.onOptionsItemSelected(item);
    }
}