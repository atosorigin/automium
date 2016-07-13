package com.nithind.automium;

import com.nithind.automium.exceptions.DuplicateIdException;
import com.nithind.automium.exceptions.PreExecutionException;
import com.nithind.automium.test.PreReqTestCase;
import com.nithind.automium.test.XMLTestCase;
import com.nithind.automium.test.XMLTestCases;
import com.nithind.automium.constants.ExecutionStatus;
import com.nithind.automium.exceptions.NotAutomiumTestCaseException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by a592282.
 */

public class AutomiumExecutor {

    /*private static List<XMLTestCase> executedTestCase = new ArrayList<XMLTestCase>();*/
    private static List<String> executedTestCase = new ArrayList<String>();

  /*  @Autowired
    Runner runner;*/


    public static void main(String[] args) throws JAXBException {
        XMLTestCases testCases = new XMLTestCases();
        testCases.setTestCases(new ArrayList<XMLTestCase>());
        //Create two testCases
        XMLTestCase testCase1 = new XMLTestCase();
        testCase1.setId("1");
        testCase1.setExecutionOrder(1);
        testCase1.setTestClassName("com.atos.gcs.testcase1");

        XMLTestCase testCase2 = new XMLTestCase();
        testCase2.setTestClassName("com.atos.gcs.login");
        testCase2.setId("1");
        testCase2.setExecutionOrder(2);
        List<PreReqTestCase> testcaselist = new ArrayList<PreReqTestCase>();

        PreReqTestCase pre = new PreReqTestCase();
        pre.setId("1");
        pre.setTestClassName("test class name");
        testcaselist.add(pre);

        PreReqTestCase pre2 = new PreReqTestCase();
        pre2.setId("2");
        pre2.setTestClassName("test class name 2");
        testcaselist.add(pre2);

        //testCase2.setNotPreTestCases(testcaselist);
        testCase2.setPreTestCases(testcaselist);

        //Add the testCases in list
        testCases.getTestCases().add(testCase1);
        testCases.getTestCases().add(testCase2);
        JAXBContext jaxbContext = JAXBContext.newInstance(XMLTestCases.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        //Marshal the testCases list in console
        jaxbMarshaller.marshal(testCases, System.out);

        //Marshal the testCases list in file
        jaxbMarshaller.marshal(testCases, new File("c:/temp/testCases.xml"));
    }

    public void run(File xmlPath) throws Exception {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(XMLTestCases.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            XMLTestCases xmlTestCases = (XMLTestCases) jaxbUnmarshaller.unmarshal(xmlPath);
            List<XMLTestCase> xmlTestCaseList = xmlTestCases.getTestCases();

            if(hasDuplicateId(xmlTestCaseList)) {
                throw new DuplicateIdException();
            }

            for (XMLTestCase xmlTestCase : xmlTestCaseList) {
                if(xmlTestCase.getPreTestCases() !=  null) {
                    checkPreMandatory(xmlTestCase.getPreTestCases());
                }

                Class<?> clazz = Class.forName(xmlTestCase.getTestClassName());
                Constructor<?> ctor = clazz.getConstructor();
                Object object = ctor.newInstance(new Object[]{});

                TestCase testCase = null;
                if (object instanceof TestCase) {
                    testCase = (TestCase) object;
                } else {
                    throw new NotAutomiumTestCaseException();
                }

                executeTestCases(testCase);
                executedTestCase.add(xmlTestCase.getId());
            }
            Reporter.produceResult();

        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }

    private boolean checkPreMandatory(List<PreReqTestCase> preList) throws PreExecutionException {
        for (PreReqTestCase pre : preList) {
                if (!executedTestCase.contains(pre.getId())) {
                    throw new PreExecutionException("for Id "+pre.getId());
                }
        }
        return true;
    }


    private boolean hasDuplicateId(List<XMLTestCase> xmlTestCaseList) {
        List<String> idList = new ArrayList<String>();
        for (XMLTestCase xmlTestCase : xmlTestCaseList) {
            if (xmlTestCase.getId() != null) {
                if (idList.contains(xmlTestCase.getId())) {
                    return true;
                }
                idList.add(xmlTestCase.getId());
            }
        }
        return false;
    }

    private void executeTestCases(TestCase testCase) {
        AutomiumLog autoLog = testCase.run();

        if (autoLog.getExecutionStatus() == ExecutionStatus.SUCCESS) {
            Reporter.success(testCase.getClass().getCanonicalName(), "Yes");
        } else if (autoLog.getExecutionStatus() == ExecutionStatus.FAIL) {
            Reporter.fail(testCase.getClass().getCanonicalName(), "");
        } else if (autoLog.getExecutionStatus() == ExecutionStatus.WARN) {
            Reporter.warn(testCase.getClass().getCanonicalName(), "");
        }
        if (autoLog.getExecutionStatus() == ExecutionStatus.NOLOGS) {
            Reporter.warn(testCase.getClass().getCanonicalName(), "No Log Reports Found In The Execution Returned");
        }
        if (autoLog.getExecutionStatus() != ExecutionStatus.NOLOGS) {

            Reporter.setClassLogs(autoLog);
        }
    }
}