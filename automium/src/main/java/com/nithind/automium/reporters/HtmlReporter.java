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
public class HtmlReporter {

    private static String fileName = null;

    public HtmlReporter() {
    }

    public static void write(String message) {
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
            bw.write(message);
            bw.close();

            System.out.println("Done");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
