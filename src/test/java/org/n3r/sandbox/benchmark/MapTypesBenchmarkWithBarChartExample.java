package org.n3r.sandbox.benchmark;

import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.carrotsearch.junitbenchmarks.annotation.AxisRange;
import com.carrotsearch.junitbenchmarks.annotation.BenchmarkMethodChart;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

//-Djub.consumers=CONSOLE,H2 -Djub.db.file=.benchmarks
@AxisRange(min = 0, max = 1)
@BenchmarkMethodChart(filePrefix = "map-types-benchmark-barchart")
public class MapTypesBenchmarkWithBarChartExample {
    @Rule
    public BenchmarkRule benchmarkRun = new BenchmarkRule();

    static final int MAX_ENTRIES = 1500000;

    @Test
    public void hashMap() throws Exception {
        testMap(new HashMap<Integer, Foo>());
    }

    @Test
    public void linkedHashMap() throws Exception {
        testMap(new LinkedHashMap<Integer, Foo>());
    }

    @Test
    public void treeMap() throws Exception {
        testMap(new TreeMap<Integer, Foo>());
    }

    private void testMap(final Map<Integer, Foo> map) {
        for (int i = 0; i < MAX_ENTRIES; i++) {
            map.put(i, new Foo(i));
        }
        assertThat(map.size(), is(MAX_ENTRIES));
        for (int i = 0; i < MAX_ENTRIES; i++) {
            assertThat(map.get(i), is(notNullValue()));
        }
    }


}
