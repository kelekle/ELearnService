package edu.bjtu.android.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.bjtu.android.entity.*;
import edu.bjtu.android.dao.*;

@RestController
@RequestMapping("/elearn")
public class SimpleElearnService {
	@Autowired
	CourseDao courseDao;
	
	@Autowired
	MaterialDao materialDao;
	
	@Autowired
	TeacherDao teacherDao;
	

	@RequestMapping(method = RequestMethod.GET, path = "/courses", produces = "application/json")
	public List<Course> allCources() {
		return courseDao.selectAll();
	}

//	@RequestMapping(method = RequestMethod.PUT, path = "/addUser", consumes = "application/json")
//	public String addUser(@RequestBody User user) {
//		boolean isAdded = UserDao.insertUser(user);
//		String response = "";
//		if (isAdded) {
//			response = "Inserted Successfully!";
//		}
//		return response;
//	}

	@RequestMapping(method = RequestMethod.GET, path = "courses/{courseid}/materials", produces = "application/json")
	public List<Material> getMaterials(@PathVariable("courseid") String courseid) {
		List<Material> materials = materialDao.selectByCourseId(courseid);
		return materials;
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "courses/{courseid}/teachers", produces = "application/json")
	public List<Teacher> getTeacherbyCourseId(@PathVariable("courseid") String courseid) {
		List<Teacher> teacher = teacherDao.selectByCourseId(courseid);
		return teacher;
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/courses/{id}", produces = "application/json")
	public Course getCourse(@PathVariable("id") String id) {
		Course course = courseDao.selectByPrimaryKey(id);
		return course;
	}
}
