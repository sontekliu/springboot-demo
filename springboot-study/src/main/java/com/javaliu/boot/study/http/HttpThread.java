package com.javaliu.boot.study.http;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.sql.SQLOutput;

/**
 * 描 述：  TODO
 * 类 名：HttpThread
 * 作 者：liushijun
 * 创 建：2019年05月20日 21:08:00
 * 版 本：V1.0.0
 */
public class HttpThread extends Thread{

    private CloseableHttpClient closeableHttpClient;
    private HttpGet httpGet;

    public HttpThread(CloseableHttpClient closeableHttpClient, HttpGet httpGet){
        this.closeableHttpClient = closeableHttpClient;
        this.httpGet = httpGet;
    }


    @Override
    public void run() {
        try {
            CloseableHttpResponse response = closeableHttpClient.execute(httpGet);
            System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
