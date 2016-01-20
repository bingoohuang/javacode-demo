package org.n3r.sandbox.bytebuddy;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;

import java.lang.reflect.Method;

public class GeneralInterceptor {
    @RuntimeType
    public Object intercept(@AllArguments Object[] allArguments,
                            @Origin Method method) {
        // intercept any method of any signature
        Class<?> returnType = method.getReturnType();
        if (returnType == String.class) return "bingoo";
        else if (returnType == int.class) return 2048;
        return null;
    }
}
