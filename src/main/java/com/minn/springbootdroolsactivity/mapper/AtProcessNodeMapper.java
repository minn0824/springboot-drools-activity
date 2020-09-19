package com.minn.springbootdroolsactivity.mapper;

import com.minn.springbootdroolsactivity.entity.AtProcessNode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AtProcessNodeMapper {
    public List<AtProcessNode> selectRuslt(String templateId, String processNodeType);

    public List<AtProcessNode> selectNodeId(String templateId);
}
