package org.n3r.sandbox.spring.aop;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ProfilingAdvisorMain {
    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring/springAop.xml");
        FooBean fooBean = ctx.getBean(FooBean.class);
        fooBean.foo();
    }
}
