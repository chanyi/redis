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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.xml.crypto.Data;

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
        dataOper.setStringIsNotExistWithExpire(RedisKey.setNotExistLock,setNotExistLock,ttl);
        try{
            bussCode();
        }finally {

        }


    }



}
