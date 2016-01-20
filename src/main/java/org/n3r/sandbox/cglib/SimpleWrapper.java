package org.n3r.sandbox.cglib;

public abstract class SimpleWrapper extends Source implements Wrapper<Source> {
    @Override
    public String a() {
        return "wrapped [" + getSource().a() + "]";
    }
}
