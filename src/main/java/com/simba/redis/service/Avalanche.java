package com.simba.redis.service;

import com.simba.redis.model.Subscriber;

/**
 * 订阅发布接口
 */

public interface Avalanche {


    String  dealWithLock()throws InterruptedException;

    public String  dealWithLockOptimize() throws InterruptedException;

}
