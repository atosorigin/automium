package com.nithind.automium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by a592282 on 12-07-2016.
 */
public class DriverSupport {
    private static WebDriver driver;

    private DriverSupport() {

    }

    public static WebDriver getDriver() {
        if (null == driver) {
            driver = new FirefoxDriver();
        }

        return driver;
    }

    public void setDriver(WebDriver driver) {
        //this.driver = driver;
    }

    public DriverSupport(String pathToFireFox) {
        //this.driver = new FirefoxDriver(pathToFireFox);
    }

    //following for testing
    public static void configure() {

        // And now use this to visit Google
        driver.get("http://www.google.com");
        // Alternatively the same thing can be done like this
        // driver.navigate().to("http://www.google.com");

        // Find the text input element by its name
        WebElement element = driver.findElement(By.name("q"));

        // Enter something to search for
        element.sendKeys("Cheese!");

        // Now submit the form. WebDriver will find the form for us from the element
        element.submit();

        // Check the title of the page
        System.out.println("Page title is: " + driver.getTitle());

    }
}