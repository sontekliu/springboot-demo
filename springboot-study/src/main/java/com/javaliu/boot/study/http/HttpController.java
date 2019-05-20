package com.javaliu.boot.study.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * 描 述：Http超时测试
 * 类 名：HttpController
 * 作 者：liushijun
 * 创 建：2019年05月20日 19:28:00
 * 版 本：V1.0.0
 */
@RestController
@RequestMapping(value = "/http/")
public class HttpController {

    private static final Logger log = LoggerFactory.getLogger(HttpController.class);

    /**
     * 测试 connectionRequestTimeout 的含义
     * @return
     * @throws InterruptedException
     */
    @RequestMapping(value = "testConnectRequestTimeout", method = RequestMethod.GET)
    public String testConnectRequestTimeout() throws InterruptedException {
        Thread.sleep(500);
        return "connectionRequestTimeout";
    }

    /**
     * 测试 socketTimeout 的含义
     * @return
     * @throws InterruptedException
     */
    @RequestMapping(value = "testSocketTimeout", method = RequestMethod.GET)
    public String testSocketTimeout() throws InterruptedException {
        Thread.sleep(3000);
        return "testSocketTimeout";
    }

    /**
     * 测试 socketTimeout 的含义
     * @return
     * @throws InterruptedException
     */
    @RequestMapping(value = "testSocketTimeout2", method = RequestMethod.GET)
    public void testSocketTimeout2(HttpServletResponse response) throws Exception {
        for (int i=0;i<10;i++){
            response.getWriter().println("This is number : " + i);
            response.flushBuffer();
            Thread.sleep(800);
        }
    }


}
