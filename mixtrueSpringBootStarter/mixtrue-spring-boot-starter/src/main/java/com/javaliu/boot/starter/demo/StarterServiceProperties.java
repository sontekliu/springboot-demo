package com.javaliu.boot.starter.demo;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("demo.starter")
public class StarterServiceProperties {

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    private String config;

}
