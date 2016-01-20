package org.n3r.sandbox.spring.aop.rediscache;

import com.github.bingoohuang.utils.redis.Redis;
import com.github.bingoohuang.utils.redis.RedisConfig;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class RedisCacheEnabledSpringConfig {
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }

    @Bean
    public RedisCacheEnabledMethodInterceptor redisCacheEnabledMethodInterceptor() {
        RedisConfig redisConfig = new RedisConfig();
        redisConfig.setHost("127.0.0.1");
        redisConfig.setPort(6379);
        Redis redis = new Redis(redisConfig);

        return new RedisCacheEnabledMethodInterceptor(redis);
    }
}
