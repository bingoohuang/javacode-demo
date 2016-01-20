package org.n3r.sandbox.spring.aop;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Splitter;
import com.google.common.base.Throwables;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Iterables;
import org.aopalliance.intercept.MethodInvocation;
import org.n3r.diamond.client.DiamondListenerAdapter;
import org.n3r.diamond.client.DiamondManager;
import org.n3r.diamond.client.DiamondStone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class DiamondCachingHolder {
    final Cache<String, Optional<Object>> cache = CacheBuilder.newBuilder().softValues().build();
    Logger log = LoggerFactory.getLogger(DiamondCachingHolder.class);

    public void prepare(Method method) {
        DiamondCaching diamondCaching = method.getAnnotation(DiamondCaching.class);
        String group = diamondCaching.group();
        if (isBlank(group)) group = method.getDeclaringClass().getName();
        String dataId = diamondCaching.dataId();
        if (isBlank(dataId)) dataId = method.getName();

        log.debug("DiamondCaching for group={}, dataId={}", group, dataId);

        DiamondManager diamondManager = new DiamondManager(group, dataId);
        diamondManager.addDiamondListener(new DiamondListenerAdapter() {
            @Override
            public void accept(DiamondStone diamondStone) {
                String content = diamondStone.getContent();
                Splitter splitter = Splitter.on('\n').omitEmptyStrings().trimResults();
                Iterable<String> keys = splitter.split(content);
                cache.invalidateAll(keys);
                log.debug("DiamondCaching invalidateAll for {}", Iterables.toString(keys));
            }
        });
        diamondManager.getDiamond(); // ignore result, just to enable listener
    }

    public Object getCache(final MethodInvocation invocation, final Callable<Optional<Object>> callable) {
        Joiner joiner = Joiner.on(':').useForNull("null");
        String key = joiner.join(invocation.getArguments());
        if (isBlank(key)) key = "*";


        try {
            return cache.get(key, callable).orNull();
        } catch (ExecutionException e) {
            throw Throwables.propagate(e);
        }
    }
}
