package com.simba.redis.service;

import com.simba.redis.model.Subscriber;

import java.util.Set;

/**
 * 订阅发布接口
 */

public interface PubSub {


    void  sub(Subscriber subscriber,String... channels);

    void pub(String channel,String message);

}
