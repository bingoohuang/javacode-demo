package org.n3r.sandbox.cglib;

public class ClassicWrapper extends Source {

    private Source source;

    public ClassicWrapper(Source source) {
        this.source = source;
    }

    public Source getSource() {
        return source;
    }

    @Override
    public String a() {
        return "wrapped [" + getSource().a() + "]";
    }

    @Override
    public void b() {
        getSource().b();
    }
}
