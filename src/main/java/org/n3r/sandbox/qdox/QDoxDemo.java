package org.n3r.sandbox.qdox;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaPackage;
import com.thoughtworks.qdox.model.JavaSource;

import java.io.File;
import java.util.Collection;
import java.util.List;

public class QDoxDemo {
    // QDox is a high speed, small footprint parser for extracting class/interface/method definitions from source files
    // http://qdox.codehaus.org/model.html
    public static void main(String[] args) {
        JavaProjectBuilder builder = new JavaProjectBuilder();
        try {
            JavaSource src = builder.addSource(new File(
                    "/Users/bingoohuang/github/java-sandbox/src/main/java/org/n3r/sandbox/qdox/DemoJava.java"));
            JavaPackage pkg = src.getPackage();
            String name = pkg.getName();
            String toString = pkg.toString();
            JavaPackage parent = pkg.getParentPackage();
            Collection<JavaClass> classes = pkg.getClasses();
            List<JavaMethod> methods = classes.iterator().next().getMethods();
            for (JavaMethod method : methods) {
                System.out.println("Method Name : " + method.getName());
            }
            System.out.println("pkg name : " + name);
            System.out.println("pkg to String : " + toString);
            System.out.println("pkg parent name : " + parent);

//            Method Name : printName
//            Method Name : createListOfNames
//            pkg name : org.n3r.sandbox.qdox
//            pkg to String : package org.n3r.sandbox.qdox
//            pkg parent name : null
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
