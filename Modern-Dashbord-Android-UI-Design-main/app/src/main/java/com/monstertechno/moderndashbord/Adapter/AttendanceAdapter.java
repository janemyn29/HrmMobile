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

import com.monstertechno.moderndashbord.Model.Attendance;
import com.monstertechno.moderndashbord.Model.Overtime;
import com.monstertechno.moderndashbord.Model.SelectListener;
import com.monstertechno.moderndashbord.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class AttendanceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private Context mContext;
    private List<Attendance> mListUser;
    private SelectListener listener;
    private boolean isLoadingAdd;

    public AttendanceAdapter(Context mContext, SelectListener listener) {
        this.mContext = mContext;
        this.listener = listener;
    }

    private static final int TYPE_ITEM =1;
    private static final int TYPE_LOADING =2;

    public void setData(List<Attendance> list){
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attendance,parent,false);
            return new AttendanceAdapter.AttendanceViewHoder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading,parent,false);
            return new AttendanceAdapter.LoadingAttendanceViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType()==TYPE_ITEM){
            Attendance attendance = mListUser.get(position);
            if(attendance == null){
                return;
            }
            AttendanceAdapter.AttendanceViewHoder attendanceViewHoder = (AttendanceAdapter.AttendanceViewHoder) holder;
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String formattedDate = dateFormat.format(attendance.day);

            DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            String start = timeFormat.format(attendance.startTime);
            String end = "";
            if(attendance.endTime!=null){
                end = timeFormat.format(attendance.endTime);
            }
            String work = "";
            if(attendance.timeWork !=null){
                work =String.valueOf( attendance.timeWork);
            }

            String ot = "";
            if(attendance.timeWork !=null){
                ot =String.valueOf( attendance.overTime);
            }

            attendanceViewHoder.tvDate.setText("Ngày: "+formattedDate);
            attendanceViewHoder.tvShift.setText("Ca làm việc: "+attendance.shiftEnum);
            attendanceViewHoder.tvStart.setText("Giờ bắt đầu: "+start);
            attendanceViewHoder.tvEnd.setText("Giờ kết thúc: "+end);
            attendanceViewHoder.tvWork.setText("Tổng giờ làm: "+work);

            attendanceViewHoder.tvOT.setText("Giờ tăng ca: "+ot);

            attendanceViewHoder.ivDetail.setImageResource(R.drawable.baseline_remove_red_eye_24);
            //No++;
            attendanceViewHoder.ivDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClicked(attendance);
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

    public class AttendanceViewHoder extends RecyclerView.ViewHolder {

        TextView tvDate, tvShift, tvStart, tvEnd, tvWork, tvOT;
        ImageView ivDetail;

        public AttendanceViewHoder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.item_attendance_Date);
            tvShift = itemView.findViewById(R.id.item_attendance_shift);
            tvStart = itemView.findViewById(R.id.item_attendance_start);
            ivDetail = itemView.findViewById(R.id.item_attendance_detail);
            tvEnd = itemView.findViewById(R.id.item_attendance_end);
            tvWork = itemView.findViewById(R.id.item_attendance_workingTime);
            tvOT = itemView.findViewById(R.id.item_attendance_ot);
        }
    }

    public  class LoadingAttendanceViewHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;

        public LoadingAttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.item_progressBar);
        }
    }


    public void addFooterLoading(){
        isLoadingAdd=true;
        mListUser.add(new Attendance());
    }

    public void removeFooterLoading(){
        isLoadingAdd=false;
        int position = mListUser.size()-1;
        Attendance attendance = mListUser.get(position);
        if(attendance!=null){
            mListUser.remove(position);
            notifyItemRemoved(position);
        }
    }
}
