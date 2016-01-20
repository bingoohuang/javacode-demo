package org.n3r.sandbox.spring.aop;

import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

@Component
public class DiamondCachingMethodInterceptor implements MethodInterceptor {
    Logger log = LoggerFactory.getLogger(DiamondCachingMethodInterceptor.class);
    LoadingCache<Method, DiamondCachingHolder> methodHolderCache = CacheBuilder.newBuilder()
            .build(new CacheLoader<Method, DiamondCachingHolder>() {
                @Override
                public DiamondCachingHolder load(Method method) throws Exception {
                    DiamondCachingHolder holder = new DiamondCachingHolder();
                    holder.prepare(method);

                    return holder;
                }
            });

    @Override
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        DiamondCachingHolder holder = methodHolderCache.getUnchecked(invocation.getMethod());

        return holder.getCache(invocation, new Callable<Optional<Object>>() {
            @Override
            public Optional<Object> call() throws Exception {
                try {
                    return Optional.fromNullable(invocation.proceed());
                } catch (Throwable throwable) {
                    log.error("call method {} error", invocation.getMethod().toGenericString(), throwable);
                    return Optional.absent();
                }
            }
        });
    }
}