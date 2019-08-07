package com.zyx.manager.interceptor;

import com.zyx.manager.entity.User;
import net.sf.json.JSONObject;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/*
    * 登录拦截器
    *
    * */
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String url = httpServletRequest.getRequestURI();
        Object user =  httpServletRequest.getSession().getAttribute("user");
        if(user == null){
            //未登录或登录失效
            System.out.println("进入拦截器，未登录");
            if("XMLHttpRequest".equals(httpServletRequest.getHeader("X-Requested-With"))){
                //界面由ajax发起请求。重定向无法刷新界面
                Map<String,String> ret = new HashMap<>();
                    ret.put("type","error");
                    ret.put("msg","登录状态已失效");
                    httpServletResponse.getWriter().write(JSONObject.fromObject(ret).toString());
                    return false;
            }
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath()+"/system/login");
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
