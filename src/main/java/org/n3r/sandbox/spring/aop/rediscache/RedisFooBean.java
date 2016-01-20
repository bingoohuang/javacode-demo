package org.n3r.sandbox.spring.aop.rediscache;

import org.springframework.stereotype.Component;

@Component
public class RedisFooBean {
    @RedisCacheEnabled(expirationMillis = 90000)
    public String getAccessToken(String tid) {
        return tid + ":" + System.currentTimeMillis();
    }
}
