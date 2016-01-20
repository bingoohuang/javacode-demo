package org.n3r.sandbox.spring.aop.rediscache;

import com.github.bingoohuang.utils.codec.Json;
import com.github.bingoohuang.utils.redis.Redis;
import com.google.common.base.Joiner;
import net.jodah.expiringmap.ExpiringMap;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static net.jodah.expiringmap.ExpiringMap.ExpirationPolicy.CREATED;

public class RedisCacheEnabledMethodInterceptor implements MethodInterceptor {
    final Redis redis;
    final ExpiringMap<String, Object> cache = ExpiringMap.builder()
            .variableExpiration()
            .build();

    final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public RedisCacheEnabledMethodInterceptor(Redis redis) {
        this.redis = redis;
    }

    @Override
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        String prefix = getNamePrefix(invocation);
        String arguments = getArguments(invocation);

        Object value = cache.get(getCacheKey(prefix, arguments));
        if (value != null) {
            tryRefreshAhead(prefix, arguments, invocation);
        } else {
            value = refreshRedis(prefix, arguments, invocation);
        }

        return value;
    }

    private String getCacheKey(String prefix, String arguments) {
        return prefix + ":" + arguments;
    }

    private String getArguments(MethodInvocation invocation) {
        Joiner joiner = Joiner.on(':').useForNull("null");
        return joiner.join(invocation.getArguments());
    }

    private void tryRefreshAhead(final String prefix, final String arguments,
                                 final MethodInvocation invocation) {
        RedisCacheEnabled redisCacheAnn = invocation.getMethod().getAnnotation(RedisCacheEnabled.class);
        long expectedExpiration = cache.getExpectedExpiration(getCacheKey(prefix, arguments));
        if (expectedExpiration > redisCacheAnn.aheadMillis()) return;

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                refreshRedis(prefix, arguments, invocation);
            }
        });
    }

    private Object refreshRedis(String prefix, String arguments, MethodInvocation invocation) {
        String redisKey = prefix + ":value:" + arguments;
        RedisCacheEnabled redisCacheAnn = invocation.getMethod().getAnnotation(RedisCacheEnabled.class);
        Long ttl = redis.ttl(redisKey);
        if (ttl * 1000 > redisCacheAnn.aheadMillis()) {
            return readRedisAndCacheToLocal(getCacheKey(prefix, arguments), redisKey);
        } else {
            return tryRefreshOrReadRedis(prefix, arguments, invocation);
        }
    }

    private String getNamePrefix(MethodInvocation invocation) {
        Method method = invocation.getMethod();
        String methodName = method.getName();
        String simpleName = method.getDeclaringClass().getSimpleName() + ":";
        return methodName.startsWith("get")
                ? simpleName + methodName.substring(3)
                : simpleName + methodName;
    }

    private Object tryRefreshOrReadRedis(String prefix, String arguments, MethodInvocation invocation) {
        String redisLockKey = prefix + ":lock:" + arguments;

        boolean lock = false;
        try {
            lock = redis.tryLock(redisLockKey);
            if (lock) return invokeMethodAndSaveCache(prefix, arguments, invocation);
        } finally {
            if (lock) redis.del(redisLockKey);
        }

        return waitLockReleaseAndReadRedis(prefix, arguments);
    }

    private Object invokeMethodAndSaveCache(String prefix, String arguments,
                                            MethodInvocation invocation) {
        Object value;
        try {
            value = invocation.proceed();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }

        RedisCacheEnabled redisCacheAnn = invocation.getMethod().getAnnotation(RedisCacheEnabled.class);
        long millis = redisCacheAnn.expirationMillis();
        String redisValueKey = prefix + ":value:" + arguments;
        redis.setex(redisValueKey, Json.jsonWithType(value), millis, TimeUnit.MILLISECONDS);
        cache.put(getCacheKey(prefix, arguments), value, CREATED, millis, TimeUnit.MILLISECONDS);
        return value;
    }

    private Object waitLockReleaseAndReadRedis(String prefix, String arguments) {
        String redisLockKey = prefix + ":lock:" + arguments;
        String redisValueKey = prefix + ":value:" + arguments;
        while (true) {
            sleep(100);
            boolean locked = redis.isLocked(redisLockKey);
            if (locked) continue;

            return readRedisAndCacheToLocal(getCacheKey(prefix, arguments), redisValueKey);
        }
    }

    private Object readRedisAndCacheToLocal(String cacheKey, String redisKey) {
        String redisValue = redis.get(redisKey);
        Object value = Json.unJsonWithType(redisValue);

        Long ttl = redis.ttl(redisKey);
        cache.put(cacheKey, value, CREATED, ttl, TimeUnit.SECONDS);

        return value;
    }

    private void sleep(int milis) {
        try {
            TimeUnit.MILLISECONDS.sleep(milis);
        } catch (InterruptedException e) {
            // ignore
        }
    }
}