package com.genius.todolist;

/**
 * Created by 112 on 2016/3/10.
 */
public class TaskBean {

    /** 类型为标题 **/
    public static final int TASKBEAN_TYPE_TITLE=1;
    /** 类型为任务 **/
    public static final int TASKBEAN_TYPE_TASK=2;


    /**
     * 任务内容
     **/
    private String content;
    /**
     * 类型（1. 标题 ; 2. 任务）
     **/
    private int type;
    /**
     * 状态（true : 已完成 ， false :　未完成）
     */
    private boolean complete;

    public TaskBean(String content, int type, boolean complete) {
        this.content = content;
        this.type = type;
        this.complete = complete;
    }

    public TaskBean() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean getComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}
