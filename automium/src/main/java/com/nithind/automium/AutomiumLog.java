package com.nithind.automium;


import com.nithind.automium.constants.ExecutionStatus;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nithin Devang on 25-02-2015.
 */
public class AutomiumLog {

    String className;

    private Map<String, String> successMap = new HashMap<String, String>();
    private Map<String, String> failureMap = new HashMap<String, String>();
    private Map<String, String> warningMap = new HashMap<String, String>();
    private StringBuilder tableHtml = new StringBuilder();

    private AutomiumLog() {
    }

    public  AutomiumLog(TestCase o) {
        className = o.getClass().getCanonicalName();
    }

    public String getClassName() {
        return className;
    }

    public void success(String message, String optionalString) {
        log(message, optionalString, "success");
    }

    public void fail(String message, String optionalMessage) {
        log(message, optionalMessage, "failure");
    }

    public void warn(String message, String optionalMessage) {
        log(message, optionalMessage, "warning");
    }

    public void success(String message) {
        log(message, "", "success");
    }

    public void fail(String message) {
        log(message, "", "failure");
    }

    public void warn(String message) {
        log(message, "", "warning");
    }

    public boolean log(String message, String optionalMessage, String type) {
        if (type.equalsIgnoreCase("success")) {
            successMap.put(message, optionalMessage);
            tableHtml.append("<tr class=\"Success\">" +
                    "<td>" + message + "</td>\n" +
                    "<td>" + optionalMessage + "</td>\n" +
                    "<td>" + type + "</td>\n" +
                    "  </tr>");
        }
        if (type.equalsIgnoreCase("failure")) {
            failureMap.put(message, optionalMessage);
            tableHtml.append("<tr class=\"Failure\"> \n" +
                    "<td>" + message + "</td>\n" +
                    "<td>" + optionalMessage + "</td>\n" +
                    "<td>" + type + "</td>\n" +
                    "  </tr>");
        }
        if (type.equalsIgnoreCase("warning")) {
            warningMap.put(message, optionalMessage);
            tableHtml.append("<tr class=\"Warning\"> \n" +
                    "<td>" + message + "</td>\n" +
                    "<td>" + optionalMessage + "</td>\n" +
                    "<td>" + type + "</td>\n" +
                    "  </tr>");
        }
        return true;

    }

    public String getResult() {
        VelocityEngine ve = new VelocityEngine();
        //ve.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, PropertyConfig.getProperty("velocity.template.path"));

        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();
        Template t = ve.getTemplate("classlogs.vm");
        VelocityContext context = new VelocityContext();
        String classNameTemp = className.replace(".", "_");
        context.put("classname", classNameTemp);
        context.put("success", successMap.size());
        context.put("failure", failureMap.size());
        context.put("warning", warningMap.size());
        context.put("resultTable", tableHtml);
        StringWriter writer = new StringWriter();
        t.merge(context, writer);
        return writer.toString();
    }

    public ExecutionStatus getExecutionStatus() {
        if(failureMap.size() > 0) {
            return ExecutionStatus.FAIL;
        } else if(warningMap.size() > 0) {
            return ExecutionStatus.WARN;
        } else if(successMap.size() > 0) {
            return ExecutionStatus.SUCCESS;
        }
        //nothing found in log
        return ExecutionStatus.NOLOGS;
    }

    public int getSuccessCount () {
        return successMap.size();
    }
    public int getWarnCount () {
        return warningMap.size();
    }
    public int getErrorCount () {
        return failureMap.size();
    }
}
