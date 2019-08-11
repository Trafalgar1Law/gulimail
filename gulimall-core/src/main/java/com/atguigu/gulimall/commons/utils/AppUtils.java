package com.atguigu.gulimall.commons.utils;

public class AppUtils {

    public static String arrayToStringWithSeparater(String[] arr,String sep){
        StringBuffer stringBuffer = new StringBuffer();
        String substring=null;
        if(arr!=null&&arr.length>0){
            for (String s : arr) {
                stringBuffer.append(s);
                stringBuffer.append("sep");
            }

            substring= stringBuffer.toString().substring(0, stringBuffer.length() - 1);
        }

        return substring;
    }
}
