package com.javaliu.boot.starter.demo;

import org.apache.commons.lang3.StringUtils;

public class StarterService {

    private String config;
    public StarterService(String config) {
        this.config = config;
    }

    public String[] split(String separatorChar) {
        return StringUtils.split(this.config, separatorChar);
    }
}
