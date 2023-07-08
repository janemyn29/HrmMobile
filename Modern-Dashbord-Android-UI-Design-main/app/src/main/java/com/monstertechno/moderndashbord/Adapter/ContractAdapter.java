package com.monstertechno.moderndashbord.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.monstertechno.moderndashbord.Api.ApiService;
import com.monstertechno.moderndashbord.ContractActivity;
import com.monstertechno.moderndashbord.Data.DataManager;
import com.monstertechno.moderndashbord.LoginActivity;
import com.monstertechno.moderndashbord.Model.Contract;
import com.monstertechno.moderndashbord.Model.PagingContract;
import com.monstertechno.moderndashbord.Model.SelectListener;
import com.monstertechno.moderndashbord.Model.TempInfor;
import com.monstertechno.moderndashbord.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContractAdapter extends RecyclerView.Adapter<ContractAdapter.ContractViewHoder> {
    private Context mContext;
    private List<Contract> mListUser;
    private SelectListener listener;
    private int No = 1;

    public ContractAdapter(Context mContext, SelectListener listener) {
        this.mContext = mContext;
        this.listener = listener;
    }

    public void setData(List<Contract> list){
        mListUser = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ContractViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contract,parent,false);
        return new ContractViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContractViewHoder holder, int position) {
        Contract contract = mListUser.get(position);
        if(contract == null){
            return;
        }
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(contract.endDate);
        holder.tvNo.setText("No: "+No);
        holder.tvName.setText("Mã hợp đông: "+contract.contractCode);
        holder.tvDate.setText("Ngày hết hạn: "+formattedDate);
        holder.btnStatus.setText(contract.status);
        if(contract.status.equals("Waiting")){
            int color = ContextCompat.getColor(mContext, R.color.yellow);
            holder.btnStatus.setTextColor(color);
        }else if(contract.status.equals("Pending")){
            int color = ContextCompat.getColor(mContext, R.color.Green);
            holder.btnStatus.setTextColor(color);
        }else{
            int color = ContextCompat.getColor(mContext, R.color.error_200);
            holder.btnStatus.setTextColor(color);
        }
        holder.ivDetail.setImageResource(R.drawable.baseline_remove_red_eye_24);
        No++;
    }

    @Override
    public int getItemCount() {
        if(mListUser!=null){
            return mListUser.size();
        }
        return 0;
    }

    public class ContractViewHoder extends ViewHolder {

        TextView tvName, tvDate, btnStatus, tvNo;
        ImageView ivDetail;

        public ContractViewHoder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.item_contract_tvName);
            tvDate = itemView.findViewById(R.id.item_contract_tvDate);
            btnStatus = itemView.findViewById(R.id.item_contract_btnStatus);
            ivDetail = itemView.findViewById(R.id.item_contract_ivDetail);
            tvNo = itemView.findViewById(R.id.item_contract_tvNo);
        }
    }

}