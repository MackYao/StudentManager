package com.zyx.manager.controller;

//主页控制七

import com.github.pagehelper.util.StringUtil;
import com.zyx.manager.entity.Student;
import com.zyx.manager.entity.User;
import com.zyx.manager.service.StudentService;
import com.zyx.manager.service.UserService;
import com.zyx.manager.util.CpachaUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/system")
@Controller
public class SystemController {

    @Autowired
    public UserService userService;
    @Autowired
    private StudentService studentService;

    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public ModelAndView index(ModelAndView modelAndView){
        modelAndView.setViewName("system/index");
        return modelAndView;
    }


    /*
    * 跳转登录界面
    * */
    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public ModelAndView login(ModelAndView modelAndView){
        modelAndView.setViewName("system/login");
        return modelAndView;
    }

    /*
    * 注销logout
    * */
    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    public String loginout(HttpServletRequest request){
        request.getSession().setAttribute("user", null);
        return "redirect:login";
    }

    /*
    * 登录表单提交
    * */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,String> login(
            @RequestParam(value = "username",required = true) String username,
            @RequestParam(value = "password",required = true) String password,
            @RequestParam(value = "vcode",required = true) String vcode,
            @RequestParam(value = "type",required = true) int type,
            HttpServletRequest request
    ){
       Map<String,String> ret = new HashMap<>();
       if(StringUtils.isEmpty(username)){
           ret.put("type","error");
           ret.put("msg","用户名不能为空！");
           return ret;
       }
        if(StringUtils.isEmpty(password)){
            ret.put("type","error");
            ret.put("msg","密码不能为空！");
            return ret;
        }
        if(StringUtils.isEmpty(vcode)){
            ret.put("type","error");
            ret.put("msg","验证码不能为空！");
            return ret;
        }
        String loginCpacha = (String) request.getSession().getAttribute("loginCpacha");
        if(StringUtil.isEmpty(loginCpacha)){
            ret.put("type","error");
            ret.put("msg","长时间未操作，会话已失效，请刷新重试");
            return ret;
        }
        if(!vcode.toUpperCase().equals(loginCpacha.toUpperCase())){
            ret.put("type","error");
            ret.put("msg","验证码错误");
            return  ret;
        }
        //清除session中的
        request.getSession().setAttribute("loginCpacha",null);
        request.getSession().setAttribute("userType",type);
        if(type == 2){//登录身份为学生
            Student student = null;
            try {
                student =studentService.findByUserName(username);
            }catch (Exception e){
                ret.put("type","error");
                ret.put("msg","网络异常");
                return ret;
            }

            if(student==null){
                ret.put("type","error");
                ret.put("msg","学生不存在");
                return  ret;
            }
            if(!password.equals(student.getPassword())){
                ret.put("type","error");
                ret.put("msg","密码错误");
                return ret;
            }
            request.getSession().setAttribute("user",student);
            ret.put("type","success");
            ret.put("msg","登录成功！");
            return ret;
        }
        if(type == 1){//登录身份为管理员
            //检查用户账号密码
            User user = null;
            try {
                user = userService.findByUserName(username);
            }catch (Exception e){
                ret.put("type","error");
                ret.put("msg","网络异常");
                return ret;
            }

            if(user==null){
                ret.put("type","error");
                ret.put("msg","用户不存在");
                return  ret;
            }
            if(!password.equals(user.getPassword())){
                ret.put("type","error");
                ret.put("msg","密码错误");
                return ret;
            }
            request.getSession().setAttribute("user",user);
            ret.put("type","success");
            ret.put("msg","登录成功！");
            return ret;
        }
        ret.put("type","error");
        ret.put("msg","系统出错");
        return ret;
    }



    /*
    * 验证码图片生成
    * */
    @RequestMapping(value = "/get_cpacha", method = RequestMethod.GET)
    public void getCpacha(HttpServletRequest request,
                          @RequestParam(value = "vl",defaultValue = "4",required = false) Integer vl,
                          @RequestParam(value = "w",defaultValue = "98",required = false) Integer w,
                          @RequestParam(value = "h",defaultValue = "33",required = false) Integer h,
                          HttpServletResponse reponse){
        CpachaUtil cpachaUtil = new CpachaUtil(vl,w,h);
        String vcode = cpachaUtil.generatorVCode();
        //生成图片同时将验证码放入session
        request.getSession().setAttribute("loginCpacha",vcode);
        final BufferedImage bufferedImage = cpachaUtil.generatorRotateVCodeImage(vcode, true);
        try {
            ImageIO.write(bufferedImage,"gif",reponse.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
