package com.nithind.automium.exceptions;

/**
 * Created by Nithin Devang on 12-07-2016.
 */
public class NotAutomiumTestCaseException extends Exception {
    public NotAutomiumTestCaseException() {
        super("Not Automium Test Case");
    }

    public NotAutomiumTestCaseException(String s) {
        super(s);
    }

    public NotAutomiumTestCaseException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public NotAutomiumTestCaseException(Throwable throwable) {
        super(throwable);
    }

    protected NotAutomiumTestCaseException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
