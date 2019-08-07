package com.zyx.manager.service;

import com.zyx.manager.entity.Student;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface StudentService {
    public int add(Student student);
    public List<Student> findList(Map<String, Object> queryMap);
    public List<Student> findAll();
    public int getTotal(Map<String, Object> queryMap);
    public int delete(String ids);
    public int edit(Student student);
    public Student findByUserName(String username);
}
