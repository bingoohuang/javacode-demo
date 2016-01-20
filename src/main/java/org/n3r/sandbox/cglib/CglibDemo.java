package org.n3r.sandbox.cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CglibDemo {
    public static void main(String[] args) {
        demo2();
    }

    private static void demo2() {
        Enhancer enhancer = new Enhancer();
        SampleClass sampleClass = new SampleClass();
        enhancer.setSuperclass(SampleClass.class);
        enhancer.setCallback(new SampleClassInterceptor(sampleClass));

        SampleClass proxy = (SampleClass) enhancer.create(
                new Class[]{SampleClass.class},
                new Object[]{sampleClass});

        proxy.test(null);
    }

    public static class SampleClassInterceptor implements MethodInterceptor {
        private final SampleClass sampleClass;

        public SampleClassInterceptor(SampleClass sampleClass) {
            this.sampleClass = sampleClass;
        }

        @Override
        public Object intercept(Object o, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            System.out.println(method.getName());
            return method.invoke(sampleClass, args);
        }
    }

    private static void demo1() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(SampleClass.class);
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy)
                    throws Throwable {
                if (method.getDeclaringClass() != Object.class && method.getReturnType() == String.class) {
                    return "Hello cglib!";
                } else {
                    return proxy.invokeSuper(obj, args);
                }
            }
        });
        SampleClass proxy = (SampleClass) enhancer.create();
        System.out.println(proxy.test(null)); // "Hello cglib!"
        System.out.println(proxy.toString()); // org.n3r.sandbox.cglib.CglibDemo$SampleClass$$EnhancerByCGLIB$$7db1b140@6013a567

        int hashCode = proxy.hashCode();// Does not throw an exception or result in an endless loop.
        System.out.println(hashCode);
    }

    public static class SampleClass {
        public String test(String input) {
            return "Hello world!";
        }
    }
}
