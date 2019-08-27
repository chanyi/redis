package com.simba.redis.service.serviceImpl;

import com.simba.redis.service.DataOper;
import com.simba.redis.util.InitRedis;
import com.simba.redis.util.SerializeUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.swing.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * 数据操作实现类
 */

@Component
public class DataOperImpl implements DataOper {

    private final  static Log logger = LogFactory.getLog(DataOperImpl.class);

    @Autowired
    private InitRedis initRedis;

    ///////////////////////////////////////////  string操作  /////////////////////////////////////////////////////
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
     * 给不存在的key赋值，如果存在，赋值不成功
     * 主要用于解决分布式锁
     * @param key
     * @param value
     */
    @Override
    public void setStringIsNotExist(String key,String value){
        handler(jedis -> jedis.setnx(key,value));
    }

    @Override
    public  void setStringIsNotExistWithExpire(String key,String value,int seconds){
        handler(jedis -> {
            jedis.setnx(key,value);
            return jedis.expire(key,seconds);
        });

    }

    /**
     * 取一个值的一部分，传入key，起始位和结束位（从0开始）
     * @param key
     * @param startOfferset
     * @param endOfferset
     * @return
     */
    @Override
    public String getRangeValue(String key,int startOfferset,int endOfferset){
        return handler(jedis -> jedis.getrange(key,startOfferset,endOfferset));
    }

    /**
     * 返回旧值，同时赋给新值
     * @param key
     * @param newValue
     * @return
     */
    @Override
    public String getAndset(String key,String newValue){
        return  handler(jedis -> jedis.getSet(key,newValue));
    }

    /**
     * 返回值的长度
     * @param key
     * @return
     *
     */
    @Override
    public Long getValueLength(String key){
        return handler(jedis -> jedis.strlen(key));
    }

    /**
     * 值自增1，返回增后的值
     * 具有原子性，多线程的情况下优先考虑使用
     * @param key
     */
    @Override
    public Long increaseValue(String key){
        return handler(jedis -> jedis.incr(key));
    }

    /**
     * 自定义 值自增的大小,返回增后的值
     * 具有原子性，多线程的情况下优先考虑使用
     * @param key
     */
    @Override
    public Long increaseValueBy(String key ,int num){
        return handler(jedis -> jedis.incrBy(key,num));
    }

    /**
     * 值自减1，返回减后的值
     * 具有原子性，多线程的情况下优先考虑使用
     * @param key
     */
    @Override
    public Long decrease(String key){
        return handler(jedis -> jedis.decr(key));
    }


    /**
     * 自定义 值自减 的大小，返回减后的值
     * 具有原子性，多线程的情况下优先考虑使用
     * @param key
     */
    @Override
    public Long decrease(String key,int num){
        return handler(jedis -> jedis.decrBy(key,num));
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



    ///////////////////////////////////////////  hash操作  /////////////////////////////////////////////////////
    /**
     * 设置hash值，通过表名，列名，值
     * @param tableName
     * @param field
     * @param value
     */
    @Override
    public void setHash(String tableName,String field,String value){
        handler(jedis -> jedis.hset(tableName,field,value));
    }


    /**
     * 设置hash值，通过map写入
     * @param key
     * @param hash
     */
    @Override
    public void setHashWithMap(String key,Map<String,String> hash){
        handler(jedis -> jedis.hset(key, hash));
    }

    /**
     * 获取 hash
     * @param field
     * @return
     */
    @Override
    public String getHash(String tableName,String field){
        return handler(jedis -> jedis.hget(tableName,field));
    }

    /**
     * 获取hash的多个列的值
     * @param tableName
     * @param field
     * @return
     */
    @Override
    public List<String> getHashMuiltValue(String tableName, String ... field){
        return handler(jedis -> jedis.hmget(tableName,field));
    }

    /**
     * 获取hash的所有的值，返回map
     * @param tableName
     * @return
     */
    @Override
    public Map<String,String> getHashAllValue(String tableName){
        return handler(jedis -> jedis.hgetAll(tableName));
    }

    /**
     * 获取hash的所有的field
     * @param tableName
     * @return
     */
    @Override
    public Set<String> getHashAllFields(String tableName){
        return handler(jedis -> jedis.hkeys(tableName));
    }

    /**
     * 获取hash的field的数量
     * @param tableName
     * @return
     */
    @Override
    public Long getHashFieldCount(String tableName){
        return handler(jedis -> jedis.hlen(tableName));
    }

    /**
     * 删除hash的多个field
     * @param tableName
     * @param field
     */
    @Override
    public void delHashField(String tableName,String... field){
        handler(jedis -> jedis.hdel(tableName,field));
    }

    /**
     * 自增hash的field值，返回增加结果
     * @param tableName
     * @param field
     * @param num
     * @return
     */
    @Override
    public Long increaseHashFieldValue(String tableName,String  field,Long num){
        return handler(jedis -> jedis.hincrBy(tableName,field,num));
    }

    /**
     * 判断一个hash的field是否存在
     * @param tableName
     * @param field
     * @return
     */
    @Override
    public Boolean decreaseHashFieldValue(String tableName,String field){
        return handler(jedis -> jedis.hexists(tableName,field));
    }


    ///////////////////////////////////////////  set操作  /////////////////////////////////////////////////////


    /**
     * 元素添加进集合
     * @param key
     * @param value
     * @return
     */
    @Override
    public Long addSet(String key,String... value){

        return handler(jedis -> jedis.sadd(key,value));
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

    private <T> T handlerMulti(){

    }
}
