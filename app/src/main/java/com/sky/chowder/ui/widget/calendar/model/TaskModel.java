package com.sky.chowder.ui.widget.calendar.model;

import java.io.Serializable;

/**
 * @author hezhiqiang
 * @date 17/2/21
 */

public class TaskModel implements Serializable {

    private static final long serialVersionUID = -7218022578887232723L;

    public String id;
    public String title;            //任务标题
    public String dueDate;          //截止时间
    public String status;           //状态
    public int priority;            //优先级
    public int fileAmount;          //附件数量
    public int childTask;           //子任务数量
    public int completedChildTask;  //已完成子任务数
}
