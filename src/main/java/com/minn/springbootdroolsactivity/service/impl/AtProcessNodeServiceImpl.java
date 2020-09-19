package com.minn.springbootdroolsactivity.service.impl;

import com.minn.springbootdroolsactivity.entity.AtProcessNode;
import com.minn.springbootdroolsactivity.mapper.AtProcessNodeMapper;
import com.minn.springbootdroolsactivity.service.AtProcessNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AtProcessNodeServiceImpl implements AtProcessNodeService {
    @Autowired
    AtProcessNodeMapper atProcessNodeMapper;

    @Override
    public List<AtProcessNode> selectRuslt(String templateId, String processNodeType) {
        return atProcessNodeMapper.selectRuslt(templateId,processNodeType);
    }

    @Override
    public List<AtProcessNode> selectNodeId(String templateId) {
        return atProcessNodeMapper.selectNodeId(templateId);
    }
}
