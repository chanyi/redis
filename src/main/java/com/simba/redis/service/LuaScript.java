package com.simba.redis.service;

import com.simba.redis.model.Subscriber;

/**
 * 订阅发布接口
 */

public interface LuaScript {


    Object  doScript(String script ,int argsCount ,String... args);

}
