package com.zyx.manager.dao;

import com.zyx.manager.entity.Clazz;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ClazzDao {
    public int add(Clazz clazz);
    public int edit(Clazz clazz);
    public int delete(String ids);
    public List<Clazz> findAll();
    public List<Clazz> findList(Map<String, Object> queryMap);
    public int getTotal(Map<String, Object> queryMap);
}
