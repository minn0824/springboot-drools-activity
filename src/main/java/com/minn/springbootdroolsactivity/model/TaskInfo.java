package com.minn.springbootdroolsactivity.model;

public class TaskInfo {
    //任务ID
    private String taskId;
    //任务名称
    private String taskName;
    //实例ID
    private String processDefinitionId;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    @Override
    public String toString() {
        return "TaskInfo{" +
                "taskId='" + taskId + '\'' +
                ", taskName='" + taskName + '\'' +
                ", processDefinitionId='" + processDefinitionId + '\'' +
                '}';
    }
}
