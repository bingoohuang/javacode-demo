package org.n3r.sandbox.seckill;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang.math.RandomUtils;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class ArrayListAndHashSet {
    static AtomicLong mobile = new AtomicLong(13012340000L);

    @State(Scope.Thread)
    public static class HashSetState {
        volatile Set<Long> set = Sets.newHashSet();
    }

    @State(Scope.Thread)
    public static class ArrayListState {
        volatile List<Long> list = Lists.newArrayList();
    }

    @Benchmark
    public void measureAddArrayList(ArrayListState arrayListState) throws IOException {
        arrayListState.list.add(mobile.incrementAndGet());
    }

    @Benchmark
    public void measureAddHashSet(HashSetState hashSetState) throws IOException {
        hashSetState.set.add(mobile.incrementAndGet());
    }


    @Benchmark
    public boolean measureGetArrayList(ArrayListState arrayListState) throws IOException {
        return arrayListState.list.contains(RandomUtils.nextLong());
    }

    @Benchmark
    public boolean measureGetHashSet(HashSetState hashSetState) throws IOException {
        return hashSetState.set.contains(RandomUtils.nextLong());
    }

    public static void main(String[] args) throws RunnerException, IOException {
        Options opt = new OptionsBuilder()
                .include(ArrayListAndHashSet.class.getSimpleName())
                .warmupIterations(1)
                .threads(1)
                .measurementIterations(3)
                .forks(0)
                .build();

        new Runner(opt).run();
    }
}
