package com.nithind.automium.reporters;


import com.nithind.automium.AutomiumLog;
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
        HtmlReporter.write(getHtmlReport());
        try {
            String threshhold = PropertyConfig.getProperty("slack.result.post.threshold");
            if (null == threshhold) {
                threshhold = "WARN";
            }
            if (threshhold.equalsIgnoreCase("FAIL") && !(failureMap.size()>0)) {
                return;
            }
            else if (threshhold.equalsIgnoreCase("WARN")) {
                if (!((failureMap.size()>0) || (warningMap.size()>0))) {
                    return;
                }
            }
            SlackReporter.updateSlack(getSlackReport());
        } catch (Exception e) {
            System.out.print("Unable to connect to slack :(. Check proxy configuration");
        }
    }

    private static String getSlackReport() {
        String colorCode = "#36a64f"; //Green success
        String preText = "All Execution Successful.";
        String attachmentValue = "All execution successful. Successful testcase count "+successMap.size();
        if(failureMap.size()>0) {
            colorCode = "#d00000"; //red failure
            preText = "GCS Automatiom found some test failures.";
            attachmentValue = failureMap.size()+" failure(s), " +warningMap.size()+" warning(s). "+successMap.size() +" successful";
        } else if (warningMap.size()>0) {
            colorCode = "#edb431"; //amber warning
            attachmentValue = warningMap.size()+" warning(s), 0 failures. "+successMap.size() +" successful";
            preText = "GCS Automatiom found some test warnings.";
        }


        String failureMessage = "";
        if (failureMap.size()>0) {
            failureMessage = failureMessage +"{\n" +
                    "         \"color\":\"#d00000\",\n" +
                    "         \"fields\":[\n" +
                    "            {\n" +
                    "               \"title\":\"FAILURE\",\n" +
                    "               \"value\":\""+failureMap.size()+"\",\n" +
                    "               \"short\":true\n" +
                    "            }\n" +
                    "         ]\n" +
                    "      }";
        }
        String warningMessage = "";
        if (warningMap.size()>0) {
            if (failureMap.size()>0) {
                warningMessage = warningMessage + ",";
            }

            warningMessage= warningMessage+"{\n" +
                    "         \"color\":\"#edb431\",\n" +
                    "         \"fields\":[\n" +
                    "            {\n" +
                    "               \"title\":\"WARNING\",\n" +
                    "               \"value\":\""+warningMap.size()+"\",\n" +
                    "               \"short\":true\n" +
                    "            }\n" +
                    "         ]\n" +
                    "      }";
        }
        String successMessage = "";
        if (successMap.size()>0) {
            if (failureMap.size()>0 || warningMap.size()>0) {
                successMessage = successMessage + ",";
            }
            successMessage = successMessage + "{\n" +
                    "         \"color\":\"#36a64f\",\n" +
                    "         \"fields\":[\n" +
                    "            {\n" +
                    "               \"title\":\"SUCCESS\",\n" +
                    "               \"value\":\""+successMap.size()+"\",\n" +
                    "               \"short\":true\n" +
                    "            }\n" +
                    "         ]\n" +
                    "      }";
        }

        String message = "{\n" +
                "\"username\":\"GCS Automation Result\",\n" +
                "   \"attachments\":[\n" +
                "\t  "+failureMessage+"\n" +
                "\t  "+warningMessage+"\n" +
                "\t "+successMessage+" \n" +
                "   ]\n" +
                "}";
        return message;

    }

    private static String getHtmlReport() {
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
        return writer.toString();

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
