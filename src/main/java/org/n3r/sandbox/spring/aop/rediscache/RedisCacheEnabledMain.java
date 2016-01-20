package org.n3r.sandbox.spring.aop.rediscache;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class RedisCacheEnabledMain {
    public static void main(String[] args) throws InterruptedException {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(RedisCacheEnabledSpringConfig.class);
        RedisFooBean fooBean = ctx.getBean(RedisFooBean.class);

        while (true) {
            System.out.println(fooBean.getAccessToken("bingoo"));
            System.out.println(fooBean.getAccessToken("huangg"));
            Thread.sleep(3000);
        }
    }
}
