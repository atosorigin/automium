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
4.Using global selenium web driver do all the activities. (doc http://www.seleniumhq.org/docs/03_webdriver.jsp#introducing-webdriver)

5. Full example of login class.
```java
package net.atos.gcs.testcases;

import com.nithind.automium.AutomiumLog;
import com.nithind.automium.DriverSupport;
import com.nithind.automium.TestCase;
import com.nithind.automium.utils.PropertyConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Created by a592282 on 12-07-2016.
 */
public class Login implements TestCase {

    AutomiumLog automiumLog = new AutomiumLog(this);
    WebDriver driver;


    @Override
    public AutomiumLog run() {
        driver = DriverSupport.getDriver();
        driver.get("http://localhost:8881/CardSTOP-admin/");
        invalidPassword();
        tryLogin(); //right username n password

        //DriverSupport.configure();
        return automiumLog;
        //return ExecutionStatus.SUCCESS;
    }

    public boolean login() {
        try {
            WebElement loginTestBox = driver.findElement(By.id("login"));
            loginTestBox.sendKeys(PropertyConfig.getProperty("form.login.username"));
            WebElement passwordTestBox = driver.findElement(By.id("password"));
            passwordTestBox.sendKeys(PropertyConfig.getProperty("form.login.password"));
            passwordTestBox.submit();
            if(driver.getCurrentUrl().contains("cardstop/home")) {
                automiumLog.success("Successfully logged in", "Able to determine home url");
                return true;
            }  else {
                automiumLog.fail("Unable to login", "Could not determine home page URL");
                return false;
            }
            //WebElement submitButton = driver.findElement(By.id("bthp"));
        } catch(Exception e) {
            automiumLog.fail("Unable to login", "");
            return false;
        }
    }

    public boolean invalidPassword() {
        try {
            WebElement loginTestBox = driver.findElement(By.id("login"));
            loginTestBox.sendKeys(PropertyConfig.getProperty("form.login.username"));
            WebElement passwordTestBox = driver.findElement(By.id("password"));
            passwordTestBox.sendKeys("dummy password");
            passwordTestBox.submit();
            if(!driver.getCurrentUrl().contains("cardstop/home") && driver.getCurrentUrl().contains("cardstop/login")) {
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


