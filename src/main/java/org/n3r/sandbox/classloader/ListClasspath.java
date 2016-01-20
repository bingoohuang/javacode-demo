package org.n3r.sandbox.classloader;

import java.net.URL;
import java.net.URLClassLoader;

public class ListClasspath {
    public static void main(String[] args) {
        ClassLoader cl = ClassLoader.getSystemClassLoader();

        URL[] urls = ((URLClassLoader)cl).getURLs();

//        /Users/bingoohuang/.m2/repository/com/nflabs/grok/0.0.5/grok-0.0.5.jar
//        /Users/bingoohuang/.m2/repository/com/github/tony19/named-regexp/0.2.3/named-regexp-0.2.3.jar
//        /Users/bingoohuang/.m2/repository/org/apache/commons/commons-lang3/3.1/commons-lang3-3.1.jar
//        /Users/bingoohuang/.m2/repository/com/google/code/gson/gson/2.2.2/gson-2.2.2.jar
//        /Users/bingoohuang/.m2/repository/org/slf4j/slf4j-log4j12/1.7.5/slf4j-log4j12-1.7.5.jar
//        /Users/bingoohuang/.m2/repository/org/slf4j/slf4j-api/1.6.1/slf4j-api-1.6.1.jar
//        /Users/bingoohuang/.m2/repository/log4j/log4j/1.2.17/log4j-1.2.17.jar
        for(URL url: urls){
            System.out.println(url.getFile());
        }
    }
}
