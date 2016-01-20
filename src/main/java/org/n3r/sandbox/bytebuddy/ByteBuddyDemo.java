package org.n3r.sandbox.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.MethodDelegation;

import java.util.Comparator;

import static net.bytebuddy.matcher.ElementMatchers.*;

public class ByteBuddyDemo {
    public static interface Bingoo {
    }

    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        ByteBuddyDemo byteBuddyDemo = new ByteBuddyDemo();
        byteBuddyDemo.bingoo();
    }

    public void hello() throws IllegalAccessException, InstantiationException {

        Class<?> dynamicType = new ByteBuddy()
                .subclass(Bingoo.class)
                        // .name(namingStrategy)
                .method(named("toString")).intercept(FixedValue.value("Hello World!"))
                .make()
                .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded();
        Object instance = dynamicType.newInstance();
        System.out.println(instance.toString()); // "Hello World!"
        System.out.println(dynamicType.getName());
        //assertThat(dynamicType.newInstance().toString(), is("Hello World!"));
    }

    public void comparator() throws IllegalAccessException, InstantiationException {
        Class<? extends Comparator> dynamicType = new ByteBuddy()
                .subclass(Comparator.class)
                .method(named("compare"))
                .intercept(MethodDelegation.to(new ComparisonInterceptor()))
                .make()
                .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded();

        Comparator comparator = dynamicType.newInstance();
        int compare = comparator.compare(3, 1);
        System.out.println(compare); // "Hello World!"
        System.out.println(dynamicType.getName());
        // assertThat(dynamicType.newInstance().compare(3, 1), is(2));
    }


    public void bingoo() throws IllegalAccessException, InstantiationException {
        Class<? extends BingooVisitor> dynamicType = new ByteBuddy()
                .subclass(BingooVisitor.class)
                .method(any())
                .intercept(MethodDelegation.to(new GeneralInterceptor()))
                .make()
                .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded();

        BingooVisitor bingooVisitor = dynamicType.newInstance();
        System.out.println(dynamicType.getName());
        String s = bingooVisitor.sayHello();
        System.out.println(s);
        System.out.println(bingooVisitor.sayWorld());

    }

}
