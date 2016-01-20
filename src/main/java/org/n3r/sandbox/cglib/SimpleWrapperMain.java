package org.n3r.sandbox.cglib;

public class SimpleWrapperMain {
    public static void main(String[] args) {
        Source source = new Source();
        SimpleWrapper wrapper = CglibWrapper.createWrapper(source, SimpleWrapper.class);
        System.out.println(wrapper.a());;
    }
}
