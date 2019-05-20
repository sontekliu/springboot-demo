package com.javaliu.boot.study.http;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 描 述：Http 客户端工具
 * 类 名：HttpClient
 * 作 者：liushijun
 * 创 建：2019年05月20日 19:40:00
 * 版 本：V1.0.0
 */
public class HttpClient {

    /**
     * http://www.voidcn.com/article/p-dlrlohmn-bnx.html
     * https://www.baeldung.com/httpclient-connection-management
     */

    @Test
    public void testConnectionTimeout(){
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://149.28.80.135");
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(1000)
                .setSocketTimeout(3500).setConnectTimeout(5000).build();
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
    public void testSocketTimeout(){
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://172.16.43.155:8080/http/testSocketTimeout");
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(3500).build();
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
    public void testSocketTimeout2(){
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://172.16.43.155:8080/http/testSocketTimeout2");
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(1000).build();
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
    public void testConnectionRequestTimeout(){
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
        manager.setMaxTotal(100);
        // manager.setDefaultMaxPerRoute(20);
        CloseableHttpClient client = HttpClients.custom().setConnectionManager(manager).build();
        ExecutorService service = Executors.newFixedThreadPool(10);
        for (int i=0;i<10;i++){
            service.execute(() -> {
                HttpGet httpGet = new HttpGet("http://172.16.43.155:8080/http/testConnectRequestTimeout");
                RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(10000).build();
                httpGet.setConfig(requestConfig);
                try {
                    CloseableHttpResponse response = client.execute(httpGet);
                    System.out.println(EntityUtils.toString(response.getEntity()));
                } catch (ConnectTimeoutException e) {
                    e.printStackTrace();
                } catch (Exception e){
                    e.printStackTrace();
                }
            });
        }
//        HttpGet httpGet = new HttpGet("http://172.16.43.155:8080/http/testConnectRequestTimeout");
//        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(2000).build();
//        httpGet.setConfig(requestConfig);
//        try {
//            CloseableHttpResponse response = client.execute(httpGet);
//            System.out.println(EntityUtils.toString(response.getEntity()));
//        } catch (ConnectTimeoutException e) {
//            e.printStackTrace();
//        } catch (Exception e){
//            e.printStackTrace();
//        }
    }

    @Test
    public void testPerRoute()throws Exception{
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
        manager.setDefaultMaxPerRoute(10);
        CloseableHttpClient client = HttpClients.custom().setConnectionManager(manager).build();
        HttpGet httpGet = new HttpGet("http://172.16.43.155:8080/http/testConnectRequestTimeout");
        HttpThread thread1 = new HttpThread(client, httpGet);
        HttpThread thread2 = new HttpThread(client, httpGet);
        HttpThread thread3 = new HttpThread(client, httpGet);

        thread1.start();
        thread2.start();
        thread3.start();
        thread1.join();
        thread2.join();
        thread3.join();
    }
}
