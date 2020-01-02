package edu.bjtu.android.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.bjtu.android.entity.Course;

@Component
public class CourseDao implements CourseMapper {

	@Autowired
	CourseMapper mapper;
	
	@Override
	public int deleteByPrimaryKey(String id) {
		// TODO Auto-generated method stub
		return mapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(Course record) {
		// TODO Auto-generated method stub
		return mapper.insert(record);
		
	}

	@Override
	public Course selectByPrimaryKey(String id) {
		// TODO Auto-generated method stub
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public List<Course> selectAll() {
		// TODO Auto-generated method stub
		return mapper.selectAll();
	}

	@Override
	public int updateByPrimaryKey(Course record) {
		// TODO Auto-generated method stub
		return mapper.updateByPrimaryKey(record);
		
	}

}
