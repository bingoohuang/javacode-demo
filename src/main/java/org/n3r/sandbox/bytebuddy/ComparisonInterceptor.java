package org.n3r.sandbox.bytebuddy;

public class ComparisonInterceptor {
    public int intercept(Object first, Object second) {
        return first.hashCode() - second.hashCode();
    }
}
