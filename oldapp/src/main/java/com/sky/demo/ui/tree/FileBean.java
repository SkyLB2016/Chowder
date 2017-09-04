package com.sky.demo.ui.tree;

import com.sky.demo.ui.tree.annotation.TreeNode;
/**
 * @author LiBin
 * @Description: TODO
 * @date 2015/9/29 9:57
 */
public class FileBean {
    private static final long serialVersionUID = 1L;
    @TreeNode("id")
    private String id;
    @TreeNode("pId")
    private String pId;
    @TreeNode("name")
    private String name;

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
}