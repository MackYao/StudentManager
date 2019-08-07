package com.zyx.manager.service.impl;

import com.zyx.manager.dao.GradeDao;
import com.zyx.manager.entity.Grade;
import com.zyx.manager.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GradeServiceImpl implements GradeService {

    @Autowired
    private GradeDao gradeDao;

    @Override
    public int addGrade(Grade grade) {
        return gradeDao.add(grade);
    }

    @Override
    public List<Grade> findList(Map<String, Object> queryMap) {
        return gradeDao.findList(queryMap);
    }

    @Override
    public List<Grade> findAll() {
        return gradeDao.findAll();
    }

    @Override
    public int getTotal(Map<String, Object> queryMap) {
        return gradeDao.getTotal(queryMap);
    }

    @Override
    public int delete(String ids) {
        return gradeDao.delete(ids);
    }

    @Override
    public int editGrade(Grade grade) {
        return gradeDao.edit(grade);
    }
}
