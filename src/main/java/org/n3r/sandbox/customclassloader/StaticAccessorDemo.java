package org.n3r.sandbox.customclassloader;

public class StaticAccessorDemo {
    /**
     * This main method shows what happens when we load two
     * classes with two different ClassLoader's and access
     * some other class' static field from them.
     */
    public static void main(String... strings) throws Exception {
        // Using the first ClassLoader
        CustomClassLoader loader1 =
                new CustomClassLoader(StaticAccessorDemo.class.getClassLoader());
        Class<?> clazz1 = loader1.loadClass("org.n3r.sandbox.customclassloader.StaticAccessor");
        Object instance1 = clazz1.newInstance();
        clazz1.getMethod("runMe").invoke(instance1);


        // Using the second ClassLoader
        CustomClassLoader loader2 =
                new CustomClassLoader(StaticAccessorDemo.class.getClassLoader());
        Class<?> clazz2 = loader2.loadClass("org.n3r.sandbox.customclassloader.StaticAccessor");
        Object instance2 = clazz2.newInstance();
        clazz2.getMethod("runMe").invoke(instance2);
    }
}
