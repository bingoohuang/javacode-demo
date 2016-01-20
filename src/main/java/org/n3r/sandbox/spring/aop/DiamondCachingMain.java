package org.n3r.sandbox.spring.aop;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class DiamondCachingMain {
    public static void main(String[] args) throws InterruptedException {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(DiamondCachingSpringConfig.class);
        FooBean fooBean = ctx.getBean(FooBean.class);

        while (true) {
            System.out.println(fooBean.time("bingoo"));
            System.out.println(fooBean.time2());
            Thread.sleep(3000);
        }
    }
}
