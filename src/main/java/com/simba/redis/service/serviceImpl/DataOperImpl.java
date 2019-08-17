package com.simba.redis.service.serviceImpl;

import com.simba.redis.service.DataOper;
import com.simba.redis.util.InitRedis;
import com.simba.redis.util.SerializeUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.Map;
import java.util.function.Function;

/**
 * 数据操作实现类
 */
public class DataOperImpl implements DataOper {

    private final  static Log logger = LogFactory.getLog(DataOperImpl.class);

    @Autowired
    private InitRedis initRedis;

    /**
     * get String
     * @param key
     * @return
     */
    @Override
    public String getString(String key){
        return handler(jedis -> jedis.get(key));
    }

    /**
     * set String
     * @param key
     * @param value
     */
    @Override
    public void setString(String key,String value){
        handler(jedis -> jedis.set(key,value));
    }

    /**
     * get object
     * @param key
     * @return
     */
    @Override
    public Object getObject(String key){
        return handler(jedis -> SerializeUtil.unserialize(jedis.get(key.getBytes())));

    }

    /**
     * set object
     * @param key
     * @param value
     */
    @Override
    public void setObject(String key,Object value){
         handler(jedis -> jedis.set(key.getBytes(),SerializeUtil.serialize(value)));
    }

    /**
     * set hash
     * @param key
     * @param hash
     */
    @Override
    public void setHash(String key,Map<String,String> hash){
        handler(jedis -> jedis.hset(key, hash));
    }

    /**
     * get hash
     * @param key
     * @return
     */
    @Override
    public String getHash(String tableName,String key){
       return handler(jedis -> jedis.hget(tableName,key));
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
