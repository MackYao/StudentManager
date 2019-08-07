package com.zyx.manager.dao;

import com.zyx.manager.entity.Grade;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface GradeDao {
    public int add(Grade grade);
    public int edit(Grade grade);
    public int delete(String ids);
    public List<Grade> findAll();
    public List<Grade> findList(Map<String,Object> queryMap);
    public int getTotal(Map<String,Object> queryMap);
}
