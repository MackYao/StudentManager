package com.zyx.manager.service.impl;

import com.zyx.manager.dao.UserDao;
import com.zyx.manager.entity.User;
import com.zyx.manager.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao ;
    @Override
    public User findByUserName(String username) {
        return userDao.findByUserName(username);
    }

    @Override
    public int addUser(User user) {
        return userDao.add(user);
    }

    @Override
    public List<User> findList(Map<String ,Object> queryMap) {
        return userDao.findList(queryMap);
    }

    @Override
    public int getTotal(Map<String, Object> queryMap) {
        return userDao.getTotal(queryMap);
    }

    @Override
    public int delete(String ids) {
        return userDao.delete(ids);
    }

    @Override
    public int editUser(User user) {
        return userDao.edit(user);
    }
}
