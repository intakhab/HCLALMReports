package com.app.alm.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/***
 * 
 * @author intakhabalam.s@hcl.com
 * @see Configuration
 * @see Bean
 * @see TerminateBean
 */
@Configuration
public class ShutdownConfig {
   /***
    * @return {@link TerminateBean}
    */
    @Bean
    public TerminateBean getTerminateBean() {
        return new TerminateBean();
    }
}
