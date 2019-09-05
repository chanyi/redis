package com.simba.redis.controller;

import com.simba.redis.model.Student;
import com.simba.redis.service.StudentResitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StudentController {

	@Autowired
	private StudentResitory studentResitory;

	/**
	 * 查询数据库中的所有
	 *
	 * @return
	 */
	@GetMapping(value = "/hello")
	public List<Student> getStuList() {
		return studentResitory.findAll();
	}


	/**
	 * 添加一个学生记录
	 */
	@PostMapping(value = "/hello")
	public Student addStu(@RequestParam("name") String name, @RequestParam("age") Integer age) {
		Student stu = new Student();
		stu.setName(name);
		stu.setAge(age);
		return studentResitory.save(stu);
	}

	/**
	 * 查询一个学生，根据字段id
	 */
	@GetMapping(value = "/hello/{id}")
	public Student getStu(@PathVariable("id") Integer id) {
//		return studentResitory.findOne(id);
		return  new Student();
	}

	/**
	 * 更新数据库
	 */
	@PutMapping(value = "/hello/{id}")
	public Student updateStu(@PathVariable("id") Integer id,
							 @RequestParam("name") String name,
							 @RequestParam("age") Integer age) {
		Student stu = new Student();
		stu.setId(id);
		stu.setName(name);
		stu.setAge(age);
		return studentResitory.save(stu);
	}

	/**
	 * 删除数据
	 */
	@DeleteMapping(value = "/hello/{id}")
	public void deleteStu(@PathVariable("id") Integer id) {
//		studentResitory.delete(id);
	}

	/**
	 * 根据年龄进行查询
	 */
	@GetMapping(value = "/hello/age/{age}")
	public List<Student> getStuList(@PathVariable("age") Integer age) {
		return studentResitory.findByAge(age);
	}
}