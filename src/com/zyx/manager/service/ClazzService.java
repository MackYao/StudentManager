package com.zyx.manager.service;

import com.zyx.manager.entity.Clazz;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ClazzService {
    public int addClazz(Clazz clazz);
    public List<Clazz> findList(Map<String, Object> queryMap);
    public List<Clazz> findAll();
    public int getTotal(Map<String, Object> queryMap);
    public int delete(String ids);
    public int editClazz(Clazz clazz);
}
