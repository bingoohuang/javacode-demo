package org.n3r.sandbox.classloader;

import org.xeustechnologies.jcl.JarClassLoader;
import org.xeustechnologies.jcl.JclObjectFactory;
import org.xeustechnologies.jcl.JclUtils;

public class JclDemo {
    public static void main(String[] args) {
        JarClassLoader jcl1 = new JarClassLoader();
        jcl1.add(JclDemo.class.getResource("/jcldemo1.jar"));

        JarClassLoader jcl2 = new JarClassLoader();
        jcl2.add(JclDemo.class.getResource("/jcldemo2.jar"));

        //Create default factory
        JclObjectFactory factory = JclObjectFactory.getInstance();
        //Create object of loaded class
        Object obj1 = factory.create(jcl1, "org.n3r.sandbox.classloader.JclDemo");
        //Obtain interface reference in the current classloader
        JclTutorial mi1 = JclUtils.cast(obj1, JclTutorial.class);
        System.out.println(mi1.hello("bingoo")); // hello:bingoo

        //Create object of loaded class
        Object obj2 = factory.create(jcl2, "org.n3r.sandbox.classloader.JclDemo");
        //Obtain interface reference in the current classloader
        JclTutorial mi2 = JclUtils.cast(obj2, JclTutorial.class);
        System.out.println(mi2.hello("bingoo")); // world:bingoo
    }
}
