package com.simba.redis.service.serviceImpl;

import com.simba.redis.model.Subscriber;
import com.simba.redis.service.LuaScript;
import com.simba.redis.service.PubSub;
import com.simba.redis.util.InitRedis;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

/**
 * 订阅发布实现类
 */
@Service
public class LuaScriptImpl implements LuaScript {

    private final  static Log logger = LogFactory.getLog(LuaScriptImpl.class);

    @Autowired
    private InitRedis initRedis;

    /**
     * 执行lua脚本
     * @param script，脚本
     * @param keyCount 参数个数
     * @param args 参数
     */
    @Override
    public Object  doScript(String script ,int keyCount ,String... args){
        Jedis jedis = initRedis.getJedis();
        Object object = new Object();
        try{
            object = jedis.eval(script,keyCount,args);

        }catch (Exception e){
            logger.info("订阅出错");

        }finally {
            jedis.close();
        }
        return  object;
    }

}
