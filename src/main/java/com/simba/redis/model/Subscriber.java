package com.simba.redis.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import redis.clients.jedis.JedisPubSub;

/**
 * 订阅者
 * 继承jedis的JedisPubSub抽象函数
 */
public class Subscriber extends JedisPubSub {

   private final static Log logger = LogFactory.getLog(Subscriber.class);

    public Subscriber(){
        super();
    }

    //实现抽象方法

    /**
     *接收到消息执行
     * @param channel
     * @param message
     */
    @Override
    public void onMessage(String channel, String message) {
        logger.info("订阅者收到消息："+channel+"->"+message);
    }

    /**
     * 订阅了channel后执行
     * @param channel
     * @param subscribedChannels
     */
    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        logger.info("订阅者取消了订阅频道："+channel);
    }

    /**
     * 取消了订阅后执行
     * @param channel
     * @param subscribedChannels
     */
    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        logger.info("订阅者取消了订阅频道："+channel);
    }

}
