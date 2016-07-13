package com.nithind.automium.exceptions;

/**
 * Created by Nithin Devang on 13-07-2016.
 */
public class DuplicateIdException extends Exception {
    public DuplicateIdException() {
        super("Duplicate Id's found for test orders");
    }

    public DuplicateIdException(String s) {
        super(s);
    }

    public DuplicateIdException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public DuplicateIdException(Throwable throwable) {
        super(throwable);
    }

    protected DuplicateIdException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
