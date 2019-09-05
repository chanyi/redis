package com.simba.redis.service;

import com.simba.redis.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentResitory extends JpaRepository<Student,Integer> {

	//根据年龄来查询数据
	public List<Student> findByAge(Integer age);

	//根据年龄来查询数据
	public List<Student> findByName(String name);

 }