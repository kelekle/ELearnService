package edu.bjtu.android.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.bjtu.android.entity.Teacher;

@Component
public class TeacherDao implements TeacherMapper {

	@Autowired
	TeacherMapper mapper;
	
	@Override
	public int deleteByPrimaryKey(String userid) {
		// TODO Auto-generated method stub
		return mapper.deleteByPrimaryKey(userid);
	}

	@Override
	public int insert(Teacher record) {
		// TODO Auto-generated method stub
		return mapper.insert(record);
	}

	@Override
	public Teacher selectByPrimaryKey(String userid) {
		// TODO Auto-generated method stub
		return mapper.selectByPrimaryKey(userid);
	}

	@Override
	public List<Teacher> selectAll() {
		// TODO Auto-generated method stub
		return mapper.selectAll();
	}

	@Override
	public int updateByPrimaryKey(Teacher record) {
		// TODO Auto-generated method stub
		return mapper.updateByPrimaryKey(record);
	}

	@Override
	public List<Teacher> selectByCourseId(String courseid) {
		// TODO Auto-generated method stub
		return mapper.selectByCourseId(courseid);
	}

}
