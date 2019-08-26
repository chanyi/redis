package com.simba.redis.service.serviceImpl;

import com.simba.redis.model.Subscriber;
import com.simba.redis.service.CommonOper;
import com.simba.redis.service.PubSub;
import com.simba.redis.util.InitRedis;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.Set;
import java.util.function.Function;

/**
 * 订阅发布实现类
 */
@Service
public class PubSubImpl implements PubSub {

    private final  static Log logger = LogFactory.getLog(PubSubImpl.class);

    @Autowired
    private InitRedis initRedis;

    /**
	 * 订阅
     * @param channels
     */
    @Override
    public void  sub(Subscriber subscriber ,String... channels){
        Jedis jedis = initRedis.getJedis();
        try{
            jedis.subscribe(subscriber,channels);
        }catch (Exception e){
    	    logger.info("订阅出错");

        }finally {
    	    jedis.close();
        }

    }

    /**
     * 发布
     * @param channel
     * @param message
     */
    @Override
    public void pub(String channel,String message){
        Jedis jedis = initRedis.getJedis();
        try{
            logger.info("频道："+channel+"===发送消息："+message);
            Long pubCount = jedis.publish(channel,message);
            logger.info("频道："+channel+"===返回："+pubCount);
        }catch (Exception e){
            logger.info("发布出错");
            return;
        }finally {
            jedis.close();
        }
        return;
    }

}
