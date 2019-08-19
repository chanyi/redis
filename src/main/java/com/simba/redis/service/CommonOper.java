package com.simba.redis.service;

import java.util.Map;
import java.util.Set;

/**
 * 数据操作接口
 */
public interface CommonOper {


    void  delKey(String key);

    /**
     * 删除多个key
     * @param keys
     */
    void delKeys(String keys);
    /**
     * 获取key的序列化值，磁盘中存储形式为序列化后的byte数组
     * @param key
     * @return
     */
    byte[] dump(String key);

    /**
     * 判断key是否存在，存在返回true，不存在返回false
     * @param key
     * @return
     */
    boolean existKey(String key);

    /**
     * 查看key的剩余过期时间，单位为s
     * 如果没有设置过期时间，返回-1
     * 如果已经过期，返回-2
     * @param key
     * @return
     */
    Long ttlKey(String key);

    /**
     * 查看key的过期时间，返回单位为毫秒
     * @param key
     * @return
     */
    Long ttlKeyMs(String key);

    /**
     * 设置key的过期时间，单位是s
     * @param key
     * @param seconds
     */
    void setKeyExpire(String key,int seconds);

    /**
     * 设置一个key的过期时间，参数为毫秒
     * @param key
     * @param millisSeconds
     */
    void setKeyExpireMs(String key,int millisSeconds);

    /**
     * 移除key的过期时间，变成永久有效
     * @param key
     */
    void removeKeyExpire(String key);

    /**
     * 查找key，可以使用通配符，*表示所有 ？表示站一位
     * @param pattern
     * @return
     */
    Set<String> getKeys(String pattern);

    /**
     * 从当前的数据库中随机返回一个key
     * @return
     */
    String getRandomKey();


    /**
     * 修改key的名字
     * @param oldName
     * @param newName
     */
    void renameKey(String oldName,String newName);


    /**
     * 将key移动到其他数据库中，
     * @param key
     * @param dbIndex 数据库的编号
     */
    void moveKey2OtherDb(String key,Integer dbIndex);


    /**
     * 返回key所存储的value值的类型
     * @param key
     * @return
     */
    String getValueType(String key);

}
