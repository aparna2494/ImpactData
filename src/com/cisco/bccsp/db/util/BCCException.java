package com.cisco.bccsp.db.util;

@SuppressWarnings("serial")
public class BCCException extends Exception {
	
	private String code;

    public BCCException(String code, String message) {
        super(message);
    	this.code = code;
    }

    public String getCode() {
    	return code;
    }
}