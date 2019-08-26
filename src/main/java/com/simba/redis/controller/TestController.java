package com.simba.redis.controller;

import com.simba.redis.model.Subscriber;
import com.simba.redis.service.PubSub;
import com.simba.redis.util.InitRedis;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import redis.clients.jedis.Jedis;

@Controller
@RequestMapping("/test")
public class TestController {

	private final static Log logger = LogFactory.getLog(TestController.class);

	private final static  String  testChannel = "testc";

	@Autowired
	private PubSub pubSub;

	@Autowired
	private InitRedis initRedis;


	RequestMapping("");
	@Test
	public void testPub(){
		System.out.println("启动发布消息-->");
		Subscriber subscriber = new Subscriber();
		pubSub.sub(subscriber,testChannel);
	}

	@Test
	public  void testSub(){
		System.out.println("启动订阅-->");
		pubSub.pub(testChannel,"testMessage");
	}

}
