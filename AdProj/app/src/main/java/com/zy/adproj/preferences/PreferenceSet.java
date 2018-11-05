package com.zy.adproj.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceSet {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor perferenceEdite;
    public final static String CodeAuth = "CodeAuth";//授权码
    public final static String CodeDevice = "CodeDevice";//设备编码
    public final static String IP_AUTH = "IP_AUTH";//授权地址
    public final static String IP_AD = "IP_AD";//广告地址

    public PreferenceSet(Context context){
        sharedPreferences = context.getSharedPreferences("config", context.MODE_PRIVATE);
        perferenceEdite = sharedPreferences.edit();
    }

    public void setStr(String name,String value){
        perferenceEdite.putString(name,value)
                .commit();

//        SharedPreferences sharedPreferences = context.getSharedPreferences("config_Jump_Guide",
//                context.MODE_PRIVATE);
//        sharedPreferences
//                .edit()
//                .putBoolean("is_user_guide_showed", true)
//                .commit();

        //如果实在是不理解其中的机制、可以看源码说明或者看下面的说明：
        //第一个参数  是最终保存的文件名，不用指定文件后缀，因为SharedPreferences这个API默认就是xml格式保存
        //第二个参数是文件操作模式，这里是只能本软件自己访问的私有操作模式
//        SharedPreferences sharedPreferences = context.getSharedPreferences("config", context.MODE_PRIVATE);
//        sharedPreferences
//                .edit() //开始偏好设置的编辑
//                .putString("name", value)//保存boolean类型的参数 is_user_guide_showed 的值为true
//                .commit(); //提交

        //下面是获得已经保存的偏好设置的值：
//        SharedPreferences isJumpGuideActivitySPF = context.getSharedPreferences("config_Jump_Guide",
//                context.MODE_PRIVATE);
//        boolean isGuideShow = isJumpGuideActivitySPF.getBoolean("is_user_guide_showed", false);

        //基本上就是这样；至于偏好设置能保存什么样的类型、可以自行查阅他人的博客、在此我只是为了以后使用的方便。

    }

    public String getStr(String name){
        return sharedPreferences.getString(name,null);
    }

    public String getStr(String name,String defalutStr){
        return sharedPreferences.getString(name,defalutStr);
    }
}
