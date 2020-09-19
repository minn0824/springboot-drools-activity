package com.minn.springbootdroolsactivity;

import com.minn.springbootdroolsactivity.entity.AtProcessNode;
import com.minn.springbootdroolsactivity.model.*;
import com.minn.springbootdroolsactivity.service.AtProcessNodeService;
import com.minn.springbootdroolsactivity.utils.ProcessCreationUtil;
import com.minn.springbootdroolsactivity.utils.ProcessOperationUtil;
import com.minn.springbootdroolsactivity.utils.ProcessSelectUtil;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.InclusiveGateway;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringbootDroolsActivityApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringbootDroolsActivityApplicationTests {
    @Autowired
    AtProcessNodeService atProcessNodeService;

    public List<UserTaskModel> getUserTask(String templateId){
        List<AtProcessNode> AtProcessNodeList = atProcessNodeService.selectRuslt(templateId,"1");
        List<UserTaskModel> userTaskModelList = new ArrayList<>();
        for (AtProcessNode atProcessNode:AtProcessNodeList){
            UserTaskModel userTaskModel = new UserTaskModel();
            userTaskModel.setId(atProcessNode.getNodeId());
            userTaskModel.setName(atProcessNode.getNodeName());
            /*List list = new ArrayList();
            list.add(atProcessNode.getNodeAttribute1());
            userTaskModel.setCandidateUsers(list);*/
            userTaskModel.setAssignee(atProcessNode.getNodeAttribute1());
            userTaskModel.setListener(atProcessNode.getNodeAttribute4());
            userTaskModelList.add(userTaskModel);
        }
        return userTaskModelList;
    }

    public List<ServiceTaskModel> getServiceTask(String templateId){
        List<AtProcessNode> AtProcessNodeList = atProcessNodeService.selectRuslt(templateId,"2");
        List<ServiceTaskModel> serviceTaskModelList = new ArrayList<>();
        for (AtProcessNode atProcessNode:AtProcessNodeList){
            ServiceTaskModel serviceTaskModel = new ServiceTaskModel();
            serviceTaskModel.setId(atProcessNode.getNodeId());
            serviceTaskModel.setName(atProcessNode.getNodeName());
            serviceTaskModel.setRef(atProcessNode.getNodeAttribute1());
            serviceTaskModel.setType("class");
            serviceTaskModelList.add(serviceTaskModel);
        }
        return serviceTaskModelList;
    }

    public List<BusinessRuleTaskModel> getBusinessRuleTask(String templateId){
        List<AtProcessNode> AtProcessNodeList = atProcessNodeService.selectRuslt(templateId,"3");
        List<BusinessRuleTaskModel> businessRuleTaskModelList = new ArrayList<>();
        for (AtProcessNode atProcessNode:AtProcessNodeList){
            BusinessRuleTaskModel businessRuleTaskModel = new BusinessRuleTaskModel();
            businessRuleTaskModel.setId(atProcessNode.getNodeId());
            businessRuleTaskModel.setName(atProcessNode.getNodeName());
            String[] str = atProcessNode.getNodeAttribute2().split(",");
            List<String> list = new ArrayList();
            for (int i=0;i<str.length;i++){
                list.add(str[i].toString());
            }
            businessRuleTaskModel.setRules(list);
            businessRuleTaskModel.setOutputValue(atProcessNode.getNodeAttribute1());
            List lists = new ArrayList();
            lists.add(atProcessNode.getNodeAttribute3());
            businessRuleTaskModel.setInputValue(lists);
            businessRuleTaskModelList.add(businessRuleTaskModel);
        }
        return businessRuleTaskModelList;
    }

    public List<SequenceFlowModel> getSequenceFlow(String templateId){
        List<AtProcessNode> AtProcessNodeList = atProcessNodeService.selectRuslt(templateId,"4");
        List<SequenceFlowModel> sequenceFlowModelList = new ArrayList<>();
        for (int i=0;i<AtProcessNodeList.size();i++){
            SequenceFlowModel sequenceFlowModel = new SequenceFlowModel();
            sequenceFlowModel.setId("f"+i);
            sequenceFlowModel.setName(AtProcessNodeList.get(i).getNodeName());
            sequenceFlowModel.setFrom(AtProcessNodeList.get(i).getNodeAttribute1());
            sequenceFlowModel.setTo(AtProcessNodeList.get(i).getNodeAttribute2());
            sequenceFlowModel.setConditionExpression(AtProcessNodeList.get(i).getNodeAttribute3());
            sequenceFlowModelList.add(sequenceFlowModel);
        }
        return sequenceFlowModelList;
    }

    //包含网关
    public List<InclusiveGatewayModel> getInclusiveGateway(String templateId){
        List<AtProcessNode> AtProcessNodeList = atProcessNodeService.selectRuslt(templateId,"5");
        List<InclusiveGatewayModel> InclusiveGatewayModelList = new ArrayList<>();
        for(AtProcessNode atProcessNode:AtProcessNodeList){
            InclusiveGatewayModel inclusiveGatewayModel = new InclusiveGatewayModel();
            inclusiveGatewayModel.setId(atProcessNode.getNodeId());
            inclusiveGatewayModel.setName(atProcessNode.getNodeName());
            InclusiveGatewayModelList.add(inclusiveGatewayModel);
        }
        return InclusiveGatewayModelList;
    }

    @Test
    void tests() throws FileNotFoundException, XMLStreamException {
        BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
        XMLInputFactory factory = XMLInputFactory.newFactory();
        InputStream in = new FileInputStream("D://test.xml");
        //InputStream stream = XMLInputFactory.class.getClassLoader().getResources("test.xml");
        XMLStreamReader reader = factory.createXMLStreamReader(in);
        BpmnModel bpmnModel = bpmnXMLConverter.convertToBpmnModel(reader);

        File rul = new File("D://leave.drl");
        InputStream inputStreamDrl = new FileInputStream(rul);

        //部署流程图
        DeploymentBuilder deploymentBuilder = ProcessEngines.getDefaultProcessEngine().getRepositoryService().createDeployment()
                //.addInputStream(processId+".drl",inputStreamDrl)
                .addInputStream("test.drl",inputStreamDrl)
                .addBpmnModel("test.bpmn",bpmnModel)
                //.addInputStream("test.bpmn",inputStreamfile)
                .name("测试");
        Deployment deployment = deploymentBuilder.deploy();
    }

    @Test
    public void testStartProcessInstance(){
        /*ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //myProcess_1:1:4取自数据库中act_re_procdef表的ID
        processEngine.getRuntimeService().startProcessInstanceById("myProcess_1:1:4");//启动流程实例，通过PID
        //myProcess_1取自数据库中act_re_procdef表的KEY_
        processEngine.getRuntimeService().startProcessInstanceByKey("myProcess_1");//根据pdkey启动流程实例，默认启动最高版本的*/

        Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("leave", new Leave("张男", 2));
        List<String> list = new ArrayList<>();
        list.add("张三");
        vars.put("assigneeList",list);
        ProcessInstance processInstance = ProcessEngines.getDefaultProcessEngine()
                .getRuntimeService().startProcessInstanceById("test:1:5",vars);

        taskService.setVariable("","leave",new Leave("张男", 3));

        /**
         * 当前任务
         */
        /*Task task = ProcessEngines.getDefaultProcessEngine().getTaskService().createTaskQuery().processInstanceId(processInstance.getId()).singleResult();

        ProcessEngines.getDefaultProcessEngine().getTaskService().complete(task.getId(), vars);

        System.out.println(task.getId() + " , " + task.getName());*/
    }


    @Test
    void contextLoads() {
        ProcessCreationUtil.createProcess(getServiceTask("2"),getBusinessRuleTask("2"),
                getUserTask("2"),getSequenceFlow("2"),getInclusiveGateway("2")
                ,"leave","请假流程", "D://","D://leave.drl");
    }

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    /**
     * 启动流程，请假申请
     */
    @Test
    public void applyLeave(){
        Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("leave", new Leave("张男", 1));
        ProcessInstance processInstance = runtimeService.startProcessInstanceById("leave:1:5",vars);

        /**
         * 当前任务
         */
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();

        taskService.complete(task.getId());

        System.out.println(task.getId() + " , " + task.getName());

    }

    /**
     * 审批
     */
    @Test
    public void process(){
        /*Map map = new HashMap();
        map.put("adm","adm");*/
        ProcessOperationUtil.processApproval("7523",null);
        //ProcessOperationUtil.getAdminTask("2510");
        //taskService.complete("7503");
        //ProcessOperationUtil.setAdminTask("10001");
    }


    /**
     * 所有审批信息
     */
    @Test
    public void select(){
        List<TaskInfo> taskInfos = ProcessSelectUtil.queryHisTasks("5001");
        for(TaskInfo taskInfo :taskInfos){
            System.out.println(taskInfo);
        }
    }


    /**
     * 查询已处理的任务
     */
    @Test
    public void selectone(){
        List<TaskInfo> taskInfos = ProcessSelectUtil.queryDoneTasks("staff");
        for(TaskInfo taskInfo :taskInfos){
            System.out.println(taskInfo);
        }
    }

    /**
     *查询用户待办任务列表。
     */
    @Test
    public void selecttow(){
        List<TaskInfo> taskInfos = ProcessSelectUtil.queryToDoTasks("depmanager");
        for(TaskInfo taskInfo :taskInfos){
            System.out.println(taskInfo);
        }
    }

}
