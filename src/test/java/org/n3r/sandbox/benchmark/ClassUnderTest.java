package org.n3r.sandbox.benchmark;

public class ClassUnderTest {
    public boolean doTest() throws InterruptedException {
        for (int i = 0; i < 3000000; i++)
            new String("foo_" + i);

        Thread.sleep(500);
        return true;
    }
}