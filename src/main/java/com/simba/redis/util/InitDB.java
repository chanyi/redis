package com.simba.redis.util;

import com.simba.redis.model.configure.Start;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class InitDB {

    @Autowired
    private Start start;

    public Jedis getJedis(){
        System.out.println("初始化redis");
        System.out.println("redis配置："+start.toString());
        //判断是否存在需要密码
        if(!start.getPassword().isEmpty()){
            return new JedisPool(geneConfig(start),start.getHost(),start.getPort(),start.getTimeOut(),start.getPassword()).getResource();
        }else {
            return new JedisPool(geneConfig(start), start.getHost(), start.getPort(), start.getTimeOut()).getResource();
        }
    }
    private GenericObjectPoolConfig geneConfig(Start start){
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxIdle(start.getMaxIdle());
        genericObjectPoolConfig.setMaxTotal(start.getMaxTotal());
        genericObjectPoolConfig.setMinIdle(start.getMinIdle());
        return genericObjectPoolConfig;
    }
}
