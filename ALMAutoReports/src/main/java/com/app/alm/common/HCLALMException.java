package com.app.alm.common;

/***
 * 
 * @author intakhabalam.s@hcl.com
 * @see Exception 
 */
public class HCLALMException extends Exception {
	private static final long serialVersionUID = 1L;
    /**
     * 
     * @param message {@link String}
     */
	public HCLALMException(String message) {
		super(message);
	}

}
