package com.minn.springbootdroolsactivity.utils;

import com.minn.springbootdroolsactivity.model.TaskInfo;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;

import java.util.ArrayList;
import java.util.List;

public class ProcessSelectUtil {
    private static ProcessEngine processEngine;
    //历史服务组件
    private static HistoryService historyService;
    //运行服务组件
    private static RuntimeService runtimeService;
    //任务服务组件
    private static TaskService taskService;
    //流程存储服务组件
    private static RepositoryService repositoryService;

    static {
        if(processEngine == null){
            processEngine = ProcessEngines.getDefaultProcessEngine();
        }
        if(runtimeService == null){
            runtimeService = processEngine.getRuntimeService();
        }
        if(historyService == null){
            historyService = processEngine.getHistoryService();
        }
        if(taskService == null){
            taskService = processEngine.getTaskService();
        }
        if(repositoryService == null){
            repositoryService = processEngine.getRepositoryService();
        }
    }

    /**
     * 根据流程实例ID查询获取上一个节点信息
     * @return
     */
    public static HistoricTaskInstance getLastNodeInfo(String proInsId){
        //上一个节点
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(proInsId).orderByHistoricTaskInstanceEndTime().desc().list();
        HistoricTaskInstance taskInstance = null;
        if (!list.isEmpty()) {
            if (list.get(0).getEndTime() != null) {
                taskInstance = list.get(0);
            }
        }
        return taskInstance;
    }




    /**
     * 查询用户待办任务列表。
     *
     * @param candidateUser 用户
     * @return 任务列表
     */
    public static List<TaskInfo> queryToDoTasks(String candidateUser) {
        List<Task> taskList  = taskService.createTaskQuery().taskCandidateUser(candidateUser).list();
        List<TaskInfo> taskInfoList = new ArrayList<>();
        for (Task task : taskList) {
            TaskInfo taskInfo = new TaskInfo();
            taskInfo.setTaskId(task.getId());
            taskInfo.setTaskName(task.getName());
            taskInfo.setProcessDefinitionId(task.getProcessDefinitionId());
            taskInfoList.add(taskInfo);
        }
        return taskInfoList;
    }

    /**
     * 查询已处理任务列表。
     *
     * @param candidateUset 用户
     * @return 已处理任务列表
     */
    public static List<TaskInfo> queryDoneTasks(String candidateUset) {
        List<HistoricTaskInstance> taskList  = historyService.createHistoricTaskInstanceQuery()
                .taskCandidateUser(candidateUset).finished().list();

        List<TaskInfo> taskInfoList = new ArrayList<TaskInfo>();
        for (HistoricTaskInstance task : taskList) {
            TaskInfo taskInfo = new TaskInfo();
            taskInfo.setTaskId(task.getId());
            taskInfo.setTaskName(task.getName());
            taskInfo.setProcessDefinitionId(task.getProcessDefinitionId());
            taskInfoList.add(taskInfo);
        }
        return taskInfoList;
    }

    /**
     * 查询所有审批信息
     * @param processId 实例ID
     * @return
     */
    public static List<TaskInfo> queryHisTasks(String processId){
        List<HistoricTaskInstance> taskList  = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processId).list();
        List<TaskInfo> taskInfoList = new ArrayList<TaskInfo>();
        for (HistoricTaskInstance task : taskList) {
            TaskInfo taskInfo = new TaskInfo();
            taskInfo.setTaskId(task.getId());
            taskInfo.setTaskName(task.getName());
            taskInfo.setProcessDefinitionId(task.getProcessDefinitionId());
            taskInfoList.add(taskInfo);
        }
        return taskInfoList;
    }
}
