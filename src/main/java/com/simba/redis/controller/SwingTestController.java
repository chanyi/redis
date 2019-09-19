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

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/test")
public class SwingTestController {

	private final static Log logger = LogFactory.getLog(SwingTestController.class);


	@ResponseBody
	public static  void main( String[] args){
		logger.info("swing test");
		JFrame jFrame = new JFrame("my Frame");
		Container container = jFrame.getContentPane();
		JButton jButton1 = new JButton("my button1");
		jButton1.setBounds(50,20,100,20);
		JButton jButton2 = new JButton("my button2");

		jButton1.addActionListener(event->{
			System.out.println("button pressed");
		});

		container.add(jButton1);
		container.add(jButton2);
		jFrame.pack();
		jFrame.setVisible(true);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}
