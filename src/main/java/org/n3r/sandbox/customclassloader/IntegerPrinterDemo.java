package org.n3r.sandbox.customclassloader;

// http://www.javablogging.com/java-classloader-2-write-your-own-classloader/
public class IntegerPrinterDemo {
    /**
     * This main method shows a use of our CustomClassLoader for
     * loading some class and running it. All the objects referenced
     * from the IntegerPrinter class will be loaded with
     * our CustomClassLoader.
     */
    public static void main(String[] args) throws Exception {
        CustomClassLoader loader = new CustomClassLoader(
                IntegerPrinterDemo.class.getClassLoader());
        Class<?> clazz =
                loader.loadClass("org.n3r.sandbox.customclassloader.IntegerPrinter");
        Object instance = clazz.newInstance();
        clazz.getMethod("runMe").invoke(instance);
    }
}
