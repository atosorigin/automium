package com.nithind.automium.utils;

import java.util.Properties;

/**
 * Created by Nithin Devang on 26-11-2015.
 */
public class PropertyConfig {
    private static Properties properties;

    public static void load(Properties properties){
        PropertyConfig.properties = properties;
    }

    public static String getProperty(String propertyName){
        return properties.getProperty(propertyName);
    }

}
