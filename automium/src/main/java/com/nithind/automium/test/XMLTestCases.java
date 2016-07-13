package com.nithind.automium.test;

/**
 * Created by Nithin Devang on 12-07-2016.
 */
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "testCases")
@XmlAccessorType (XmlAccessType.FIELD)
public class XMLTestCases
{
    @XmlElement(name = "testCase")
    private List<XMLTestCase> testCases = null;

    public List<XMLTestCase> getTestCases() {
        return testCases;
    }

    public void setTestCases(List<XMLTestCase> testCases) {
        this.testCases = testCases;
    }
}