package com.example.administrator.fztask;

/**
 * Created by Administrator on 3/20/2018.
 */

public class Task {
    private String taskAssignmentId, taskType, city;

    public Task() {
    }

    public Task(String taskAssignmentId, String taskType, String city) {
        this.taskAssignmentId = taskAssignmentId;
        this.taskType = taskType;
        this.city = city;
    }

    public String getTaskAssignmentId() {
        return taskAssignmentId;
    }

    public void settaskAssignmentId(String taskAssignmentId) {
        this.taskAssignmentId = taskAssignmentId;
    }

    public String gettaskType() {
        return taskType;
    }

    public void settaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
