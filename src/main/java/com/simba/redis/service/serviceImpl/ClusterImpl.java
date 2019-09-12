package com.simba.redis.service.serviceImpl;

import com.simba.redis.model.Subscriber;
import com.simba.redis.service.Cluster;
import com.simba.redis.service.PubSub;
import com.simba.redis.util.InitRedis;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

/**
 * redis的集群实现类
 */
@Service
public class ClusterImpl implements Cluster {

    private final  static Log logger = LogFactory.getLog(ClusterImpl.class);

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

}
