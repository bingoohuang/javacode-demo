package org.n3r.sandbox.benchmark;

import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.carrotsearch.junitbenchmarks.annotation.BenchmarkHistoryChart;
import com.carrotsearch.junitbenchmarks.annotation.LabelType;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

// -Djub.consumers=CONSOLE,H2 -Djub.db.file=.benchmarks
@BenchmarkHistoryChart(filePrefix = "map-types-benchmark-history-chart", labelWith = LabelType.CUSTOM_KEY, maxRuns = 20)
public class HistoryChartOutputTests {
    @Rule
    public BenchmarkRule benchmarkRun = new BenchmarkRule();

    static final int MAX_ENTRIES = 15000;

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
