package com.javaliu.boot.study.http;

import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.ConnectionRequest;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 描 述：Http 客户端工具
 * 类 名：HttpClient
 * 作 者：liushijun
 * 创 建：2019年05月20日 19:40:00
 * 版 本：V1.0.0
 */
public class HttpClient {

    public static final String URL = "http://39.105.28.88:8080/";

    /**
     * http://www.voidcn.com/article/p-dlrlohmn-bnx.html
     * https://www.baeldung.com/httpclient-connection-management
     */

    /**
     * org.apache.http.conn.ConnectTimeoutException: Connect to www.baidu.com:80 [www.baidu.com/220.181.38.150, www.baidu.com/220.181.38.149] failed: connect timed out
     * ConnectTimeout 网络连接超时，是指本地和目标主机建立连接时超时
     * 例如：
     *      连接地址是百度，设置连接超时时间为 5 毫秒或者更少，就会出现网络超时
     */
    @Test
    public void testConnectionTimeout(){
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://www.baidu.com");
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(1000)
                .setConnectionRequestTimeout(5000)
                .setConnectTimeout(5).build();
        httpGet.setConfig(requestConfig);
        try {
            CloseableHttpResponse response = client.execute(httpGet);
            System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * java.net.SocketTimeoutException: Read timed out
     */
    @Test
    public void testSocketTimeout(){
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(URL + "/http/testSocketTimeout");
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(3500).build();
        httpGet.setConfig(requestConfig);
        try {
            CloseableHttpResponse response = client.execute(httpGet);
            System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testSocketTimeout2(){
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(URL+"http/testSocketTimeout2");
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(950).build();
        httpGet.setConfig(requestConfig);
        try {
            CloseableHttpResponse response = client.execute(httpGet);
            System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (ConnectTimeoutException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testConnectionRequestTimeout() throws InterruptedException{
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
        manager.setMaxTotal(3);
        CloseableHttpClient client = HttpClients.custom().setConnectionManager(manager).build();
        for (int i=0;i<10;i++){
            HttpGet httpGet = new HttpGet(URL+"http/testSocketTimeout");
            HttpThread thread = new HttpThread(client, httpGet);
            thread.start();
        }
        HttpGet httpGet = new HttpGet(URL + "http/testSocketTimeout");
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(1000).build();
        httpGet.setConfig(requestConfig);
        try {
            CloseableHttpResponse response = client.execute(httpGet);
            System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (ConnectTimeoutException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testPerRoute()throws Exception{
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
        manager.setDefaultMaxPerRoute(10);
        System.out.println(manager.getDefaultMaxPerRoute());
        System.out.println(manager.getMaxTotal());
        System.out.println(manager.getTotalStats().getMax());
        System.out.println(manager.getTotalStats().getAvailable());
        System.out.println(manager.getTotalStats().getLeased());
        System.out.println(manager.getTotalStats().getPending());
        CloseableHttpClient client = HttpClients.custom().setKeepAliveStrategy(
                new ConnectionKeepAliveStrategy() {
                    @Override
                    public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                        HeaderElementIterator it = new BasicHeaderElementIterator
                                (response.headerIterator(HTTP.CONN_KEEP_ALIVE));
                        while (it.hasNext()) {
                            HeaderElement he = it.nextElement();
                            String param = he.getName();
                            String value = he.getValue();
                            if (value != null && param.equalsIgnoreCase
                                    ("timeout")) {
                                return Long.parseLong(value) * 1000;
                            }
                        }
                        return 5 * 1000;
                    }
                }
        ).setConnectionManager(manager).build();
        // CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(URL + "http/testConnectRequestTimeout");
        HttpThread thread1 = new HttpThread(client, httpGet);
        HttpThread thread2 = new HttpThread(client, httpGet);
        HttpThread thread3 = new HttpThread(client, httpGet);
        HttpThread thread4 = new HttpThread(client, httpGet);
        HttpThread thread5 = new HttpThread(client, httpGet);
        HttpThread thread6 = new HttpThread(client, httpGet);
        HttpThread thread7 = new HttpThread(client, httpGet);
        HttpThread thread8 = new HttpThread(client, httpGet);
        HttpThread thread9 = new HttpThread(client, httpGet);

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
        thread6.start();
        thread7.start();
        thread8.start();
        thread9.start();

        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();
        thread5.join();
        thread6.join();
        thread7.join();
        thread8.join();
        thread9.join();
        // manager.close();

        Thread.sleep(10000);
        System.out.println(manager.getTotalStats().getMax());
        System.out.println(manager.getTotalStats().getAvailable());
        System.out.println(manager.getTotalStats().getLeased());
        System.out.println(manager.getTotalStats().getPending());
    }

    @Test
    public void basicPool() throws Exception{
        BasicHttpClientConnectionManager basicConnManager =
                new BasicHttpClientConnectionManager();
        HttpClientContext context = HttpClientContext.create();

        // low level
        HttpRoute route = new HttpRoute(new HttpHost("www.baidu.com", 80));
        ConnectionRequest connRequest = basicConnManager.requestConnection(route, null);
        HttpClientConnection conn = connRequest.get(10, TimeUnit.SECONDS);
        basicConnManager.connect(conn, route, 1000, context);
        basicConnManager.routeComplete(conn, route, context);

        HttpRequestExecutor exeRequest = new HttpRequestExecutor();
        context.setTargetHost((new HttpHost("www.baidu.com", 80)));
        HttpGet get = new HttpGet("http://www.baidu.com");
        exeRequest.execute(get, conn, context);

        basicConnManager.releaseConnection(conn, null, 1, TimeUnit.SECONDS);

        // high level
        CloseableHttpClient client = HttpClients.custom()
                .setConnectionManager(basicConnManager)
                .build();
        client.execute(get);
    }
}
