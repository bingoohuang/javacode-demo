package org.n3r.sandbox.spring;

import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.SchedulingException;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import java.util.Map;

//@Component
public class MySchedulerFactoryBean extends SchedulerFactoryBean implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void start() throws SchedulingException {
        Map<String,JobDetail> jobDetailsMap = applicationContext.getBeansOfType(JobDetail.class);
        super.setJobDetails(jobDetailsMap.values().toArray(new JobDetail[0]));

        Map<String,Trigger> triggersMap = applicationContext.getBeansOfType(Trigger.class);
        super.setTriggers(triggersMap.values().toArray(new Trigger[0]));
        try {
            registerJobsAndTriggers();
        } catch (SchedulerException e) {
            throw new SchedulingException(e.toString());
        }


        super.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
