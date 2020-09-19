package com.minn.springbootdroolsactivity.model;

import java.util.List;

/**
 * 规则，配合drools
 */
public class BusinessRuleTaskModel {
    private String id;
    private String name;
    //规则名称，即 drl文件中的规则名称
    private List<String> rules;
    //输入参数
    private List<String> inputValue;
    //输出参数
    private String outputValue;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getRules() {
        return rules;
    }

    public void setRules(List<String> rules) {
        this.rules = rules;
    }

    public List<String> getInputValue() {
        return inputValue;
    }

    public void setInputValue(List<String> inputValue) {
        this.inputValue = inputValue;
    }

    public String getOutputValue() {
        return outputValue;
    }

    public void setOutputValue(String outputValue) {
        this.outputValue = outputValue;
    }
}
