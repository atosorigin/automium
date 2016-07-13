package com.nithind.automium;


import com.nithind.automium.utils.PropertyConfig;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nithin Devang on 25-02-2015.
 */
public class Reporter {

    private static Map<String, String> successMap = new HashMap<String, String>();
    private static Map<String, String> failureMap = new HashMap<String, String>();
    private static Map<String, String> warningMap = new HashMap<String, String>();
    private static StringBuilder tableHtml = new StringBuilder();

    private static Map<String, AutomiumLog> classLogs = new HashMap<String, AutomiumLog>();

    private static String fileName = null;

    public Reporter() {
    }

    public static void success(String message, String optionalString) {
        log(message, optionalString, "success");
    }

    public static void fail(String message, String optionalMessage) {
        log(message, optionalMessage, "failure");
    }

    public static void warn(String message, String optionalMessage) {
        log(message, optionalMessage, "warning");
    }

    public static boolean log(String message, String optionalMessage, String type) {
        if (type.equalsIgnoreCase("success")) {
            successMap.put(message, optionalMessage);
            String href = message.replace(".", "_");
            tableHtml.append("<tr class=\"Success\">" +
                    "<td> <a href=\"#"+href+"\">" + message + "</a></td>\n" +
                    "<td>" + optionalMessage + "</td>\n" +
                    "<td>" + type + "</td>\n" +
                    "  </tr>");
        }
        if (type.equalsIgnoreCase("failure")) {
            String href = message.replace(".", "_");
            failureMap.put(message, optionalMessage);
            tableHtml.append("<tr class=\"Failure\"> \n" +
                    "<td> <a href=\"#"+href+"\">" + message + "</a></td>\n" +
                    "<td>" + optionalMessage + "</td>\n" +
                    "<td>" + type + "</td>\n" +
                    "  </tr>");
        }
        if (type.equalsIgnoreCase("warning")) {
            String href = message.replace(".", "_");
            warningMap.put(message, optionalMessage);
            tableHtml.append("<tr class=\"Warning\"> \n" +
                    "<td> <a href=\"#"+href+"\">" + message + "</a></td>\n" +
                    "<td>" + optionalMessage + "</td>\n" +
                    "<td>" + type + "</td>\n" +
                    "  </tr>");
        }
        return true;

    }

    public static void produceResult() {
        VelocityEngine ve = new VelocityEngine();
        //ve.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, PropertyConfig.getProperty("velocity.template.path"));

        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();
        Template t = ve.getTemplate("chart.vm");
        VelocityContext context = new VelocityContext();
        context.put("success", successMap.size());
        context.put("failure", failureMap.size());
        context.put("warning", warningMap.size());
        context.put("resultTable", tableHtml);
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, AutomiumLog> entry : Reporter.classLogs.entrySet())
        {
            sb.append("<div class=\"test_result_holder\" id=\""+entry.getKey().replace(".","_")+"\">");
            sb.append("<a href=\"#top\">TOP</a>");
            sb.append("<h3>"+entry.getKey()+"</h3>");
            sb.append("<h3>"+entry.getValue().getResult()+"</h3>");
            sb.append("</div>");
        }
        context.put("classLogsString", sb.toString());
        StringWriter writer = new StringWriter();
        t.merge(context, writer);
        write(writer.toString());
        System.out.println(writer.toString());
    }

    public static void write(String html) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy___HHmm_ss");
            String timeValue = simpleDateFormat.format(new Date());
            fileName = "result".concat(timeValue).concat(".html");
            File file = new File(PropertyConfig.getProperty("final.test.result.path").concat(fileName));
            file.getParentFile().mkdirs();
            FileWriter writer = new FileWriter(file);
            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(html);
            bw.close();

            System.out.println("Done");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setClassLogs(AutomiumLog classLogs) {
        Reporter.classLogs.put(classLogs.getClassName(), classLogs);
    }
}
