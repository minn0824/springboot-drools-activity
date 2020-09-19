package com.minn.springbootdroolsactivity.service;

import com.minn.springbootdroolsactivity.entity.AtProcessNode;

import java.util.List;

public interface AtProcessNodeService {
    public List<AtProcessNode> selectRuslt(String templateId, String processNodeType);

    public List<AtProcessNode> selectNodeId(String templateId);
}
