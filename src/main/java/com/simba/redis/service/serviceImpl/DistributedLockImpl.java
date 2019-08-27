package com.simba.redis.service.serviceImpl;

import com.simba.redis.model.Subscriber;
import com.simba.redis.model.constant.RedisKey;
import com.simba.redis.service.CommonOper;
import com.simba.redis.service.DataOper;
import com.simba.redis.service.DistributedLock;
import com.simba.redis.service.PubSub;
import com.simba.redis.util.InitRedis;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.xml.crypto.Data;
import java.util.UUID;

/**
 * 订阅发布实现类
 */
@Service
public class DistributedLockImpl implements DistributedLock {

    private final  static Log logger = LogFactory.getLog(DistributedLockImpl.class);

    private final  static  int totalCount = 100;

    private final  static  String setNotExistLock = "lock";

    private final  static  int ttl = 30;

    @Autowired
    private InitRedis initRedis;

    @Autowired
    private DataOper dataOper;

    @Autowired
    private CommonOper commonOper;




    /**
     * 使用redis的特性实现一个分布式锁
     */
    @Override
    public void  lock(){

        init();
        bussCode();

    }



    //初始化
    private void init(){
        dataOper.setString(RedisKey.decreaseCount,totalCount+"");
    }

    //业务代码
    private  void bussCode(){
        //总数递减(非原子操作)
        int count = Integer.parseInt(dataOper.getString(RedisKey.decreaseCount));
        if(count >0){
            count --;
            logger.info("当前数量："+count);
            dataOper.setString(RedisKey.decreaseCount,count+"");
        }else{
            logger.info("数量以小于0");
        }
    }

    //适合于单机部署,不适合分布式环境
    private void syncBussCode(){
        synchronized (this){
            bussCode();
        }
    }

    //使用redis的setnx函数完成初步的分布式锁
    private void distributBussCode1(){
        //设置一个简单的redis锁
        dataOper.setStringIsNotExistWithExpire(RedisKey.setNotExistLock,setNotExistLock,ttl);
        try{
            bussCode();
        }finally {
            commonOper.delKey(RedisKey.setNotExistLock);
        }
    }

    //考虑到程序运行时间超过固定的过期时间情况，
    // 导致后一个锁被前一个锁del,所以增加判断，每次执行只能del自己的锁
    private void distributBussCode2(){
        String uuid = UUID.randomUUID().toString();
        dataOper.setStringIsNotExistWithExpire(RedisKey.setNotExistLock,uuid,ttl);
        try{
            bussCode();
        }finally {
            //只能删除自己set的锁
            Object lock = dataOper.getString(RedisKey.setNotExistLock);
            if(lock !=null && uuid.equals(lock.toString())){
                commonOper.delKey(RedisKey.setNotExistLock);
            }
        }
    }

    //使用redisson实现一个分布式锁
    private void redissonDistributLock(){
        Config redissonConfig = new Config();
        //单例情况
        redissonConfig.useSingleServer().setAddress("127.0.0.1:6379");
        //集群情况,先设置扫描间隔毫秒数，再添加节点
//        redissonConfig.useClusterServers().setScanInterval(2000).addNodeAddress("127.0.0.1:6379").addNodeAddress("127.0.0.1:6380").addNodeAddress("127.0.0.1:6381");
        //哨兵模式情况，
//        redissonConfig.useSentinelServers().setMasterName("masterName").addSentinelAddress("127.0.0.1:6379").addSentinelAddress("127.0.0.1:6380").addSentinelAddress("127.0.0.1:6381");
        //主从模式情况
//        redissonConfig.useMasterSlaveServers().setMasterAddress("127.0.0.1:6379").addSlaveAddress("127.0.0.1:6380").addSlaveAddress("127.0.0.1:6381").addSlaveAddress("127.0.0.1:6382");

        RedissonClient redissonClient = Redisson.create(redissonConfig);
        RLock rLock = redissonClient.getLock(RedisKey.setNotExistLock);
        rLock.tryLock();
        rLock.unlock();

    }



}
