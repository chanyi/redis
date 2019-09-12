package com.simba.redis.util;

import com.simba.redis.model.configure.Start;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

@Component
public class InitRedisCluster {

    @Autowired
    private Start start;

    public void initRedisCluster() {
        System.out.println("初始化redisCluster");
        //创建jedisCluster对象，有一个参数 nodes是Set类型，Set包含若干个HostAndPort对象
        Set<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("127.0.0.1", 7001));
        nodes.add(new HostAndPort("127.0.0.1", 7002));
        nodes.add(new HostAndPort("127.0.0.1", 7003));
        nodes.add(new HostAndPort("127.0.0.1", 7004));
        nodes.add(new HostAndPort("127.0.0.1", 7005));
        nodes.add(new HostAndPort("127.0.0.1", 7006));
        JedisCluster jedisCluster = new JedisCluster(nodes);
        //使用jedisCluster操作redis,往集群中写入数据
        jedisCluster.set("key", "value");
        //从集群中读取数据
        String str = jedisCluster.get("key");
        System.out.println(str);
        //关闭连接池
        jedisCluster.close();
    }
}
