package com.simba.redis.service.serviceImpl;

import com.simba.redis.model.Student;
import com.simba.redis.model.Subscriber;
import com.simba.redis.service.Avalanche;
import com.simba.redis.service.DataOper;
import com.simba.redis.service.PubSub;
import com.simba.redis.service.StudentResitory;
import com.simba.redis.util.InitRedis;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 订阅发布实现类
 */
@Service
public class AvalancheImpl implements Avalanche {

    private static final Log logger = LogFactory.getLog(AvalancheImpl.class);

    private static final String TestKey = "test";

    private static final int ThreadCount = 10000;

    private CountDownLatch countDownLatch = new CountDownLatch(ThreadCount);

    @Autowired
    private InitRedis initRedis;

    @Autowired
    private DataOper dataOper;

    @Autowired
    private StudentResitory studentResitory;

    //处理的问题描述：在一个时间点大量缓存或者热点的缓存时间过期，导致大量的请求直接访问到数据库中
    //以上问题即为缓存雪崩，为了处理此问题，可以使用一下多种解决方案
    //

    /**
     * 执行多线程的测试，模拟高并发的实际场景
     */
    @Test
    public void concurrntTest(){
        //新建1w个线程
        Thread[] threads = new Thread[ThreadCount];
        for(int i=0;i<threads.length;i++){
            Thread thread = new Thread(()->{
                try{
                    //当countDownLatch为0的时候，所有线程一起执行start命令
                    countDownLatch.await();
                    dealWithLock();
                }catch (Exception e){
                    logger.info(e);
                }
            });
            threads[i] = thread;
            thread.start();
            //每执行一次countDownLatch减1
            countDownLatch.countDown();
        }
    }

    /**
	 * 使用锁的方式处理缓存雪崩
     * 优点：简单有效，使用范围广
     * 缺点：锁阻塞其他线程，导致其他没有抢到锁的用户等待时间久，体验不好
     *      锁的颗粒度太粗，所有缓存重建请求被一把锁阻塞。不同的请求参数分开处理。
     * 优化： 1、粒度由粗变细，根据不同的业务分析，对不同的参数控制锁的粒度
     *       2、使用非阻塞锁
     */
    @Override
    public String  dealWithLock() throws InterruptedException{
        Lock lock = new ReentrantLock();
        lock.lock();//获取一个锁
//        lock.tryLock();//尝试是否可以获取到锁，不等待
//        lock.tryLock(1000, TimeUnit.MILLISECONDS);//尝试获取锁，自定义等待时间
        //从缓存中获取一个值
        String value = dataOper.getString(TestKey);
        if(value != null){
            logger.info("data from redis");
            return value;
        }
        //如果缓存中没有，则加锁，让大量的请求进行排队，
        try{
            //先去缓存拿，第一个抢到锁的线程已经将数据放入了缓存中。
            String redisValue = dataOper.getString(TestKey);
            if(redisValue != null){
                logger.info("data from redis");
                return redisValue;
            }
            //拿到锁的线程去数据表中查询
            String dbValue = studentResitory.getOne(1).toString();
            //拿到之后放入缓存，设置过期时间为一分钟
            dataOper.setStringIsNotExistWithExpire(TestKey,dbValue,60);


        }finally {
            lock.unlock();//释放一个锁
        }

        return "";
    }


    /**
     * 优化锁控制并发访问
     * 对每一个参数（一个参数就是一种情况，有的情况需要高并发处理，有的则不需要）
     * 按照不同的参数，细分锁
     * 优点：灵活多变，根据业务的情况自行处理
     * 缺点：降级策略要和业务紧密相关，要求较高。如果使用备份缓存的话，需要考虑到数据的一致性
     *
     * @return
     * @throws InterruptedException
     */
    @Override
    public String  dealWithLockOptimize() throws InterruptedException{
        String name = "";

        //key存放每一个不同的参数，value存放这个是否抢到锁
        Map<String,String> lockMap =  new ConcurrentHashMap<>();
        boolean lock =false;

        //从缓存中获取一个值
        String value = dataOper.getString(TestKey);
        if(value != null){
            logger.info("data from redis");
            return value;
        }
        //如果缓存中没有，则加锁，让大量的请求进行排队，
        try{
            //此方法类似于redis的setnx,如果已经有值，返回当前的值，如果没有设置值，返回null
            lock = lockMap.putIfAbsent(name,"true") == null ? true:false;
            if(lock){
                //先去缓存拿，第一个抢到锁的线程已经将数据放入了缓存中。
                String redisValue = dataOper.getString(TestKey);
                if(redisValue != null){
                    logger.info("data from redis");
                    return redisValue;
                }
                //拿到锁的线程去数据表中查询
                String dbValue = studentResitory.findByName(name).toString();
                //拿到之后放入缓存，设置过期时间为一分钟
                dataOper.setStringIsNotExistWithExpire(TestKey,dbValue,60);
            }else{
                //没拿到锁的情况处理
                //方案1：缓存降级。有的情况下可以不用一定要拿到锁，可以直接返回，可以返回一个固定的值,或者从备份缓存中拿
                return "default";
                //方案2：

            }
        }finally {
            //清除锁
            lockMap.remove(name);
        }

        return "";
    }

}
