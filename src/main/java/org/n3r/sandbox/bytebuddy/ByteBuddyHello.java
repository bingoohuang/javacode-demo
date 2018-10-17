package org.n3r.sandbox.bytebuddy;

import lombok.SneakyThrows;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.matcher.ElementMatchers;

import java.io.File;


public class ByteBuddyHello {
    @SneakyThrows
    public static void main(String[] args) {
        new ByteBuddy()
                .subclass(Object.class)
                .method(ElementMatchers.named("toString"))
                .intercept(FixedValue.value("Hello World!"))
                .make()
                .load(ByteBuddyHello.class.getClassLoader())
                .saveIn(new File("."));


    }
}
