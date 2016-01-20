package org.n3r.sandbox.benchmark;

import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class SimpleTestBenchmark {
    ClassUnderTest classUnderTest = new ClassUnderTest();

    @Rule
    public BenchmarkRule benchmarkRun = new BenchmarkRule();

    @Test
    public void testSomething() throws Exception {
        assertTrue(classUnderTest.doTest());
    }

    static class ClassUnderTest {
        public boolean doTest() throws InterruptedException {
            for (int i = 0; i < 3000000; i++)
                new String("foo_" + i);

            Thread.sleep(500);
            return true;
        }
    }
}
