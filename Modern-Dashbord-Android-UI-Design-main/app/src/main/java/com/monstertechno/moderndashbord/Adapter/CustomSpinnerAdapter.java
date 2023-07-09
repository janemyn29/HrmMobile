package com.monstertechno.moderndashbord.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.monstertechno.moderndashbord.Model.DefaultModel;

import java.util.List;

public class CustomSpinnerAdapter extends ArrayAdapter<DefaultModel> {
    public CustomSpinnerAdapter(Context context, List<DefaultModel> itemList) {
        super(context, android.R.layout.simple_spinner_item, itemList);
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView textView = (TextView) super.getView(position, convertView, parent);
        textView.setVisibility(View.GONE);  // Ẩn giá trị trong view chính
        return textView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
        textView.setVisibility(View.VISIBLE);  // Hiển thị nội dung trong danh sách thả xuống
        return textView;
    }
}

