package com.zyx.manager.controller;

import com.zyx.manager.entity.Clazz;
import com.zyx.manager.entity.Grade;
import com.zyx.manager.page.Page;
import com.zyx.manager.service.ClazzService;
import com.zyx.manager.service.GradeService;
import net.sf.json.JSONArray;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* 班级信息管理
* */
@RequestMapping(value = "/clazz")
@Controller
public class ClazzController {

    @Autowired
    GradeService gradeService;
    @Autowired
    ClazzService clazzService;
    /*
     *跳转班级列表
     * */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(ModelAndView modelAndView){
        modelAndView.setViewName("clazz/clazz_list");
        List<Grade> all = gradeService.findAll();
        modelAndView.addObject("gradeList",all);
        modelAndView.addObject("gradeListJson", JSONArray.fromObject(all));
        return modelAndView;
    }

    /*
     * 获取班级列表
     * */
    @RequestMapping(value = "/get_list",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> getList(
            @RequestParam(value = "name",defaultValue = "",required = false) String name,
            @RequestParam(value = "gradeId",required = false) Long gradeId,
            Page page
    ){
        Map<String,Object> ret = new HashMap<>();
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("name","%"+name+"%");
        if(gradeId!=null){
            queryMap.put("gradeId",gradeId);
        }
        queryMap.put("offset",page.getOffset());
        queryMap.put("pageSize",page.getRows());
        //easyUI规定的数据格式 rows---数据,total---总数
        ret.put("rows",clazzService.findList(queryMap));
        ret.put("total",clazzService.getTotal(queryMap));
        return ret;
    }

    /*
    *
    * 增加班级
    * */
    @RequestMapping(value = "/add" , method = RequestMethod.POST)
    @ResponseBody
    public Map<String,String>add(Clazz clazz){
        Map<String,String> ret = new HashMap<>();
        if(clazz==null){
            ret.put("type","error");
            ret.put("msg","数据绑定出错");
            return ret;
        }
        if(clazz.getGradeId()==0){
            ret.put("type","error");
            ret.put("msg","所属班级不能为空");
            return ret;
        }
        if(StringUtils.isEmpty(clazz.getName())){
            ret.put("type","error");
            ret.put("msg","班级名不能未空");
            return ret;
        }
        if(clazzService.addClazz(clazz)<=0){
            ret.put("type","error");
            ret.put("msg","添加失败");
            return ret;
        }
        ret.put("type","success");
        ret.put("msg","添加成功");
        return ret;
    }

    /*
    * 编辑级信息
    * */
    @RequestMapping(value = "/edit" , method = RequestMethod.POST)
    @ResponseBody
    public Map<String,String>edit(Clazz clazz){
        Map<String,String> ret = new HashMap<>();
        if(clazz==null){
            ret.put("type","error");
            ret.put("msg","数据绑定出错");
            return ret;
        }
        if(StringUtils.isEmpty(clazz.getName())){
            ret.put("type","error");
            ret.put("msg","班级名不能未空");
            return ret;
        }
        if(clazz.getGradeId()==0){
            ret.put("type","error");
            ret.put("msg","所属班级不能未空");
            return ret;
        }
        if(clazzService.editClazz(clazz)<=0){
            ret.put("type","error");
            ret.put("msg","修改失败");
            return ret;
        }
        ret.put("type","success");
        ret.put("msg","修改成功");
        return ret;
    }
    /*
    * 删除班级信息
    * */
    @RequestMapping(value = "/delete" , method = RequestMethod.POST)
    @ResponseBody
    public Map<String ,Object>delete(
            @RequestParam(value = "ids[]",required = true) Long[] ids)
    {
        Map<String,Object> ret = new HashMap<>();
        if(ids==null){
            ret.put("type","error");
            ret.put("msg","请选择删除的数据");
        }
        String idsString = "";
        for (long id:ids){
            idsString += id +",";
        }
        idsString = idsString.substring(0,idsString.length()-1);
        try {
            if(clazzService.delete(idsString)<=0){
                ret.put("type","error");
                ret.put("msg","删除失败");
            }
        }catch (Exception e){
            ret.put("type","error");
            ret.put("msg","该班级下存在学生信息,无法删除");
            return  ret;
        }

        ret.put("type","success");
        ret.put("msg","删除成功");
        return ret;
    }


}
