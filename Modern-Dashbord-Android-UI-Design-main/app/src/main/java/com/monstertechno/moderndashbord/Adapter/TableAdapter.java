package com.monstertechno.moderndashbord.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.monstertechno.moderndashbord.R;

import java.util.List;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHolder> {

    private List<String[]> data;

    public TableAdapter(List<String[]> data) {
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_table, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String[] rowData = data.get(position);

        holder.textView1.setText(rowData[0]);
        holder.textView2.setText(rowData[1]);
        holder.textView3.setText(rowData[2]);

        if (position == 0) {
            // Đổi màu cho hàng đầu tiên
            holder.textView1.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
            holder.textView2.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
            holder.textView3.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.pastel));
        } else {
            // Đặt màu mặc định cho các hàng khác
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView1;
        TextView textView2;
        TextView textView3;

        public ViewHolder(View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
        }
    }
}
