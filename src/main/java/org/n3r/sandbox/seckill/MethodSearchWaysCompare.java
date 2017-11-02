package org.n3r.sandbox.seckill;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.lang.reflect.Method;

public class MethodSearchWaysCompare {
    public static boolean findMethodInClassByException(Class<?> aClass, Method method) {
        try {
            aClass.getMethod(method.getName(), method.getParameterTypes());
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    public static boolean findMethodInClassBySearch(Class<?> aClass, Method method) {
        for (Method aMethod : aClass.getMethods()) {
            if (aMethod.getName().equals(method.getName())
                    && sameParameterTypes(aMethod.getParameterTypes(), method.getParameterTypes())) {
                return true;
            }
        }

        return false;
    }


    private static boolean sameParameterTypes(Class<?>[] parameterTypes1, Class<?>[] parameterTypes2) {
        if (parameterTypes1.length != parameterTypes2.length) return false;

        for (int i = 0; i < parameterTypes1.length; ++i) {
            if (parameterTypes1[i] != parameterTypes2[i]) return false;
        }

        return true;
    }

    @Benchmark
    public void methodNotFoundException() throws Exception {
        Class<?> aClass = Object.class;
        Method aMethod = MethodSearchWaysCompare.class.getMethod("methodNotFoundException");

        findMethodInClassByException(aClass, aMethod);
    }

    @Benchmark
    public void methodsSearch() throws Exception {
        Class<?> aClass = Object.class;
        Method aMethod = MethodSearchWaysCompare.class.getMethod("methodNotFoundException");

        findMethodInClassBySearch(aClass, aMethod);
    }


    public static void main(String[] args) throws RunnerException, IOException {
        Options opt = new OptionsBuilder()
                .include(MethodSearchWaysCompare.class.getSimpleName())
                .warmupIterations(1)
                .threads(1)
                .measurementIterations(10)
                .forks(0)
                .build();

        new Runner(opt).run();
    }
}
