package com.minn.springbootdroolsactivity.model;

import java.util.List;

/**
 * 任务节点
 */
public class UserTaskModel {
    private String id;
    private String name;
    //指定组 ，即：角色定义
    private List<String> candidateGroups;
    //指定用户
    private List<String> candidateUsers;
    //指定某个人
    private String assignee;
    //监听
    private String listener;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getCandidateGroups() {
        return candidateGroups;
    }

    public void setCandidateGroups(List<String> candidateGroups) {
        this.candidateGroups = candidateGroups;
    }

    public List<String> getCandidateUsers() {
        return candidateUsers;
    }

    public void setCandidateUsers(List<String> candidateUsers) {
        this.candidateUsers = candidateUsers;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getListener() {
        return listener;
    }

    public void setListener(String listener) {
        this.listener = listener;
    }
}
