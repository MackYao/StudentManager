package com.zyx.manager.controller;

import com.zyx.manager.entity.Grade;
import com.zyx.manager.page.Page;
import com.zyx.manager.service.GradeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@RequestMapping(value = "/grade")
@Controller
public class GradeController {

    @Autowired
    GradeService gradeService;

    /*
     *跳转 年级列表
     * */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(ModelAndView modelAndView){
        modelAndView.setViewName("grade/grade_list");
        return modelAndView;
    }

    /*
     * 获取年级列表
     * */
    @RequestMapping(value = "/get_list",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> getList(
            @RequestParam(value = "name",defaultValue = "",required = false) String name,
            Page page
    ){
        Map<String,Object> ret = new HashMap<>();
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("name","%"+name+"%");
        queryMap.put("offset",page.getOffset());
        queryMap.put("pageSize",page.getRows());
        //easyUI规定的数据格式 rows---数据,total---总数
        ret.put("rows",gradeService.findList(queryMap));
        ret.put("total",gradeService.getTotal(queryMap));
        return ret;
    }

    /*
    *
    * 增加年级
    * */
    @RequestMapping(value = "/add" , method = RequestMethod.POST)
    @ResponseBody
    public Map<String,String>add(Grade grade){
        Map<String,String> ret = new HashMap<>();
        if(grade==null){
            ret.put("type","error");
            ret.put("msg","数据绑定出错");
            return ret;
        }
        if(StringUtils.isEmpty(grade.getName())){
            ret.put("type","error");
            ret.put("msg","年级名不能未空");
            return ret;
        }
        if(gradeService.addGrade(grade)<=0){
            ret.put("type","error");
            ret.put("msg","添加失败");
            return ret;
        }
        ret.put("type","success");
        ret.put("msg","添加成功");
        return ret;
    }

    /*
    * 编辑年级信息
    * */
    @RequestMapping(value = "/edit" , method = RequestMethod.POST)
    @ResponseBody
    public Map<String,String>edit(Grade grade){
        Map<String,String> ret = new HashMap<>();
        if(grade==null){
            ret.put("type","error");
            ret.put("msg","数据绑定出错");
            return ret;
        }
        if(StringUtils.isEmpty(grade.getName())){
            ret.put("type","error");
            ret.put("msg","年级名不能未空");
            return ret;
        }
        if(gradeService.editGrade(grade)<=0){
            ret.put("type","error");
            ret.put("msg","修改失败");
            return ret;
        }
        ret.put("type","success");
        ret.put("msg","修改成功");
        return ret;
    }
    /*
    * 删除年级信息
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
            if(gradeService.delete(idsString)<=0){
                ret.put("type","error");
                ret.put("msg","删除失败");
            }
        }catch (Exception e){
            ret.put("type","error");
            ret.put("msg","该年级下存在班级信息,无法删除");
            return  ret;
        }

        ret.put("type","success");
        ret.put("msg","删除成功");
        return ret;
    }


}
