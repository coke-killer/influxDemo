package com.example.influxdbdemo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class InfluxConfiguration {
    @Autowired
    InfluxProperties influxProperties;


    @Bean
    public InfluxConnection getConnection() {
         InfluxConnection influxConnection=new InfluxConnection(influxProperties.getUserName(), influxProperties.getPassword(), influxProperties.getUrl(), influxProperties.getDatabase(), influxProperties.getRetentionPolicy(), influxProperties.getRetentionPolicyTime());
        log.info("init influxdb::[{}]", influxProperties);
        return influxConnection;
    }
}
