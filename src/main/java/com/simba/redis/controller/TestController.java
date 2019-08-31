package com.simba.redis.controller;

import com.simba.redis.model.Subscriber;
import com.simba.redis.service.LuaScript;
import com.simba.redis.service.PubSub;
import com.simba.redis.util.InitRedis;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

@Controller
@RequestMapping("/test")
public class TestController {

	private final static Log logger = LogFactory.getLog(TestController.class);

	private final static  String  testChannel = "testc";

	@Autowired
	private PubSub pubSub;

	@Autowired
	private LuaScript luaScript;

	@Autowired
	private InitRedis initRedis;

	@ResponseBody
	@RequestMapping("/test")
	public String test( ){
		logger.info("test");
		return "testsd";
	}

	@Test
	@RequestMapping("/testPub")
	public void testPub(String message){
		logger.info("启动发布消息-->");
		pubSub.pub(testChannel,message);
	}

	@Test
    @RequestMapping("/testSub")
	public  void testSub(){
		logger.info("启动订阅-->");
		Subscriber subscriber = new Subscriber();
		pubSub.sub(subscriber,testChannel);
	}


	@Test
	@RequestMapping("/testLua")
	public  void testLuab(){
		logger.info("测试Lua脚本-->");
		//lua脚本，可以定义变量也可以写入文本文件中
		String script = "";
		luaScript.doScript(script,2,"test1","test2");
	}
}
