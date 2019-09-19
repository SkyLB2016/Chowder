package com.sky.chowder.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sky.chowder.R;
import com.sky.chowder.model.Employe;
import com.sky.chowder.ui.activity.TestActivity;

import java.util.List;

/**
 * Created by libin on 2019/08/29 19:18 Thursday.
 */
public class HrmAdapter extends RecyclerView.Adapter<HrmAdapter.HrmHolder> {
    List<Employe> employes;

    public HrmAdapter(List<Employe> employes) {
        this.employes = employes;
    }

    @NonNull
    @Override
    public HrmHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new HrmHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_hrm_five, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HrmHolder hrmHolder, int i) {
        Employe employe = employes.get(i);
        hrmHolder.tv01.setText(employe.getName());
        hrmHolder.tv02.setText(employe.getSn());
        hrmHolder.tv03.setText(employe.getDept());
        hrmHolder.tv04.setText(employe.getMobile());
        hrmHolder.tv05.setText(employe.getDate());
    }

    @Override
    public int getItemCount() {
        return employes == null ? 0 : employes.size();
    }

    class HrmHolder extends RecyclerView.ViewHolder {
        TextView tv01;
        TextView tv02;
        TextView tv03;
        TextView tv04;
        TextView tv05;

        public HrmHolder(@NonNull View itemView) {
            super(itemView);
            tv01 = itemView.findViewById(R.id.tv01);
            tv02 = itemView.findViewById(R.id.tv02);
            tv03 = itemView.findViewById(R.id.tv03);
            tv04 = itemView.findViewById(R.id.tv04);
            tv05 = itemView.findViewById(R.id.tv05);
        }
    }
}