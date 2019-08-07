package com.zyx.manager.controller;

import com.zyx.manager.entity.User;
import com.zyx.manager.page.Page;
import com.zyx.manager.service.UserService;
import com.zyx.manager.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/*
* 用户管理控制器（管理员）
*
* 方法参数传过程中，获取前端post的数据。若参数是spring容器中已注解的直接用该类即可，若不是则需要@RequestParam(value="参数字段名")
* */
@RequestMapping("/user")
@Controller
public class UserController {
    @Autowired
    public UserService userService;

    /*
    *跳转 用户列表
    * */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(ModelAndView modelAndView){
        modelAndView.setViewName("user/user_list");
        return modelAndView;
    }
    /*
    * 获取用户列表
    * */
    @RequestMapping(value = "/get_list",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> getList(
            @RequestParam(value = "username",defaultValue = "",required = false) String username,
            Page page
    ){
        Map<String,Object> ret = new HashMap<>();
        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("username","%"+username+"%");
        queryMap.put("offset",page.getOffset());
        queryMap.put("pageSize",page.getRows());
        //easyUI规定的数据格式 rows---数据,total---总数
        ret.put("rows",userService.findList(queryMap));
        ret.put("total",userService.getTotal(queryMap));
        return ret;
    }

    /*
     * 删除用户
     * */
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> delete(
            @RequestParam(value = "ids[]",required = true) Long[] ids
    ){
        Map<String,Object> ret = new HashMap<>();
        if(ids==null){
            ret.put("type","error");
            ret.put("msg","请选择删除的数据");
        }
        if(userService.delete(StringUtil.joinString(Arrays.asList(ids),","))<=0){
            ret.put("type","error");
            ret.put("msg","删除失败");
        }
        ret.put("type","success");
        ret.put("msg","删除成功");
        return ret;
    }

    /*
     * 编辑用户
     * */
    //ResponseBody转为json格式后返回
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,String> edit(User user){
        //表单提交请求时携带的数据为json。由于User类加了component注解。springMVC会检查携带的数据中是否存在该字段的值并自动生成对象
        // 即此处可以得到一个封装好的user对象
        Map<String,String> ret = new HashMap<>();
        if(user==null){
            ret.put("type","error");
            ret.put("msg","数据绑定出错");
            return ret;
        }
        if(StringUtils.isEmpty(user.getUsername())){
            ret.put("type","error");
            ret.put("msg","用户名不能未空");
            return ret;
        }
        if(StringUtils.isEmpty(user.getPassword())){
            ret.put("type","error");
            ret.put("msg","密码不能未空");
            return ret;
        }
        User existUser = null;
        try {
            existUser =userService.findByUserName(user.getUsername());
        }catch (Exception e){
            ret.put("type","error");
            ret.put("msg","网络异常");
            return ret;
        }

        if(existUser!=null){
            if(existUser.getId() != user.getId()){
                ret.put("type","error");
                ret.put("msg","用户已存在");
                return ret;
            }
        }
        if(userService.editUser(user)<=0){
            ret.put("type","error");
            ret.put("msg","修改失败");
            return ret;
        }
        ret.put("type","success");
        ret.put("msg","修改成功");
        return ret;
    }


    /*
    * 添加用户
    * */
    //ResponseBody转为json格式后返回
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,String> add(User user){
        //表单提交请求时携带的数据为json。由于User类加了component注解。springMVC会检查携带的数据中是否存在该字段的值并自动生成对象
        // 即此处可以得到一个封装好的user对象
        Map<String,String> ret = new HashMap<>();
        if(user==null){
            ret.put("type","error");
            ret.put("msg","数据绑定出错");
            return ret;
        }
        if(StringUtils.isEmpty(user.getUsername())){
            ret.put("type","error");
            ret.put("msg","用户名不能未空");
            return ret;
        }
        if(StringUtils.isEmpty(user.getPassword())){
            ret.put("type","error");
            ret.put("msg","密码不能未空");
            return ret;
        }
        User existUser = null;
        try {
            existUser =userService.findByUserName(user.getUsername());
        }catch (Exception e){
            ret.put("type","error");
            ret.put("msg","网络异常");
            return ret;
        }

        if(existUser!=null){
            ret.put("type","error");
            ret.put("msg","用户已存在");
            return ret;
        }
        if(userService.addUser(user)<=0){
            ret.put("type","error");
            ret.put("msg","添加失败");
            return ret;
        }
        ret.put("type","success");
        ret.put("msg","添加成功");
        return ret;
    }

}
