package org.n3r.sandbox.spring.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;


@Component
public class ProfilingMethodInterceptor implements MethodInterceptor {
    Logger log = LoggerFactory.getLogger(ProfilingMethodInterceptor.class);

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        final StopWatch stopWatch = new StopWatch(method.toGenericString());
        stopWatch.start("invocation.proceed()");

        try {
            log.info("~~~~~~~~ START METHOD {} ~~~~~~~~", method.toGenericString());
            return invocation.proceed();
        } finally {
            stopWatch.stop();
            log.info(stopWatch.prettyPrint());
            log.info("~~~~~~~~ END METHOD {} ~~~~~~~~", method.toGenericString());
        }
    }
}