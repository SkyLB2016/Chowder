package com.sky.chowder.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sky.chowder.R;
import com.sky.chowder.ui.activity.TestActivity;

/**
 * Created by libin on 2019/08/29 19:18 Thursday.
 */
public class HrmAdapter extends RecyclerView.Adapter<HrmHolder> {
    @NonNull
    @Override
    public HrmHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new HrmHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_hrm, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HrmHolder hrmHolder, int i) {

        hrmHolder.tv01.setText("年假");
        hrmHolder.tv02.setText("2");
        hrmHolder.tv03.setText("1");
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}

class HrmHolder extends RecyclerView.ViewHolder {
    TextView tv01;
    TextView tv02;
    TextView tv03;

    public HrmHolder(@NonNull View itemView) {
        super(itemView);
        tv01 = itemView.findViewById(R.id.tv01);
        tv02 = itemView.findViewById(R.id.tv02);
        tv03 = itemView.findViewById(R.id.tv03);
    }
}