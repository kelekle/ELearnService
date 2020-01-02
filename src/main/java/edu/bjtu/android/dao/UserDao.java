package edu.bjtu.android.dao;

import edu.bjtu.android.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDao implements UserMapper{

    @Autowired
    UserMapper userMapper;

    @Override
    public int deleteByPrimaryKey(Integer userid) {
        return userMapper.deleteByPrimaryKey(userid);
    }

    @Override
    public int insert(User record) {
        return userMapper.insert(record);
    }

    @Override
    public User selectByPrimaryKey(Integer userid) {
        return userMapper.selectByPrimaryKey(userid);
    }

    @Override
    public List<User> selectAll() {
        return userMapper.selectAll();
    }

    @Override
    public int updateByPrimaryKey(User record) {
        return userMapper.updateByPrimaryKey(record);
    }

    @Override
    public int insertByEmail(String email, String password) {
        return userMapper.insertByEmail(email, password);
    }

    @Override
    public User selectByEmail(String email) {
        return userMapper.selectByEmail(email);
    }

    @Override
    public int changePasswordByEmail(String email, String password) {
        return userMapper.changePasswordByEmail(email, password);
    }

    @Override
    public int changeUsernameByEmail(String email, String username) {
        return userMapper.changeUsernameByEmail(email, username);
    }

    @Override
    public int changeDescriptionByEmail(String email, String description) {
        return userMapper.changeDescriptionByEmail(email, description);
    }

    @Override
    public int changePhotoByEmail(String email, String photo) {
        return userMapper.changePhotoByEmail(email, photo);
    }


}
