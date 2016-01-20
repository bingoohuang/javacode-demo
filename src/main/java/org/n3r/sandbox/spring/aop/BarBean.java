package org.n3r.sandbox.spring.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class BarBean {
    Logger log = LoggerFactory.getLogger(BarBean.class);

    @ProfileExecution
    public void bar() {
        log.info("Executing method BarBean.bar.");
    }
}
