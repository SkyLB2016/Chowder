package com.sky.demo.ui.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LiBin
 * @Description: TODO
 * @date 2015/9/29 9:56
 */
public class Node {
    private String id;
    private String pId;
    private String name;
    private int level;//层级
    private boolean isExpand = false;//是否展开
    private int icon;
    private Node parent;
    private List<Node> child = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return parent == null ? 0 : parent.getLevel() + 1;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setIsExpand(boolean isExpand) {
        this.isExpand = isExpand;
        if (!isExpand)
            for (Node node : child)
                node.setIsExpand(isExpand);
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public List<Node> getChild() {
        return child;
    }

    public void setChild(List<Node> child) {
        this.child = child;
    }

    /**
     * 是否是根节点
     *
     * @return
     */
    public boolean isRoot() {
        return parent == null;
    }

    /**
     * 判断父节点是否展开
     *
     * @return
     */
    public boolean isParentExpand() {
        if (parent == null) return false;
        return parent.isExpand();
    }

    /**
     * 是否有子类
     *
     * @return
     */
    public boolean hasLeaf() {
        return child.size() == 0;
    }
}
