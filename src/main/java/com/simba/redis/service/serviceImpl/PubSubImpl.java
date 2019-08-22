package com.simba.redis.service.serviceImpl;

import com.simba.redis.model.Subscriber;
import com.simba.redis.service.CommonOper;
import com.simba.redis.service.PubSub;
import com.simba.redis.util.InitRedis;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.Set;
import java.util.function.Function;

/**
 * 订阅发布实现类
 */
public class PubSubImpl implements PubSub {

    private final  static Log logger = LogFactory.getLog(PubSubImpl.class);

    @Autowired
    private InitRedis initRedis;

//    private Subscriber subscriber = new Subscriber();

    /**
	 * 订阅
     * @param channel
     */
    @Override
    public void  sub(JedisPubSub jedisPubSub,String... channel){

    	handler(jedis -> jedis.subscribe(jedisPubSub,channel));
    }




    private <T> T handler(Function<Jedis,T> fun){
        Jedis jedis = null;
        try{
            jedis =initRedis.getJedis();
            return fun.apply(jedis);
        }catch (Exception e){
            logger.info("redis init failed" + e.getMessage());
            throw new RuntimeException(e);
        }finally {
            jedis.close();
        }
    }
}
