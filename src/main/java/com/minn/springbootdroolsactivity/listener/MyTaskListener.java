package com.minn.springbootdroolsactivity.listener;

import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 并行监听
 */
public class MyTaskListener implements TaskListener {
    @Autowired
    private TaskService taskService;

    @Override
    public void notify(DelegateTask delegateTask) {
//        delegateTask.setAssignee("admin");
        System.out.println("1111111---->"+delegateTask.getAssignee());
        if("hr".equals(delegateTask.getAssignee())){
            delegateTask.setAssignee("admin");
        }
        System.out.println("监听中.....");
    }
}
