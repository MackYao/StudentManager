package com.zyx.manager.util;

import java.util.Date;
import java.util.List;
/*
* 字符串拼接。将long数组拼接成以split为分隔符的字符串
* */
public class StringUtil {
    public static String joinString(List<Long> list,String spilt){
        String res = "";
        for(Long o:list){
            res +=o+spilt;
        }
        if(!res.equals("")){
            res = res.substring(0,res.length()-1);
        }
        return res;
    }
    public static String generateSn(String prefix,String suffix){
        return prefix+new Date().getTime()+suffix;
    }
}
