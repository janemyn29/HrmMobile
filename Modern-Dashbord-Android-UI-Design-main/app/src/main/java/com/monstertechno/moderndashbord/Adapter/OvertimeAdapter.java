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
import com.monstertechno.moderndashbord.Model.Overtime;
import com.monstertechno.moderndashbord.Model.SelectListener;
import com.monstertechno.moderndashbord.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class OvertimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private Context mContext;
    private List<Overtime> mListUser;
    private SelectListener listener;
    private boolean isLoadingAdd;

    public OvertimeAdapter(Context mContext, SelectListener listener) {
        this.mContext = mContext;
        this.listener = listener;
    }

    private static final int TYPE_ITEM =1;
    private static final int TYPE_LOADING =2;

    public void setData(List<Overtime> list){
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leave,parent,false);
            return new OvertimeAdapter.OvertimeViewHoder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading,parent,false);
            return new OvertimeAdapter.LoadingOvertimeViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType()==TYPE_ITEM){
            Overtime overtime = mListUser.get(position);
            if(overtime == null){
                return;
            }
            OvertimeAdapter.OvertimeViewHoder overtimeViewHoder = (OvertimeAdapter.OvertimeViewHoder) holder;
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String formattedDate = dateFormat.format(overtime.date);
            if(position==0){
                overtimeViewHoder.tvNo.setText("STT: "+ position+1);

            }else{
                overtimeViewHoder.tvNo.setText("STT: "+ position/10+1);
            }
            overtimeViewHoder.tvDate.setText("Ngày tăng ca: "+formattedDate);
            overtimeViewHoder.tvShift.setText("Số giờ tăng ca: "+overtime.hours);
            overtimeViewHoder.btnStatus.setText(overtime.status);
            if(overtime.status.equals("Request")){
                int color = ContextCompat.getColor(mContext, R.color.yellow);
                overtimeViewHoder.btnStatus.setTextColor(color);
            }else if(overtime.status.equals("Approved")){
                int color = ContextCompat.getColor(mContext, R.color.Green);
                overtimeViewHoder.btnStatus.setTextColor(color);
            }else if(overtime.status.equals("Cancel")){
                int color = ContextCompat.getColor(mContext, R.color.error_200);
                overtimeViewHoder.btnStatus.setTextColor(color);
            }
            overtimeViewHoder.ivDetail.setImageResource(R.drawable.baseline_remove_red_eye_24);
            //No++;
            overtimeViewHoder.ivDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClicked(overtime);
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

    public class OvertimeViewHoder extends RecyclerView.ViewHolder {

        TextView tvDate, tvShift, btnStatus, tvNo;
        ImageView ivDetail;

        public OvertimeViewHoder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.item_leave_tvDate);
            tvShift = itemView.findViewById(R.id.item_leave_tvShift);
            btnStatus = itemView.findViewById(R.id.item_leave_btnStatus);
            ivDetail = itemView.findViewById(R.id.item_leave_ivDetail);
            tvNo = itemView.findViewById(R.id.item_leave_tvNo);
        }
    }

    public  class LoadingOvertimeViewHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;

        public LoadingOvertimeViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.item_progressBar);
        }
    }


    public void addFooterLoading(){
        isLoadingAdd=true;
        mListUser.add(new Overtime());
    }

    public void removeFooterLoading(){
        isLoadingAdd=false;
        int position = mListUser.size()-1;
        Overtime overtime = mListUser.get(position);
        if(overtime!=null){
            mListUser.remove(position);
            notifyItemRemoved(position);
        }
    }
}
