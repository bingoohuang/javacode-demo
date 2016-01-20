package org.n3r.sandbox.qdox;

import java.util.ArrayList;
import java.util.List;

public class DemoJava {
    public void printName() {
        System.out.println("Hi print name method");
    }

    public List<String> createListOfNames() {
        List<String> names = new ArrayList<String>();
        names.add("sandeep");
        names.add("Sangeeta");
        return names;
    }
}