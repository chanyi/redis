package com.simba.redis.service.serviceImpl;

import com.simba.redis.service.CommonOper;
import com.simba.redis.service.DataOper;
import com.simba.redis.util.InitRedis;
import com.simba.redis.util.SerializeUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * 数据操作实现类
 */
public class CommonOperImpl implements CommonOper {

    private final  static Log logger = LogFactory.getLog(CommonOperImpl.class);

    @Autowired
    private InitRedis initRedis;

    /**
	 * 删除单个key
     * @param key
     */
    @Override
    public void  delKey(String key){
        handler(jedis -> jedis.del(key));
    }

    /**
     * 删除多个key
     * @param keys
     */
    @Override
    public void delKeys(String keys){
        handler(jedis -> jedis.del(keys));
    }

    /**
	 * 获取key的序列化值，磁盘中存储形式为序列化后的byte数组
     * @param key
	 * @return
	 */
    @Override
    public byte[] dump(String key){
        return handler(jedis -> jedis.dump(key));
    }

	/**
	 * 判断key是否存在，存在返回true，不存在返回false
     * @param key
	 * @return
	 */
    @Override
    public boolean existKey(String key){
        return handler(jedis -> jedis.exists(key));
    }

	/**
	 * 查看key的剩余过期时间，单位为s
     * 如果没有设置过期时间，返回-1
     * 如果已经过期，返回-2
     * @param key
	 * @return
	 */
    @Override
    public Long ttlKey(String key){
        return handler(jedis -> jedis.ttl(key));
    }

	/**
	 * 查看key的过期时间，返回单位为毫秒
     * @param key
	 * @return
	 */
    @Override
    public Long ttlKeyMs(String key){
        return  handler(jedis -> jedis.pttl(key));
    }

    /**
     * 设置key的过期时间，单位是s
     * @param key
     * @param seconds
     */
    @Override
    public void setKeyExpire(String key,int seconds){
        handler(jedis -> jedis.expire(key,seconds));
    }

    /**
	 * 设置一个key的过期时间，参数为毫秒
     * @param key
	 * @param millisSeconds
     */
    @Override
    public void setKeyExpireMs(String key,int millisSeconds){
        handler(jedis -> jedis.pexpire(key,millisSeconds));
    }

	/**
	 * 移除key的过期时间，变成永久有效
     * @param key
     */
    @Override
    public void removeKeyExpire(String key){
        handler(jedis -> jedis.persist(key));
    }

    /**
     * 查找key，可以使用通配符，*表示所有 ？表示站一位
     * 可以使用*查出所有当前数据库里的所有key
     * @param pattern
     * @return
     */
    @Override
    public Set<String> getKeys(String pattern){
        return handler(jedis -> jedis.keys(pattern));
    }

    /**
     * 从当前的数据库中随机返回一个key
     * @return
     */
    @Override
    public String getRandomKey(){
        return handler(jedis -> jedis.randomKey());
    }

    /**
     * 修改key的名字
     * @param oldName
     * @param newName
     */
    @Override
    public void renameKey(String oldName,String newName){
        handler(jedis -> jedis.rename(oldName,newName));
    }

    /**
     * 将key移动到其他数据库中，
     * @param key
     * @param dbIndex 数据库的编号
     */
    @Override
    public void moveKey2OtherDb(String key,Integer dbIndex){
        handler(jedis -> jedis.move(key,dbIndex));
    }

    /**
     * 返回key所存储的value值的类型
     * @param key
     * @return
     */
    @Override
    public String getValueType(String key){
        return  handler(jedis -> jedis.type(key));
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
