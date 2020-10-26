package com.example.influxdbdemo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "influx")
@Configuration
@Data
public class InfluxProperties {
    private String userName;
    private String password;
    private String url;
    private String database;
    private String retentionPolicy;
    private String retentionPolicyTime;

}
