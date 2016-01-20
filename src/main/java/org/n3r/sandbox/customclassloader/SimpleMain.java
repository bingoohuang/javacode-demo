package org.n3r.sandbox.customclassloader;

public class SimpleMain {
    /**
     * If you run this main method supplying the
     * -Djava.system.class.loader=javablogging.CustomClassLoader
     * parameter, class SimpleMain will be loaded with
     * our CustomClassLoader. Every other
     * class referenced from here will be also loaded with it.
     */
    public static void main(String... strings) {
        System.out.print("This is my ClassLoader: "
                + SimpleMain.class.getClassLoader());
    }
}
