package com.zyx.manager.service;

import com.zyx.manager.entity.Grade;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface GradeService {
    public int addGrade(Grade grade);
    public List<Grade> findList(Map<String ,Object> queryMap);
    public List<Grade> findAll();
    public int getTotal(Map<String ,Object> queryMap);
    public int delete(String ids);
    public int editGrade(Grade grade);
}
