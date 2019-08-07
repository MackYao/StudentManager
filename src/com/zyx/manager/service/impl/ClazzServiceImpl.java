package com.zyx.manager.service.impl;

import com.zyx.manager.dao.ClazzDao;
import com.zyx.manager.entity.Clazz;
import com.zyx.manager.service.ClazzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ClazzServiceImpl implements ClazzService {

    @Autowired
    private ClazzDao clazzDao;

    @Override
    public int addClazz(Clazz clazz) {
        return clazzDao.add(clazz);
    }

    @Override
    public List<Clazz> findList(Map<String, Object> queryMap) {
        return clazzDao.findList(queryMap);
    }

    @Override
    public List<Clazz> findAll() {
        return clazzDao.findAll();
    }

    @Override
    public int getTotal(Map<String, Object> queryMap) {
        return clazzDao.getTotal(queryMap);
    }

    @Override
    public int delete(String ids) {
        return clazzDao.delete(ids);
    }

    @Override
    public int editClazz(Clazz clazz) {
        return clazzDao.edit(clazz);
    }
}
