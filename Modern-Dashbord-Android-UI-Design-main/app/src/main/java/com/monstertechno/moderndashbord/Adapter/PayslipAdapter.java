package com.monstertechno.moderndashbord.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.monstertechno.moderndashbord.Model.Leave;
import com.monstertechno.moderndashbord.Model.Payslip;
import com.monstertechno.moderndashbord.Model.SelectListener;
import com.monstertechno.moderndashbord.R;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class PayslipAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<Payslip> mListUser;
    private SelectListener listener;
    private boolean isLoadingAdd;

    public PayslipAdapter(Context mContext, SelectListener listener) {
        this.mContext = mContext;
        this.listener = listener;
    }

    private static final int TYPE_ITEM =1;
    private static final int TYPE_LOADING =2;

    public void setData(List<Payslip> list){
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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(TYPE_ITEM==viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payslip,parent,false);
            return new PayslipAdapter.PayslipViewHoder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading,parent,false);
            return new PayslipAdapter.LoadingPayslipViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType()==TYPE_ITEM){
            Payslip payslip = mListUser.get(position);
            if(payslip == null){
                return;
            }
            PayslipAdapter.PayslipViewHoder leaveViewHoder = (PayslipAdapter.PayslipViewHoder) holder;
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String paidDate = dateFormat.format(payslip.paydayCal);
            String fromDate = dateFormat.format(payslip.fromTime);
            String toDate = dateFormat.format(payslip.toTime);
            String salary = convertToFormattedString(payslip.finalSalary) + " VNĐ";

            leaveViewHoder.tvDate.setText("Ngày tính: "+ paidDate);
            leaveViewHoder.tvFrom.setText("Từ ngày: "+ fromDate);
            leaveViewHoder.tvTo.setText("Đến ngày: "+ toDate);
            leaveViewHoder.tvTotal.setText("Tổng lương: "+ salary);
            leaveViewHoder.ivDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClicked(payslip);
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

    public class PayslipViewHoder extends RecyclerView.ViewHolder {

        TextView tvDate, tvFrom, tvTo, tvTotal;
        ImageView ivDetail;

        public PayslipViewHoder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.item_payslip_dateCal);
            tvFrom = itemView.findViewById(R.id.item_payslip_fromdate);
            tvTo = itemView.findViewById(R.id.item_payslip_todate);
            ivDetail = itemView.findViewById(R.id.item_payslip_detail);
            tvTotal = itemView.findViewById(R.id.item_payslip_total);
        }
    }

    public  class LoadingPayslipViewHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;

        public LoadingPayslipViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.item_progressBar);
        }
    }


    public void addFooterLoading(){
        isLoadingAdd=true;
        mListUser.add(new Payslip());
    }

    public void removeFooterLoading(){
        isLoadingAdd=false;
        int position = mListUser.size()-1;
        Payslip leave = mListUser.get(position);
        if(leave!=null){
            mListUser.remove(position);
            notifyItemRemoved(position);
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
