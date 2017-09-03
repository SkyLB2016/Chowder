package com.sky.demo.ui.adapter;

import android.view.View;

import com.sky.adapter.RecyclerAdapter;
import com.sky.adapter.RecyclerHolder;
import com.sky.demo.R;
import com.sky.demo.model.ProcessEntity;

/**
 * @author sky QQ:1136096189
 * @Description: 进程适配器
 * @date 15/12/27 下午4:22
 */
public class ProcessAdapter extends RecyclerAdapter<ProcessEntity, RecyclerHolder> {
    public ProcessAdapter(int layoutId) {
        super(layoutId);
    }

    @Override
    protected RecyclerHolder onCreateBodyHolder(View view) {
        return new RecyclerHolder(view);
    }

    @Override
    protected void onAchieveHolder(RecyclerHolder holder, int position) {

        holder.setText(R.id.tv_appName,"程序名称:" + datas.get(position).getAppName());
        holder.setText(R.id.tv_uid,"进程UID:" + datas.get(position).getUid());
        holder.setText(R.id.tv_pid,"进程PID:" + datas.get(position).getPid());
        holder.setText(R.id.tv_memSize,"进程大小:" + datas.get(position).getMemSize() + "KB");
        holder.setText(R.id.tv_processName,"进程名:" + datas.get(position).getProcessName());
    }
}
