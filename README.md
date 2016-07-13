# automium
Automium is an automation testing framework built on top of Selenium. Test cases are modularized to classes and execution will be taken care by framework. Test case execution order can be easily shuffled and merged.


#use cases will be updated.

#Steps.
1. Clone/download this code and build using mvn clean install command so that jars are available in .m2. Currently the jars are not available in nexus.
2. Create a new maven project for automation testing of your project and add the following dependency.
	<dependency>
            <groupId>com.nithind.automium</groupId>
            <artifactId>automium</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
3.  create main class method and initialize test files. also set properties 
		//todo need to be updated.
		
4.Create test case for each use case example Login.java for login functionality testing. impliment com.nithind.automium.TestCase and overide methods.
			eg.
```java
			public class Login implements TestCase {
				AutomiumLog automiumLog = new AutomiumLog(this); //Initiate automiumLog
				WebDriver driver;
				@Override
				public AutomiumLog run() {
				}
			}```

