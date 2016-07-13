package com.nithind.automium.test;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Nithin Devang on 12-07-2016.
 */


@XmlRootElement(name = "pre")
@XmlAccessorType(XmlAccessType.FIELD)
public class PreReqTestCase {
    @XmlAttribute(name = "id")
    private String id;

    @XmlAttribute(name = "class")
    private String testClassName;

    //Getters and Setters


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
}