package com.simba.redis.service.serviceImpl;

import com.simba.redis.model.Subscriber;
import com.simba.redis.service.Cluster;
import com.simba.redis.service.Search;
import com.simba.redis.util.InitRedis;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

/**
 * redis的集群实现类
 */
@Service
public class SearchImpl implements Search {

    private final  static Log logger = LogFactory.getLog(SearchImpl.class);

    @Autowired
    private InitRedis initRedis;

    /**
     * 扫描数据
     */
    public void  scan(){
        Jedis jedis = initRedis.getJedis();
        try{
            String key = "";
            ScanParams scanParams = new ScanParams();
            ScanResult<String> scanResult = jedis.scan(key,scanParams);

        }catch (Exception e){
    	    logger.info("订阅出错");

        }finally {
    	    jedis.close();
        }

    }

}
