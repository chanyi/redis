package com.simba.redis.model.configure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "redis.start")
public class Start {
    private  String host;
    private String password;
    private  int port;
    private  int maxIdle;
    private  int maxTotal;
    private  int minIdle;
    private  int timeOut;
}
