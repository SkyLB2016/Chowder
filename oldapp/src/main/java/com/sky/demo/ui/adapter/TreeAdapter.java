package com.sky.demo.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sky.adapter.RecyclerAdapter;
import com.sky.adapter.RecyclerHolder;
import com.sky.demo.R;
import com.sky.demo.ui.tree.Node;
import com.sky.demo.ui.tree.TreeHelper;

import java.util.List;

/**
 * @author LiBin
 * @Description: TODO
 * @date 2015/9/28 17:34
 */
public class TreeAdapter<T> extends RecyclerAdapter<Node, RecyclerView.ViewHolder> {
    private List<Node> nodes;

    public TreeAdapter(int layoutId, int layoutFootViewId) {
        super(layoutId, layoutFootViewId);
    }

    public void setAllNodes(List<T> nodes) {
        try {
            datas = TreeHelper.getSortedNodes(nodes, 1);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        setNodes(datas);
    }

    private void setNodes(List<Node> nodes) {
        this.nodes = TreeHelper.filterVisibleNodes(nodes);
        notifyDataSetChanged();
    }

    public void toggle(int position) {
        Node node = nodes.get(position);
        if (node.hasLeaf()) return;
        node.setIsExpand(!node.isExpand());
        nodes = TreeHelper.filterVisibleNodes(datas);
        notifyDataSetChanged();
    }

    @Override
    protected RecyclerView.ViewHolder onCreateBodyHolder(View view) {
        return  new RecyclerHolder(view);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateFootHolder(View footView) {
        return new RecyclerHolder(footView);
    }

    @Override
    protected void onAchieveHolder(final RecyclerView.ViewHolder viewHolder, int position) {
        if (position + 1 == getItemCount()){
            viewHolder.itemView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    notifyItemRemoved(viewHolder.getLayoutPosition() + 1);
                }
            }, 3000);
            return;
        }

//        if (viewHolder instanceof FooterViewHolder) {
//            viewHolder.itemView.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    notifyItemRemoved(viewHolder.getLayoutPosition() + 1);
//                }
//            }, 3000);
//            return;
//        }
        RecyclerHolder holder = (RecyclerHolder) viewHolder;
        Node node = nodes.get(position);
        if (node.getIcon() == -1) {
            holder.getView(R.id.img).setVisibility(View.INVISIBLE);
        } else {
            holder.getView(R.id.img).setVisibility(View.VISIBLE);
            ((ImageView)holder.getView(R.id.img)).setImageResource(node.getIcon());
        }
        holder.itemView.setPadding(node.getLevel() * ((ImageView)holder.getView(R.id.img)).getDrawable().getIntrinsicWidth(), 3, 3, 3);
        ((TextView)holder.getView(R.id.tv)).setText(node.getName());
    }

    @Override
    public int getItemCount() {
        return nodes == null ? 0 : (nodes.size() + 1);
    }


    public void addNode(String text, int position) {

        Node node = new Node();
        Node parent = nodes.get(position);
        node.setId(parent.getpId() + "_1");
        node.setpId(parent.getId());
        node.setName(text);
        node.setParent(parent);
        parent.getChild().add(node);
        datas.add(datas.indexOf(parent) + 1, node);
        nodes = TreeHelper.filterVisibleNodes(datas);
        notifyDataSetChanged();

    }

    public Node getNode(int position) {
        return nodes.get(position);
    }
}

//class FooterViewHolder extends RecyclerView.ViewHolder {
//
//    public FooterViewHolder(View view) {
//        super(view);
//    }
//
//}