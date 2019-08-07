package com.zyx.manager.dao;

import com.zyx.manager.entity.Student;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface StudentDao {
    public int add(Student student);
    public int edit(Student student);
    public int delete(String ids);
    public List<Student> findAll();
    public List<Student> findList(Map<String, Object> queryMap);
    public int getTotal(Map<String, Object> queryMap);
    public Student findByUserName(String username);
}
