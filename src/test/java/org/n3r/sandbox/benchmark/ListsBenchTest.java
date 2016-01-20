package org.n3r.sandbox.benchmark;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.carrotsearch.junitbenchmarks.annotation.AxisRange;
import com.carrotsearch.junitbenchmarks.annotation.BenchmarkMethodChart;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import java.util.*;

@AxisRange(min = 0, max = 1)
@BenchmarkMethodChart(filePrefix = "benchmark-lists")
@BenchmarkOptions(callgc = false, benchmarkRounds = 20, warmupRounds = 3)
public class ListsBenchTest {
    @Rule
    public TestRule benchmarkRun = new BenchmarkRule();

    private static Object singleton = new Object();
    private static int COUNT = 50000;
    private static int[] rnd;

    /**
     * Prepare random numbers for tests.
     */
    @BeforeClass
    public static void prepare() {
        rnd = new int[COUNT];

        final Random random = new Random();
        for (int i = 0; i < COUNT; i++) {
            rnd[i] = Math.abs(random.nextInt());
        }
    }

    @Test
    public void arrayList() throws Exception {
        runTest(new ArrayList<Object>());
    }

    @Test
    public void linkedList() throws Exception {
        runTest(new LinkedList<Object>());
    }

    @Test
    public void vector() throws Exception {
        runTest(new Vector<Object>());
    }

    private void runTest(List<Object> list) {
        assert list.isEmpty();

        // First, add a number of objects to the list.
        for (int i = 0; i < COUNT; i++)
            list.add(singleton);

        // Randomly delete objects from the list.
        for (int i = 0; i < rnd.length; i++)
            list.remove(rnd[i] % list.size());
    }
}
