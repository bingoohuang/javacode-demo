package org.n3r.sandbox.sync;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class WaitGroup {
    static ExecutorService pool = Executors.newCachedThreadPool();

    private List<Future<?>> futures = new ArrayList<Future<?>>();

    public <V> PureFuture<V> submit(Callable<V> callable) {
        Future<V> future = pool.submit(callable);
        futures.add(future);

        return new PureFuture(future);
    }

    public static class PureFuture<V> {
        private final Future<V> future;

        public PureFuture(Future<V> future) {
            this.future = future;
        }

        public V get() {
            try {
                return future.get();
            } catch (Throwable e) {
                throw propogate(e);
            }
        }
    }


    public void waitAll(int timeout, TimeUnit timeUnit) {
        long start = System.currentTimeMillis();
        long timeoutMillis = timeUnit.toMillis(timeout);
        long end = start + timeoutMillis;

        try {
            for (Future future : futures) {
                if (future.isDone()) continue;

                future.get(timeout, timeUnit);
                checkTimeout(end, timeout, timeUnit);
            }
        } catch (Throwable e) {
            throw propogate(e);
        } finally {
            cleanUp();
        }
    }

    private void checkTimeout(long end, int timeout,
                              TimeUnit timeUnit) throws TimeoutException {
        if (end > System.currentTimeMillis()) return;

        throw new TimeoutException("time out " + timeout + " " + timeUnit);
    }

    private void cleanUp() {
        for (Future future : futures) {
            if (future.isDone()) continue;

            future.cancel(true);
        }
    }

    public static RuntimeException propogate(Throwable throwable) {
        if (throwable instanceof RuntimeException) {
            return (RuntimeException) throwable;
        }

        if (throwable instanceof ExecutionException) {
            if (throwable.getCause() instanceof RuntimeException)
                return (RuntimeException) throwable.getCause();
            else
                return new RuntimeException(throwable.getCause());
        }

        return new RuntimeException(throwable);
    }
}
