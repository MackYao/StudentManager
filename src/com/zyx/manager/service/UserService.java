package com.zyx.manager.service;

import com.zyx.manager.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface UserService {
    public User findByUserName(String username);
    public int addUser(User user);
    public List<User> findList(Map<String ,Object> queryMap);
    public int getTotal(Map<String ,Object> queryMap);
    public int delete(String ids);
    public int editUser(User user);
}
