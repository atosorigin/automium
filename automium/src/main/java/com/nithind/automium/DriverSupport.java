package com.nithind.automium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by Nithin Devang on 12-07-2016.
 */
public class DriverSupport {
    private static WebDriver driver;

    private DriverSupport() {

    }

    public static WebDriver getFirefoxDriver() {
        if (null == driver) {

            driver = new FirefoxDriver();
        }

        return driver;
    }

    public static WebDriver getChromeDriver() {
        if (null == driver) {

            driver = new ChromeDriver();
        }

        return driver;
    }



    public void setNewDriver(WebDriver driver) {
        this.driver = driver;
    }

    public DriverSupport(String pathToFireFox) {
        //this.driver = new FirefoxDriver(pathToFireFox);
    }
}