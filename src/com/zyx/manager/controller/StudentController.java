package com.zyx.manager.controller;

import com.zyx.manager.entity.Clazz;
import com.zyx.manager.entity.Grade;
import com.zyx.manager.entity.Student;
import com.zyx.manager.page.Page;
import com.zyx.manager.service.ClazzService;
import com.zyx.manager.service.GradeService;
import com.zyx.manager.service.StudentService;
import com.zyx.manager.util.StringUtil;
import net.sf.json.JSONArray;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.print.DocFlavor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* 学生信息管理
* */
@RequestMapping(value = "/student")
@Controller
public class StudentController {

    @Autowired
    StudentService studentService;
    @Autowired
    ClazzService clazzService;
    /*
     *跳转学生列表
     * */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(ModelAndView modelAndView){
        modelAndView.setViewName("student/student_list");
        List<Clazz> clazzAll = clazzService.findAll();
        modelAndView.addObject("clazzList",clazzAll);
        modelAndView.addObject("clazzListJson", JSONArray.fromObject(clazzAll));
        return modelAndView;
    }

    /*
     * 获取学生列表
     * */
    @RequestMapping(value = "/get_list",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> getList(
            @RequestParam(value = "name",defaultValue = "",required = false) String name,
            @RequestParam(value = "clazzId",required = false) Long clazzId,
            HttpServletRequest request,
            Page page
    ){
        Map<String,Object> ret = new HashMap<>();
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("username","%"+name+"%");
        Object userType = request.getSession().getAttribute("userType");
        if("2".equals(userType.toString())){
//            说明是学生
            Student loginedStudent = (Student) request.getSession().getAttribute("user");
            queryMap.put("username","%"+loginedStudent.getUsername()+"%");
        }
        if(clazzId!=null){
            queryMap.put("clazzId",clazzId);
        }
        queryMap.put("offset",page.getOffset());
        queryMap.put("pageSize",page.getRows());
        //easyUI规定的数据格式 rows---数据,total---总数
        ret.put("rows",studentService.findList(queryMap));
        ret.put("total",studentService.getTotal(queryMap));
        return ret;
    }


    /*
    * 上传图片
    * */
    @RequestMapping(value = "upload_photo",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,String> uploadPhoto(MultipartFile photo,
                                          HttpServletRequest request,
                                          HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        Map<String,String> ret = new HashMap<>();
        if(photo == null){
            ret.put("type","error");
            ret.put("msg","请选择文件");
            return  ret;
        }
        if(photo.getSize()>10458760){
            ret.put("type","error");
            ret.put("msg","图片大小不能超过10m");
            return  ret;
        }
        String suffix = photo.getOriginalFilename().substring(photo.getOriginalFilename().lastIndexOf(".")+1,photo.getOriginalFilename().length());
        if(!"jpg,png,gif,jpeg".contains(suffix.toLowerCase())){
            ret.put("type","error");
            ret.put("msg","文件格式错误,仅支持jpg,png,gif,jpeg");
            return  ret;
        }
        String savePath =  request.getServletContext().getRealPath("/")+"\\upload\\";
        File savePathFile = new File(savePath);
        if(!savePathFile.exists()){
            savePathFile.mkdir();//若不存在则创建文件夹
        }
        String fileName = new Date().getTime()+"."+suffix;
        //将图片转存到服务器
        photo.transferTo(new File(savePath+fileName));
        ret.put("type","success");
        ret.put("src",request.getServletContext().getContextPath()+"/upload/"+fileName);
        ret.put("msg","上传成功");
        return ret;
    }

    /*
    *
    * 增加学生
    * */
    @RequestMapping(value = "/add" , method = RequestMethod.POST)
    @ResponseBody
    public Map<String,String>add(Student student){
        Map<String,String> ret = new HashMap<>();
            if(student==null){
                ret.put("type","error");
                ret.put("msg","数据绑定出错");
                return ret;
            }
            if(student.getClazzId()==0){
                ret.put("type","error");
                ret.put("msg","请选择所属班级");
                return ret;
        }
        if(StringUtils.isEmpty(student.getUsername())){
            ret.put("type","error");
            ret.put("msg","学生名不能未空");
            return ret;
        }
        if(StringUtils.isEmpty(student.getPassword())){
            ret.put("type","error");
            ret.put("msg","密码不能未空");
            return ret;
        }
        if(isExist(student.getUsername(),null)){
            ret.put("type","error");
            ret.put("msg","该姓名已存在");
            return ret;
        }
        student.setSn(StringUtil.generateSn("S",""));
        if(studentService.add(student)<=0){
            ret.put("type","error");
            ret.put("msg","添加失败");
            return ret;
        }
        ret.put("type","success");
        ret.put("msg","添加成功");
        return ret;
    }


    private boolean isExist(String username,Long id){
        Student student = studentService.findByUserName(username);
        if(student != null){
            if(id ==null)
                return true;
            if(student.getId() != id.longValue()){
                return true;
            }
        }
        return false;
    }

    /*
    * 编辑学生信息
    * */
    @RequestMapping(value = "/edit" , method = RequestMethod.POST)
    @ResponseBody
    public Map<String,String>edit(Student student){
        Map<String,String> ret = new HashMap<>();
        if(student==null){
            ret.put("type","error");
            ret.put("msg","数据绑定出错");
            return ret;
        }
        if(student.getClazzId()==0){
            ret.put("type","error");
            ret.put("msg","请选择所属班级");
            return ret;
        }
        if(StringUtils.isEmpty(student.getUsername())){
            ret.put("type","error");
            ret.put("msg","学生名不能未空");
            return ret;
        }
        if(StringUtils.isEmpty(student.getPassword())){
            ret.put("type","error");
            ret.put("msg","密码不能未空");
            return ret;
        }
        if(isExist(student.getUsername(),student.getId())){
            ret.put("type","error");
            ret.put("msg","该姓名已存在");
            return ret;
        }
        if(studentService.edit(student)<=0){
            ret.put("type","error");
            ret.put("msg","修改失败");
            return ret;
        }
        ret.put("type","success");
        ret.put("msg","修改成功");
        return ret;
    }
    /*
    * 删除学生信息
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
            if(studentService.delete(idsString)<=0){
                ret.put("type","error");
                ret.put("msg","删除失败");
            }
        }catch (Exception e){
            ret.put("type","error");
            ret.put("msg","该学生下存在其他信息,无法删除");
            return  ret;
        }

        ret.put("type","success");
        ret.put("msg","删除成功");
        return ret;
    }


}
