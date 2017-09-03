package com.sky.demo.ui.adapter;

import android.animation.AnimatorInflater;
import android.graphics.Outline;
import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;

import com.sky.adapter.RecyclerAdapter;
import com.sky.adapter.RecyclerHolder;
import com.sky.demo.R;
import com.sky.demo.model.ActivityModel;
import com.sky.demo.utils.LogUtils;

/**
 * @author sky QQ:1136096189
 * @Description:
 * @date 15/12/9 下午8:52
 */
public class MainAdapter extends RecyclerAdapter<ActivityModel, RecyclerHolder> {
    public MainAdapter(int layoutId) {
        super(layoutId);
    }

    //速度测试
    private long totalTime;
    private long start;

    @Override
    protected RecyclerHolder onCreateBodyHolder(View view) {
        start = System.nanoTime();
//        return new BaseHolder(view);//19433695,588215
        return new RecyclerHolder(view);//19139763,1215232
    }

    @Override
    protected void onAchieveHolder(RecyclerHolder holder, int position) {
//        holder.cardView.setRadius(new Random().nextInt(50));
//        holder.cardView.setCardElevation(new Random().nextInt(100));
////        holder.cardView.setElevation(new Random().nextInt(100));
//        holder.className.setText(position + 1 + "." + datas.get(position).getClassName());
//        holder.describe.setText(datas.get(position).getDescribe());
//        holder.img.setBackgroundResource(datas.get(position).getImg());
//        ((CardView) holder.getView(R.id.cardView)).setRadius(new Random().nextInt(50));
//        ((CardView) holder.getView(R.id.cardView)).setCardElevation(new Random().nextInt(100));
//        holder.cardView.setElevation(new Random().nextInt(100));

        holder.setText(R.id.tv_name, position + 1 + "." + datas.get(position).getClassName());
        holder.setText(R.id.tv_describe, datas.get(position).getDescribe());
        holder.setImage(R.id.img_describe, datas.get(position).getImg());
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            //使用ripple
            //((CardView) holder.getView(R.id.cardView)).setRadius(new Random().nextInt(50));
            holder.getView(R.id.cardView).setBackground(context.getDrawable(R.drawable.ripple));
            //点击效果，阴影效果
            //((CardView) holder.getView(R.id.cardView)).setCardElevation(new Random().nextInt(100));
            holder.getView(R.id.cardView).setStateListAnimator(
                    AnimatorInflater.loadStateListAnimator(context, R.drawable.state_list_animator));
            //视图裁剪
            holder.getView(R.id.img_describe).setClipToOutline(true);
            holder.getView(R.id.img_describe).setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    outline.setRoundRect(view.getLeft(), view.getTop(),
                            view.getRight(), view.getBottom(), 30);
                }
            });
        }
        //开始打印测试时间
        long last = System.nanoTime() - start;
        totalTime += last;
        LogUtils.i(totalTime + "");
    }

//    class BaseHolder extends RecyclerView.ViewHolder {
//
//        CardView cardView;
//        TextView className;
//        TextView describe;
//        ImageView img;
//
//        public BaseHolder(View itemView) {
//            super(itemView);
//            cardView = (CardView) itemView.findViewById(R.id.cardView);
//            className = (TextView) itemView.findViewById(R.id.tv_name);
//            describe = (TextView) itemView.findViewById(R.id.tv_describe);
//            img = (ImageView) itemView.findViewById(R.id.img_describe);
//        }
//    }
}