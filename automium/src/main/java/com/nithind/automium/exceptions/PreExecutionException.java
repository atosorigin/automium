package com.nithind.automium.exceptions;

/**
 * Created by Nithin Devang on 13-07-2016.
 */
public class PreExecutionException extends Exception {
    public PreExecutionException() {
        super("Pre Execution condition failed.");
    }

    public PreExecutionException(String s) {
        super("Pre Execution condition failed."+s);
    }

    public PreExecutionException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public PreExecutionException(Throwable throwable) {
        super(throwable);
    }

    protected PreExecutionException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
