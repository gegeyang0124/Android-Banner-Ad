package com.zy.adproj.http;

public class HttpUrls {

    public static String IP_AUTH_DEFAULT = "";//授权IP地址 默认
    public static String IP_AD_DEFAULT = "";//广告IP地址 默认
    public static String IP_AUTH = "";
    public static String IP_AD = "";
    public static String urlPalyerList = IP_AD + "";//获取播放列表
    public static String urlRegist = IP_AD + "";//设备注册接口
    public static String urlAuth = IP_AUTH + "";//设备授权接口

    public static void init(){
        urlPalyerList = IP_AD + "";//获取播放列表
        urlRegist = IP_AD + "";//设备注册接口
        urlAuth = IP_AUTH + "";//设备授权接口
    }
}
