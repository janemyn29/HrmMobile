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

import com.monstertechno.moderndashbord.Model.Dependent;
import com.monstertechno.moderndashbord.Model.Leave;
import com.monstertechno.moderndashbord.Model.SelectListener;
import com.monstertechno.moderndashbord.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class DependentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Dependent> mListUser;
    private SelectListener listener;
    private boolean isLoadingAdd;

    public DependentAdapter(Context mContext, SelectListener listener) {
        this.mContext = mContext;
        this.listener = listener;
    }

    private static final int TYPE_ITEM =1;
    private static final int TYPE_LOADING =2;

    public void setData(List<Dependent> list){
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dependent,parent,false);
            return new DependentAdapter.DependentViewHoder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading,parent,false);
            return new DependentAdapter.LoadingDependentViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType()==TYPE_ITEM){
            Dependent leave = mListUser.get(position);
            if(leave == null){
                return;
            }
            DependentAdapter.DependentViewHoder dependentViewHoder = (DependentAdapter.DependentViewHoder) holder;
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String formattedDate = dateFormat.format(leave.birthDate);

            dependentViewHoder.tvDate.setText("Ngày sinh: "+formattedDate);
            dependentViewHoder.tvName.setText("Họ tên: "+leave.name);
            dependentViewHoder.tvRela.setText("Quan hệ: "+leave.relationship);
            dependentViewHoder.tvDes.setText("Mô tả: "+leave.desciption);

            dependentViewHoder.btnStatus.setText(leave.acceptanceType);
            if(leave.acceptanceType.equals("Request")){
                int color = ContextCompat.getColor(mContext, R.color.yellow);
                dependentViewHoder.btnStatus.setTextColor(color);
            }else if(leave.acceptanceType.equals("Accept")){
                int color = ContextCompat.getColor(mContext, R.color.Green);
                dependentViewHoder.btnStatus.setTextColor(color);
            }else if(leave.acceptanceType.equals("Deny")){
                int color = ContextCompat.getColor(mContext, R.color.error_200);
                dependentViewHoder.btnStatus.setTextColor(color);
            }
        }
    }

    @Override
    public int getItemCount() {
        if(mListUser!=null){
            return mListUser.size();

        }
        return 0;
    }

public class DependentViewHoder extends RecyclerView.ViewHolder {

    TextView tvDate, tvName, btnStatus, tvRela, tvDes;

    public DependentViewHoder(@NonNull View itemView) {
        super(itemView);
        tvDate = itemView.findViewById(R.id.item_depen_Date);
        tvName = itemView.findViewById(R.id.item_depen_Name);
        btnStatus = itemView.findViewById(R.id.item_depen_status);
        tvRela = itemView.findViewById(R.id.item_depen_Relationship);
        tvDes = itemView.findViewById(R.id.item_depen_des);
    }
}

public  class LoadingDependentViewHolder extends RecyclerView.ViewHolder{
    ProgressBar progressBar;

    public LoadingDependentViewHolder(@NonNull View itemView) {
        super(itemView);
        progressBar = itemView.findViewById(R.id.item_progressBar);
    }
}


    public void addFooterLoading(){
        isLoadingAdd=true;
        mListUser.add(new Dependent());
    }

    public void removeFooterLoading(){
        isLoadingAdd=false;
        int position = mListUser.size()-1;
        Dependent leave = mListUser.get(position);
        if(leave!=null){
            mListUser.remove(position);
            notifyItemRemoved(position);
        }
    }

}

