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
import com.monstertechno.moderndashbord.ContractDetailActivity;
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

public class ContractAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;
    private List<Contract> mListUser;
    private SelectListener listener;
    private boolean isLoadingAdd;

    public ContractAdapter(Context mContext, SelectListener listener) {
        this.mContext = mContext;
        this.listener = listener;
    }

    private static final int TYPE_ITEM =1;
    private static final int TYPE_LOADING =2;

    public void setData(List<Contract> list){
        this.mListUser = list;
        notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position) {
        if(mListUser!=null && position ==mListUser.size()-1 && isLoadingAdd){
            return TYPE_LOADING;
        }
        return TYPE_ITEM;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(TYPE_ITEM==viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contract,parent,false);
            return new ContractViewHoder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading,parent,false);
            return new LoadingContractViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(holder.getItemViewType()==TYPE_ITEM){
            Contract contract = mListUser.get(position);
            if(contract == null){
                return;
            }
            ContractViewHoder contractViewHoder = (ContractViewHoder) holder;
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String formattedDate = dateFormat.format(contract.endDate);
            contractViewHoder.tvNo.setText("STT: "+ position+1);
            contractViewHoder.tvName.setText("Mã hợp đồng: "+contract.contractCode);
            contractViewHoder.tvDate.setText("Ngày hết hạn: "+formattedDate);
            contractViewHoder.btnStatus.setText(contract.status);
            if(contract.status.equals("Waiting")){
                int color = ContextCompat.getColor(mContext, R.color.yellow);
                contractViewHoder.btnStatus.setTextColor(color);
            }else if(contract.status.equals("Pending")){
                int color = ContextCompat.getColor(mContext, R.color.Green);
                contractViewHoder.btnStatus.setTextColor(color);
            }else{
                int color = ContextCompat.getColor(mContext, R.color.error_200);
                contractViewHoder.btnStatus.setTextColor(color);
            }
            contractViewHoder.ivDetail.setImageResource(R.drawable.baseline_remove_red_eye_24);
            //No++;
            contractViewHoder.ivDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClicked(contract);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(mListUser!=null){
            return mListUser.size();

        }
        return 0;
    }

    public class ContractViewHoder extends RecyclerView.ViewHolder {

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

    public  class LoadingContractViewHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;

        public LoadingContractViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.item_progressBar);
        }
    }


    public void addFooterLoading(){
        isLoadingAdd=true;
        mListUser.add(new Contract());
    }

    public void removeFooterLoading(){
        isLoadingAdd=false;
        int position = mListUser.size()-1;
        Contract contract = mListUser.get(position);
        if(contract!=null){
            mListUser.remove(position);
            notifyItemRemoved(position);
        }
    }

}