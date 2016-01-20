package org.n3r.sandbox.metrics;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

// Gauges是一个最简单的计量，一般用来统计瞬时状态的数据信息，比如系统中处于pending状态的job。
/**
 * User: hzwangxx
 * Date: 14-2-17
 * Time: 14:47
 * 测试Gauges，实时统计pending状态的job个数
 */
public class GaugesGetStarted {
    /**
     * 实例化一个registry，最核心的一个模块，相当于一个应用程序的metrics系统的容器，维护一个Map
     */
    private static final MetricRegistry metrics = new MetricRegistry();

    private static Queue<String> queue = new LinkedBlockingDeque<String>();

    /**
     * 在控制台上打印输出
     */
    private static ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics).build();

    public static void main(String[] args) throws InterruptedException {
        reporter.start(3, TimeUnit.SECONDS);

        //实例化一个Gauge
        Gauge<Integer> gauge = new Gauge<Integer>() {
            @Override
            public Integer getValue() {
                return queue.size();
            }
        };

        //注册到容器中
        metrics.register(MetricRegistry.name(GaugesGetStarted.class, "pending-job", "size"), gauge);

//        //测试JMX
//        JmxReporter jmxReporter = JmxReporter.forRegistry(metrics).build();
//        jmxReporter.start();

        //模拟数据
        for (int i = 0; i < 20; i++) {
            queue.add("a");
            Thread.sleep(1000);
        }

    }
}
