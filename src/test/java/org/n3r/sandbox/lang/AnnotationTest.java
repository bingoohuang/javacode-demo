package org.n3r.sandbox.lang;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;

public class AnnotationTest {
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface MyAnnotation {
        String value() default "";
    }

    public void myMethod(@MyAnnotation("test") String st) {

    }

    public static void main(String[] args) throws NoSuchMethodException, SecurityException {
        Class<AnnotationTest> clazz = AnnotationTest.class;
        Method method = clazz.getMethod("myMethod", String.class);

        Annotation[][] annotations = method.getParameterAnnotations();
        for (Annotation[] ann : annotations) {
            System.out.printf("%d annotatations\n", ann.length);
            for (Annotation an : ann) {
                if (an instanceof MyAnnotation) System.out.println(((MyAnnotation)an).value());
                else System.out.println(an);
            }
        }


    }
}
