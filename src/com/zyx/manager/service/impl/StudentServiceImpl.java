package com.zyx.manager.service.impl;

import com.zyx.manager.dao.StudentDao;
import com.zyx.manager.entity.Student;
import com.zyx.manager.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private  StudentDao studentDao;

    @Override
    public int add(Student student) {
        return studentDao.add(student);
    }

    @Override
    public List<Student> findList(Map<String, Object> queryMap) {
        return studentDao.findList(queryMap);
    }

    @Override
    public List<Student> findAll() {
        return studentDao.findAll();
    }

    @Override
    public int getTotal(Map<String, Object> queryMap) {
        return studentDao.getTotal(queryMap);
    }

    @Override
    public int delete(String ids) {
        return studentDao.delete(ids);
    }

    @Override
    public int edit(Student student) {
        return studentDao.edit(student);
    }

    @Override
    public Student findByUserName(String username) {
        return studentDao.findByUserName(username);
    }
}
