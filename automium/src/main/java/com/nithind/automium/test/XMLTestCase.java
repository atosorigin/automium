package com.nithind.automium.test;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by Nithin Devang on 12-07-2016.
 */


@XmlRootElement(name = "testCase")
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLTestCase {
    @XmlAttribute(name = "id")
    private String id;

    @XmlAttribute(name = "class")
    private String testClassName;

    @XmlAttribute(name = "order")
    private Integer executionOrder;

    @XmlElement(name = "pre")
    private List<PreReqTestCase> preTestCases;


    //Getters and Setters

    public Integer getExecutionOrder() {
        return executionOrder;
    }

    public void setExecutionOrder(Integer executionOrder) {
        this.executionOrder = executionOrder;
    }

    public String getTestClassName() {
        return testClassName;
    }

    public void setTestClassName(String testClassName) {
        this.testClassName = testClassName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<PreReqTestCase> getPreTestCases() {
        return preTestCases;
    }

    public void setPreTestCases(List<PreReqTestCase> preTestCases) {
        this.preTestCases = preTestCases;
    }

}