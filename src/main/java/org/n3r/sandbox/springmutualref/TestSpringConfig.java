package org.n3r.sandbox.springmutualref;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class TestSpringConfig {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context;
        context = new AnnotationConfigApplicationContext(TestSpringConfig.class);
        ServiceA bean = context.getBean(ServiceA.class);
        bean.hello2();
    }
}
