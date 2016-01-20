package org.n3r.sandbox.benchmark;


public class Foo implements Comparable<Foo> {
    private final Integer x;

    public Foo(final int x) {
        this.x = x;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Foo other = (Foo) obj;
        if (x != other.x)
            return false;
        return true;
    }

    @Override
    public int compareTo(final Foo o) {
        return x.compareTo(o.x);
    }
}