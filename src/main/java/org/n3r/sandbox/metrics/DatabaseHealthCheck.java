package org.n3r.sandbox.metrics;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;

import java.util.Map;
import java.util.Random;

// Metrics提供了一个独立的模块：Health Checks，用于对Application、其子模块或者关联模块的运行是否正常做检测。
public class DatabaseHealthCheck extends HealthCheck {
    private final Database database;

    public DatabaseHealthCheck(Database database) {
        this.database = database;
    }

    @Override
    protected HealthCheck.Result check() throws Exception {
        if (database.ping()) {
            return HealthCheck.Result.healthy();
        }
        return HealthCheck.Result.unhealthy("Can't ping database.");
    }

    /**
     * 模拟Database对象
     */
    static class Database {
        /**
         * 模拟database的ping方法
         * @return 随机返回boolean值
         */
        public boolean ping() {
            Random random = new Random();
            return random.nextBoolean();
        }
    }

    public static void main(String[] args) {
//        MetricRegistry metrics = new MetricRegistry();
//        ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics).build();
        HealthCheckRegistry registry = new HealthCheckRegistry();
        registry.register("database1", new DatabaseHealthCheck(new Database()));
        registry.register("database2", new DatabaseHealthCheck(new Database()));
        while (true) {
            for (Map.Entry<String, Result> entry : registry.runHealthChecks().entrySet()) {
                if (entry.getValue().isHealthy()) {
                    System.out.println(entry.getKey() + ": OK");
                } else {
                    System.err.println(entry.getKey() + ": FAIL, error message: " + entry.getValue().getMessage());
                    final Throwable e = entry.getValue().getError();
                    if (e != null) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }
        }
    }
}
