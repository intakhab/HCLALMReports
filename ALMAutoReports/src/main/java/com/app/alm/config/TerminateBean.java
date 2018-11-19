package com.app.alm.config;


import javax.annotation.PreDestroy;
/**
 * 
 * @author intakhabalam.s@hcl.com
 * 
 */
public class TerminateBean {
   /**
    * @see PreDestroy
    * @throws Exception exception
    */
    @PreDestroy
    public void onDestroy() throws Exception {
        
    }
}