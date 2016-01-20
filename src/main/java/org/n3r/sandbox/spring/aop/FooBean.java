package org.n3r.sandbox.spring.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FooBean {
    @Autowired
    BarBean barBean;

    Logger log = LoggerFactory.getLogger(FooBean.class);

    @ProfileExecution
    public void foo() {
        log.info("Executing method FooBean.foo.");
        barBean.bar(); // will be intercepted
        bar(); // will not interccepted
    }

    @ProfileExecution
    public void bar() {
        log.info("Executing method FooBean.bar.");
    }

    @DiamondCaching
    public String time(String tag) {
        return tag + ":" + System.currentTimeMillis();
    }

    @DiamondCaching
    public String time2() {
        return "time2:" + System.currentTimeMillis();
    }

}