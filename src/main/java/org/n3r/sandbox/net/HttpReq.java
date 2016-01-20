package org.n3r.sandbox.net;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.util.EncodingUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

/**
 * HttpClient v3 简单封装。
 * 支持Get/Post两种方法。
 * 例如：
 * 1) HttpReq.get("http://www.baidu.com").execute().getResponseString();
 * 2) HttpReq.post("http://localhost:8983/solr/core11/select")
         .httpConnectionManager(multiThreadedHttpConnectionManager)
         .param("q", "addressSolr_ik:计委宿舍8号楼201房间")
         .basicAuth("guest", "guest")
         .execute().getResponseString();
 */
public class HttpReq {
    private static volatile MultiThreadedHttpConnectionManager mtHttpConnectionManager;
//    static  {
//        mtHttpConnectionManager = multithreadsHttpConnectionManager();
//    }
    private final String url;
    private String proxyHost; // 代理主机
    private int proxyPort; // 代理端口
    private int timeout = 60000; // 3 seconds
    private int retryTimes = 3; // IOException时，重试次数
    private String authUser, authPass; // 基本认证用户名、密码

    private List<NameValuePair> params = new ArrayList<NameValuePair>(); // GET/POST参数名值对
    private HttpReqMethod reqMethod; // 请求GET或者POST方法
    private HttpConnectionManager httpConnectionManager; // 连接管理器
    private boolean externalConnectionManager; // 连接管理器是否由外部提供，外部提供时，在execute中不进行shutdown操作

    public HttpReq(String url, HttpReqMethod reqMethod) {
        this.url = url;
        this.reqMethod = reqMethod;
    }

    public static void main(String[] args) throws IOException {
        String responseString = HttpReq.get("http://www.baidu.com").execute().getResponseString();
        System.out.println(responseString);
//
//        responseString = HttpReq.get("http://www.baidu.com")
//                //.proxy("localhost", 8888)
//                .timeout(30000)
//                .execute().getResponseString();
//        System.out.println(responseString);
//
//        MultiThreadedHttpConnectionManager multiThreadedHttpConnectionManager = multithreadsHttpConnectionManager();
//
//        responseString = HttpReq.get("http://localhost:8983/solr/core11/select")
//                .httpConnectionManager(multiThreadedHttpConnectionManager)
//                .param("q", "addressSolr_ik:计委宿舍8号楼201房间")
//                .basicAuth("guest", "guest")
//                .execute().getResponseString();
//        multiThreadedHttpConnectionManager.shutdown();
//
//        System.out.println(responseString);
//
//        /*在我的WIN7上出现java.net.SocketException: No buffer space available (maximum connections reached?): JVM_Bind
//         * 参考：http://rwatsh.blogspot.com/2012/04/resolution-for-no-buffer-space.html
//         * The resolution is to open the registry editor and locate the registry subkey:
//         * HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\Tcpip\Parameters and add a new entry as shown below:
//         * Value Name: MaxUserPort
//         * Value Type: DWORD
//         * Value data: 65534
//         */
//        for (int i = 0; i < 10000; ++i) {
//            responseString = HttpReq.post("http://localhost:8983/solr/core11/select")
//                    .param("q", "addressSolr_ik:计委宿舍8号楼201房间")
//                    .basicAuth("guest", "guest")
//                    .execute().getResponseString();
//            System.out.println(responseString);
//        }
    }

    public static synchronized MultiThreadedHttpConnectionManager multithreadsHttpConnectionManager() {
        if (mtHttpConnectionManager == null) {
            mtHttpConnectionManager = new MultiThreadedHttpConnectionManager();
            HttpConnectionManagerParams httpConnectionManagerParams = new HttpConnectionManagerParams();
            httpConnectionManagerParams.setDefaultMaxConnectionsPerHost(20000); // 默认2
            httpConnectionManagerParams.setMaxTotalConnections(20000); // 默认20

            mtHttpConnectionManager.setParams(httpConnectionManagerParams);
        }

        return mtHttpConnectionManager;
    }

    /**
     * 以Get方法调用HTTP。
     * @param url 请求地址
     * @return
     */
    public static HttpReq get(String url) {
        return new HttpReq(url, HttpReqMethod.GET);
    }

    /**
     * 以Post方式调用HTTP.
     * @param url 请求地址
     * @return
     */
    public static HttpReq post(String url) {
        return new HttpReq(url, HttpReqMethod.POST);
    }

    /**
     * 使用独立的连接管理器。
     * @param connectionManager 链接管理器
     * @return
     */
    public HttpReq httpConnectionManager(HttpConnectionManager connectionManager) {
        httpConnectionManager = connectionManager;
        externalConnectionManager = true;

        return this;
    }

    /**
     * 传递Get/Post参数
     * @param name 参数名称
     * @param value 参数取值
     * @return
     */
    public HttpReq param(String name, String value) {
        NameValuePair pair = new NameValuePair(name, value);
        params.add(pair);
        return this;
    }

    /**
     * 服务器端要求基本认证
     * @param user 认证用户名
     * @param pass 认证密码
     * @return
     */
    public HttpReq basicAuth(String user, String pass) {
        authUser = user;
        authPass = pass;
        return this;
    }

    protected HttpMethodBase createMethod() {
        if (reqMethod == HttpReqMethod.GET) {
            GetMethod method = new GetMethod(url);
            String queryString = method.getQueryString();
            NameValuePair[] nameValuePairs = params.toArray(new NameValuePair[0]);
            String newQueryString = EncodingUtil.formUrlEncode(nameValuePairs, "UTF-8");
            method.setQueryString(StringUtils.isEmpty(queryString) ? newQueryString
                    : (queryString + '&' + newQueryString));
            params.clear();
            return method;
        }

        if (reqMethod == HttpReqMethod.POST) {
            PostMethod method = new PostMethod(url);
            method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
            for (NameValuePair pair : params) {
                method.addParameter(pair);
            }

            params.clear();

            return method;
        }

        return null;
    }

    /**
     * 执行HTTP GET/POST
     * @return 执行结果
     * @throws IOException
     */
    public HttpResponse execute() throws IOException {
        HttpClient httpClient = createHttpClient();
        HttpMethodBase method = null;

        try {
            createProxy(httpClient);
            basicAuth(httpClient);

            method = createMethod(); // 放在try中，防止url有错误，异常抛出

            return executeMethod(httpClient, method);
        } finally {
            shutdown(method);
        }
    }

    private void shutdown(HttpMethodBase method) {
        if (method != null) {
            method.releaseConnection();
        }
        if (!externalConnectionManager) {
            ((SimpleHttpConnectionManager) httpConnectionManager).shutdown();
        }
    }

    private HttpClient createHttpClient() {
        HttpClientParams httpClientParams = new HttpClientParams();
        // httpClientParams.setParameter(HttpMethodParams.SO_TIMEOUT, timeout);
        httpClientParams.setParameter(HttpMethodParams.COOKIE_POLICY, CookiePolicy.IGNORE_COOKIES);
        httpClientParams.setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(retryTimes,
                false));

        if (httpConnectionManager == null) {
            httpConnectionManager = new SimpleHttpConnectionManager();
        }

        httpConnectionManager.getParams().setConnectionTimeout(timeout);
        httpConnectionManager.getParams().setSoTimeout(timeout);

        return new HttpClient(httpClientParams, httpConnectionManager);
    }

    private HttpResponse executeMethod(HttpClient httpClient, HttpMethodBase method) throws IOException {
        int responseCode = httpClient.executeMethod(method);
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setResponseCode(responseCode);
        httpResponse.setResponseContent(method.getResponseBodyAsStream());
        httpResponse.setResponseCharSet(method.getResponseCharSet());
        return httpResponse;
    }

    private void createProxy(HttpClient httpClient) {
        if (getProxyHost() != null && getProxyHost().length() > 0) {
            httpClient.getHostConfiguration().setProxy(getProxyHost(), getProxyPort());
        }
    }

    private void basicAuth(HttpClient httpClient) {
        if (authUser != null && authUser.length() > 0) {
            httpClient.getParams().setAuthenticationPreemptive(true);
            Credentials credentials = new UsernamePasswordCredentials(authUser, authPass);
            httpClient.getState().setCredentials(AuthScope.ANY, credentials);
        }
    }

    public String getUrl() {
        return url;
    }

    public HttpReq proxy(String proxyHost, int proxyPort) {
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        return this;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public String getQueryString() {
        return EncodingUtil.formUrlEncode(params.toArray(new NameValuePair[] {}), "UTF-8");
    }

    public HttpReq timeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public static enum HttpReqMethod {
        GET, POST
    }

    public static class HttpResponse {
        private int responseCode;
        private String responseCharSet;
        private byte[] responseContent;

        public byte[] getResponseContent() {
            return responseContent;
        }

        public void setResponseContent(byte[] responseContent) {
            this.responseContent = responseContent;
        }

        public void setResponseContent(InputStream is) throws IOException {
            responseContent = IOUtils.toByteArray(is);
        }

        public String getResponseString() {
            return responseContent == null ? null
                    : EncodingUtil.getString(responseContent, getResponseCharSet());
        }

        public int getResponseCode() {
            return responseCode;
        }

        public void setResponseCode(int responseCode) {
            this.responseCode = responseCode;
        }

        public String getResponseCharSet() {
            return responseCharSet;
        }

        public void setResponseCharSet(String responseCharSet) {
            this.responseCharSet = responseCharSet;
        }
    }
}
