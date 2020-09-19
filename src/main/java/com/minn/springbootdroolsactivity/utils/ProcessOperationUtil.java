package com.minn.springbootdroolsactivity.utils;

import org.activiti.engine.*;
import org.activiti.engine.impl.RuntimeServiceImpl;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * 流程图操作
 */
public class ProcessOperationUtil {
    //private static ProcessEngine processEngine;
    //历史服务组件
    private static HistoryService historyService;
    //运行服务组件
    private static RuntimeService runtimeService;
    //任务服务组件
    private static TaskService taskService;
    //流程存储服务组件
    private static RepositoryService repositoryService;

    static {
        if(runtimeService == null){
            runtimeService = ProcessEngines.getDefaultProcessEngine().getRuntimeService();
        }
        if(historyService == null){
            historyService = ProcessEngines.getDefaultProcessEngine().getHistoryService();
        }
        if(taskService == null){
            taskService = ProcessEngines.getDefaultProcessEngine().getTaskService();
        }
        if(repositoryService == null){
            repositoryService = ProcessEngines.getDefaultProcessEngine().getRepositoryService();
        }
    }


    /**
     * 流程启动
     * @param key
     */
    public static void processStart(String key){
        runtimeService.startProcessInstanceByKey(key);
    }



    /**
     * 流程审批
     * @param taskId
     * @param map
     */
    public static void processApproval(String taskId, Map<String,Object> map){
        taskService.complete(taskId, map);
    }

    /**
     * 流程拒绝
     * @param executionId at_ru_task中的 execution_Id字段
     */
    public static void processRejection(String executionId){
        ((RuntimeServiceImpl) runtimeService).getCommandExecutor().execute(new ProcessRejectionCmd(executionId));
    }

    /**
     * 流程跳转指定节点
     * @param executionId
     * @param taskDefinitionKey 指定节点ID
     */
    public static void processTransferredNode(String executionId,String taskDefinitionKey){
        ((RuntimeServiceImpl) runtimeService).getCommandExecutor().execute(new ProcessTransferredNodeCmd(executionId,taskDefinitionKey));
    }

    /**
     * 流程挂起
     * @param processInstanceId 流程实例ID
     */
    public static void processSuspend(String processInstanceId){
        runtimeService.suspendProcessInstanceById(processInstanceId);
    }

    /**
     * 流程激活
     * @param processInstanceId
     */
    public static void processActivation(String processInstanceId){
        runtimeService.activateProcessInstanceById(processInstanceId);
    }

    //指定管理员
    public static void getAdminTask(String processID){
        List<Task> list = taskService.createTaskQuery().processInstanceId(processID).list();
        for(Task task:list){

            System.out.println("指定人是："+task.getAssignee());
            task.setAssignee("#{admin}");
            taskService.saveTask(task);
        }

    }

}
