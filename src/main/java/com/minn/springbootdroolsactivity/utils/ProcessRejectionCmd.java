package com.minn.springbootdroolsactivity.utils;

import org.activiti.engine.impl.cmd.NeedsActiveExecutionCmd;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;

/**
 * 流程拒绝
 */
public class ProcessRejectionCmd extends NeedsActiveExecutionCmd<Object> {
    private static final long serialVersionUID = 1L;

    public ProcessRejectionCmd(String executionId) {
        super(executionId);
    }

    @Override
    protected Object execute(CommandContext commandContext, ExecutionEntity executionEntity) {
        executionEntity.end();
        return null;
    }
}
