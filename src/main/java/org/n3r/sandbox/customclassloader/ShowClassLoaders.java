package org.n3r.sandbox.customclassloader;

// http://www.javablogging.com/java-classloader-1-what-is-a-classloader/
public class ShowClassLoaders {
    /**
     * This main method shows which class
     * loaders are used for loading classes
     * like Integer, BlowfishCipher (lib/ext)
     * and ShowClassLoaders.
     */
    public static void main(String[] args) {
        System.out.println("class loader for Integer: "
                + Integer.class.getClassLoader());
        System.out.println("class loader for BlowfishCipher: "
                + com.sun.crypto.provider.BlowfishCipher.class.getClassLoader());
        System.out.println("class loader for this class: "
                + ShowClassLoaders.class.getClassLoader());
    }
}
