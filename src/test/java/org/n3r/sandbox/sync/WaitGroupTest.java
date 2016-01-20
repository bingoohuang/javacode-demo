package org.n3r.sandbox.sync;

import org.junit.Test;
import org.n3r.sandbox.sync.WaitGroup.PureFuture;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class WaitGroupTest {
    @Test
    public void test1() {
        WaitGroup waitGroup = new WaitGroup();
        PureFuture<String> result1 = waitGroup.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "1";
            }
        });

        PureFuture<String> result2 = waitGroup.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "2";
            }
        });

        waitGroup.waitAll(5, TimeUnit.SECONDS);

        assertThat(result1.get(), is("1"));
        assertThat(result2.get(), is("2"));

    }
}
