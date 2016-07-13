# automium
Automium is an automation testing framework built on top of Selenium. Test cases are modularized to classes and execution will be taken care by framework. Test case execution order can be easily shuffled and merged.


#use cases will be updated.

#Steps.
1. Clone/download this code and build using mvn clean install command so that jars are available in .m2. Currently the jars are not available in nexus.

2. Create a new maven project for automation testing of your project and add the following dependency.
```
	<dependency>
            <groupId>com.nithind.automium</groupId>
            <artifactId>automium</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
```
		
3.Create test case for each use case example Login.java for login functionality testing. impliment com.nithind.automium.TestCase and overide methods.
			eg.
			
```java
			public class Login implements TestCase {
				AutomiumLog automiumLog = new AutomiumLog(this); //Initiate automiumLog
				WebDriver driver;
				@Override
				public AutomiumLog run() {
					driver = DriverSupport.getDriver(); // get global selenium web driver.
				}
			}
```
4.Using global selenium web driver do all the activities. (doc http://www.seleniumhq.org/docs/03_webdriver.jsp#introducing-webdriver).

5. automiumLog.fail(String, String), automiumLog.warn(String, String), automiumLog.success(String, String) is used to log failure, warning or success scenarios.

6. Full example of login class.
```java
package net.atos.demos.testcases;

import com.nithind.automium.AutomiumLog;
import com.nithind.automium.DriverSupport;
import com.nithind.automium.TestCase;
import com.nithind.automium.utils.PropertyConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Created by Nithin Devang on 12-07-2016.
 */
public class Login implements TestCase {

    AutomiumLog automiumLog = new AutomiumLog(this);
    WebDriver driver;


    @Override
    public AutomiumLog run() {
        driver = DriverSupport.getDriver();
        driver.get("http://localhost:8881/demo/");
        invalidPassword();
        tryLogin(); //right username n password
        return automiumLog;
    }

    public boolean login() {
        try {
            WebElement loginTestBox = driver.findElement(By.id("login"));
            loginTestBox.sendKeys("right_username");
            WebElement passwordTestBox = driver.findElement(By.id("password"));
            passwordTestBox.sendKeys("right_password");
            passwordTestBox.submit();
            if(driver.getCurrentUrl().contains("demo/home")) {
                automiumLog.success("Successfully logged in", "Able to determine home url");
                return true;
            }  else {
                automiumLog.fail("Unable to login", "Could not determine home page URL");
                return false;
            }
        } catch(Exception e) {
            automiumLog.fail("Unable to login", "");
            return false;
        }
    }

    public boolean invalidPassword() {
        try {
            WebElement loginTestBox = driver.findElement(By.id("login"));
            loginTestBox.sendKeys("right_username");
            WebElement passwordTestBox = driver.findElement(By.id("password"));
            passwordTestBox.sendKeys("wrong password");
            passwordTestBox.submit();
            if(!driver.getCurrentUrl().contains("demo/home") && driver.getCurrentUrl().contains("demo/login")) {
                automiumLog.success("Didnot login to dashboard with wrong password", "Was able to determine login URL");
            }  else {
                automiumLog.fail("Goes to login with invalid username", "Username : dummyUsername");
            }

            WebElement errLogHolder = driver.findElement(By.id("ferrorlg"));
            if(errLogHolder.getAttribute("innerHTML").contains("Authentication failed.")) {
                automiumLog.success("Authentication failed message displayed", "");
            } else {
                automiumLog.fail("Authentication failed message displayed  not displayed", "");
            }
            //WebElement submitButton = driver.findElement(By.id("bthp"));
            return true;
        } catch(Exception e) {
            automiumLog.fail("Unable to login", "");
            return false;
        }
    }
    
}

```

7. Similar way create testcase classes for various scenarios.

#Running test cases.

1. Create testcase.xml file in classpath.
    example:
```
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<testCases>
    <testCase id="login"  class="net.atos.demo.testcases.Login"/>
    <testCase id="homepage"  class="net.atos.demo.testcases.HomePage">
        <pre id="login"/>
    </testCase>
    <testCase id="logout" class="net.atos.demo.testcases.Logout">
        <pre id="login"/>
    </testCase>
</testCases>

```
2. Specify test classes in the sequential order it need to be executed.
3. pre tag tells it is mandatory to run this testcase before running the current testcase.
4. final.test.result.path should be set in system property with the path where report should be generated.
5. write a main method (or anything similar) and pass this xml file to new object of AutomiumExecutor.run(File) method.
example:
```java
public static void main(String[] args) throws Exception {
        PropertyReader.load();
        URL url = Thread.currentThread().getContextClassLoader().getResource("testcase.xml");
        File file = new File(url.toURI());
        AutomiumExecutor automiumExecutor = new AutomiumExecutor();
        automiumExecutor.run(file);
    }
```
6. This will run all the test cases sequentially.
