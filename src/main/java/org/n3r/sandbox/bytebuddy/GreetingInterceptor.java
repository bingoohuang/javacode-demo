package org.n3r.sandbox.bytebuddy;

import lombok.SneakyThrows;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

import java.io.File;

public class GreetingInterceptor {
    public Object greet(Object argument) {
        return "Hello from " + argument;
    }

    @SneakyThrows
    public static void main(String[] args) {
        new ByteBuddy()
                .subclass(java.util.function.Function.class)
                .method(ElementMatchers.named("apply"))
                .intercept(MethodDelegation.to(new GreetingInterceptor()))
                .make()
                .load(GreetingInterceptor.class.getClassLoader())
                .saveIn(new File("."));
    }
}
