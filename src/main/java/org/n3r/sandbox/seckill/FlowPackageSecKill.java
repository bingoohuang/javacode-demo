package org.n3r.sandbox.seckill;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

public class FlowPackageSecKill {

    static AtomicLong mobile = new AtomicLong(13012340000L);
    static HttpClient httpClient;
    static MultiThreadedHttpConnectionManager connectionManager;

    static {
        HttpClientParams params = new HttpClientParams();
        HttpConnectionManagerParams httpConnectionManagerParams = new HttpConnectionManagerParams();
        httpConnectionManagerParams.setDefaultMaxConnectionsPerHost(10); // 默认2
        httpConnectionManagerParams.setMaxTotalConnections(10); // 默认20

        connectionManager = new MultiThreadedHttpConnectionManager();
        connectionManager.setParams(httpConnectionManagerParams);
        httpClient = new HttpClient(params, connectionManager);
    }


    @Benchmark
    public void measureNginx() throws IOException {
        mease(9001);
    }

    @Benchmark
    public void measureJava() throws IOException {
        mease(8080);
    }

    private void mease(int port) throws IOException {
        long thisMobile = mobile.incrementAndGet();
        long thisPrice = (thisMobile - 13012340000L - 1) / 100000 + 1;

        String uri = "http://localhost:" + port + "/kill?mobile=" + thisMobile + "&price=" + thisPrice;
        GetMethod method = new GetMethod(uri);
        httpClient.executeMethod(method);
        String response = method.getResponseBodyAsString();
        if (!response.trim().equals("OK")) {
            System.out.println("thisMobile:" + thisMobile + ",thisPrice:" + thisPrice + ",error:" + response.trim());
        }

        method.releaseConnection();
    }

    public static void main(String[] args) throws RunnerException, IOException {
        Options opt = new OptionsBuilder()
                .include(FlowPackageSecKill.class.getSimpleName())
                .warmupIterations(3)
                .threads(10)
                .measurementIterations(50)
                .forks(0)
                .build();

        new Runner(opt).run();
    }
}
