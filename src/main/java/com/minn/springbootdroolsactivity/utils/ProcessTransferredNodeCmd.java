package com.minn.springbootdroolsactivity.utils;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.RuntimeServiceImpl;
import org.activiti.engine.impl.cmd.NeedsActiveExecutionCmd;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 流程跳转指定节点
 */
public class ProcessTransferredNodeCmd extends NeedsActiveExecutionCmd<Object> {
    private static final long serialVersionUID = 1L;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;

    private String executionId;

    private String taskDefinitionKey;

    public ProcessTransferredNodeCmd(String executionId,String taskDefinitionKey) {
        super(executionId);
        this.executionId = executionId;
        this.taskDefinitionKey = taskDefinitionKey;
    }

    @Override
    protected Object execute(CommandContext commandContext, ExecutionEntity executionEntity) {
        //获取当前任务信息
        TaskEntity taskEntity = (TaskEntity)taskService.createTaskQuery().executionId(executionId).singleResult();
        //获取当前任务活动节点
        ExecutionEntity execution = (ExecutionEntity) runtimeService.createExecutionQuery().executionId(executionId).singleResult();

        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(taskEntity.getProcessDefinitionId());

        //targetTaskDefinitionKey为null，默认查询当前任务活动节点
        if (taskDefinitionKey == null) {
            taskDefinitionKey = taskEntity.getTaskDefinitionKey();
        }

        ActivityImpl activity = ((ProcessDefinitionImpl) processDefinitionEntity).findActivity(taskDefinitionKey);

        //包装一个Command对象
        ((RuntimeServiceImpl) runtimeService).getCommandExecutor().execute(
                new Command<Void>(){
                    public Void execute(CommandContext commandContext) {
                        //创建新任务
                        //activity.setProperties(properties);
                        execution.setActivity(activity);
                        execution.executeActivity(activity);

                        //删除当前的任务
                        //不能删除当前正在执行的任务，所以要先清除掉关联
                        taskEntity.setExecutionId(null);
                        taskService.saveTask(taskEntity);
                        taskService.deleteTask(taskEntity.getId(), true);

                        return null;
                    }
                });

        return null;
    }
}
