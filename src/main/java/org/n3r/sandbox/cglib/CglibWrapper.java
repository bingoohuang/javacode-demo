package org.n3r.sandbox.cglib;

import com.google.common.collect.ImmutableSet;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CglibWrapper {
    public static <S, W> W createWrapper(final S source, final Class<W> wrapperClass) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(wrapperClass);

        Set<Class> allInterfaces = new HashSet<Class>();
        allInterfaces.addAll(Arrays.asList(source.getClass().getInterfaces()));
        allInterfaces.addAll(Arrays.asList(wrapperClass.getInterfaces()));
        enhancer.setInterfaces(allInterfaces.toArray(new Class[allInterfaces.size()]));

//        enhancer.setInterfaces(wrapperClass.getInterfaces());
        enhancer.setCallback(new MethodInterceptor() {
            final Set<Method> wrapperClassDeclaredMethods = ImmutableSet.copyOf(wrapperClass.getDeclaredMethods());

            public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                if ("getSource".equals(method.getName()) && Wrapper.class.equals(method.getDeclaringClass())) {
                    return source;
                }

//                if (!Modifier.isAbstract(method.getModifiers())) {
//                    return methodProxy.invokeSuper(proxy, args);
//                }

                if (wrapperClassDeclaredMethods.contains(method)) {
                    return methodProxy.invokeSuper(proxy, args);
                }

                return methodProxy.invoke(source, args);
            }
        });

        return (W) enhancer.create();
    }

}
