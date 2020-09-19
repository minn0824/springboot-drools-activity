package com.minn.springbootdroolsactivity.utils;

import com.minn.springbootdroolsactivity.model.*;
import org.activiti.bpmn.BpmnAutoLayout;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * 创建流程图工具类
 */
public class ProcessCreationUtil {

    //开始节点
    public static StartEvent createStartEvent() {
        StartEvent startEvent = new StartEvent();
        startEvent.setId("startEvent");
        startEvent.setName("开始");
        return startEvent;
    }

    //结束节点
    public static EndEvent createEndEvent() {
        EndEvent endEvent = new EndEvent();
        endEvent.setId("endEvent");
        endEvent.setName("结束");
        return endEvent;
    }

    /**
     * 自动化服务
     * 应用场景：
     * 当客户有这么一个需求：下一个任务我需要自动执行一些操作，并且这个节点不需要任何的人工干涉，也就是说这个节点是自动化的。
     * 那么，这个当前面一个经办人员把任务发送下去的时候，自然而然的下一个节点就会开始马上执行。
     *
     * 实现步骤：
     *  1.设计流程
     *  2.编写委托类
     *  3.把委托类设置到流程监听类上面
     */
    public static ServiceTask createServiceTask(ServiceTaskModel serviceTaskModel){
        ServiceTask serviceTask = new ServiceTask();
        serviceTask.setId(serviceTaskModel.getId());
        serviceTask.setName(serviceTaskModel.getName());
        serviceTask.setImplementationType(serviceTaskModel.getType());
        serviceTask.setImplementation(serviceTaskModel.getRef());  //委托类的路径
        serviceTask.setAsynchronous(false);
        serviceTask.setNotExclusive(true);
        return serviceTask;
    }

    /**
     * 创建规则，配合drools
     */
    public static BusinessRuleTask createBusinessRuleTask(BusinessRuleTaskModel businessRuleTaskModel){
        BusinessRuleTask businessRuleTask = new BusinessRuleTask();
        businessRuleTask.setId(businessRuleTaskModel.getId());
        businessRuleTask.setName(businessRuleTaskModel.getName());
        businessRuleTask.setRuleNames(businessRuleTaskModel.getRules());
        businessRuleTask.setInputVariables(businessRuleTaskModel.getInputValue());
        //businessRuleTask.setResultVariableName(businessRuleTaskModel.getOutputValue());
        //businessRuleTask.setNotExclusive(true);
        return businessRuleTask;
    }

    /**
     * 任务节点
     * @return
     */
    public static UserTask createUserTask(UserTaskModel userTaskModel) {
        UserTask userTask = new UserTask();
        userTask.setId(userTaskModel.getId());
        userTask.setName(userTaskModel.getName());
        userTask.setCandidateGroups(userTaskModel.getCandidateGroups());
        userTask.setCandidateUsers(userTaskModel.getCandidateUsers());
        userTask.setAssignee(userTaskModel.getAssignee());
        userTask.setNotExclusive(true);
        if(userTaskModel.getListener()!=null){
            List<ActivitiListener> list = new ArrayList<ActivitiListener>();
            ActivitiListener activitiListener = new ActivitiListener();
            activitiListener.setEvent("create");
            activitiListener.setImplementationType("class");
            activitiListener.setImplementation(userTaskModel.getListener());
            list.add(activitiListener);
            userTask.setTaskListeners(list);
        }
        return userTask;
    }

    //连线
    public static SequenceFlow createSequenceFlow(SequenceFlowModel sequenceFlowModel) {
        SequenceFlow sequenceFlow = new SequenceFlow();
        sequenceFlow.setId(sequenceFlowModel.getId());
        sequenceFlow.setName(sequenceFlowModel.getName());
        sequenceFlow.setSourceRef(sequenceFlowModel.getFrom());
        sequenceFlow.setTargetRef(sequenceFlowModel.getTo());
        sequenceFlow.setConditionExpression(sequenceFlowModel.getConditionExpression());
        return sequenceFlow;
    }

    //排他网关
    public static ExclusiveGateway createExclusiveGateway(String id, String name) {
        ExclusiveGateway exclusiveGateway = new ExclusiveGateway();
        exclusiveGateway.setId(id);
        exclusiveGateway.setName(name);
        return exclusiveGateway;
    }

    //并行网关
    public static ParallelGateway createParallelGateway(String id, String name){
        ParallelGateway gateway = new ParallelGateway();
        gateway.setId(id);
        gateway.setName(name);
        return gateway;
    }

    //包含网管
    public static InclusiveGateway createInclusiveGateway(InclusiveGatewayModel inclusiveGatewayModel){
        InclusiveGateway inclusiveGateway = new InclusiveGateway();
        inclusiveGateway.setId(inclusiveGatewayModel.getId());
        inclusiveGateway.setName(inclusiveGatewayModel.getName());
        return inclusiveGateway;
    }


    //创建生成流程图，部署
    public static void createProcess(List<ServiceTaskModel> serviceTaskModelList
            , List<BusinessRuleTaskModel> businessRuleTaskModelList, List<UserTaskModel> userTaskModelList
                , List<SequenceFlowModel> sequenceFlowModelList,List<InclusiveGatewayModel> inclusiveGatewayList, String processId,
                                     String processName,String fileUrl,String drlUrl){
        //创建bpmn模型
        BpmnModel model = new BpmnModel();
        Process process = new Process();
        model.addProcess(process);
        process.setId(processId);

        process.addFlowElement(createStartEvent());
        for(ServiceTaskModel serviceTaskModel:serviceTaskModelList){
            process.addFlowElement(createServiceTask(serviceTaskModel));
        }
        for(BusinessRuleTaskModel businessRuleTaskModel:businessRuleTaskModelList){
            process.addFlowElement(createBusinessRuleTask(businessRuleTaskModel));
        }
        for(UserTaskModel userTaskModel:userTaskModelList){
            process.addFlowElement(createUserTask(userTaskModel));
        }
        for(InclusiveGatewayModel inclusiveGatewayModel:inclusiveGatewayList){
            process.addFlowElement(createInclusiveGateway(inclusiveGatewayModel));
        }

        process.addFlowElement(createEndEvent());

        //创建连线
        for(SequenceFlowModel sequenceFlowModel : sequenceFlowModelList){
            process.addFlowElement(createSequenceFlow(sequenceFlowModel));
        }

        // 2.生成BPMN自动布局
        new BpmnAutoLayout(model).execute();

        //生成xml文件
        try {
            byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(model);
            InputStream inputStream = new ByteArrayInputStream(bpmnBytes);
            FileUtils.copyInputStreamToFile(inputStream, new File(fileUrl+processId+".xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            File rul = new File(drlUrl);
            InputStream inputStreamDrl = new FileInputStream(rul);


            //部署流程图
            DeploymentBuilder deploymentBuilder = ProcessEngines.getDefaultProcessEngine().getRepositoryService().createDeployment()
                    .addInputStream(processId+".drl",inputStreamDrl)
                    .addBpmnModel(processId+".bpmn",model)
                    .name(processName);
            Deployment deployment = deploymentBuilder.deploy();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
