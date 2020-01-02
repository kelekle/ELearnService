package edu.bjtu.android.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import edu.bjtu.android.entity.Material;

@Component
public class MaterialDao implements MaterialMapper {

	@Autowired
	MaterialMapper mapper;

	@Override
	public int deleteByPrimaryKey(String id) {
		// TODO Auto-generated method stub
		return mapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(Material record) {
		// TODO Auto-generated method stub
		return mapper.insert(record);
	}

	@Override
	public Material selectByPrimaryKey(String id) {
		// TODO Auto-generated method stub
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public List<Material> selectAll() {
		// TODO Auto-generated method stub
		return mapper.selectAll();
	}

	@Override
	public int updateByPrimaryKey(Material record) {
		// TODO Auto-generated method stub
		return mapper.updateByPrimaryKey(record);
	}

	@Override
	public List<Material> selectByCourseId(String courseid) {
		// TODO Auto-generated method stub
		return mapper.selectByCourseId(courseid);
		
	}

}
