package org.n3r.sandbox.lang;

import com.google.common.util.concurrent.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrentTest {
    static final int PoolSize = 20;
    static ListeningExecutorService service;

    static {
        int processors = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(processors * PoolSize);
        service = MoreExecutors.listeningDecorator(executorService);
    }

    public static void execute(Callable<?>... callalbes) {
        for (Callable<?> callable : callalbes) {
            ListenableFuture<?> explosion = service.submit(callable);
            Futures.addCallback(explosion, new FutureCallback<Object>() {
                // we want this handler to run immediately after we push the big red button!
                public void onSuccess(Object explosion) {

                }

                public void onFailure(Throwable thrown) {

                }
            });
        }

    }
}
