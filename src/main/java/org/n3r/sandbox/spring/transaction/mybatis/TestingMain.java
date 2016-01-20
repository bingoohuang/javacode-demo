package org.n3r.sandbox.spring.transaction.mybatis;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestingMain {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext ctx;
        ctx = new ClassPathXmlApplicationContext("spring/springMyBatis.xml");
        TestingService bean = ctx.getBean(TestingService.class);
        bean.insert();
    }
}
