package org.n3r.sandbox.spring;

import org.quartz.SchedulerException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

// http://learningviacode.blogspot.com/2012/04/beannameaware-and-beanfactoryaware.html
//@Component
public class SimpleCar implements BeanNameAware, BeanFactoryAware {
    public Object target;

    public String describe() {
        return "Car is an empty car";
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("received the beanFactory " + beanFactory);

    }

    @Override
    public void setBeanName(String name) {
        System.out.println("the name of the bean is " + name);

    }

    public void setTarget(Object target) {
        this.target = target;
        System.out.println("target:" + target);
    }

    public static void main(String[] args) throws SchedulerException, NoSuchMethodException, ClassNotFoundException {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring*.xml");

        SimpleCar car = context.getBean("SimpleCar", SimpleCar.class);
        System.out.println(car.describe());

//        Scheduler scheduler = (Scheduler) context.getBean("scheduleFactory");
//        System.out.println(scheduler.getJobGroupNames());


    }
}
