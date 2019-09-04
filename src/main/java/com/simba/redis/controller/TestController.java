package com.simba.redis.controller;

import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
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

import java.util.ArrayList;
import java.util.List;

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

	@Test
	@RequestMapping("/testBloomFilter")
	public  void testBloomFilter(){
		logger.info("测试bloom算法-->");
		//初始化bloom过滤器，将自己的数据存储在bloom中
		List<String> list = new ArrayList<String>();//自定义的数据
		Double tpp = 0.0003;//误判率，1万个由三个误判
		BloomFilter bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charsets.UTF_8),list.size(),tpp);
		for(int i = 0;i<list.size();i++){
			bloomFilter.put(list.get(i));
		}
		int count =0;
		//验证一个key是否存在bloomFilter里
		for(int i=0;i<list.size();i++){
			String testKey = "testKey"+i;
			if(bloomFilter.mightContain(testKey)){
				//过滤器检测到可能包含
				count ++;
			}
		}
		//计算出多少个可能存在

	}
}
